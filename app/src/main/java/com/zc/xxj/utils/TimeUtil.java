package com.zc.xxj.utils;

public class TimeUtil {
    public static String getTime(long s){
        StringBuffer time = new StringBuffer();
        int sec_ = (int)(s / 1000), hour, min, sec;
        hour = sec_ / 3600;
        if (hour > 0) sec_ = sec_ - hour * 3600;
        min = sec_ / 60;
        if (min > 0) sec_ = sec_ - min * 60;
        sec = sec_;
        if (hour>0 && hour<=9) time.append(0).append(hour).append(":");
        else if (hour > 9) time.append(hour).append(":");
        if (min>0 && min<=9) time.append(0).append(min).append(":");
        else if (min > 9) time.append(min).append(":");
        else if (min == 0) time.append("00:");
        if (sec>=0 && sec<=9) time.append(0).append(sec);
        else if (sec > 9) time.append(sec);
        return time.toString();
    }
}
