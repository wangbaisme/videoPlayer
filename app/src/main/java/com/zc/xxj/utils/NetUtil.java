package com.zc.xxj.utils;

import android.annotation.SuppressLint;
import android.net.TrafficStats;

public class NetUtil {
    private static final String TAG = NetUtil.class.getSimpleName();
    private static long lastTotalNetBytes = 0;
    private static long lastStepTime = 0;

    @SuppressLint("DefaultLocale")
    public static String getNetSpeed(int uid) {
        long nowTotalRxBytes = getTotalRxBytes(uid);
        long nowTimeStamp = System.currentTimeMillis();
        long speed = ((nowTotalRxBytes - lastTotalNetBytes) * 1000 / (nowTimeStamp - lastStepTime));//毫秒转换
        lastStepTime = nowTimeStamp;
        lastTotalNetBytes = nowTotalRxBytes;
        if (speed < 1024) return (speed + " kb/s");
        else return String.format("%.2f",(float)speed/1024.0) + " mb/s";
    }

    public static long getTotalRxBytes(int uid) {
        return TrafficStats.getUidRxBytes(uid) == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getTotalRxBytes() / 1024);
    }

}
