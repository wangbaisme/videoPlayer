package com.zc.player.util;

import android.util.Log;

public class LoggerUtil {
    private static final boolean isShowMianTestLog = true;
    private static final boolean isShowDubugLog = true;
    public static void showMianTestLog(String msg){
        if (isShowMianTestLog) Log.d("maintest",msg);
    }

    /*********** -----------------------   DEBUG   -------------------------*********/
    public static void showDebugLog(String TAG, String msg){
        if (isShowDubugLog) Log.d(TAG,msg);
    }
}
