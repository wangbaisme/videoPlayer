package com.zc.xxj.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yanzhenjie.nohttp.rest.Response;
import com.zc.xxj.R;
import com.zc.xxj.nohttp.APIServer;
import com.zc.xxj.nohttp.HttpListener;
import com.zc.xxj.utils.StatusBarUtil;
import com.zc.xxj.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginByCodeActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener,
        CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.phone_text)
    EditText mPhoneText;
    @BindView(R.id.code_text)
    EditText mCodeText;
    @BindView(R.id.btn_login)
    TextView mBtnLogin;
    @BindView(R.id.login_load)
    ProgressBar mLoginLoad;
    @BindView(R.id.btn_get_code)
    TextView mBtnGetCode;
    @BindView(R.id.cb_agreement)
    CheckBox mCbAgreement;
    @BindView(R.id.platform_agreement)
    TextView mPlatformAgreement;
    @BindView(R.id.privacy_agreement)
    TextView mPrivacyAgreement;
    @BindView(R.id.ll_status_bar)
    LinearLayout mStatusBar;
    @BindView(R.id.page_bg)
    LinearLayout mPage;
    @BindView(R.id.btn_back)
    ImageView mBtnBack;

    private boolean isConfirmAgreement = false;
    private boolean isFirstUse;
    private String phone;
    private int timed;

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event=msg.arg1;
            int result=msg.arg2;
            if (event == 10001){ //倒计时
                if (result >= 1){
                    timed = result - 1;
                    mBtnGetCode.setText(result + getString(R.string.s_over_get));
                    mBtnGetCode.setClickable(false);
                    handler.postDelayed(autoTimedTask,1000);
                }else {
                    mBtnGetCode.setClickable(true);
                    mBtnGetCode.setText(getString(R.string.get_verification_code));
                }
                return;
            }
        }

    };

    Runnable autoTimedTask = new Runnable() {
        @Override
        public void run() {
            Message msgTime=new Message();
            msgTime.arg1 = 10001; //事件
            msgTime.arg2 = timed; //时间
            handler.sendMessage(msgTime);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_get_code:
                if (mPhoneText.getText().toString().isEmpty()) {
                    ToastUtil.getInstance(mContext).showShortToast(getString(R.string.number_is_null));
                }else {
                    phone = mPhoneText.getText().toString();
                    Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                    Matcher m = p.matcher(phone);
                    phone = m.replaceAll("");
                    if (phone.length() != 11 || Integer.valueOf(phone.substring(0,1)) != 1){
                        ToastUtil.getInstance(mContext).showShortToast(getString(R.string.please_enter_the_correct_number));
                    } else{
                        Message msg=new Message();
                        msg.arg1 = 10001; //事件
                        msg.arg2 = 60; //时间
                        handler.sendMessage(msg);

                    }
                }
                break;
            case R.id.btn_login:
                if (!isConfirmAgreement) {
                    ToastUtil.getInstance(mContext).showShortToast(getString(R.string.please_agree_agreement));
                    break;
                }else {
                    if (mPhoneText.getText().toString().isEmpty()){
                        ToastUtil.getInstance(mContext).showShortToast(getString(R.string.phone_not_allow_null));
                        break;
                    }
                    if (mCodeText.getText().toString().isEmpty()){
                        ToastUtil.getInstance(mContext).showShortToast(getString(R.string.code_not_allow_null));
                        break;
                    }

                }
                break;
            case R.id.platform_agreement:
                Intent i_platform = new Intent(this, WebViewActivity.class);
                i_platform.putExtra("title",mPlatformAgreement.getText().toString());
                i_platform.putExtra("url", APIServer.platformAgreement);
                i_platform.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i_platform);
                break;
            case R.id.privacy_agreement:
                Intent i_privacy = new Intent(this, WebViewActivity.class);
                i_privacy.putExtra("title",mPrivacyAgreement.getText().toString());
                i_privacy.putExtra("url", APIServer.privacyAgreement);
                i_privacy.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i_privacy);
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        try {
            if (i == EditorInfo.IME_ACTION_DONE || keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                switch (textView.getId()){
                    case R.id.code_text:
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null){
                            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                        }
                        break;
                }
            }
        }catch (Exception e){}
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.cb_agreement:
                if (b) isConfirmAgreement = true;
                else isConfirmAgreement = false;
                break;
        }
    }


    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_login_by_code);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        mStatusBar.setLayoutParams(StatusBarUtil.statusBarParams(mContext));

//        if (isFirstUse = JsonUtil.getFirstUse(mContext)){
//            mCbAgreement.setChecked(true);
//            isConfirmAgreement = true;
//        }

        mCbAgreement.setOnCheckedChangeListener(this);
        mCodeText.setOnEditorActionListener(this);

        Drawable drawable1 = getResources().getDrawable(R.mipmap.account);
        drawable1.setBounds(0, 0, 50, 50);
        mPhoneText.setCompoundDrawables(drawable1, null, null, null);

        Drawable drawable2 = getResources().getDrawable(R.mipmap.code);
        drawable2.setBounds(0, 0, 50, 50);
        mCodeText.setCompoundDrawables(drawable2, null, null, null);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, screenWidth*280/750);
        mPage.setLayoutParams(params);

        mBtnLogin.setOnClickListener(this);
        mPlatformAgreement.setOnClickListener(this);
        mPrivacyAgreement.setOnClickListener(this);
        mBtnGetCode.setOnClickListener(this);
        mBtnBack.setOnClickListener(this);
    }

    private HttpListener<String> callback = new HttpListener<String>(){
        public void onStart(int what){}

        public void onFinish(int what){}

        public void onSucceed(int what, Response<String> response){

        }
        public void onFailed(int what, Response<String> response){

        }

    };
}
