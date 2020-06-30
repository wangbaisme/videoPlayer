package com.zc.xxj;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import androidx.multidex.MultiDex;

public class XXJApplication extends Application {
    static final String channel = "xxj";

    protected void attachBaseContext(Context context){
        super.attachBaseContext(context);
        MultiDex.install(context);
    }

    public void onCreate(){
        super.onCreate();

//        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
//        strategy.setAppChannel(channel);
//        strategy.setAppVersion(getVersionName(getApplicationContext()));
//        strategy.setAppPackageName(getApplicationContext().getPackageName());
//        CrashReport.initCrashReport(getApplicationContext(), "c925629456", false, strategy);
//
//        UMConfigure.init(getApplicationContext(), "5ebb68c8978eea0802a652ea", channel, UMConfigure.DEVICE_TYPE_PHONE, null);
//        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5ec1e50a");
    }

    private String getVersionName(Context context){
        PackageManager packageManager = context.getPackageManager();
        String versionName = "";
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(),0);
            versionName = packageInfo.versionName+"";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
}
