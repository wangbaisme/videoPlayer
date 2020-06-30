package com.zc.xxj.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zc.xxj.R;

import androidx.annotation.NonNull;


public class AlertPopupWindow extends PopupWindow implements View.OnClickListener, PopupWindow.OnDismissListener {
    private TextView btnCancel;
    private TextView btnConfirm;
    private TextView tvDialogContent;
    private Context mContext;

    private OnInputDialogButtonClickListener onInputDialogButtonClickListener;

    public void setOnInputDialogButtonClickListener(OnInputDialogButtonClickListener onInputDialogButtonClickListener) {
        this.onInputDialogButtonClickListener = onInputDialogButtonClickListener;
    }


    public AlertPopupWindow(@NonNull Context context) {
        super(context);
        mContext = context;
        View mView = LayoutInflater.from(context).inflate(R.layout.dialog_alert, null);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setWidth(((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth());
        setContentView(mView);
        setTouchable(true);
        setFocusable(true);
        setOutsideTouchable(false);
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isOutsideTouchable()) {
                    View mView = getContentView();
                    if (null != mView)
                        mView.dispatchTouchEvent(event);
                }
                return isFocusable() && !isOutsideTouchable();
            }
        });
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initView(mView);
        initEvent();
    }

    private void initView(View view) {
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnConfirm = view.findViewById(R.id.btn_confirm);
        tvDialogContent = view.findViewById(R.id.dialog_alert_content);
        RelativeLayout mRlView = view.findViewById(R.id.rl_view);
        mRlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test","1111...");
            }
        });
    }

    public void setDialogContent(String title) {
        tvDialogContent.setText(title);
    }


    private void initEvent() {
        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        setOnDismissListener(this);
    }


    @Override
    public void onClick(View v) {
        if (onInputDialogButtonClickListener != null) {
            switch (v.getId()) {
                case R.id.btn_cancel:
                    onInputDialogButtonClickListener.onCancel();
                    //dismiss();
                    break;
                case R.id.btn_confirm:
                    onInputDialogButtonClickListener.onConfirm();
                    break;
            }
        }
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1f);
    }


    private void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams layoutParams = ((Activity) mContext).getWindow().getAttributes();
        layoutParams.alpha = alpha;
        ((Activity) mContext).getWindow().setAttributes(layoutParams);
    }

    public void show() {
        setBackgroundAlpha(0.5f);
        showAtLocation(((Activity) mContext).getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    public interface OnInputDialogButtonClickListener {
        void onCancel();

        void onConfirm();
    }

    public void enableCancleButton(){
        btnCancel.setVisibility(View.VISIBLE);
    }

    public void disableCancleButton(){
        btnCancel.setVisibility(View.GONE);
    }
}
