package com.gd.orh.incomeExpMgt.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.gd.orh.config.AlipayConfig;
import com.gd.orh.dto.DriverBalanceDTO;
import com.gd.orh.dto.PaymentDTO;
import com.gd.orh.dto.TripOrderDTO;
import com.gd.orh.dto.WithdrawalDTO;
import com.gd.orh.entity.DriverBalance;
import com.gd.orh.entity.ResultCode;
import com.gd.orh.entity.TripOrder;
import com.gd.orh.incomeExpMgt.service.DriverBalanceService;
import com.gd.orh.tripOrderMgt.service.TripOrderService;
import com.gd.orh.utils.RestResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/payment/alipay")
public class AlipayController {

    @Autowired
    private TripOrderService tripOrderService;

    @Autowired
    private DriverBalanceService driverBalanceService;

    @PostMapping("/pay")
    public ResponseEntity<?> pay(@Valid @RequestBody PaymentDTO paymentDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(RestResultFactory.getFreeResult(
                            ResultCode.NOT_FOUND,
                            "The tripOrderId could not be empty",
                            paymentDTO
                    ));
        }

        TripOrder tripOrder = new TripOrder();
        tripOrder.setId(paymentDTO.getTripOrderId());

        if (tripOrderService.isTripOrderCanBePaid(tripOrder)) {
            return ResponseEntity
                    .badRequest()
                    .body(RestResultFactory.getFailResult(
                            "The tripOrder could not be paid!",
                            paymentDTO
                    ));
        }

        // 商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = paymentDTO.getTripOrderId().toString();
        // 订单名称，必填
        String subject = "行程订单" + paymentDTO.getTripOrderId().toString();
        // 付款金额，必填
        String total_amount = paymentDTO.getTotalAmount().toString();
        // 商品描述，可空
        String body = "";
        // 超时时间 可空
        String timeout_express = "2m";
        // 销售产品码 必填
        String product_code = "QUICK_WAP_WAY";

        AlipayClient client =
                new DefaultAlipayClient(
                        AlipayConfig.URL,
                        AlipayConfig.APPID,
                        AlipayConfig.RSA_PRIVATE_KEY,
                        AlipayConfig.FORMAT,
                        AlipayConfig.CHARSET,
                        AlipayConfig.ALIPAY_PUBLIC_KEY,
                        AlipayConfig.SIGNTYPE
                );

        AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();

        // 封装请求支付信息
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setOutTradeNo(out_trade_no);
        model.setSubject(subject);
        model.setTotalAmount(total_amount);
        model.setBody(body);
        model.setTimeoutExpress(timeout_express);
        model.setProductCode(product_code);
        alipay_request.setBizModel(model);
        // 设置异步通知地址
        alipay_request.setNotifyUrl(AlipayConfig.notify_url);
        // 设置同步地址
        alipay_request.setReturnUrl(AlipayConfig.return_url);

