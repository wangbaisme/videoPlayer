package com.zc.xxj.ui.Contract;

import com.zc.xxj.bean.ModuleInfo;

import java.util.ArrayList;

public interface VideoPlayerContract extends BaseContract {
    interface VideoPlayerModel extends BaseModel{
        void getCurrentVideoInfo(String videoId);
        void getTotalVideoList(String videoId);
        void getExpandVideoList(String videoId);
    }

    interface VideoPlayerPresenter extends BasePresenter{
        void requestCurrentVideoInfo(String videoId);
        void requestTotalVideoList(String videoId);
        void requestExpandVideoList(String videoId);
    }

    interface VideoPlayerView extends BaseView{
        void refreshCurrentVideoMessage(ModuleInfo moduleInfo);
        void refreshTotalVideoList(ArrayList<ModuleInfo> list);
        void refreshExpandVideoList(ArrayList<ModuleInfo> list);
    }
}
