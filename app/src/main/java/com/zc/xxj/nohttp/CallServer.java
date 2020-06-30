package com.zc.xxj.nohttp;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;

public class CallServer {
    private static CallServer instance;
    private RequestQueue mRequestQueue;
//    private DownloadQueue mDownloadQueue;

    private CallServer(){
        mRequestQueue = NoHttp.newRequestQueue(5);
//        mDownloadQueue = NoHttp.newDownloadQueue(3);
    }

    public synchronized static CallServer getInstance(){
        if (instance == null){
            instance = new CallServer();
        }
        return instance;
    }

    public<T> void request(int what, Request<T> request, OnResponseListener<T> listener){
        mRequestQueue.add(what, request, listener);
    }

    public<T> void request(int what, Request<T> request, HttpListener<T> callback){
        mRequestQueue.add(what, request, new HttpResponseListener<>(callback));
    }

//    public void download(int what, DownloadRequest request, DownloadListener listener){
//        mDownloadQueue.add(what, request, listener);
//    }
//
//    public void download(int what, DownloadRequest request, DownListener listener){
//        mDownloadQueue.add(what, request, new DownResponseListener(listener));
//    }
}
