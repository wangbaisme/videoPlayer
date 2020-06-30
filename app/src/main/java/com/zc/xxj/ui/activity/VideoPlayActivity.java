package com.zc.xxj.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.zc.player.player.VideoPlayer;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

import com.zc.player.util.LoggerUtil;
import com.zc.xxj.adapter.SeriesDetailedAdapter;
import com.zc.xxj.adapter.VideoAdapter;
import com.zc.xxj.bean.CommentBean;
import com.zc.xxj.bean.CommentDetailBean;
import com.zc.xxj.bean.ModuleInfo;
import com.zc.xxj.bean.ReplyDetailBean;
import com.zc.xxj.ui.Contract.VideoPlayerContract;
import com.zc.xxj.ui.Presenter.VideoPlayerPresenter;
import com.zc.xxj.ui.fragment.CommentFragment;
import com.zc.xxj.ui.fragment.CoursesFragment;
import com.zc.xxj.ui.fragment.SynopsisFragment;
import com.zc.xxj.utils.AudioMngHelper;
import com.zc.xxj.utils.NetUtil;
import com.zc.xxj.utils.SystemUtil;
import com.zc.xxj.utils.TimeUtil;
import com.zc.xxj.utils.ToastUtil;
import com.zc.xxj.view.FlowLayout;
import com.zc.xxj.view.NoScrollViewPager;
import com.zc.xxj.view.VerticalProgress;
import com.zc.xxj.view.ViewControlFrameLayout;
import com.zc.xxj.R;

