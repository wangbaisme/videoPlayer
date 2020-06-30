package com.zc.xxj.nohttp;

import android.util.Log;

import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.NotFoundCacheError;
import com.yanzhenjie.nohttp.error.TimeoutError;
import com.yanzhenjie.nohttp.error.URLError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;

public class HttpResponseListener<T> implements OnResponseListener<T> {
    private static final String TAG = HttpResponseListener.class.getSimpleName();

    private HttpListener<T> callback;

    public HttpResponseListener(HttpListener<T> httpCallback){
        callback = httpCallback;
    }

    @Override
    public void onStart(int what) {
        if (callback != null){
            callback.onStart(what);
        }
    }

    @Override
    public void onFinish(int what) {
        if (callback != null){
            callback.onFinish(what);
        }
    }

    @Override
    public void onSucceed(int what, Response<T> response) {
        if (callback != null){
            callback.onSucceed(what, response);
        }
    }

    @Override
    public void onFailed(int what, Response<T> response) {
        Exception exception = response.getException();
        if (exception instanceof NetworkError) {// 网络不好
            Log.d(TAG,"网络已断开");
        } else if (exception instanceof TimeoutError) {// 请求超时
            Log.d(TAG,"请求超时");
        } else if (exception instanceof UnKnownHostError) {// 找不到服务器
            Log.d(TAG,"找不到服务器");
        } else if (exception instanceof URLError) {// URL是错的
            Log.d(TAG,"url 错误");
        } else if (exception instanceof NotFoundCacheError) {
            // 这个异常只会在仅仅查找缓存时没有找到缓存时返回
            // 没有缓存一般不提示用户，如果需要随你。
        } else {
            Log.d(TAG,"未知错误");
        }
        Log.e(TAG,"错误：" + exception.getMessage());
        if (callback != null) {
            callback.onFailed(what, response);
        }
    }

}
