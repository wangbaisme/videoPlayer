package com.zc.xxj.nohttp;

import com.yanzhenjie.nohttp.rest.Response;

public class HttpListener<T> {

    public void onStart(int what){}

    public void onFinish(int what){}

    public void onSucceed(int what, Response<T> response){}

    public void onFailed(int what, Response<T> response){}

}
