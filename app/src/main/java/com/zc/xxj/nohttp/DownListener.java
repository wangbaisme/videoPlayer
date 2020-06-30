package com.zc.xxj.nohttp;

import com.yanzhenjie.nohttp.Headers;

public class DownListener {

    public void onDownloadError(int what, Exception exception) {

    }

    public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {

    }

    public void onProgress(int what, int progress, long fileCount, long speed) {

    }

    public void onFinish(int what, String filePath) {

    }

    public void onCancel(int what) {

    }
}
