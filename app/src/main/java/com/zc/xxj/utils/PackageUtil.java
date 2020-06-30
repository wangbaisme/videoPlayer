package com.zc.xxj.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;

import com.yanzhenjie.nohttp.rest.Response;
import com.zc.xxj.R;
import com.zc.xxj.bean.UpdateBean;
import com.zc.xxj.dialog.AlertPopupWindow;
import com.zc.xxj.nohttp.HttpListener;
import com.zc.xxj.nohttp.WebFactory;

import org.json.JSONObject;

public abstract class PackageUtil {
    public static String getVersionName(Context context){
        PackageManager packageManager=context.getPackageManager();
        PackageInfo packageInfo;
        String versionName="";
        try {
            packageInfo=packageManager.getPackageInfo(context.getPackageName(),0);
            versionName=packageInfo.versionName+"";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    private Context mContext;
    private String result = null;

    public void versionDetect(Context mContext) {
        this.mContext = mContext;
        WebFactory.getInstance().versionCheckUpdate(callback);
    }

    private Handler handler = new Handler();
    private void queryVersion(UpdateBean updatedata) {
        try {
            String verName = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionName;
            if (updatedata != null && !TextUtils.isEmpty(verName) && !TextUtils.isEmpty(updatedata.getVersion())) {
                String newVersion = updatedata.getVersion();
                String minVersion = updatedata.getMinVersion();
                if (minVersion != null && verName.compareToIgnoreCase(minVersion) < 0){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showAlertPopupWindow(updatedata, mContext.getString(R.string.mustUpdate), false);
                        }
                    });
                } else if (verName.compareToIgnoreCase(newVersion) < 0) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showAlertPopupWindow(updatedata, mContext.getString(R.string.updateNote), true);
                        }
                    });
                }else {
                    latestVersion();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void showAlertPopupWindow(UpdateBean updatedata, String dialogContent, boolean showCancleButton){
        AlertPopupWindow mAlertDialog = new AlertPopupWindow(mContext);
        mAlertDialog.setDialogContent(dialogContent);
        if (showCancleButton) mAlertDialog.enableCancleButton();
        else {
            mAlertDialog.setFocusable(false);
            mAlertDialog.setOutsideTouchable(false);
            mAlertDialog.disableCancleButton();
        }
        mAlertDialog.setOnInputDialogButtonClickListener(new AlertPopupWindow.OnInputDialogButtonClickListener() {
            @Override
            public void onCancel() {
                if (showCancleButton) {
                    mAlertDialog.dismiss();
                }
            }

            @Override
            public void onConfirm() {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(String.valueOf(updatedata.getUrl()));
                intent.setData(content_url);
                mContext.startActivity(intent);
                if (showCancleButton) {
                    mAlertDialog.dismiss();
                }
            }
        });
        mAlertDialog.show();
    }

    private HttpListener<String> callback = new HttpListener<String>(){
        public void onSucceed(int what, Response<String> response){
            result = response.get();
            try {
                JSONObject jo = new JSONObject(response.get());
                String data = jo.getString("data");
                JSONObject object = new JSONObject(data);
                String minVersion = object.getString("min_version");
                String version = object.getString("version");
                String minCode = object.getString("min_code");
                String code = object.getString("code");
                String url = object.getString("url");
                UpdateBean updatedata = new UpdateBean(version, minVersion, code, minCode, url);
                queryVersion(updatedata);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public void onFailed(int what, Response<String> response){
            errcheckVersion();
        }
    };

    public abstract void latestVersion();
    public abstract void errcheckVersion();
}
