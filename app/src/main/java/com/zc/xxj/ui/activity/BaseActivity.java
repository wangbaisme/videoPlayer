package com.zc.xxj.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.zc.xxj.utils.SystemUtil;

public class BaseActivity extends AppCompatActivity {

    protected Context mContext;
    protected int screenWidth;

    private String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private final int permissionCode = 1001;

    public void onCreate(Bundle saveInstanceState){
        super.onCreate(null);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = this;
        screenWidth = SystemUtil.getScreenWidth(mContext);
        SystemUtil.hideSupportActionBar(this, true, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            registerPermission();
    }

    private void registerPermission(){
        for (int i=0; i<permissions.length; i++){
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, permissionCode);
                return;
            }
        }
    }
}
