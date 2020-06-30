package com.zc.xxj.ui.Model;

import android.os.Environment;

import com.zc.xxj.R;
import com.zc.xxj.bean.ModuleInfo;
import com.zc.xxj.ui.Contract.SearchResultContract;

import java.util.ArrayList;

public abstract class SearchResultModel implements SearchResultContract.SearchResultModel {

    private String [] mUrls = {
            Environment.getExternalStorageDirectory().getPath()+"/dxpyyzs.mp4",
            "http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4",
            "http://vfx.mtime.cn/Video/2019/03/19/mp4/190319212559089721.mp4"
    };

    @Override
    public void searchVideoWithKeyword(String keyword) {
        getVideoList(keyword);
    }

    private void getVideoList(String keywork){
        getVideoInfoList();
    }

    private void getVideoInfoList(){
        ArrayList<ModuleInfo> mModuleInfoList = new ArrayList<>();
        for (int i=0; i<8; i++){
            if (i%4 == 0) mModuleInfoList.add(new ModuleInfo("我们连觉也没睡决定连夜赶去拜访艾立克克莱普顿", R.mipmap.img_2,mUrls[0],true));
            if (i%4 == 1) mModuleInfoList.add(new ModuleInfo("如果写不出好的和弦就该在洒满阳光的钢琴前一起吃布丁",R.mipmap.img_3,mUrls[1],false));
            if (i%4 == 2) mModuleInfoList.add(new ModuleInfo("即使全世界都嫌弃这首歌肉麻又俗气我还是要把它献给你",R.mipmap.img_4,mUrls[2],false));
            if (i%4 == 3) mModuleInfoList.add(new ModuleInfo("遇见你的时候所有星星都落到我头上",R.mipmap.img_1,mUrls[0],false));
        }
        callbackWithList(mModuleInfoList);
    }

    public abstract void callbackWithList(ArrayList<ModuleInfo> list);
}
