package com.zc.xxj.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {
    private final String TAG = BaseFragment.class.getSimpleName();

    private boolean isFragmentVisible;
    private boolean isReuseView;
    private boolean isFirstVisible;
    private View rootView;

    public void setUserVisibleHint(boolean isVisiberToUser){
        super.setUserVisibleHint(isVisiberToUser);
        if (rootView == null) return;
        if (isFirstVisible && isVisiberToUser){
            onFragmentFirstVisible();
            isFirstVisible = false;
        }
        if (isVisiberToUser){
            onFragmentVisibilityChange(true);
            isFragmentVisible = true;
            return;
        }
        if (isFragmentVisible){
            isFragmentVisible =false;
            onFragmentVisibilityChange(false);
        }
    }

    public void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        init();
    }

    public void onViewCreated(View view, @Nullable Bundle saveInstanceState){
        if (rootView == null){
            rootView = view;
            if (getUserVisibleHint()){
                if (isFirstVisible){
                    isFirstVisible = false;
                    onFragmentFirstVisible();
                }
                onFragmentVisibilityChange(true);
                isFragmentVisible = true;
            }
        }
        super.onViewCreated(view, saveInstanceState);
    }

    public void onDestroyView(){
        super.onDestroyView();
    }

    public void onDestroy(){
        super.onDestroy();
        init();
    }

    private void init(){
        isFirstVisible = true;
        isReuseView = false;
        isFragmentVisible = false;
        rootView = null;
    }

    protected void reuseView(boolean isReuse){
        isReuseView = isReuse;
    } //设置是否复用view

    protected abstract void onFragmentVisibilityChange(boolean isVisible); //页面可见与否回调，你要干啥

    protected abstract void onFragmentFirstVisible(); //页面第一次可见，你要干啥

    protected boolean isFragmentVisible(){
        return isFragmentVisible;
    }

}
