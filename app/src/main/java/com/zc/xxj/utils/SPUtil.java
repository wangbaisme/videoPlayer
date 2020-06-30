package com.zc.xxj.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtil {
    private static final String SPNAME = "xxj";

    private static SPUtil mspUtil;
    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    private SPUtil(){}

    public synchronized static SPUtil getInstance(Context context){
        if (mspUtil == null){
            init(context);
        }
        return mspUtil;
    }

    private static void init(Context context){
        mspUtil = new SPUtil();
        sp = context.getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void putString(String key, String value){
        editor.putString(key,value);
        editor.commit();
    }

    public String getString(String key){
        return sp.getString(key, null);
    }
}
