package com.gd.orh.incomeExpMgt.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.gd.orh.config.AlipayConfig;
import com.gd.orh.dto.PaymentDTO;
import com.gd.orh.utils.RestResultFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment/alipay")
public class AlipayController {

    @PostMapping("/pay")
    public ResponseEntity<?> pay(@RequestBody PaymentDTO paymentDTO) {
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

        AlipayClient client = new DefaultAlipayClient(
                AlipayConfig.URL,
                AlipayConfig.APPID,
                AlipayConfig.RSA_PRIVATE_KEY,
                AlipayConfig.FORMAT,
                AlipayConfig.CHARSET,
                AlipayConfig.ALIPAY_PUBLIC_KEY,
                AlipayConfig.SIGNTYPE
        );

        AlipayTradeWapPayRequest alipay_request=new AlipayTradeWapPayRequest();

        // 封装请求支付信息
        AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
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
            return ResponseEntity
                    .ok(RestResultFactory.getSuccessResult("支付成功", form));
        } catch (AlipayApiException e) {
            return ResponseEntity
                    .badRequest()
                    .body(RestResultFactory.getFailResult("支付失败"));
        }
    }

    @GetMapping("/return")
    public ResponseEntity<?> syncReturn(HttpServletRequest request) throws UnsupportedEncodingException {
        //获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String key : requestParams.keySet()) {
            String[] vals = requestParams.get(key);
            String valStr = "";
            for (int i = 0; i < vals.length; i++) {
                valStr = (i == vals.length - 1) ? valStr + vals[i]
                        : valStr + vals[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valStr = new String(valStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(key, valStr);
        }

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        //商户订单号

        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

        //支付宝交易号

        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
        //计算得出通知验证结果
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        try {
            boolean verify_result = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, "RSA2");
            if (verify_result) {//验证成功
                //////////////////////////////////////////////////////////////////////////////////////////
                //请在这里加上商户的业务逻辑程序代码
                //该页面可做页面美工编辑
                //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
                return ResponseEntity
                        .ok()
                        .body(RestResultFactory.getFailResult("验证成功"));
                //////////////////////////////////////////////////////////////////////////////////////////
            } else {
                //该页面可做页面美工编辑
                return ResponseEntity
                        .badRequest()
                        .body(RestResultFactory.getFailResult("验证失败"));
            }
        } catch (AlipayApiException e) {
            return ResponseEntity
                    .badRequest()
                    .body(RestResultFactory.getFailResult("验证出现异常"));
        }
    }

    @PostMapping("/notify")
    public String asyncNotify(HttpServletRequest request) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String key : requestParams.keySet()) {
            String[] vals = requestParams.get(key);
            String valStr = "";
            for (int i = 0; i < vals.length; i++) {
                valStr = (i == vals.length - 1) ? valStr + vals[i]
                        : valStr + vals[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(key, valStr);
        }
        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        //商户订单号

        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
        //支付宝交易号

        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

        //交易状态
        String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
        //计算得出通知验证结果
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        try {
            boolean verify_result = AlipaySignature.rsaCheckV1(
                    params, AlipayConfig.ALIPAY_PUBLIC_KEY,
                    AlipayConfig.CHARSET,
                    "RSA2"
            );

            if(verify_result){//验证成功
                //////////////////////////////////////////////////////////////////////////////////////////
                //请在这里加上商户的业务逻辑程序代码

                //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

                if(trade_status.equals("TRADE_FINISHED")){
                    //判断该笔订单是否在商户网站中已经做过处理
                    //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                    //如果有做过处理，不执行商户的业务程序

                    //注意：
                    //如果签约的是可退款协议，退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
                    //如果没有签约可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
                } else if (trade_status.equals("TRADE_SUCCESS")){
                    //判断该笔订单是否在商户网站中已经做过处理
                    //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                    //如果有做过处理，不执行商户的业务程序

                    //注意：
                    //如果签约的是可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
                }

                //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
                // out.clear();
                return "success";	//请不要修改或删除

                //////////////////////////////////////////////////////////////////////////////////////////
            }else{//验证失败
                return "fail";
            }
        } catch (AlipayApiException e) {
            return "verify exception occurred";
        }
    }
}
