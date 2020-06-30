package com.zc.xxj.ui.Model;

import android.content.Context;

import com.zc.xxj.ui.Contract.SearchContract;
import com.zc.xxj.utils.JsonUtil;
import com.zc.xxj.utils.OthersUtil;

import java.util.List;

public abstract class SearchModel implements SearchContract.SearchModel {

    private Context mContext;

    public SearchModel(Context context){
        this.mContext = context;
    }

    private void updateSearchHistoryData(List<String> list){
        JsonUtil.updateHistoryJson(mContext, list, OthersUtil.SPSEARCHHISTORY, OthersUtil.SPSEARCHHISTORY_ITEM);
    }


    private void historyData(){
        List<String> list = JsonUtil.getConfigureJson(mContext, OthersUtil.SPSEARCHHISTORY, OthersUtil.SPSEARCHHISTORY_ITEM);
        callbackSearchHistory(list);
    }

    private void hotRecommentData(){
        List<String> list = JsonUtil.getConfigureJson(mContext, OthersUtil.TAGMANAGEMENT_NOTADDED, OthersUtil.TAGMANAGEMENT_NOTADDED_ITEM);
        callbackHotRecomment(list);
    }


    @Override
    public void getSearchHistory() {
        historyData();
    }

    @Override
    public void getHotRecomment() {
        hotRecommentData();
    }

    @Override
    public void updateSearchHistory(List<String> list) {
        updateSearchHistoryData(list);
    }

    public abstract void callbackSearchHistory(List<String> list);
    public abstract void callbackHotRecomment(List<String> list);
}
