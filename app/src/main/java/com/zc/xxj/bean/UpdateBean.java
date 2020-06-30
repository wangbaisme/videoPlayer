package com.zc.xxj.bean;

public class UpdateBean {

    private String minVersion;
    private String minCode;
    private String version;
    private String code;
    private String url;

    public String getMinVersion() {
        return minVersion;
    }

    public String getMinCode() {
        return minCode;
    }

    public String getVersion() {
        return version;
    }

    public String getCode() {
        return code;
    }

    public String getUrl() {
        return url;
    }

    public UpdateBean(String version, String minVersion, String code, String minCode , String url){
        this.minVersion = minVersion;
        this.version = version;
        this.code = code;
        this.minCode = minCode;
        this.url = url;
    }

}
