package com.zc.xxj.ui.Model;

import android.content.Context;

import com.zc.xxj.ui.Contract.TagManagementContract;
import com.zc.xxj.utils.JsonUtil;
import com.zc.xxj.utils.OthersUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public abstract class TagManagementModel implements TagManagementContract.TagManagementModel {

    private Context mContext;

    public TagManagementModel(Context context){
        mContext = context;
    }

    @Override
    public void getTag() {
        getTotalTag();
    }

    public void updateTagInfo(List<String> list, List<String> list1){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                JsonUtil.updateConfigureJson(mContext, list, OthersUtil.TAGMANAGEMENT_ADDED, OthersUtil.TAGMANAGEMENT_ADDED_ITEM);
                JsonUtil.updateConfigureJson(mContext, list1, OthersUtil.TAGMANAGEMENT_NOTADDED, OthersUtil.TAGMANAGEMENT_NOTADDED_ITEM);
            }
        });
    }

    private void getTotalTag(){
        List<String> mAddedList = new ArrayList<>();
        List<String> mNotAddedList = new ArrayList<>();
        if (JsonUtil.getConfigureJson(mContext, OthersUtil.TAGMANAGEMENT_ADDED, OthersUtil.TAGMANAGEMENT_ADDED_ITEM).size() == 0
            || JsonUtil.getConfigureJson(mContext, OthersUtil.TAGMANAGEMENT_NOTADDED, OthersUtil.TAGMANAGEMENT_NOTADDED_ITEM).size() == 0){
            mAddedList.add("推荐");
            mAddedList.add("儿歌");
            mAddedList.add("故事");
            mAddedList.add("绘本");
            mAddedList.add("亲子");
            mAddedList.add("百科");
            mAddedList.add("国学");
            mAddedList.add("手工");
            mAddedList.add("识字");
            mAddedList.add("数学");
            mAddedList.add("英语");

            mNotAddedList.add("美术");
            mNotAddedList.add("舞蹈");
            mNotAddedList.add("音乐");
            mNotAddedList.add("诗词");
            mNotAddedList.add("口才");
            mNotAddedList.add("运动");
            mNotAddedList.add("电影");
            mNotAddedList.add("动画");
            mNotAddedList.add("益智");
            mNotAddedList.add("玩具");
            mNotAddedList.add("早教");
            mNotAddedList.add("启蒙认知");
            mNotAddedList.add("交通工具");
            mNotAddedList.add("探索");
            mNotAddedList.add("冒险");
            mNotAddedList.add("其他");
        }else {
            mAddedList = JsonUtil.getConfigureJson(mContext, OthersUtil.TAGMANAGEMENT_ADDED, OthersUtil.TAGMANAGEMENT_ADDED_ITEM);
            mNotAddedList = JsonUtil.getConfigureJson(mContext, OthersUtil.TAGMANAGEMENT_NOTADDED, OthersUtil.TAGMANAGEMENT_NOTADDED_ITEM);
        }
        callbackTotalTag(mAddedList, mNotAddedList);
    }

    public abstract void callbackTotalTag(List<String> mAddedTagList, List<String> mNotAddedTagList);
}
