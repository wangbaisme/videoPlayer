package com.zc.xxj.nohttp;

import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.download.DownloadListener;

public class DownResponseListener implements DownloadListener {

    private DownListener callback;

    public DownResponseListener(DownListener callback){
        this.callback = callback;
    }

    @Override
    public void onDownloadError(int what, Exception exception) {
        if (callback != null){
            callback.onDownloadError(what, exception);
        }
    }

    @Override
    public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {
        if (callback != null){
            callback.onStart(what, isResume, rangeSize, responseHeaders, allCount);
        }
    }

    @Override
    public void onProgress(int what, int progress, long fileCount, long speed) {
        if (callback != null){
            callback.onProgress(what, progress, fileCount, speed);
        }
    }

    @Override
    public void onFinish(int what, String filePath) {
        if (callback != null){
            callback.onFinish(what, filePath);
        }
    }

    @Override
    public void onCancel(int what) {
        if (callback != null){
            callback.onCancel(what);
        }
    }
}
