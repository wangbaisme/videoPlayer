package com.zc.xxj.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener,
        CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.account_text)
    EditText mAccountText;
    @BindView(R.id.password_text)
    EditText mPasswordText;
    @BindView(R.id.btn_remember_password)
    CheckBox mCheckRememberPassword;
    @BindView(R.id.btn_registration)
    TextView mBtnRegistration;
    @BindView(R.id.btn_login_by_other)
    TextView mBtnLoginByOther;
    @BindView(R.id.btn_login)
    TextView mBtnLogin;
    @BindView(R.id.login_load)
    ProgressBar mLoginLoad;
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
    private boolean isRememberPassword;
    private boolean isFirstUse;
    private String phone;
    private String password;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_registration:
                Intent i_registration = new Intent(this, RegistrationActivity.class);
                i_registration.putExtra("from_activity","registration");
                i_registration.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i_registration);
                break;
            case R.id.btn_login:
                if (!isConfirmAgreement) {
                    ToastUtil.getInstance(mContext).showShortToast(getString(R.string.please_agree_agreement));
                    break;
                }else {
                    if (mAccountText.getText().toString().isEmpty()){
                        ToastUtil.getInstance(mContext).showShortToast(getString(R.string.account_not_allow_null));
                        break;
                    }
                    if (mPasswordText.getText().toString().isEmpty()){
                        ToastUtil.getInstance(mContext).showShortToast(getString(R.string.password_not_allow_null));
                        break;
                    }

                }
                break;
            case R.id.btn_login_by_other:
                Intent i_login = new Intent(this, LoginByCodeActivity.class);
                i_login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i_login);
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
                    case R.id.account_text:
                        mAccountText.clearFocus();
                        mPasswordText.setFocusable(true);
                        mPasswordText.setFocusableInTouchMode(true);
                        mPasswordText.requestFocus();
                        break;
                    case R.id.password_text:
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
            case R.id.btn_remember_password:
                if (b) {
                    isRememberPassword = true;

                } else {
                    isRememberPassword = false;

                }
                break;
            case R.id.cb_agreement:
                if (b) isConfirmAgreement = true;
                else isConfirmAgreement = false;
                break;
        }
    }


    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
    }

    private void init(){

        mLoginLoad.setVisibility(View.GONE);
        mBtnBack.setVisibility(View.GONE);

//        PackageUtil packageUtil = new PackageUtil() {
//            @Override
//            public void latestVersion() {
//            }
//            @Override
//            public void errcheckVersion() {
//            }
//        };
//        packageUtil.versionDetect(mContext);

        mPasswordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mStatusBar.setLayoutParams(StatusBarUtil.statusBarParams(mContext));

        Drawable drawable1 = getResources().getDrawable(R.mipmap.account);
        drawable1.setBounds(0, 0, 50, 50);
        mAccountText.setCompoundDrawables(drawable1, null, null, null);

        Drawable drawable2 = getResources().getDrawable(R.mipmap.password);
        drawable2.setBounds(0, 0, 50, 50);
        mPasswordText.setCompoundDrawables(drawable2, null, null, null);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, screenWidth*280/750);
        mPage.setLayoutParams(params);

//        if (isRememberPassword = JsonUtil.getBoxCheck(mContext)){
//            mCheckRememberPassword.setChecked(true);
//        }
//
//        if (isFirstUse = JsonUtil.getFirstUse(mContext)){
//            mCbAgreement.setChecked(true);
//            isConfirmAgreement = true;
//        }
//
//        if (JsonUtil.getPasswordState(mContext)){
//            String mainInfo = JsonUtil.getUserMainInfo(mContext);
//            if (mainInfo != null){
//                String[] userinfo = mainInfo.split("@");
//                mAccountText.setText(userinfo[0]);
//                if (userinfo.length == 2){
//                    mPasswordText.setText(userinfo[1]);
//                }
//            }
//        }

        mAccountText.setOnEditorActionListener(this);
        mPasswordText.setOnEditorActionListener(this);
        mCheckRememberPassword.setOnCheckedChangeListener(this);
        mCbAgreement.setOnCheckedChangeListener(this);
        mBtnRegistration.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mPlatformAgreement.setOnClickListener(this);
        mPrivacyAgreement.setOnClickListener(this);
        mBtnLoginByOther.setOnClickListener(this);
        mBtnBack.setOnClickListener(this);

    }

    private HttpListener<String> callback = new HttpListener<String>(){
        public void onStart(int what){
            mLoginLoad.setVisibility(View.VISIBLE);
        }

        public void onFinish(int what){
            mLoginLoad.setVisibility(View.GONE);
        }

        public void onSucceed(int what, Response<String> response){

        }

        public void onFailed(int what, Response<String> response){

        }
    };
}
