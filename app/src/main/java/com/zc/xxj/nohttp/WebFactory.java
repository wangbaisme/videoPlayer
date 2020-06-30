package com.zc.xxj.nohttp;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;

public class WebFactory {

    public static final int REQUEST_SMS_CODE = 10001;
    public static final int REGISTER_CODE = 10002;
    public static final int LOGIN_BY_PASSWORD_CODE = 10003;
    public static final int LOGIN_BY_CODE_CODE = 10004;
    public static final int VERSION_UPDATE = 10005;


    private static WebFactory webFactory;

    private WebFactory(){}

    public synchronized static WebFactory getInstance(){
        if (webFactory == null){
            webFactory = new WebFactory();
        }
        return webFactory;
    }

    /** --------------------------- 客户端获取验证码 --------------------------- **/
    public void sendSMS(int what, HttpListener<String> callback, String url){
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        CallServer.getInstance().request(what, request, callback);
    }

    /** ------------------------------ 服务器接口 ------------------------------ **/
    public void versionCheckUpdate(HttpListener<String> callback){
        Request<String> request = NoHttp.createStringRequest(APIServer.VERSION_UPDATE, RequestMethod.GET);
        CallServer.getInstance().request(VERSION_UPDATE, request, callback);
    }

    public void getCode(String tel, HttpListener<String> callback){
        Request<String> request = NoHttp.createStringRequest(APIServer.REQUEST_CODE_SMS, RequestMethod.POST);
        request.add("tel", tel);
        request.add("type", "code");
        CallServer.getInstance().request(REQUEST_SMS_CODE, request, callback);
    }

    public void regiter(String code, String tel, String password, HttpListener<String> callback){
        Request<String> request = NoHttp.createStringRequest(APIServer.REGISTER, RequestMethod.POST);
        request.add("code", code);
        request.add("tel", tel);
        request.add("password", password);
        CallServer.getInstance().request(REGISTER_CODE, request, callback);
    }

    public void login_by_password(String tel, String password, HttpListener<String> callback){
        Request<String> request = NoHttp.createStringRequest(APIServer.LOGIN, RequestMethod.POST);
        request.add("tel", tel);
        request.add("password", password);
        CallServer.getInstance().request(LOGIN_BY_PASSWORD_CODE, request, callback);
    }

    public void login_by_code(String tel, String code, HttpListener<String> callback){
        Request<String> request = NoHttp.createStringRequest(APIServer.LOGIN, RequestMethod.POST);
        request.add("tel", tel);
        request.add("code", code);
        CallServer.getInstance().request(LOGIN_BY_CODE_CODE, request, callback);
    }

}
