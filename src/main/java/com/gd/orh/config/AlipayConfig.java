package com.gd.orh.config;

public class AlipayConfig {
    // 商户appid
    public static String APPID = "2016080200148537";
    // 私钥 pkcs8格式的
    public static String RSA_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDDkO+uiT4CdS5g0i26xrEYOUegy7DEQ9ini+TtMnRlFp6uc4WxSvR9k5rvxumcWcvOuskHCPHqHC9F2wByyu+l3ePY9tLFf+yFoGuCZv5KLw5kS0w+9WiiEknpUfco07OaASmtiL9+mAfgLjgAa1vfdBwAB2MdoQTM3d/Ub422+kK0gz6u4BoYKnURrLnKT0Sz0YYKGEMKq9MYJp7ffqzbOKnnmP2Et0revVgM5zOrYhGsS1/ktPWk8PBx+e0mUL9P6OnJr3pMhj4Y7t8Ft1LzmBayt1JEuuzR0zgRF6mn//6izMu6S2hG5XT8OYHNNGjrtX/3AFxX4/HSTRPHI7azAgMBAAECggEAYkt67NL2Y+3Z/bgmW/Z6FJ7VrbAzjLI9NxlNWxs8/YI9B5nw3Ie8E1IZUeM8j+ViVunzGKIaVX0NFaNZuljd0xSP7QFCNxpxG0jiQIzbyxWTuj1OeWaHzM48cBVOkSq23vUuqZZOCacIbllMtIlBiDKn0CvmWYIRE5eBZRkctZZ63IwWwq5OUoPoGkg6jnyR8vT3FZUiBlmZTkFMAeB8zVR+a2WUqLhJ8GKg+618/67+JEtxR4cXsQbUKK4xXgFu5k1USTKazlpEyNGq5Q2VarTA7C0HB+h5hSYnZ8v7xA/kjoXBo1wyhsHX4uj2Z4RarYvwHLFxw/UyLDkUuSdIIQKBgQDnM2uz2r2yfBOZJUGERKJ9KO+0wbXU/qYG7quJebG3lS3DvQQwOiwY7R5VBbeyNg1AgGoiEnDwWFts5oDOgkkP554lKLoJwYK3vakd1p1hEQQDSkZ3rMTqepFvvD+WBAcdn+vsnMA7CiFtqppaEDqG4xwqOadNf/T0ncPJh6JdtwKBgQDYiwROED9+g6s3VTW7s/Bb3IZIM34RsSimBQbdCy293N2jfGba66DXicFyBWztkbax/zId/xcLTEjgKAISg+05J7kTOkmfah2VEVQ2VD8TDW4nHBuG1qLHU0+8eODgsFjus6a08/dgEowFStaSpF1v3jURKD5N4G2mveI42mku5QKBgQCZnFGHyuLUrpl63AT+s3dePw+cgkOkg5id7H9uNLu+JNCmR3R78nqQCSNpDAZz0nPVkjxx7Ny644nC/hdAndGbTqGFQNzyVG7cU5gEai5gaqTG5teQ9epTfCIbIfEBZCWyKinfKHqT/3QcYAhzpGquakCxUWL2G/kNebC2unZHWwKBgCepruwthbPCokXMUb9Dwi7RQbdP1zOeY3elTBCIz6QLJH2hd6V5q0hPbyNXYxBQtEn+GEvuQWZMXrzQFzEVAQqLABdJgdq1MR3wwqAA5lyzBQ4c093qyn4AAIbhDS+anW97cHmMhi8Otp7xikPp30NyS/4MhPz2sp7sANexCbsRAoGAP+7/0QeQmsxzflDYXBpSVgdtV/ys4G+AI/jYqdbpQdr9eKly8WVJQv77Yy8R83Vj0utx8gm/9PXBGZjKQ9HZBEt4YrhyYjCpa3/g49L7T4syhaYX2xe4gpmn3mQs6j3vzTH5NEmU0GGiXer/CDdAnXP0q+eTbwBTq7pA0msap/4=";
    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://localhost:8080/api/payment/alipay/notify";
    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    public static String return_url = "http://localhost:8080/api/payment/alipay/return";
    // 请求网关地址
    public static String URL = "https://openapi.alipaydev.com/gateway.do";
    // 编码
    public static String CHARSET = "UTF-8";
    // 返回格式
    public static String FORMAT = "json";
    // 支付宝公钥
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAogOSytdfAR8ulE1g5jWP1ZAm5+Ct0W+5SFG75Ex8CR+LGGQxRPGVSgiLltr/CFYuh2Oj9CLxfIyDXiFKiM6PccXW5ihM/DmOYi6FN+Lx6QvcQbrsjJl8eSpeZqHfQJ0esNS384f9oilcbrkub8uSTlyt8YZ5UP2XTMaq3jpH92G1NQgLfIPZ91zpIlpoJOnk/FII+nU3Paj+rQPnUwnlruM3N6mEVMgkPqjdQmnvt2LVY8lFmZh6CRGZuGoMODITmzEqsChgKgz2KXoekG+a1Flw8CHkgycv/XDj5cRqnxh6ImkS1DBcvIcYti1e86qvN2fywomPbR4PQukIopnucQIDAQAB";
    // 日志记录目录
    public static String log_path = "/log";
    // RSA2
    public static String SIGNTYPE = "RSA2";
}
