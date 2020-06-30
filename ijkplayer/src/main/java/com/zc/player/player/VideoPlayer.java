package com.zc.player.player;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.zc.player.util.LoggerUtil;

import java.io.IOException;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

public class VideoPlayer extends FrameLayout {

    private IMediaPlayer mMediaPlayer = null;

    private String mPath ;
    private Map<String,String> mHeader;

    private SurfaceView mSurfaceView;

    private Context mContext;
    private boolean mEnableMediaCodec;

    private AudioManager mAudioManager;
    private AudioFocusHelper mAudioFocusHelper;

    public VideoPlayer(@NonNull Context context) {
        this(context, null);
    }

    public VideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        setBackgroundColor(Color.BLACK);
        createSurfaceView();
        mAudioManager = (AudioManager)mContext.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        mAudioFocusHelper = new AudioFocusHelper();
    }

    private void createSurfaceView() {
        mSurfaceView = new SurfaceView(mContext);
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                if (mMediaPlayer != null) {
                    mMediaPlayer.setDisplay(surfaceHolder);
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT
                , LayoutParams.MATCH_PARENT, Gravity.CENTER);
        addView(mSurfaceView,0,layoutParams);
    }

    private IMediaPlayer createPlayer() {
        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "http-detect-range-support", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "min-frames", 100);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "dns_cache_clear", 1);//http与https切换400
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);
        ijkMediaPlayer.setVolume(1.0f, 1.0f);

        setEnableMediaCodec(ijkMediaPlayer,mEnableMediaCodec);
        return ijkMediaPlayer;
    }

    private void setEnableMediaCodec(IjkMediaPlayer ijkMediaPlayer, boolean isEnable) {
        int value = isEnable ? 1 : 0;
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", value);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", value);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", value);
    }

    public void setEnableMediaCodec(boolean isEnable){
        mEnableMediaCodec = isEnable;
    }

    private void setListener(IMediaPlayer player){
        player.setOnPreparedListener(mPreparedListener);
        player.setOnVideoSizeChangedListener(mVideoSizeChangedListener);
        player.setOnBufferingUpdateListener(mBufferingUpdateListener);
        player.setOnCompletionListener(mOnCompletionListener);
        player.setOnErrorListener(mOnErrorListener);
        player.setOnInfoListener(mOnInfoListener);
        player.setOnSeekCompleteListener(mOnSeekCompleteListener);
        player.setOnTimedTextListener(mOnTimedTextListener);
    }

    public void setPath(String path) {
        setPath(path,null);
    }

    public void setPath(String path,Map<String,String> header){
        mPath = path;
        mHeader = header;
    }

    public void load() throws IOException {
        if(mMediaPlayer != null){
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        mMediaPlayer = createPlayer();
        setListener(mMediaPlayer);
        mMediaPlayer.setDisplay(mSurfaceView.getHolder());
        mMediaPlayer.setDataSource(mContext, Uri.parse(mPath),mHeader);
        mMediaPlayer.prepareAsync();
    }

    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            mOnVideoPlayerStateListener.videoStart();
            mAudioFocusHelper.requestFocus();
        }
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mAudioFocusHelper.abandonFocus();
        }
    }

    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
            mAudioFocusHelper.abandonFocus();
        }
    }

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mAudioFocusHelper.abandonFocus();
        }
    }


    public void reset() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mAudioFocusHelper.abandonFocus();
            mOnVideoPlayerStateListener.videoReset();
        }
    }


    public long getDuration() {
        if (mMediaPlayer != null)
            return mMediaPlayer.getDuration();
        else
            return 0;
    }


    public long getCurrentPosition() {
        if (mMediaPlayer != null)
            return mMediaPlayer.getCurrentPosition();
        else
            return 0;
    }


    public void seekTo(long l) {
        if (mMediaPlayer != null)
            mMediaPlayer.seekTo(l);
    }

    public boolean isPlaying(){
        if(mMediaPlayer != null)
            return mMediaPlayer.isPlaying();
        return false;
    }

    private IMediaPlayer.OnPreparedListener mPreparedListener = new IMediaPlayer.OnPreparedListener(){

        @Override
        public void onPrepared(IMediaPlayer iMediaPlayer) {
            mOnVideoPlayerStateListener.videoPrepared();
        }
    };

    private IMediaPlayer.OnVideoSizeChangedListener mVideoSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
            int videoWidth = iMediaPlayer.getVideoWidth();
            int videoHeight = iMediaPlayer.getVideoHeight();
            if (videoWidth != 0 && videoHeight != 0)
                mOnVideoPlayerStateListener.videoChange(videoWidth, videoHeight);
        }
    };

    private IMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new IMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
            LoggerUtil.showMianTestLog("onBufferingUpdate >> % == "+i);
        }
    };

    private IMediaPlayer.OnCompletionListener mOnCompletionListener = new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer iMediaPlayer) {
            mOnVideoPlayerStateListener.videoCompletion();
        }
    };

    private IMediaPlayer.OnErrorListener mOnErrorListener = new IMediaPlayer.OnErrorListener(){
        @Override
        public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
            LoggerUtil.showMianTestLog("onError >> errInfo == "+i+" , == "+i1);
            mOnVideoPlayerStateListener.videoState(i);
            return false;
        }
    };

    /*************************************************************************************************************
     * int MEDIA_INFO_UNKNOWN = 1;//未知信息
     * int MEDIA_INFO_STARTED_AS_NEXT = 2;//播放下一条
     * int MEDIA_INFO_VIDEO_RENDERING_START = 3;//视频开始整备中，准备渲染
     * int MEDIA_INFO_VIDEO_TRACK_LAGGING = 700;//视频日志跟踪
     * int MEDIA_INFO_BUFFERING_START = 701;//开始缓冲中 开始缓冲
     * int MEDIA_INFO_BUFFERING_END = 702;//缓冲结束
     * int MEDIA_INFO_NETWORK_BANDWIDTH = 703;//网络带宽，网速方面
     * int MEDIA_INFO_BAD_INTERLEAVING = 800;//
     * int MEDIA_INFO_NOT_SEEKABLE = 801;//不可设置播放位置，直播方面
     * int MEDIA_INFO_METADATA_UPDATE = 802;//
     * int MEDIA_INFO_TIMED_TEXT_ERROR = 900;
     * int MEDIA_INFO_UNSUPPORTED_SUBTITLE = 901;//不支持字幕
     * int MEDIA_INFO_SUBTITLE_TIMED_OUT = 902;//字幕超时
     * int MEDIA_INFO_VIDEO_INTERRUPT= -10000;//数据连接中断，一般是视频源有问题或者数据格式不支持，比如音频不是AAC之类的
     * int MEDIA_INFO_VIDEO_ROTATION_CHANGED = 10001;//视频方向改变，视频选择信息
     * int MEDIA_INFO_AUDIO_RENDERING_START = 10002;//音频开始整备中
     * int MEDIA_INFO_AUDIO_DECODED_START = 10003;//媒体信息音频解码开始
     * int MEDIA_INFO_VIDEO_DECODED_START = 10004;//媒体信息视频频解码开始
     * int MEDIA_INFO_OPEN_INPUT = 10005;//媒体信息打开输入
     * int MEDIA_INFO_FIND_STREAM_INFO = 10006;//媒体信息查找流信息
     * int MEDIA_INFO_COMPONENT_OPEN = 10007;//媒体信息打开完成
     * int MEDIA_INFO_VIDEO_SEEK_RENDERING_START = 10008;//媒体信息视频SEEK渲染开始
     * int MEDIA_INFO_AUDIO_SEEK_RENDERING_START = 10009;//媒体信息音频SEEK渲染开始
     * int MEDIA_INFO_MEDIA_ACCURATE_SEEK_COMPLETE = 10100;//媒体信息媒体准确SEEK完成
     * int MEDIA_ERROR_SERVER_DIED = 100;//服务挂掉，视频中断，一般是视频源异常或者不支持的视频类型。
     * int MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = 200;//数据错误没有有效的回收
     * int MEDIA_ERROR_IO = -1004;//IO 错误
     * int MEDIA_ERROR_MALFORMED = -1007;
     * int MEDIA_ERROR_UNSUPPORTED = -1010;//数据不支持
     * int MEDIA_ERROR_TIMED_OUT = -110;//数据超时
     **************************************************************************************************************/
    private IMediaPlayer.OnInfoListener mOnInfoListener = new IMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
            LoggerUtil.showMianTestLog("onError >> info == "+i+" , == "+i1);
            mOnVideoPlayerStateListener.videoState(i);
            return false;
        }
    };

    private IMediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener = new IMediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(IMediaPlayer iMediaPlayer) {
            LoggerUtil.showMianTestLog("onSeekComplete >> == ");
        }
    };

    private IMediaPlayer.OnTimedTextListener mOnTimedTextListener = new IMediaPlayer.OnTimedTextListener() {
        @Override
        public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {
            LoggerUtil.showMianTestLog("onTimedText >> == ");
        }
    };

    private class AudioFocusHelper implements AudioManager.OnAudioFocusChangeListener {
        boolean startRequested = false;
        boolean pausedForLoss = false;
        int currentFocus = 0;

        @Override
        public void onAudioFocusChange(int focusChange) {
            if (currentFocus == focusChange) {
                return;
            }

            currentFocus = focusChange;
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                    if (startRequested || pausedForLoss) {
                        start();
                        startRequested = false;
                        pausedForLoss = false;
                    }
                    if (mMediaPlayer != null)
                        mMediaPlayer.setVolume(1.0f, 1.0f);
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    if (isPlaying()) {
                        pausedForLoss = true;
                        pause();
                    }
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    if (mMediaPlayer != null && isPlaying()) {
                        mMediaPlayer.setVolume(0.1f, 0.1f);
                    }
                    break;
            }
        }

        boolean requestFocus() {
            if (currentFocus == AudioManager.AUDIOFOCUS_GAIN) {
                return true;
            }

            if (mAudioManager == null) {
                return false;
            }

            int status = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED == status) {
                currentFocus = AudioManager.AUDIOFOCUS_GAIN;
                return true;
            }

            startRequested = true;
            return false;
        }

        boolean abandonFocus() {

            if (mAudioManager == null) {
                return false;
            }

            startRequested = false;
            int status = mAudioManager.abandonAudioFocus(this);
            return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == status;
        }
    }

    private OnVideoPlayerStateListener mOnVideoPlayerStateListener;
    public interface OnVideoPlayerStateListener{
        void videoStart();
        void videoReset();
        void videoState(int stateCode);
        void videoCompletion();
        void videoPrepared();
        void videoChange(float width, float height);
    }

    public void setOnVideoPlayerStateListener(OnVideoPlayerStateListener listener){
        this.mOnVideoPlayerStateListener = listener;
    }
}
