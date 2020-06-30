package com.zc.xxj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ViewControlFrameLayout extends FrameLayout {
    private final String TAG = ViewControlFrameLayout.class.getSimpleName();

    private int pointOneX, pointOneY;
    private int pointTwoX, pointTwoY;

    private long lastClickEventTime = 0;
    private OnTwoPointEventListener mOnTwoPointEventListener;
    private int mViewDisplayWidth;
    private boolean morethanTwoPointEvent = false;
    private boolean morePointEvent = false;
    private boolean moveEvent = false;
    private int onePointMoveEvent; //1为调节进度事件，2为调节亮度事件，3为调节音量事件

    public ViewControlFrameLayout(@NonNull Context context) {
        super(context);
    }

    public ViewControlFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewControlFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ViewControlFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean onTouchEvent(MotionEvent event){
        int oneX, oneY, twoX, twoY;
        int moveX, moveY;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (morePointEvent){ // 小米手机三指事件被系统拦截无法触发 MotionEvent.ACTION_POINTER_UP & MotionEvent.ACTION_UP
                    morePointEvent = false;
                    morethanTwoPointEvent = false;
                }
                if (moveEvent) moveEvent = false;
                oneX = (int) event.getX();
                oneY = (int) event.getY();
                pointOneX = oneX;
                pointOneY = oneY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (morePointEvent) break;
                //在用户第一次触发ACTION_MOVE时确认手势意图
                oneX = (int) event.getX();
                oneY = (int) event.getY();
                moveX = oneX - pointOneX >= 0 ? oneX - pointOneX : pointOneX - oneX;
                moveY = oneY - pointOneY >= 0 ? oneY - pointOneY : pointOneY - oneY;
                if (!moveEvent){
                    if ( moveX >=18 || moveY>=18){
                        moveEvent = true;
                        if (moveX > moveY) onePointMoveEvent = 1;
                        else {
                            if (pointOneX < mViewDisplayWidth / 2) onePointMoveEvent = 2;
                            else onePointMoveEvent = 3;
                        }
                        pointOneX = oneX;
                        pointOneY = oneY;
                    }
                }else {
                    switch (onePointMoveEvent){
                        case 1:
                            if (oneX > pointOneX) mOnTwoPointEventListener.progressTextEvent(true, moveX);
                            else mOnTwoPointEventListener.progressTextEvent(false, moveX);
                            break;
                        case 2:
                            if (oneY > pointOneY) mOnTwoPointEventListener.brightnessEvent(false, moveY);
                            else mOnTwoPointEventListener.brightnessEvent(true, moveY);
                            break;
                        case 3:
                            if (oneY > pointOneY) mOnTwoPointEventListener.volumeEvent(false, moveY);
                            else mOnTwoPointEventListener.volumeEvent(true, moveY);
                            break;
                    }
                    pointOneX = oneX;
                    pointOneY = oneY;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (morePointEvent){
                    morePointEvent = false;
                    morethanTwoPointEvent = false;
                    break;
                }
                if (!moveEvent){
                    if(System.currentTimeMillis() - lastClickEventTime > 300){
                        lastClickEventTime = System.currentTimeMillis();
                        mOnTwoPointEventListener.onOneClick();
                        break;
                    }
                    mOnTwoPointEventListener.onTwoClick();
                    break;
                }
                if (onePointMoveEvent == 1){ //此处进行seek操作
                    mOnTwoPointEventListener.progressSeekEvent();
                }else if (onePointMoveEvent == 3){
                    mOnTwoPointEventListener.volumeEventFinish();
                }else if (onePointMoveEvent == 2){
                    mOnTwoPointEventListener.brightnessEventFinish();
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                morePointEvent = true;
                if (event.getPointerCount() == 2){
                    oneX = (int) event.getX(0);
                    oneY = (int) event.getY(0);
                    twoX = (int) event.getX(1);
                    twoY = (int) event.getY(1);
                    pointOneX = oneX;
                    pointOneY = oneY;
                    pointTwoX = twoX;
                    pointTwoY = twoY;
                }else if (event.getPointerCount() > 2) { //过滤两指以上手势
                    morethanTwoPointEvent = true;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if (event.getPointerCount() == 2 && !morethanTwoPointEvent) {
                    oneX = (int) event.getX(0);
                    oneY = (int) event.getY(0);
                    twoX = (int) event.getX(1);
                    twoY = (int) event.getY(1);
                    int downX = pointOneX - pointTwoX >= 0 ? pointOneX - pointTwoX : pointTwoX - pointOneX;
                    int downY = pointOneY - pointTwoY >= 0 ? pointOneY - pointTwoY : pointTwoY - pointOneY;
                    int upX = oneX - twoX >= 0 ? oneX - twoX : twoX - oneX;
                    int upY = oneY - twoY >= 0 ? oneY - twoY : twoY - oneY;
                    //通过两点直线距离判断收缩或舒张
                    if (((Math.pow(downX,2)+Math.pow(downY,2))) > ((Math.pow(upX,2)+Math.pow(upY,2))))
                        mOnTwoPointEventListener.twoPointEvent(true);
                    else if ((Math.pow(downX,2)+Math.pow(downY,2)) < ((Math.pow(upX,2)+Math.pow(upY,2))))
                        mOnTwoPointEventListener.twoPointEvent(false);
                }
                break;
        }
        return true;
    }

    public interface OnTwoPointEventListener{
        void twoPointEvent(boolean isShow);
        void onTwoClick();
        void onOneClick();
        void volumeEventFinish();
        void brightnessEventFinish();
        void volumeEvent(boolean add, int percentage);
        void brightnessEvent(boolean add, int percentage);
        void progressTextEvent(boolean add, int percentage);
        void progressSeekEvent();
    }

    public void setTwoPointEventListener(OnTwoPointEventListener listener){
        this.mOnTwoPointEventListener = listener;
    }

    public void setViewDiaplayWidth(int mViewDisplayWidth){
        this.mViewDisplayWidth = mViewDisplayWidth;
    }

}
