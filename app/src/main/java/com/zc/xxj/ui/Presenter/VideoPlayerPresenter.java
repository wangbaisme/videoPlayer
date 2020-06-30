package com.zc.xxj.ui.Presenter;

import com.zc.xxj.bean.ModuleInfo;
import com.zc.xxj.ui.Contract.VideoPlayerContract;
import com.zc.xxj.ui.Model.VideoPlayerModel;

import java.util.ArrayList;

public class VideoPlayerPresenter implements VideoPlayerContract.VideoPlayerPresenter {

    private VideoPlayerContract.VideoPlayerModel mVideoPlayerModel;

    public VideoPlayerPresenter(VideoPlayerContract.VideoPlayerView mView){
        mVideoPlayerModel = new VideoPlayerModel() {
            @Override
            public void callbackCurrentVideoInfo(ModuleInfo moduleInfo) {
                mView.refreshCurrentVideoMessage(moduleInfo);
            }

            @Override
            public void callbackTotalVideoList(ArrayList<ModuleInfo> list) {
                mView.refreshTotalVideoList(list);
            }

            @Override
            public void callbackExpandVideoList(ArrayList<ModuleInfo> list) {
                mView.refreshExpandVideoList(list);
            }
        };
    }

    @Override
    public void requestCurrentVideoInfo(String videoId) {
        mVideoPlayerModel.getCurrentVideoInfo(videoId);
    }

    @Override
    public void requestTotalVideoList(String videoId) {
        mVideoPlayerModel.getTotalVideoList(videoId);
    }

    @Override
    public void requestExpandVideoList(String videoId) {
        mVideoPlayerModel.getExpandVideoList(videoId);
    }
}
