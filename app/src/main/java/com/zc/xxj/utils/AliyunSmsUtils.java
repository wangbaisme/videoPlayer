package com.zc.xxj.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AliyunSmsUtils {
    static final String accessKeyId = "LTAI8BVdu08OAaRj";
    static final String accessKeySecret = "7jDTpJa2tH9o8XeaO8cWB54ONsgh19";

    public static final String Authentication = "SMS_151455587";        // 身份验证
    public static final String LoginConfirmation = "SMS_151455586";     // 登录确认
    public static final String LoginException = "SMS_151455585";        // 登录异常
    public static final String UserRegistration = "SMS_151455584";      // 用户注册
    public static final String ChangePassword = "SMS_151455583";        // 修改密码
    public static final String ChangeOfInformation = "SMS_151455582";   // 信息变更

    private static int newcode;

    public static String SendSms(String phone, String code, String TemplateCode) throws Exception {

        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(new java.util.SimpleTimeZone(0, "GMT"));
        java.util.Map<String, String> paras = new java.util.HashMap<String, String>();
        paras.put("SignatureMethod", "HMAC-SHA1");
        paras.put("SignatureNonce", java.util.UUID.randomUUID().toString());
        paras.put("AccessKeyId", accessKeyId);
        paras.put("SignatureVersion", "1.0");
        paras.put("Timestamp", df.format(new java.util.Date()));
        paras.put("Format", "XML");
        paras.put("Action", "SendSms");
        paras.put("Version", "2017-05-25");
        paras.put("RegionId", "cn-hangzhou");
        paras.put("PhoneNumbers", phone);
        paras.put("SignName", "翔翎艺术");
        paras.put("TemplateParam", "{\"code\":\"" + code + "\"}");
        paras.put("TemplateCode", TemplateCode);
//        paras.put("OutId", "123");
        if (paras.containsKey("Signature"))
            paras.remove("Signature");
        java.util.TreeMap<String, String> sortParas = new java.util.TreeMap<String, String>();
        sortParas.putAll(paras);
        java.util.Iterator<String> it = sortParas.keySet().iterator();
        StringBuilder sortQueryStringTmp = new StringBuilder();
        while (it.hasNext()) {
            String key = it.next();
            sortQueryStringTmp.append("&").append(specialUrlEncode(key)).append("=").append(specialUrlEncode(paras.get(key)));
        }
        String sortedQueryString = sortQueryStringTmp.substring(1);
        StringBuilder stringToSign = new StringBuilder();
        stringToSign.append("GET").append("&");
        stringToSign.append(specialUrlEncode("/")).append("&");
        stringToSign.append(specialUrlEncode(sortedQueryString));
        String sign = sign(accessKeySecret + "&", stringToSign.toString());
        String signature = specialUrlEncode(sign);
        String u = "http://dysmsapi.aliyuncs.com/?Signature=" + signature + sortQueryStringTmp;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(u).build();
        try(Response response = client.newCall(request).execute()){
            return response.body().toString();
        }
    }

    public static String specialUrlEncode(String value) throws Exception {
        return java.net.URLEncoder.encode(value, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
    }

    public static String sign(String accessSecret, String stringToSign) throws Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA1");
        mac.init(new javax.crypto.spec.SecretKeySpec(accessSecret.getBytes("UTF-8"), "HmacSHA1"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        return new Decoder.BASE64Encoder().encode(signData);
    }

    public static String getNewcode() {
        if (newcode/100 >= 10) return Integer.toString(newcode);
        else return "0"+newcode;
    }
    public static void setNewcode(){
        newcode = (int)(Math.random()*9999)+100;
    }

}
