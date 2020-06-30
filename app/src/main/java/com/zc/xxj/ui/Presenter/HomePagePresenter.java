package com.zc.xxj.ui.Presenter;

import android.content.Context;

import com.zc.xxj.ui.Contract.HomePageContract;
import com.zc.xxj.ui.Model.HomePageModel;

import java.util.List;

public class HomePagePresenter implements HomePageContract.HomePagePresenter {

    private HomePageContract.HomePageModel mIHomePageModel;

    public HomePagePresenter(Context context, HomePageContract.HomePageView mView){
        mIHomePageModel = new HomePageModel(context) {
            @Override
            public void callbackTagData(List<String> list) {
                mView.refreshView(list);
            }
        };
    }

    public void getHomePageTag(){
        getHomePageTagData();
    }

    private void getHomePageTagData(){
        mIHomePageModel.instantiateTag();
    }
}
