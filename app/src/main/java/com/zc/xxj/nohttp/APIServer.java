package com.zc.xxj.nohttp;

public class APIServer {

    public static final String platformAgreement ="file:///android_asset/user_agreement.html";
    public static final String privacyAgreement ="file:///android_asset/privacy_agreement.html";

//    private static final String URL_XL_HOST = "http://10.128.25.10:8080/v1";
    private static final String URL_XL_HOST = "http://video.shenzhenyishu.com/v1";

    public static final String REQUEST_CODE_SMS = URL_XL_HOST + "/code";
    public static final String LOGIN = URL_XL_HOST + "/login";
    public static final String REGISTER = URL_XL_HOST + "/register";

    public static final String VERSION_UPDATE = URL_XL_HOST + "/getversion";


}

