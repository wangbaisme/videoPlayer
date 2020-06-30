package com.zc.xxj.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.zc.xxj.R;
import com.zc.xxj.adapter.HomePageFragmentPagerAdapter;
import com.zc.xxj.ui.Contract.HomePageContract;
import com.zc.xxj.ui.Presenter.HomePagePresenter;
import com.zc.xxj.ui.activity.SearchActivity;
import com.zc.xxj.ui.activity.TagManagementActivity;
import com.zc.xxj.utils.OthersUtil;


public class HomePageFragment extends BaseFragment implements View.OnClickListener, HomePageContract.HomePageView {
    private static final String TAG = HomePageFragment.class.getSimpleName();

    private static HomePageFragment fragment;
    private TabLayout mTabHomePage;
    private ViewPager mViewPagerHomePage;
    private ImageView mBtnTagSettings;
    private ImageView mBtnVoiceSearch;
    private HomePageFragmentPagerAdapter mHomePageAdapter;
    private EditText mSearchView;

    private List<Fragment> mModuleFragment = new ArrayList<>();
    private HomePageContract.HomePagePresenter mHomePagePresenter;

    public HomePageFragment(){}

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_tag_settings:
                Intent intent = new Intent(getActivity(), TagManagementActivity.class);
                startActivityForResult(intent, OthersUtil.HomePageFragment_requestCode);
                break;
            case R.id.search_editText:
                gotoSearchView(false);
                break;
            case R.id.btn_voice_search:
                gotoSearchView(true);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OthersUtil.HomePageFragment_requestCode && resultCode == Activity.RESULT_OK){
            mModuleFragment.clear();
            List<String> list = data.getStringArrayListExtra(OthersUtil.TAGMANAGEMENT_ADDED_TAGLIST);
            for (int i = 0; i < list.size(); i++){
                mModuleFragment.add(instantiateFragment(mViewPagerHomePage, i, new ItemFragment(list.get(i))));
            }
            mHomePageAdapter.updateData(mModuleFragment);
            for (int i = 0; i < list.size(); i++) {
                mTabHomePage.getTabAt(i).setText(list.get(i));
            }
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        super.onCreateView(inflater,container,saveInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home_page, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        mHomePagePresenter = new HomePagePresenter(getActivity(), this);

        mTabHomePage = view.findViewById(R.id.tablayout_home_page);
        mViewPagerHomePage = view.findViewById(R.id.viewpager_home_page);
        mBtnTagSettings = view.findViewById(R.id.btn_tag_settings);
        mBtnTagSettings.setOnClickListener(this);
        mBtnVoiceSearch = view.findViewById(R.id.btn_voice_search);
        mBtnVoiceSearch.setOnClickListener(this);
        mSearchView = view.findViewById(R.id.search_editText);
        mSearchView.setOnClickListener(this);

        mHomePagePresenter.getHomePageTag();
    }

    private Fragment instantiateFragment(ViewPager viewPager, int position, Fragment defaultFragment){
        String tag = "android:switcher:" + viewPager.getId() + ":" + position;
        Fragment fragment = getFragmentManager().findFragmentByTag(tag);
        return fragment == null ? defaultFragment : fragment;
    }

    @Override
    protected void onFragmentVisibilityChange(boolean isVisible) {

    }

    @Override
    protected void onFragmentFirstVisible() {

    }

    @Override
    public void refreshView(List<String> list) {
        mModuleFragment.clear();
        for (int i = 0; i < list.size(); i++){
            mModuleFragment.add(instantiateFragment(mViewPagerHomePage, i, new ItemFragment(list.get(i))));
        }
        mViewPagerHomePage.setOffscreenPageLimit(list.size()-1);

        mHomePageAdapter = new HomePageFragmentPagerAdapter(getFragmentManager(),mModuleFragment);
        mViewPagerHomePage.setAdapter(mHomePageAdapter);

        mTabHomePage.setupWithViewPager(mViewPagerHomePage);
        for (int i = 0; i < list.size(); i++){
            mTabHomePage.getTabAt(i).setText(list.get(i));
        }
    }

    private void gotoSearchView(boolean voiceSearch){
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        intent.putExtra("voiceSearch",voiceSearch);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
