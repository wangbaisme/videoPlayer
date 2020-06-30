package com.zc.xxj.ui.Model;

import android.os.Environment;

import com.zc.player.util.LoggerUtil;
import com.zc.xxj.R;
import com.zc.xxj.bean.ModuleInfo;
import com.zc.xxj.ui.Contract.VideoPlayerContract;

import java.util.ArrayList;
import java.util.Random;

public abstract class VideoPlayerModel implements VideoPlayerContract.VideoPlayerModel {
    @Override
    public void getCurrentVideoInfo(String videoId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<ModuleInfo> list = getVideoInfoList(videoId);
                for (int i=0; i<list.size(); i++){
                    if (videoId.equals(list.get(i).getmVideoName())){
                        callbackCurrentVideoInfo(list.get(i));
                        return;
                    }
                }
            }
        }).start();
    }

    @Override
    public void getTotalVideoList(String videoId) {
        new Thread(
                () -> {
                    ArrayList<ModuleInfo> list = getVideoInfoList(videoId);
                    callbackTotalVideoList(list);
                }
        ).start();
    }

    @Override
    public void getExpandVideoList(String videoId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<ModuleInfo> list = getVideoInfoList(videoId);
                callbackExpandVideoList(list);
            }
        }).start();
    }

    private ArrayList<ModuleInfo> getVideoInfoList(String videoId){
        ArrayList<ModuleInfo> mModuleInfoList = new ArrayList<>();
        for (int i=0; i<18; i++){
            Random random = new Random();
            int index = random.nextInt(4)%4;
            if (index == 0) mModuleInfoList.add(new ModuleInfo("我们连觉也没睡决定连夜赶去拜访艾立克克莱普顿", R.mipmap.img_2,mUrls[0],true));
            else if (index == 1) mModuleInfoList.add(new ModuleInfo("如果写不出好的和弦就该在洒满阳光的钢琴前一起吃布丁",R.mipmap.img_3,mUrls[1],false));
            else if (index == 2) mModuleInfoList.add(new ModuleInfo("即使全世界都嫌弃这首歌肉麻又俗气我还是要把它献给你",R.mipmap.img_4,mUrls[2],false));
            else if (index == 3) mModuleInfoList.add(new ModuleInfo("遇见你的时候所有星星都落到我头上",R.mipmap.img_1,mUrls[0],false));
        }
        return mModuleInfoList;
    }

    private String [] mUrls = {
            Environment.getExternalStorageDirectory().getPath()+"/dxpyyzs.mp4",
            "http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4",
            "http://vfx.mtime.cn/Video/2019/03/19/mp4/190319212559089721.mp4"
    };

    public abstract void callbackCurrentVideoInfo(ModuleInfo moduleInfo);
    public abstract void callbackTotalVideoList(ArrayList<ModuleInfo> list);
    public abstract void callbackExpandVideoList(ArrayList<ModuleInfo> list);

}
