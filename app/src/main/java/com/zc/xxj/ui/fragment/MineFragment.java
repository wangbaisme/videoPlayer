package com.zc.xxj.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zc.xxj.R;
import com.zc.xxj.ui.activity.UserRegistrationActivity;
import com.zc.xxj.utils.ToastUtil;

public class MineFragment extends BaseFragment implements View.OnClickListener {

    private static MineFragment fragment;
    private ImageView mBtnMineSettings;
    private TextView mBtnUserRegistration;
    private TextView mBtnAllOrderForm;
    private LinearLayout mBtnAll;
    private LinearLayout mBtnUnuse;
    private LinearLayout mBtnUsing;
    private LinearLayout mBtnUsed;
    private LinearLayout mBtnRefund;
    private RelativeLayout mBtnExtension;
    private RelativeLayout mBtnAboutUs;
    private RelativeLayout mBtnCustomerService;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_mine_settings:
                showTip("设置");
                break;
            case R.id.user_registration:
                Intent intent = new Intent(getActivity(), UserRegistrationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.btn_all_order_form:
                showTip("查看全部订单");
                break;
            case R.id.btn_all:
                showTip("全部");
                break;
            case R.id.btn_unuse:
                showTip("未使用");
                break;
            case R.id.btn_using:
                showTip("使用中");
                break;
            case R.id.btn_used:
                showTip("已使用");
                break;
            case R.id.btn_refund:
                showTip("退款");
                break;
            case R.id.btn_extension:
                showTip("推广");
                break;
            case R.id.btn_about_us:
                showTip("关于我们");
                break;
            case R.id.btn_customer_service:
                showTip("客服");
                break;
        }
    }

    public MineFragment(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        super.onCreateView(inflater, container, saveInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mine, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        mBtnMineSettings = view.findViewById(R.id.btn_mine_settings);
        mBtnUserRegistration = view.findViewById(R.id.user_registration);
        mBtnAllOrderForm = view.findViewById(R.id.btn_all_order_form);
        mBtnAll = view.findViewById(R.id.btn_all);
        mBtnUnuse = view.findViewById(R.id.btn_unuse);
        mBtnUsing = view.findViewById(R.id.btn_using);
        mBtnUsed = view.findViewById(R.id.btn_used);
        mBtnRefund = view.findViewById(R.id.btn_refund);
        mBtnExtension = view.findViewById(R.id.btn_extension);
        mBtnAboutUs = view.findViewById(R.id.btn_about_us);
        mBtnCustomerService = view.findViewById(R.id.btn_customer_service);

        mBtnMineSettings.setOnClickListener(this);
        mBtnUserRegistration.setOnClickListener(this);
        mBtnAllOrderForm.setOnClickListener(this);
        mBtnAll.setOnClickListener(this);
        mBtnUnuse.setOnClickListener(this);
        mBtnUsing.setOnClickListener(this);
        mBtnUsed.setOnClickListener(this);
        mBtnRefund.setOnClickListener(this);
        mBtnExtension.setOnClickListener(this);
        mBtnAboutUs.setOnClickListener(this);
        mBtnCustomerService.setOnClickListener(this);
    }

    @Override
    protected void onFragmentVisibilityChange(boolean isVisible) {

    }

    @Override
    protected void onFragmentFirstVisible() {

    }

    private void showTip(String msg){
        ToastUtil.getInstance(getActivity()).showShortToast(msg);
    }
}