        // form表单生产
        String form = "";
        try {
            // 调用SDK生成表单
            form = client.pageExecute(alipay_request).getBody();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(new MediaType(
                    MediaType.TEXT_HTML,
                    Charset.forName(AlipayConfig.CHARSET)
            ));

            return new ResponseEntity<String>(form, httpHeaders, HttpStatus.OK);
        } catch (AlipayApiException e) {
            return ResponseEntity
                    .badRequest()
                    .body(RestResultFactory.getFailResult(e.getMessage()));
        }
    }

    @GetMapping("/return")
    public ResponseEntity<?> syncReturn(HttpServletRequest request) throws UnsupportedEncodingException {
        // 获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String key : requestParams.keySet()) {
            String[] vals = requestParams.get(key);
            String valStr = "";
            for (int i = 0; i < vals.length; i++) {
                valStr = (i == vals.length - 1) ? valStr + vals[i]
                        : valStr + vals[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            valStr = new String(valStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(key, valStr);
        }

        // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)
        // 商户订单号
        String out_trade_no =
                new String(
                        request.getParameter("out_trade_no").getBytes("ISO-8859-1"),
                        "UTF-8"
                );
        // 支付宝交易号
        String trade_no =
                new String(
                        request.getParameter("trade_no").getBytes("ISO-8859-1"),
                        "UTF-8"
                );

        try {
            // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)
            // 计算得出通知验证结果
            boolean verify_result =
                    AlipaySignature.rsaCheckV1(
                            params,
                            AlipayConfig.ALIPAY_PUBLIC_KEY,
                            AlipayConfig.CHARSET,
                            "RSA2"
                    );

            if (verify_result) { // 验证成功
                TripOrder tripOrder = new TripOrder();
                tripOrder.setId(Long.parseLong(out_trade_no));
                tripOrder = tripOrderService.payTripOrder(tripOrder);

                TripOrderDTO tripOrderDTO = new TripOrderDTO().convertFor(tripOrder);

                return ResponseEntity
                        .ok()
                        .body(RestResultFactory.getSuccessResult("Verify succeed!", tripOrderDTO));
            } else {
                return ResponseEntity
                        .badRequest()
                        .body(RestResultFactory.getFailResult("Verify fail!"));
            }
        } catch (AlipayApiException e) {
            return ResponseEntity
                    .badRequest()
                    .body(RestResultFactory.getFailResult(e.getMessage()));
        }
    }

    @PostMapping("/notify")
    public ResponseEntity<?> asyncNotify(HttpServletRequest request) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String key : requestParams.keySet()) {
            String[] vals = requestParams.get(key);
            String valStr = "";
            for (int i = 0; i < vals.length; i++) {
                valStr = (i == vals.length - 1) ? valStr + vals[i]
                        : valStr + vals[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            // valStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(key, valStr);
        }
        // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)
        // 商户订单号
        String out_trade_no =
                new String(
                        request.getParameter("out_trade_no").getBytes("ISO-8859-1"),
                        "UTF-8"
                );
        // 支付宝交易号
        String trade_no =
                new String(
                        request.getParameter("trade_no").getBytes("ISO-8859-1"),
                        "UTF-8"
                );
        // 交易状态
        String trade_status =
                new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),
                        "UTF-8"
                );
        // 订单总金额
        String total_amount =
                new String(
                        request.getParameter("total_amount").getBytes("ISO-8859-1"),
                        "UTF-8"
                );

        // 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)
        // 计算得出通知验证结果
        try {
            boolean verify_result = AlipaySignature.rsaCheckV1(
                    params, AlipayConfig.ALIPAY_PUBLIC_KEY,
                    AlipayConfig.CHARSET,
                    "RSA2"
            );

            if (verify_result) { // 验证成功
                //////////////////////////////////////////////////////////////////////////////////////////
                //请在这里加上商户的业务逻辑程序代码

                //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

                if (trade_status.equals("TRADE_FINISHED")) {
                    //判断该笔订单是否在商户网站中已经做过处理
                    //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                    //如果有做过处理，不执行商户的业务程序

                    //注意：
                    //如果签约的是可退款协议，退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
                    //如果没有签约可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
                } else if (trade_status.equals("TRADE_SUCCESS")) {
                    //判断该笔订单是否在商户网站中已经做过处理
                    //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                    //如果有做过处理，不执行商户的业务程序

                    //注意：
                    //如果签约的是可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
                }

                //——请根据您的业务逻辑来编写程序（以上代码仅作参考）
                // out.clear();
                // return "success"; // 请不要修改或删除
                TripOrder tripOrder = new TripOrder();
                tripOrder.setId(Long.parseLong(out_trade_no));

                tripOrderService.completePayment(tripOrder);
                driverBalanceService.deposit(tripOrder.getDriver(), new BigDecimal(total_amount));

                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(new MediaType(
                        MediaType.TEXT_PLAIN,
                        Charset.forName(AlipayConfig.CHARSET)
                ));

                return new ResponseEntity("success", httpHeaders, HttpStatus.OK);

                //////////////////////////////////////////////////////////////////////////////////////////
            } else {//验证失败
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(new MediaType(
                        MediaType.TEXT_PLAIN,
                        Charset.forName(AlipayConfig.CHARSET)
                ));

                return new ResponseEntity("fail", httpHeaders, HttpStatus.BAD_REQUEST);
            }
        } catch (AlipayApiException e) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(new MediaType(
                    MediaType.TEXT_PLAIN,
                    Charset.forName(AlipayConfig.CHARSET)
            ));

            return new ResponseEntity(e.getMessage(), httpHeaders, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody WithdrawalDTO withdrawalDTO) {
        DriverBalance driverBalance = driverBalanceService.findByDriverId(withdrawalDTO.getDriverId());
        BigDecimal amount = withdrawalDTO.getAmountOfWithdrawal();

        // 提取金额大于账户余额
        if (amount.compareTo(driverBalance.getBalance()) > 0) {
            return ResponseEntity
                    .badRequest()
                    .body(RestResultFactory.getFailResult(
                            "The amount is greater than your account balance!",
                            withdrawalDTO)
                    );
        }

        AlipayClient alipayClient =
                new DefaultAlipayClient(
                        AlipayConfig.URL,
                        AlipayConfig.APPID,
                        AlipayConfig.RSA_PRIVATE_KEY,
                        AlipayConfig.FORMAT,
                        AlipayConfig.CHARSET,
                        AlipayConfig.ALIPAY_PUBLIC_KEY,
                        AlipayConfig.SIGNTYPE
                );

        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
        AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
        //商户转账唯一订单号
        model.setOutBizNo(StringUtils.collectionToDelimitedString(
                Arrays.asList(
                        "orh",
                        "driver",
                        driverBalance.getDriverId(),"deposit",
                        new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())),
                "_")
        );
        //收款方账户类型。
        //1、ALIPAY_USERID：pid ,以2088开头的16位纯数字组成。
        //2、ALIPAY_LOGONID：支付宝登录号(邮箱或手机号)
        model.setPayeeType("ALIPAY_LOGONID");
        //收款方账户。与payee_type配合使用。付款方和收款方不能是同一个账户。
        model.setPayeeAccount(driverBalance.getAlipayAccount());
        //测试金额必须大于等于0.1，只支持2位小数，小数点前最大支持13位
        model.setAmount(withdrawalDTO.getAmountOfWithdrawal().toString());
        //当付款方为企业账户且转账金额达到（大于等于）50000元，remark不能为空。
        model.setRemark("提现备注");
        request.setBizModel(model);
        try {
            AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                DriverBalance withdrawDriverBalance = driverBalanceService.withdraw(driverBalance, amount);

                DriverBalanceDTO driverBalanceDTO = new DriverBalanceDTO().convertFor(withdrawDriverBalance);

                return ResponseEntity
                        .ok(RestResultFactory.getSuccessResult(driverBalanceDTO));
            } else {
                return ResponseEntity
                        .badRequest()
                        .body(RestResultFactory.getFailResult("Deposit fail!", withdrawalDTO));
            }
        } catch (AlipayApiException e) {
            return ResponseEntity
                    .badRequest()
                    .body(RestResultFactory.getFailResult(e.getMessage()));
        }
    }
}