public class VideoPlayActivity extends BaseActivity implements View.OnClickListener, VideoPlayer.OnVideoPlayerStateListener
        , ViewControlFrameLayout.OnTwoPointEventListener, VideoAdapter.OnCompleteListener, VideoPlayerContract.VideoPlayerView {
    private final String TAG = VideoPlayActivity.class.getSimpleName();

    @BindView(R.id.video_view_control)
    ViewControlFrameLayout mVideoControl;
    @BindView(R.id.ll_change)
    LinearLayout mLlChange;
    @BindView(R.id.ll_content)
    LinearLayout mLlContent;
    @BindView(R.id.ll_full_list_control)
    LinearLayout mVideoListViewFull;
    @BindView(R.id.loading_video_list)
    LinearLayout mLoadingTip;
    @BindView(R.id.btn_full)
    ImageView mBtnFull;
    @BindView(R.id.btn_full_show_list)
    TextView mBtnShowViewList;
    @BindView(R.id.btn_full_hide_list)
    TextView mBtnHideVeiwList;
    @BindView(R.id.view_video_player)
    VideoPlayer mVideoPlayer;
    @BindView(R.id.video_list_view)
    RecyclerView mVideoListView;
    @BindView(R.id.speed_bar)
    SeekBar mSpeedBar;
    @BindView(R.id.tv_current_time)
    TextView mCurrentTimeText;
    @BindView(R.id.tv_count_time)
    TextView mMaxTimeText;
    @BindView(R.id.video_control_tail)
    LinearLayout mVideoControlTail;
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.brightness_bar)
    VerticalProgress mBrightnessBar;
    @BindView(R.id.volume_bar)
    VerticalProgress mVolumeBar;
    @BindView(R.id.speed_view)
    LinearLayout mSpeedView;
    @BindView(R.id.time_text)
    TextView mTimeText;
    @BindView(R.id.time_text_bar)
    ProgressBar mTimeTextBar;
    @BindView(R.id.buffer_loading)
    RelativeLayout mBufferLoadingView;
    @BindView(R.id.buffer_speed)
    TextView mBufferSpeed;
    @BindView(R.id.video_title)
    TextView mVideoTitle;
    @BindView(R.id.play_or_pause)
    ImageView btnPlayOrPause;
    @BindView(R.id.btn_play_pause)
    ImageView btnPlayPause;

    @BindView(R.id.lecturer_img)
    ImageView mLecturerImg;
    @BindView(R.id.lecturer_name)
    TextView mLecturerName;
    @BindView(R.id.btn_share)
    ImageView mBtnShare;
    @BindView(R.id.btn_collect)
    LikeButton mBtnCollect;
    @BindView(R.id.course_title)
    TextView mCourseTitle;
    @BindView(R.id.flow_layout_tag)
    FlowLayout mTagFlowLayout;
    @BindView(R.id.danmaku_view)
    DanmakuView danmakuView;
    @BindView(R.id.rl_video_view)
    RelativeLayout mRlVideoView;
    @BindView(R.id.ll_gravity01)
    LinearLayout mLlGravity01;
    @BindView(R.id.ll_gravity02)
    LinearLayout mLlGravity02;


    @BindView(R.id.comment_tools)
    RelativeLayout mCommentTool;
    @BindView(R.id.tab_layout)
    TabLayout mMainTab;
    @BindView(R.id.view_pager)
    NoScrollViewPager mMainPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.detail_page_do_comment)
    TextView bt_comment;

    private BottomSheetDialog dialog;
    private ArrayList<CommentDetailBean> commentsList;
    private CommentBean commentBean;
    private String[] mItems = {"课程","简介","评论"};
    private boolean isShowMore = false;

    private List<Fragment> mFragmentlist = new ArrayList<>();
    private FragmentPagerAdapter mPagerAdapter;
    private CommentFragment fragment;
    private CoursesFragment cFragment;



    private Context mContext;
    private ArrayList<ModuleInfo> totalVideoList = new ArrayList<>();
    private AudioMngHelper mAudioManager;
    private VideoAdapter mWinFullTotalAdapter;
    private boolean isWindowFull = false;
    private boolean isShowVideoList_Full = false;
    private boolean isVideoControlHide = false;
    private boolean isVideoPause = false;
    private boolean isVideoPrepared = false;
    private boolean isloading = false;
    private long lastHideVolumeBarTime = 0;
    private long lastHideControlTime = 0;
    private long lastHideBrightnessBarTime = 0;
    private long lastShowBufferloadingTime = 0;
    private long lastHideBtnPlayOrPauseTime = 0;
    private long mVideoCurrentTime = -1;
    private float mVideoContentRatioXY = 0;
    private int mVideoViewWidth, mVideoViewHeight;
    private int mAppCurrentBrightness = -1;
    private int mAudioStep = -1;
    private int mCurrentChangeVolume = 0;
    private int mCurrentVolume = -1;
    private int onGlobalIndex = 0;
    private int onGlobalIndex1 = 0;
    private String videoTitle;
    private VideoPlayerContract.VideoPlayerPresenter mVideoPlayerPresenter;
    private boolean showDanmaku;

    private DanmakuContext danmakuContext;

    List<String> list = Arrays.asList( "绘本", "亲子互动", "百科", "国学经典", "手工课");

    String [] mUrls = {
            Environment.getExternalStorageDirectory().getPath()+"/dxpyyzs.mp4",
            "http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4",
            "http://vfx.mtime.cn/Video/2019/03/19/mp4/190319212559089721.mp4"
    };


    @Override
    public void refreshCurrentVideoMessage(ModuleInfo moduleInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("currentVideoMessage",moduleInfo);
        Message msg = new Message();
        msg.what = LAODCURRENTVIDEO;
        msg.setData(bundle);
        mHandle.handleMessage(msg);
    }

    @Override
    public void refreshTotalVideoList(ArrayList<ModuleInfo> list) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("TotalVideoList",list);
        Message msg = new Message();
        msg.what = UPDATETOTALCOURSE;
        msg.setData(bundle);
        mHandle.handleMessage(msg);
    }

    @Override
    public void refreshExpandVideoList(ArrayList<ModuleInfo> list) {
//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList("ExpandVideoList",list);
//        Message msg = new Message();
//        msg.what = UPDATEEXPANDCOURSE;
//        msg.setData(bundle);
//        mHandle.handleMessage(msg);
    }

    //Adapter
    @Override
    public void loadMore() {
//        mHandle.sendEmptyMessage(LAODMORE);
    }

    @Override
    public void resetVideoContent(ModuleInfo mModuleInfo) {
        mVideoPlayer.reset();
        mVideoPlayer.setPath(mModuleInfo.getmVideoUrl());
        try {
            mVideoPlayer.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //VideoPlayer
    @Override
    public void videoStart() {
        if (mVideoPlayer == null) return;
        mHandle.sendEmptyMessage(UPDATEVIDEOCURRENTTIME);
    }

    @Override
    public void videoReset() {
        isVideoPrepared = false;
    }

    @Override
    public void videoState(int stateCode) {
        if (!isShowMore){
            isShowMore = true;
            mHandle.sendEmptyMessage(INITABOUTVIEW);
        }
        switch (stateCode){
            case 701: //开始缓冲
                mHandle.sendEmptyMessage(BUFFERSTART);
                break;
            case 702: //缓存结束
                mHandle.sendEmptyMessage(BUFFEREND);
                break;
            case 703: //网络带宽，网速方面
                break;
            case -10000: ///数据连接中断
                mHandle.sendEmptyMessage(DATAINTERRUPT);
                break;
        }
    }

    @Override
    public void videoCompletion() {
        mHandle.sendEmptyMessage(VIDEOCOMPLETION);
    }

    @Override
    public void videoPrepared() {
        isVideoPrepared = true;
        mHandle.sendEmptyMessage(UPDATEVIDEOCOUNTTIME);
        mHandle.sendEmptyMessage(UPDATEVIDEOCURRENTTIME);
    }

    @Override
    public void videoChange(float width, float height) {
        mVideoContentRatioXY = width / height;
        getVideoPlayerNewSize();
        changeViewTree();
    }

    //VideoPlayerControl
    @Override
    public void twoPointEvent(boolean isShow) {
        if (!isWindowFull) return;
        if (isShow)
            mHandle.sendEmptyMessage(FULLSHOWLIST);
        else
            mHandle.sendEmptyMessage(FULLHIDELIST);
    }

    @Override
    public void onTwoClick() {
        Message msg = new Message();
        msg.what = CHANGEVIDEOPLAYSTATE;
        msg.arg1 = 1;
        mHandle.sendMessage(msg);
    }

    @Override
    public void onOneClick() {
        mHandle.sendEmptyMessage(CHANGEVIDEOCONTROLSTATE);
    }

    @Override
    public void volumeEventFinish() {
        mCurrentVolume = -1; //此处设为-1是为了下次音量调节动画更精准
        lastHideVolumeBarTime = System.currentTimeMillis();
        mHandle.postDelayed(autoTaskHideVolumeBar,1000);
    }

    @Override
    public void brightnessEventFinish() {
        lastHideBrightnessBarTime = System.currentTimeMillis();
        mHandle.postDelayed(autoTaskHideBrightnessBar,1000);
    }

    @Override
    public void volumeEvent(boolean add, int percentage) {
        if (percentage < 2) return;
        if (mCurrentVolume == -1) mCurrentVolume = mAudioManager.get100CurrentVolume();
        percentage = (int)Math.ceil((float)(percentage-1)/2.55);
        if (add) mCurrentVolume = mCurrentVolume + percentage > 100 ? 100 : mCurrentVolume + percentage;
        else mCurrentVolume = mCurrentVolume - percentage < 0 ? 0 : mCurrentVolume - percentage;
        if (System.currentTimeMillis() - lastHideVolumeBarTime < 1000) mHandle.removeCallbacks(autoTaskHideVolumeBar);
        if (mVolumeBar.getVisibility() == View.GONE) mVolumeBar.setVisibility(View.VISIBLE);
        mVolumeBar.setProgress(mCurrentVolume);
        if (mAudioStep == -1)
            mAudioStep = (int) Math.ceil(100/mAudioManager.getSystemMaxVolume());
        mCurrentChangeVolume += percentage;
        if (mCurrentChangeVolume < mAudioStep) return;
        int mVolume = 0;
        if (add)
            mVolume = mAudioManager.get100CurrentVolume() + mCurrentChangeVolume > 100 ? 100 :
                    mAudioManager.get100CurrentVolume() + mCurrentChangeVolume;
        else
            mVolume = mAudioManager.get100CurrentVolume() - mCurrentChangeVolume < 0 ? 0 :
                    mAudioManager.get100CurrentVolume() - mCurrentChangeVolume;
        mAudioManager.setVoice100(mVolume);
        mCurrentChangeVolume = 0;
    }

    @Override
    public void brightnessEvent(boolean add, int percentage) {
        if (percentage < 2) return;
        if (mAppCurrentBrightness == -1)
            mAppCurrentBrightness = SystemUtil.getSystemBrightness(mContext);
        if (add)
            mAppCurrentBrightness = mAppCurrentBrightness + (percentage-1) > 255 ? 255 : mAppCurrentBrightness + (percentage-1);
        else
            mAppCurrentBrightness = mAppCurrentBrightness - (percentage-1) < 0 ? 0 : mAppCurrentBrightness - (percentage-1);
        if (System.currentTimeMillis() - lastHideBrightnessBarTime < 1000) mHandle.removeCallbacks(autoTaskHideBrightnessBar);
        if (mBrightnessBar.getVisibility() == View.GONE) mBrightnessBar.setVisibility(View.VISIBLE);
        mBrightnessBar.setProgress((int)Math.ceil(mAppCurrentBrightness/2.55));
        SystemUtil.changeAPPBrightness(mContext, mAppCurrentBrightness);
    }

    @Override
    public void progressTextEvent(boolean add, int percentage) {
        if (percentage < 2 && !isVideoPrepared) return;
        if (mSpeedView.getVisibility() == View.GONE) mSpeedView.setVisibility(View.VISIBLE);
        if (mVideoCurrentTime == -1) mVideoCurrentTime = mVideoPlayer.getCurrentPosition();
        if (add) mVideoCurrentTime = mVideoCurrentTime + (long)Math.ceil(mVideoPlayer.getDuration()*(percentage-1)/500) > mVideoPlayer.getDuration() ?
                mVideoPlayer.getDuration() : mVideoCurrentTime + (long)Math.ceil(mVideoPlayer.getDuration()*(percentage-1)/500);
        else mVideoCurrentTime = mVideoCurrentTime - (long)Math.ceil(mVideoPlayer.getDuration()*(percentage-1)/500) < 0 ?
                0 : mVideoCurrentTime - (long)Math.ceil(mVideoPlayer.getDuration()*(percentage-1)/500);
        mTimeText.setText(TimeUtil.getTime(mVideoCurrentTime));
        mTimeTextBar.setProgress((int)(mVideoCurrentTime*500/mVideoPlayer.getDuration()));
    }

    @Override
    public void progressSeekEvent() {
        if (mTimeTextBar == null) return;
        mVideoPlayer.seekTo((long) Math.ceil(mVideoPlayer.getDuration()*mTimeTextBar.getProgress()/500));
        mSpeedView.setVisibility(View.GONE);
    }

    private final int LAODMORE = 1001;
    private final int SHOWLOAD = 1002;
    private final int HIDELOAD = 1003;
    private final int SHOWLAND = 1004;
    private final int SHOWPORT = 1005;
    private final int FULLHIDELIST = 1006;
    private final int FULLSHOWLIST = 1007;
    private final int UPDATEVIDEOCOUNTTIME = 1008;
    private final int UPDATEVIDEOCURRENTTIME = 1009;
    private final int VIDEOCOMPLETION = 1010;
    private final int CHANGEVIDEOCONTROLSTATE = 1011;
    private final int BUFFERSTART = 1012;
    private final int BUFFEREND = 1013;
    private final int DATAINTERRUPT = 1014;
    private final int CHANGEVIDEOTITLE = 1015;
    private final int CHANGEVIDEOTAG = 1016;
    private final int UPDATETOTALCOURSE = 1017;
    private final int LAODCURRENTVIDEO = 1019;
    private final int CHANGEVIDEOPLAYSTATE = 1022;
    private final int INITABOUTVIEW = 1023;
    @SuppressLint("HandlerLeak")
    Handler mHandle = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case LAODMORE:
//                    if (isloading) return;
//                    mWinFullTotalAdapter.update(getVideoInfoList(mModuleInfoList.size()/18));
//                    mWinFullTotalAdapter.notifyDataSetChanged();
//                    mHandle.sendEmptyMessage(HIDELOAD);
                    Object lock = new Object();
                    break;
                case SHOWLOAD:
                    isloading = true;
                    mLoadingTip.setVisibility(View.VISIBLE);
                    break;
                case HIDELOAD:
                    isloading = false;
                    mLoadingTip.setVisibility(View.GONE);
                    break;
                case SHOWLAND:
                    isWindowFull = true;
                    getVideoPlayerNewSize();
                    mBtnFull.setImageResource(R.mipmap.not_window_full);
                    mLlContent.setVisibility(View.GONE);
                    mBtnShowViewList.setVisibility(View.VISIBLE);
                    SystemUtil.hideFulltStatusBar(mContext);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    break;
                case SHOWPORT:
                    isWindowFull = false;
                    isShowVideoList_Full = false;
                    mLlGravity01.setVisibility(View.GONE);
                    mLlGravity02.setVisibility(View.GONE);
                    getVideoPlayerNewSize();
                    mBtnFull.setImageResource(R.mipmap.window_full);
                    mLlContent.setVisibility(View.VISIBLE);
                    mVideoListViewFull.setVisibility(View.GONE);
                    mBtnShowViewList.setVisibility(View.GONE);
                    SystemUtil.showNotFulltStatusBar(mContext, Color.BLACK);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    break;
                case FULLHIDELIST:
                    isShowVideoList_Full = false;
                    mLlGravity01.setVisibility(View.GONE);
                    mLlGravity02.setVisibility(View.GONE);
                    getVideoPlayerNewSize();
                    mBtnShowViewList.setVisibility(View.VISIBLE);
                    mVideoListViewFull.setVisibility(View.GONE);
                    break;
                case FULLSHOWLIST:
                    isShowVideoList_Full = true;
                    mLlGravity01.setVisibility(View.VISIBLE);
                    mLlGravity02.setVisibility(View.VISIBLE);
                    getVideoPlayerNewSize();
                    mBtnShowViewList.setVisibility(View.GONE);
                    mVideoListViewFull.setVisibility(View.VISIBLE);
                    break;
                case UPDATEVIDEOCOUNTTIME:
                    mMaxTimeText.setText(TimeUtil.getTime(mVideoPlayer.getDuration()));
                    break;
                case UPDATEVIDEOCURRENTTIME:
                    mCurrentTimeText.setText(TimeUtil.getTime(mVideoPlayer.getCurrentPosition()));
                    if (mVideoPlayer.getDuration() == 0)
                        mSpeedBar.setProgress(0);
                    else
                        mSpeedBar.setProgress((int)(mVideoPlayer.getCurrentPosition()*100/mVideoPlayer.getDuration()));
                    mHandle.postDelayed(autoUpdateTime,1000);
                    break;
                case VIDEOCOMPLETION:
                    mCurrentTimeText.setText(TimeUtil.getTime(mVideoPlayer.getDuration()));
                    mSpeedBar.setProgress(100);
                    break;
                case CHANGEVIDEOCONTROLSTATE:
                    if (!isVideoControlHide){
                        isVideoControlHide = true;
                        mVideoControlTail.setVisibility(View.GONE);
                        if (isWindowFull) mVideoTitle.setVisibility(View.GONE);
                        if (System.currentTimeMillis() - lastHideControlTime < 5000){
                            lastHideControlTime = 0;
                            mHandle.removeCallbacks(autoTaskHideControl);
                        }
                        break;
                    }
                    isVideoControlHide = false;
                    mVideoControlTail.setVisibility(View.VISIBLE);
                    if (isWindowFull) mVideoTitle.setVisibility(View.VISIBLE);
                    lastHideControlTime = System.currentTimeMillis();
                    mHandle.postDelayed(autoTaskHideControl,5000);
                    break;
                case BUFFERSTART:
                    if (mBufferLoadingView.getVisibility() == View.GONE) mBufferLoadingView.setVisibility(View.VISIBLE);
                    lastShowBufferloadingTime = System.currentTimeMillis();
                    mBufferSpeed.setText(NetUtil.getNetSpeed(getApplicationInfo().uid));
                    mHandle.postDelayed(autoUpdateBufferSpeed,2000);
                    break;
                case BUFFEREND:
                    if (mBufferLoadingView.getVisibility() == View.VISIBLE) mBufferLoadingView.setVisibility(View.GONE);
                    if (System.currentTimeMillis() - lastShowBufferloadingTime < 2000) mHandle.removeCallbacks(autoUpdateBufferSpeed);
                    break;
                case DATAINTERRUPT:
                    ToastUtil.getInstance(mContext).showShortToast(getString(R.string.data_interrupt));
                    break;
                case CHANGEVIDEOTITLE:
                    mVideoTitle.setText(videoTitle);
                    mCourseTitle.setText(videoTitle);
                    break;
                case CHANGEVIDEOTAG:
                    mTagFlowLayout.setDeleteMode(false);
                    mTagFlowLayout.setClickMode(false);
                    mTagFlowLayout.showTag(list);
                    ViewTreeObserver vto = mTagFlowLayout.getViewTreeObserver();
                    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            mTagFlowLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    SystemUtil.dip2px(mContext,6) + mTagFlowLayout.getRealHeight());
                            mTagFlowLayout.setLayoutParams(params);
                        }
                    });
                    break;
                case UPDATETOTALCOURSE:
                    totalVideoList = msg.getData().getParcelableArrayList("TotalVideoList");
                    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
                    mVideoListView.setLayoutManager(mLinearLayoutManager);
                    mWinFullTotalAdapter = new VideoAdapter(totalVideoList, true, mContext);
                    mWinFullTotalAdapter.setOnCompleteListener(VideoPlayActivity.this);
                    mVideoListView.setAdapter(mWinFullTotalAdapter);
                    break;
                case LAODCURRENTVIDEO:
                    ModuleInfo moduleInfo = msg.getData().getParcelable("currentVideoMessage");
                    loadVideo(moduleInfo.getmVideoUrl());
                    break;
                case CHANGEVIDEOPLAYSTATE:
                    if (mVideoPlayer == null) return;
                    if (mVideoPlayer.isPlaying()) {
                        mVideoPlayer.pause();
                        btnPlayPause.setImageResource(R.mipmap.pause);
                        if (msg.arg1 == 1) {
                            if (System.currentTimeMillis() - lastHideBtnPlayOrPauseTime < 2000) {
                                mHandle.removeCallbacks(autoHideBtnPlayOrPause);
                            }
                            btnPlayOrPause.setVisibility(View.VISIBLE);
                            btnPlayOrPause.setImageResource(R.mipmap.pause);
                        }
                    }else {
                        mVideoPlayer.start();
                        btnPlayPause.setImageResource(R.mipmap.playing);
                        if (btnPlayOrPause.getVisibility() == View.VISIBLE){
                            btnPlayOrPause.setImageResource(R.mipmap.playing);
                            mHandle.postDelayed(autoHideBtnPlayOrPause,2000);
                        }
                        if (msg.arg1 == 1) {
                            lastHideBtnPlayOrPauseTime = System.currentTimeMillis();
                            btnPlayOrPause.setVisibility(View.VISIBLE);
                            btnPlayOrPause.setImageResource(R.mipmap.playing);
                            mHandle.postDelayed(autoHideBtnPlayOrPause,2000);
                        }
                    }
                    break;
                case INITABOUTVIEW:
                    initAboutView();
                    mHandle.sendEmptyMessage(HIDELOAD);
                    //展示&更换标签
                    mHandle.sendEmptyMessage(CHANGEVIDEOTAG);
                    mHandle.sendEmptyMessage(CHANGEVIDEOTITLE);
                    mHandle.postDelayed(autoTaskHideControl,5000);
                    break;
                default:
                    break;
            }
        }
    };

    Runnable autoUpdateTime = new Runnable() {
        @Override
        public void run() {
            if (mVideoPlayer == null) return;
            if (mVideoPlayer.isPlaying())
                mHandle.sendEmptyMessage(UPDATEVIDEOCURRENTTIME);
        }
    };
    Runnable autoTaskHideControl = new Runnable() {
        @Override
        public void run() {
            mHandle.sendEmptyMessage(CHANGEVIDEOCONTROLSTATE);
        }
    };
    Runnable autoTaskHideVolumeBar = new Runnable() {
        @Override
        public void run() {
            mVolumeBar.setVisibility(View.GONE);
        }
    };
    Runnable autoTaskHideBrightnessBar = new Runnable() {
        @Override
        public void run() {
            mBrightnessBar.setVisibility(View.GONE);
        }
    };
    Runnable autoUpdateBufferSpeed = new Runnable() {
        @Override
        public void run() {
            mHandle.sendEmptyMessage(BUFFERSTART);
        }
    };
    Runnable autoHideBtnPlayOrPause = new Runnable() {
        @Override
        public void run() {
            btnPlayOrPause.setVisibility(View.GONE);
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        mContext = this;
        SystemUtil.hideSupportActionBar(mContext,true,false);
        SystemUtil.keepScreenOn(mContext);
        setContentView(R.layout.activity_video_player);
        ButterKnife.bind(this);
        videoTitle = getIntent().getStringExtra("videoTitle");
        init();
//        initDM();
        setListener();
    }

    public void onResume(){
        super.onResume();
        if (mVideoPlayer == null) return;
        if (!mVideoPlayer.isPlaying() && isVideoPause){
            isVideoPause = false;
            mVideoPlayer.start();
        }
        if (danmakuView != null && danmakuView.isPrepared() && danmakuView.isPaused()) {
            danmakuView.resume();
        }
    }

    public void onPause(){
        super.onPause();
        if (mVideoPlayer == null) return;
        if (mVideoPlayer.isPlaying()){
            isVideoPause = true;
            mVideoPlayer.pause();
        }
        if (danmakuView != null && danmakuView.isPrepared()) {
            danmakuView.pause();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mVideoPlayer == null) return;
        mVideoPlayer.stop();
        mVideoPlayer.release();
        if (danmakuView != null) {
            danmakuView.release();
            danmakuView = null;
        }
    }

    private void init(){
        mAudioManager = new AudioMngHelper(mContext);
        lastHideControlTime = System.currentTimeMillis();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mVideoPlayerPresenter = new VideoPlayerPresenter(VideoPlayActivity.this);
                mVideoPlayerPresenter.requestCurrentVideoInfo(videoTitle);
                mVideoPlayerPresenter.requestTotalVideoList(videoTitle);
                mVideoPlayerPresenter.requestExpandVideoList(videoTitle);
//                mHandle.sendEmptyMessage(INITABOUTVIEW);
            }
        });
        thread.start();
    }

    private void initDM(){
        danmakuView.enableDanmakuDrawingCache(true);
        danmakuView.setCallback(new DrawHandler.Callback() {
            @Override
            public void prepared() {
                showDanmaku = true;
                danmakuView.start();
                generateSomeDanmaku();
            }

            @Override
            public void updateTimer(DanmakuTimer timer) {

            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {

            }

            @Override
            public void drawingFinished() {

            }
        });
        danmakuContext = DanmakuContext.create();
        danmakuView.prepare(parser, danmakuContext);
    }

    private BaseDanmakuParser parser = new BaseDanmakuParser() {
        @Override
        protected IDanmakus parse() {
            return new Danmakus();
        }
    };

    @SuppressLint("SetTextI18n")
    private void setListener(){
        mBtnFull.setOnClickListener(this);
        mBtnShowViewList.setOnClickListener(this);
        mBtnHideVeiwList.setOnClickListener(this);
        mVideoPlayer.setOnVideoPlayerStateListener(this);
        mVideoControl.setTwoPointEventListener(this);
        mBtnBack.setOnClickListener(this);
        mBtnShare.setOnClickListener(this);
        btnPlayPause.setOnClickListener(this);

        mSpeedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCurrentTimeText.setText(TimeUtil.getTime(progress*mVideoPlayer.getDuration()/100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (System.currentTimeMillis() - lastHideControlTime < 5000)
                    mHandle.removeCallbacks(autoTaskHideControl);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mVideoPlayer.seekTo(seekBar.getProgress()*mVideoPlayer.getDuration()/100);
                mHandle.postDelayed(autoTaskHideControl,5000);
            }
        });

        //收藏逻辑
        mBtnCollect.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });
    }

    private void loadVideo(String mUrl){
        try {
            mVideoPlayer.setPath(mUrl);
            mVideoPlayer.load();
        } catch (IOException e) {
            ToastUtil.getInstance(mContext).showShortToast(getString(R.string.playback_failed));
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_full:
                if (!isWindowFull)
                    mHandle.sendEmptyMessage(SHOWLAND);
                else
                    mHandle.sendEmptyMessage(SHOWPORT);
                break;
            case R.id.btn_full_show_list:
                mHandle.sendEmptyMessage(FULLSHOWLIST);
                break;
            case R.id.btn_full_hide_list:
                mHandle.sendEmptyMessage(FULLHIDELIST);
                break;
            case R.id.btn_back:
                if (isWindowFull) {
                    mHandle.sendEmptyMessage(SHOWPORT);
                    break;
                }
                finish();
                break;
            case R.id.btn_share:
                //分享
                ToastUtil.getInstance(mContext).showLongToast("分享");
                break;
            case R.id.btn_play_pause:
                Message msg = new Message();
                msg.what = CHANGEVIDEOPLAYSTATE;
                msg.arg1 = 0;
                mHandle.sendMessage(msg);
                break;
            case R.id.detail_page_do_comment:
                showCommentDialog();
                break;
            default:
                break;
        }
    }

    private void getVideoPlayerNewSize(){
        getVideoViewSize();
        ViewTreeObserver vto = mVideoPlayer.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                onGlobalIndex++;
                if (onGlobalIndex == 3){
                    onGlobalIndex = 0;
                    mVideoPlayer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                mVideoPlayer.setLayoutParams(getVideoPlayerParams());
                LoggerUtil.showDebugLog(TAG," >> videoPlayer >> index == "+onGlobalIndex+" ,width == "+mVideoPlayer.getWidth()+" ,height == "
                        +mVideoPlayer.getHeight());
            }
        });
    }

    private void getVideoViewSize(){
        ViewTreeObserver vto = mRlVideoView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                onGlobalIndex1++;
                if (onGlobalIndex1 == 3){
                    onGlobalIndex1 = 0;
                    mRlVideoView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                mRlVideoView.setLayoutParams(getVideoViewParams());
                LoggerUtil.showDebugLog(TAG," >> mRlVideoView >> index0 == "+onGlobalIndex+" ,width0 == "+mRlVideoView.getWidth()+" ,height0 == "
                        +mRlVideoView.getHeight());
            }
        });
    }

    private void getRootViewDisplay(){
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        this.mVideoViewWidth = outMetrics.widthPixels;
        this.mVideoViewHeight = outMetrics.heightPixels;
    }

    private LinearLayout.LayoutParams getVideoPlayerParams(){
        LinearLayout.LayoutParams params;
        getRootViewDisplay();
//        if (mVideoContentRatioXY == 0) {
//            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//            return params;
//        }
        if (!isWindowFull){
//            mVideoControl.setViewDiaplayWidth(this.mVideoViewWidth);
//            params = compareRatio(this.mVideoViewWidth*7, this.mVideoViewHeight*2);
            params = new LinearLayout.LayoutParams(this.mVideoViewWidth, mVideoPlayer.getWidth()*9/16);
        }else {
            if (!isShowVideoList_Full){
//                mVideoControl.setViewDiaplayWidth(this.mVideoViewWidth);
//                params = compareRatio(this.mVideoViewWidth, this.mVideoViewHeight);
                params = new LinearLayout.LayoutParams(mVideoPlayer.getHeight()*16/9, mVideoViewHeight);
                params.gravity = Gravity.CENTER;
            }else {
//                mVideoControl.setViewDiaplayWidth(this.mVideoViewWidth*3/4);
//                params = compareRatio(this.mVideoViewWidth*3, this.mVideoViewHeight*4);
                params = new LinearLayout.LayoutParams(this.mVideoViewWidth*3/4, mVideoPlayer.getWidth()*9/16);
                params.gravity = Gravity.CENTER;
            }
        }
        return params;
    }

    private LinearLayout.LayoutParams getVideoViewParams(){
        LinearLayout.LayoutParams params;
        getRootViewDisplay();
        if (!isWindowFull){
            mVideoControl.setViewDiaplayWidth(this.mVideoViewWidth);
            params = new LinearLayout.LayoutParams(this.mVideoViewWidth, mRlVideoView.getWidth()*9/16);
        }else {
            if (!isShowVideoList_Full){
                mVideoControl.setViewDiaplayWidth(this.mVideoViewWidth);
//                params = compareRatio(this.mVideoViewWidth, this.mVideoViewHeight);
                params = new LinearLayout.LayoutParams(this.mVideoViewWidth, this.mVideoViewHeight);
            }else {
                mVideoControl.setViewDiaplayWidth(this.mVideoViewWidth*3/4);
//                params = compareRatio(this.mVideoViewWidth*3, this.mVideoViewHeight*4);
                params = new LinearLayout.LayoutParams(this.mVideoViewWidth*3/4, this.mVideoViewHeight);
            }
        }
        return params;
    }


    private LinearLayout.LayoutParams compareRatio(float width, float height){
        LinearLayout.LayoutParams params;
        if ((width/height) <= mVideoContentRatioXY)
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    (int)(mVideoPlayer.getWidth()/mVideoContentRatioXY));
        else
            params = new LinearLayout.LayoutParams((int)(mVideoPlayer.getHeight()*mVideoContentRatioXY),
                    LinearLayout.LayoutParams.MATCH_PARENT);
        return params;
    }

    private void changeViewTree(){
        if (mLlChange.getVisibility() == View.VISIBLE)
            mLlChange.setVisibility(View.GONE);
        else
            mLlChange.setVisibility(View.VISIBLE);
    }

    private void MoveToPosition(LinearLayoutManager manager, int n) {
        manager.scrollToPositionWithOffset(n, 0);
        manager.setStackFromEnd(true);
    }

    private void addDanmaku(String content, boolean withBorder) {
        BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        danmaku.text = content;
        danmaku.padding = 5;
        danmaku.textSize = SystemUtil.sp2px(mContext, 20);
        danmaku.textColor = Color.WHITE;
        danmaku.setTime(danmakuView.getCurrentTime());
        if (withBorder) {
            danmaku.borderColor = Color.GREEN;
        }
        danmakuView.addDanmaku(danmaku);
    }

    private void generateSomeDanmaku() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(showDanmaku) {
                        int time = new Random().nextInt(300);
                        String content = "" + time + time;
                        addDanmaku(content, false);
                        try {
                            Thread.sleep(time);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initAboutView(){
        bt_comment.setOnClickListener(this);
        commentsList = (ArrayList<CommentDetailBean>) generateTestData();
        initViewPager();
    }

    private void initViewPager(){
        mMainPager.setNoScroll(true);
        mFragmentlist.clear();
        mFragmentlist.add((cFragment = (CoursesFragment) instantiateFragment(mMainPager, 0, new CoursesFragment())));
        cFragment.setListener(new CoursesFragment.OnResetListener() {
            @Override
            public void resetVideo(ModuleInfo info) {
                mVideoPlayer.reset();
                mVideoPlayer.setPath(info.getmVideoUrl());
                try {
                    mVideoPlayer.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mFragmentlist.add(instantiateFragment(mMainPager, 1, new SynopsisFragment(mItems[1])));
        mFragmentlist.add((fragment = (CommentFragment) instantiateFragment(mMainPager, 2, new CommentFragment(commentsList))));
        fragment.setListener(new CommentFragment.ReplyListener() {
            @Override
            public void showReply(int groupPosition) {
                showReplyDialog(groupPosition);
            }

            @Override
            public void showChildReply(int groupPosition, int childPosition) {
                showChildReplyDialog(groupPosition, childPosition);
            }

            @Override
            public void showDetailedReply(int groupPosition) {
                showDetailedReplyDialog(groupPosition);
            }

            @Override
            public void showChildDetailedReply(int groupPosition, int childPosition) {
                showChildDetailedReplyDialog(groupPosition, childPosition);
            }
        });

        mMainPager.setOffscreenPageLimit(mFragmentlist.size() - 1);
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return mFragmentlist.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentlist.size();
            }
        };
        mMainPager.setAdapter(mPagerAdapter);
        mMainTab.setupWithViewPager(mMainPager, true);
        for (int i=0; i<mItems.length; i++){
            mMainTab.getTabAt(i).setText(mItems[i]);
        }
        mMainTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 2){
                    showCommentTool();
                }else {
                    dissShowCommentTool();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void showCommentTool(){
        mCommentTool.setVisibility(View.VISIBLE);
    }

    public void dissShowCommentTool(){
        mCommentTool.setVisibility(View.INVISIBLE);
    }

    private Fragment instantiateFragment(ViewPager viewPager, int position, Fragment defaultResult){
        String tag = "android:switcher:" + viewPager.getId() + ":" + position;
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        return fragment == null ? defaultResult : fragment;
    }

    /**
     * func:生成测试数据
     * @return 评论数据
     */
    private List<CommentDetailBean> generateTestData(){
        Gson gson = new Gson();
        commentBean = gson.fromJson(testJson, CommentBean.class);
        List<CommentDetailBean> commentList = commentBean.getData().getList();
        return commentList;
    }

    /**
     * func:弹出评论框
     */
    private void showCommentDialog(){
        dialog = new BottomSheetDialog(this, R.style.BottomSheetStyle);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        dialog.setContentView(commentView);
        /**
         * 解决bsd显示不全的情况
         */
        View parent = (View) commentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        commentView.measure(0,0);
        behavior.setPeekHeight(commentView.getMeasuredHeight());

        bt_comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String commentContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(commentContent)){

                    //commentOnWork(commentContent);
                    dialog.dismiss();
                    CommentDetailBean detailBean = new CommentDetailBean("小明", commentContent,"刚刚");
                    CommentFragment f = (CommentFragment) mPagerAdapter.getItem(2);
                    f.adapter.addTheCommentData(detailBean);
                    for(int i = 0; i<f.commentsList.size(); i++){
                        f.expandableListView.expandGroup(i);
                    }
                    Toast.makeText(mContext, "评论成功", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(mContext, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    /**
     * func:弹出回复框
     */
    private void showReplyDialog(final int position){
        dialog = new BottomSheetDialog(this, R.style.BottomSheetStyle);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        commentText.setHint("回复 " + commentsList.get(position).getNickName() + " 的评论:");
        dialog.setContentView(commentView);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String replyContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(replyContent)){

                    dialog.dismiss();
                    ReplyDetailBean detailBean = new ReplyDetailBean("小红",replyContent, "");
                    CommentFragment f = (CommentFragment) mPagerAdapter.getItem(2);
                    f.adapter.addTheReplyData(detailBean, position);
                    f.expandableListView.expandGroup(position);
                    Toast.makeText(mContext, "回复成功", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(mContext,"回复内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    /**
     * func:弹出回复框
     */
    private void showChildReplyDialog(final int groupPosition, final int childPosition){
        dialog = new BottomSheetDialog(this, R.style.BottomSheetStyle);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        commentText.setHint("回复 " + commentsList.get(groupPosition).getReplyList().get(childPosition).getNickName() + " 的评论:");
        dialog.setContentView(commentView);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String replyContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(replyContent)){

                    dialog.dismiss();
                    ReplyDetailBean detailBean = new ReplyDetailBean("小红",replyContent,commentsList.get(groupPosition).getReplyList().get(childPosition).getNickName());
                    CommentFragment f = (CommentFragment) mPagerAdapter.getItem(2);
                    f.adapter.addTheReplyData(detailBean, groupPosition);
                    f.expandableListView.expandGroup(groupPosition);
                    Toast.makeText(mContext, "回复成功", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(mContext, "回复内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    /**
     * func:弹出回复框
     */
    private void showDetailedReplyDialog(final int position){
        dialog = new BottomSheetDialog(this, R.style.BottomSheetStyle);
        CommentFragment f = (CommentFragment) mPagerAdapter.getItem(2);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        commentText.setHint("回复 " + f.childList.get(position).getNickName() + " 的评论:");
        dialog.setContentView(commentView);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String replyContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(replyContent)){

                    dialog.dismiss();
                    ReplyDetailBean detailBean = new ReplyDetailBean("小红",replyContent, "");
                    f.commentAdapter.addTheReplyData(detailBean, position);
                    f.commentlistView.expandGroup(position);
                    Toast.makeText(mContext, "回复成功", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(mContext,"回复内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    /**
     * func:弹出回复框
     */
    private void showChildDetailedReplyDialog(final int groupPosition, final int childPosition){
        dialog = new BottomSheetDialog(this, R.style.BottomSheetStyle);
        CommentFragment f = (CommentFragment) mPagerAdapter.getItem(2);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);

        commentText.setHint("回复 " + f.childList.get(groupPosition).getReplyList().get(childPosition).getNickName() + " 的评论:");

        dialog.setContentView(commentView);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String replyContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(replyContent)){
                    dialog.dismiss();
                    ReplyDetailBean detailBean = new ReplyDetailBean("小红",replyContent, f.childList.get(groupPosition).getReplyList().get(childPosition).getNickName());
                    f.commentAdapter.addTheReplyData(detailBean, groupPosition);
                    f.commentlistView.expandGroup(groupPosition);
                    Toast.makeText(mContext, "回复成功", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(mContext, "回复内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    private String testJson = "{\n" +
            "\t\"code\": 1000,\n" +
            "\t\"message\": \"查看评论成功\",\n" +
            "\t\"data\": {\n" +
            "\t\t\"total\": 3,\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\t\"id\": 42,\n" +
            "\t\t\t\t\"nickName\": \"程序猿\",\n" +
            "\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\"content\": \"时间是一切财富中最宝贵的财富。\",\n" +
            "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
            "\t\t\t\t\"replyTotal\": 1,\n" +
            "\t\t\t\t\"createDate\": \"三分钟前\",\n" +
            "\t\t\t\t\"replyList\": [{\n" +
            "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
            "\t\t\t\t\t\"objectName\": \"\",\n" +
            "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\t\"id\": 40,\n" +
            "\t\t\t\t\t\"commentId\": \"42\",\n" +
            "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
            "\t\t\t\t\t\"status\": \"01\",\n" +
            "\t\t\t\t\t\"createDate\": \"一个小时前\"\n" +
            "\t\t\t\t}]\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"id\": 41,\n" +
            "\t\t\t\t\"nickName\": \"设计狗\",\n" +
            "\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\"content\": \"这世界要是没有爱情，它在我们心中还会有什么意义！这就如一盏没有亮光的走马灯。\",\n" +
            "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
            "\t\t\t\t\"replyTotal\": 12,\n" +
            "\t\t\t\t\"createDate\": \"一天前\",\n" +
            "\t\t\t\t\"replyList\": [" +
            "{\n" +
            "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
            "\t\t\t\t\t\"objectName\": \"阿猫\",\n" +
            "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\t\"commentId\": \"41\",\n" +
            "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
            "\t\t\t\t\t\"status\": \"01\",\n" +
            "\t\t\t\t\t\"createDate\": \"三小时前\"\n" +
            "\t\t\t\t}," +
            "{\n" +
            "\t\t\t\t\t\"nickName\": \"阿猫\",\n" +
            "\t\t\t\t\t\"objectName\": \"\",\n" +
            "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\t\"commentId\": \"41\",\n" +
            "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
            "\t\t\t\t\t\"status\": \"01\",\n" +
            "\t\t\t\t\t\"createDate\": \"三小时前\"\n" +
            "\t\t\t\t}," +
            "{\n" +
            "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
            "\t\t\t\t\t\"objectName\": \"阿猫\",\n" +
            "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\t\"commentId\": \"41\",\n" +
            "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
            "\t\t\t\t\t\"status\": \"01\",\n" +
            "\t\t\t\t\t\"createDate\": \"三小时前\"\n" +
            "\t\t\t\t}," +
            "{\n" +
            "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
            "\t\t\t\t\t\"objectName\": \"阿猫\",\n" +
            "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\t\"commentId\": \"41\",\n" +
            "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
            "\t\t\t\t\t\"status\": \"01\",\n" +
            "\t\t\t\t\t\"createDate\": \"三小时前\"\n" +
            "\t\t\t\t}," +
            "{\n" +
            "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
            "\t\t\t\t\t\"objectName\": \"阿猫\",\n" +
            "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\t\"commentId\": \"41\",\n" +
            "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
            "\t\t\t\t\t\"status\": \"01\",\n" +
            "\t\t\t\t\t\"createDate\": \"三小时前\"\n" +
            "\t\t\t\t}," +
            "{\n" +
            "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
            "\t\t\t\t\t\"objectName\": \"阿猫\",\n" +
            "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\t\"commentId\": \"41\",\n" +
            "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
            "\t\t\t\t\t\"status\": \"01\",\n" +
            "\t\t\t\t\t\"createDate\": \"三小时前\"\n" +
            "\t\t\t\t}," +
            "{\n" +
            "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
            "\t\t\t\t\t\"objectName\": \"阿猫\",\n" +
            "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\t\"commentId\": \"41\",\n" +
            "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
            "\t\t\t\t\t\"status\": \"01\",\n" +
            "\t\t\t\t\t\"createDate\": \"三小时前\"\n" +
            "\t\t\t\t}," +
            "{\n" +
            "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
            "\t\t\t\t\t\"objectName\": \"阿猫\",\n" +
            "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\t\"commentId\": \"41\",\n" +
            "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
            "\t\t\t\t\t\"status\": \"01\",\n" +
            "\t\t\t\t\t\"createDate\": \"三小时前\"\n" +
            "\t\t\t\t}," +
            "{\n" +
            "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
            "\t\t\t\t\t\"objectName\": \"阿猫\",\n" +
            "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\t\"commentId\": \"41\",\n" +
            "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
            "\t\t\t\t\t\"status\": \"01\",\n" +
            "\t\t\t\t\t\"createDate\": \"三小时前\"\n" +
            "\t\t\t\t}," +
            "{\n" +
            "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
            "\t\t\t\t\t\"objectName\": \"阿猫\",\n" +
            "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\t\"commentId\": \"41\",\n" +
            "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
            "\t\t\t\t\t\"status\": \"01\",\n" +
            "\t\t\t\t\t\"createDate\": \"三小时前\"\n" +
            "\t\t\t\t}," +
            "{\n" +
            "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
            "\t\t\t\t\t\"objectName\": \"阿猫\",\n" +
            "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\t\"commentId\": \"41\",\n" +
            "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
            "\t\t\t\t\t\"status\": \"01\",\n" +
            "\t\t\t\t\t\"createDate\": \"三小时前\"\n" +
            "\t\t\t\t}," +
            "{\n" +
            "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
            "\t\t\t\t\t\"objectName\": \"阿猫\",\n" +
            "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\t\"commentId\": \"41\",\n" +
            "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
            "\t\t\t\t\t\"status\": \"01\",\n" +
            "\t\t\t\t\t\"createDate\": \"三小时前\"\n" +
            "\t\t\t\t}" +
            "]\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"id\": 40,\n" +
            "\t\t\t\t\"nickName\": \"产品喵\",\n" +
            "\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\"content\": \"笨蛋自以为聪明，聪明人才知道自己是笨蛋。\",\n" +
            "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
            "\t\t\t\t\"replyTotal\": 0,\n" +
            "\t\t\t\t\"createDate\": \"三天前\",\n" +
            "\t\t\t\t\"replyList\": []\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"id\": 41,\n" +
            "\t\t\t\t\"nickName\": \"运营猪\",\n" +
            "\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\"content\": \"这世界要是没有爱情，它在我们心中还会有什么意义！这就如一盏没有亮光的走马灯。\",\n" +
            "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
            "\t\t\t\t\"replyTotal\": 1,\n" +
            "\t\t\t\t\"createDate\": \"一天前\",\n" +
            "\t\t\t\t\"replyList\": [{\n" +
            "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
            "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\t\"commentId\": \"41\",\n" +
            "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
            "\t\t\t\t\t\"status\": \"01\",\n" +
            "\t\t\t\t\t\"createDate\": \"三小时前\"\n" +
            "\t\t\t\t}]\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"id\": 41,\n" +
            "\t\t\t\t\"nickName\": \"项目牛\",\n" +
            "\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\"content\": \"这世界要是没有爱情，它在我们心中还会有什么意义！这就如一盏没有亮光的走马灯。\",\n" +
            "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
            "\t\t\t\t\"replyTotal\": 1,\n" +
            "\t\t\t\t\"createDate\": \"一天前\",\n" +
            "\t\t\t\t\"replyList\": [{\n" +
            "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
            "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\t\"commentId\": \"41\",\n" +
            "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
            "\t\t\t\t\t\"status\": \"01\",\n" +
            "\t\t\t\t\t\"createDate\": \"三小时前\"\n" +
            "\t\t\t\t}]\n" +
            "\t\t\t}\n" +
            "\t\t]\n" +
            "\t}\n" +
            "}";

}

