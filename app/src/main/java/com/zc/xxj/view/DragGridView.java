package com.zc.xxj.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.zc.xxj.R;
import com.zc.xxj.adapter.DragGridAdapter;

public class DragGridView extends GridView implements AdapterView.OnItemLongClickListener {
    private static final String TAG = DragGridView.class.getSimpleName();

    private WindowManager mWindowManager;

    private static final int MODE_DRAG = 1;
    private static final int MODE_NORMAL = 2;

    private int mode = MODE_NORMAL;
    private View view;
    private View dragView;

    private boolean canDrag = true;
    private int notMovePosition = 1;

    private int position;

    private int tempPosition;

    private WindowManager.LayoutParams layoutParams;

    private float mX;

    private float mY;

    private float mWindowX;

    private float mWindowY;

    private ItemLongClickListener listener;
    private boolean isFrist = true;

    public DragGridView(Context context) {
        this(context, null);
    }

    public DragGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        setOnItemLongClickListener(this);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mWindowX = ev.getRawX();
                mWindowY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    public void setItemLongClickListener(ItemLongClickListener listener){
        this.listener = listener;
    }

    public interface ItemLongClickListener{
        void itemLongClick(int position);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (mode == MODE_DRAG) {
            return false;
        }
        this.view = view;
        this.position = position;
        this.tempPosition = position;
        mX = mWindowX - view.getLeft() - this.getLeft();
        mY = mWindowY - view.getTop() - this.getTop();
        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(getContext())) {
                initWindow();
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getContext().getPackageName()));
                getContext().startActivity(intent);
            }
        } else {
            initWindow();
        }
        return true;
    }

    private void initWindow() {
        if (isFrist) {
            listener.itemLongClick(position);
            isFrist = false;
        }

        if (!canDrag || position <= (notMovePosition-1)) return;
        if (dragView == null) {
            dragView = View.inflate(getContext(), R.layout.drag_item, null);
            TextView tv_text = (TextView) dragView.findViewById(R.id.tv_text);
            tv_text.setText(((TextView) view.findViewById(R.id.tv_text)).getText());
        }
        if (layoutParams == null) {
            layoutParams = new WindowManager.LayoutParams();
            if (Build.VERSION.SDK_INT >= 26) {
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            }else {
                layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            }
            layoutParams.format = PixelFormat.RGBA_8888;
            layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            layoutParams.width = view.getWidth();
            layoutParams.height = view.getHeight();
            layoutParams.x = view.getLeft() + this.getLeft();
            layoutParams.y = view.getTop() + this.getTop();
            view.setVisibility(INVISIBLE);
        }
        mWindowManager.addView(dragView, layoutParams);
        mode = MODE_DRAG;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == MODE_DRAG) {
                    updateWindow(ev);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mode == MODE_DRAG) {
                    closeWindow(ev.getX(), ev.getY());
                }
                break;
        }
        return super.onTouchEvent(ev);
    }


    private void updateWindow(MotionEvent ev) {
        if (mode == MODE_DRAG) {
            float x = ev.getRawX() - mX;
            float y = ev.getRawY() - mY;
            if (layoutParams != null) {
                layoutParams.x = (int) x;
                layoutParams.y = (int) y;
                mWindowManager.updateViewLayout(dragView, layoutParams);
            }
            float mx = ev.getX();
            float my = ev.getY();
            int dropPosition = pointToPosition((int) mx, (int) my);
            if (dropPosition == tempPosition || dropPosition == GridView.INVALID_POSITION
                || dropPosition <= (notMovePosition-1)) {
                return;
            }
            itemMove(dropPosition);
        }
    }

    private void itemMove(int dropPosition) {
        TranslateAnimation translateAnimation;
        if (dropPosition < tempPosition) {
            for (int i = dropPosition; i < tempPosition; i++) {
                View view = getChildAt(i);
                View nextView = getChildAt(i + 1);
                float xValue = (nextView.getLeft() - view.getLeft()) * 1f / view.getWidth();
                float yValue = (nextView.getTop() - view.getTop()) * 1f / view.getHeight();
                translateAnimation =
                        new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, xValue, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, yValue);
                translateAnimation.setInterpolator(new LinearInterpolator());
                translateAnimation.setFillAfter(true);
                translateAnimation.setDuration(300);
                if (i == tempPosition - 1) {
                    translateAnimation.setAnimationListener(animationListener);
                }
                view.startAnimation(translateAnimation);
            }
        } else {
            for (int i = tempPosition + 1; i <= dropPosition; i++) {
                View view = getChildAt(i);
                View prevView = getChildAt(i - 1);
                float xValue = (prevView.getLeft() - view.getLeft()) * 1f / view.getWidth();
                float yValue = (prevView.getTop() - view.getTop()) * 1f / view.getHeight();
                translateAnimation =
                        new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, xValue, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, yValue);
                translateAnimation.setInterpolator(new LinearInterpolator());
                translateAnimation.setFillAfter(true);
                translateAnimation.setDuration(300);
                if (i == dropPosition) {
                    translateAnimation.setAnimationListener(animationListener);
                }
                view.startAnimation(translateAnimation);
            }
        }
        tempPosition = dropPosition;
    }

    Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            ListAdapter adapter = getAdapter();
            if (adapter != null && adapter instanceof DragGridAdapter) {
                ((DragGridAdapter) adapter).exchangePosition(position, tempPosition, true);
            }
            position = tempPosition;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private void closeWindow(float x, float y) {
        if (dragView != null) {
            mWindowManager.removeView(dragView);
            dragView = null;
            layoutParams = null;
        }
        itemDrop();
        mode = MODE_NORMAL;
    }


    private void itemDrop() {
        ListAdapter adapter = getAdapter();
        if (tempPosition == position || tempPosition == GridView.INVALID_POSITION) {
            if (adapter != null && adapter instanceof DragGridAdapter) {
                ((DragGridAdapter) adapter).setItemState(false);
            }
        } else {
            if (adapter != null && adapter instanceof DragGridAdapter) {
                ((DragGridAdapter) adapter).exchangePosition(position, tempPosition, false);
            }
        }
        getChildAt(position).setVisibility(VISIBLE);
    }

    public void setCanDrag(boolean canDrag){
        this.canDrag = canDrag;
    }

    public boolean getCanDrag(){
        return this.canDrag;
    }

    public void setNotMovePosition(int position){
        notMovePosition = position;
    }

    public int getNotMovePosition(){
        return notMovePosition;
    }
}
