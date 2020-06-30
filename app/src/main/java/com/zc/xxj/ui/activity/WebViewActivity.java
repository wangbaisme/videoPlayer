package com.zc.xxj.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zc.xxj.R;
import com.zc.xxj.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.ll_status_bar)
    LinearLayout mLlStatusBar;
    @BindView(R.id.web_view)
    WebView mWebView;
    @BindView(R.id.title_tv)
    TextView mTitle;



    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        mLlStatusBar.setLayoutParams(StatusBarUtil.statusBarParams(mContext));

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mTitle.setText(getIntent().getStringExtra("title"));
        String url = getIntent().getStringExtra("url");

        if (!TextUtils.isEmpty(url)) {
            mWebView.loadUrl(url);
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setSupportZoom(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
        }
    }
}
