package com.zc.xxj.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.zc.xxj.R;
import com.zc.xxj.utils.AliyunSmsUtils;
import com.zc.xxj.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserRegistrationActivity extends BaseActivity implements View.OnClickListener {
    static final String TAG = UserRegistrationActivity.class.getSimpleName();

    @BindView(R.id.btn_get_code)
    TextView mBtnGetCode;
    @BindView(R.id.btn_confrim)
    TextView mBtnConfrim;
    @BindView(R.id.et_code)
    EditText mEtCode;
    @BindView(R.id.et_phone)
    EditText mEtPhone;

    private String phone = "";
    private int timed = 0;

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event=msg.arg1;
            int result=msg.arg2;
            Object data=msg.obj;
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
            msgTime.obj = null;
            handler.sendMessage(msgTime);
        }
    };

    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        setContentView(R.layout.activity_user_registration);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        mBtnGetCode.setOnClickListener(this);
        mBtnConfrim.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_get_code:

                if (mEtPhone.getText().toString().isEmpty())
                    showMsgToast(getString(R.string.number_is_null));
                else if (mEtPhone.getText().toString().length() != 11
                        || Integer.valueOf(mEtPhone.getText().toString().substring(0,1)) != 1)
                    showMsgToast(getString(R.string.please_enter_the_correct_number));
                else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String callbackInfo = null;
                            try {
                                phone = mEtPhone.getText().toString();
                                AliyunSmsUtils.setNewcode();
                                callbackInfo = AliyunSmsUtils.SendSms(phone, AliyunSmsUtils.getNewcode(), AliyunSmsUtils.UserRegistration);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (callbackInfo != null){
                                Message msg=new Message();
                                msg.arg1 = 10001; //事件
                                msg.arg2 = 60; //时间
                                msg.obj = null;
                                handler.sendMessage(msg);
                            }
                        }
                    }).start();
                }


                break;
            case R.id.btn_confrim:

                if (mEtPhone.getText().toString().isEmpty() || mEtCode.getText().toString().isEmpty()){
                    showMsgToast(getString(R.string.verification_code_is_null));
                    return;
                }else {
                    if (mEtPhone.getText().toString().equals(phone) && AliyunSmsUtils.getNewcode().equals(mEtCode.getText().toString())){
                        ToastUtil.getInstance(mContext).showLongToast(getString(R.string.registration_succeed));
                    }
                }


                break;
            default:
                break;
        }
    }

    private void showMsgToast(String content){
        ToastUtil.getInstance(getApplicationContext()).showShortToast(content);
    }
}
