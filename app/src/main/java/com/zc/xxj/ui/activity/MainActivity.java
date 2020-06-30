package com.zc.xxj.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.zc.xxj.R;

import com.zc.xxj.ui.fragment.HomePageFragment;
import com.zc.xxj.ui.fragment.MineFragment;
import com.zc.xxj.ui.fragment.StudyFragment;
import com.zc.xxj.view.NoScrollViewPager;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.main_viewpager)
    NoScrollViewPager mMainViewPager;
    @BindView(R.id.main_tablayout)
    TabLayout mMainTabLayout;
    @BindView(R.id.ll_fill)
    LinearLayout mLlFill;

    LinearLayout.LayoutParams params;
    LinearLayout.LayoutParams params_ll;
    private FragmentPagerAdapter mPagerAdapter;
    private List<Fragment> mFragmentlist = new ArrayList<>();
    private String[] mItems = {"首页","我的学习","我的"};

    public void onCreate(Bundle saveInstanceState){
        super.onCreate(null);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        initViewPager();
    }

    private void init(){
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, screenWidth*137/750);
        params_ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, screenWidth*92/750);
    }

    private void initViewPager(){
        mMainViewPager.setNoScroll(true);
        mFragmentlist.clear();
        mFragmentlist.add(instantiateFragment(mMainViewPager,0, new HomePageFragment()));
        mFragmentlist.add(instantiateFragment(mMainViewPager,1, new StudyFragment()));
        mFragmentlist.add(instantiateFragment(mMainViewPager,2, new MineFragment()));
        mMainViewPager.setOffscreenPageLimit(mFragmentlist.size() - 1);
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return mFragmentlist.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentlist.size();
            }
        };
        mMainViewPager.setAdapter(mPagerAdapter);
        mLlFill.setLayoutParams(params_ll);
        mMainTabLayout.setLayoutParams(params);
        mMainTabLayout.setupWithViewPager(mMainViewPager, false);
        for (int i=0; i<mItems.length; i++){
            mMainTabLayout.getTabAt(i).setCustomView(makeTabLayoutView(mItems[i], i));
        }
    }

    private Fragment instantiateFragment(ViewPager viewPager, int position, Fragment defaultResult){
        String tag = "android:switcher:" + viewPager.getId() + ":" + position;
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        return fragment == null ? defaultResult : fragment;
    }

    private View makeTabLayoutView(String name, int i){
        View view = LayoutInflater.from(mContext).inflate(R.layout.tablayout_main, null);
        view.setTag(name);
        ImageView itemImg = view.findViewById(R.id.tablayout_main_img);
        TextView itemName = view.findViewById(R.id.tablayout_main_text);
        itemName.setText(name);
        if (name.equals(mItems[0])) itemImg.setImageResource(R.drawable.tablayout_rank_sele);
        else if (name.equals(mItems[1])) itemImg.setImageResource(R.drawable.tablayout_learning_sele);
        else itemImg.setImageResource(R.drawable.tablayout_mine_sele);
        changeViewSize(itemImg, i);
        return view;
    }

    private void changeViewSize(ImageView view, int i){
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                if (i != 1) return;
                RelativeLayout.LayoutParams params;
                if (i == 1)
                    params = new RelativeLayout.LayoutParams(view.getWidth()*195/176,view.getHeight()*195/176);
                else
                    params = new RelativeLayout.LayoutParams(view.getWidth()*3/4,view.getHeight()*3/4);

//                params = new RelativeLayout.LayoutParams(view.getWidth()*65/44,view.getHeight()*65/44);
                params.addRule(RelativeLayout.ABOVE,R.id.tablayout_main_text);
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                view.setLayoutParams(params);
            }
        });
    }
}
