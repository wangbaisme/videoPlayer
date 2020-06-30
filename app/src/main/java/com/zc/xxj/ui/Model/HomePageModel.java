package com.zc.xxj.ui.Model;

import android.content.Context;

import com.zc.xxj.ui.Contract.HomePageContract;
import com.zc.xxj.utils.JsonUtil;
import com.zc.xxj.utils.OthersUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class HomePageModel implements HomePageContract.HomePageModel {

    private Context mContext;

    public HomePageModel(Context mContext){
        this.mContext = mContext;
    }

    public void instantiateTag(){
        getTagData();
    }

    private void getTagData(){
        List<String> list = new ArrayList<>();
        list = JsonUtil.getConfigureJson(mContext, OthersUtil.TAGMANAGEMENT_ADDED, OthersUtil.TAGMANAGEMENT_ADDED_ITEM);
        if (JsonUtil.getConfigureJson(mContext, OthersUtil.TAGMANAGEMENT_ADDED, OthersUtil.TAGMANAGEMENT_ADDED_ITEM).size() <= 0){
            list.add("推荐");
            list.add("儿歌");
            list.add("故事");
            list.add("绘本");
            list.add("亲子");
            list.add("百科");
            list.add("国学");
        }
        callbackTagData(list);
    }

    public abstract void callbackTagData(List<String> list);
}
