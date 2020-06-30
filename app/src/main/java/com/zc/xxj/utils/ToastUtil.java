package com.zc.xxj.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil{
    private final String TAG = ToastUtil.class.getSimpleName();
    private static ToastUtil mToastUtil;
    private Toast mToast;

    private ToastUtil(Context context){
        if (mToast == null)
            mToast = Toast.makeText(context,"",Toast.LENGTH_SHORT);
    }

    public synchronized static ToastUtil getInstance(Context context){
        if (mToastUtil == null)
            mToastUtil = new ToastUtil(context);
        return mToastUtil;
    }

    public void showShortToast(String msg){
        if (mToast == null)return;
        mToast.setText(msg);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    public void showLongToast(String msg){
        if (mToast == null) return;
        mToast.setText(msg);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }
}
