package com.zc.xxj.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.zc.xxj.R;
import com.zc.xxj.utils.JsonUtil;
import com.zc.xxj.utils.OthersUtil;

public class FlowLayout extends ViewGroup {
    private List<ChildPos> mChildPos = new ArrayList<ChildPos>();
    private Context mContext;
    private float textSize;
    private int textColor;
    private int textColorSelector;
    private float shapeRadius;
    private int shapeLineColor;
    private int shapeBackgroundColor;
    private int shapeBackgroundColorSelector;
    private float shapeLineWidth;
    private int deleteBtnColor;

    private boolean isDeleteMode;
    private boolean isClickMode = true;

    private List<Integer> mAllSelectedNum = new ArrayList<>();
    private List<String> mAllSelectedWords = new ArrayList<>();

    private class ChildPos {
        int left, top, right, bottom;

        public ChildPos(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }
    }

    public FlowLayout(Context context) {
        this(context, null);
        mContext = context;
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
        initAttributes(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    @SuppressLint("ResourceAsColor")
    private void initAttributes(Context context, AttributeSet attrs) {
        @SuppressLint("Recycle")
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        textSize = typedArray.getDimension(R.styleable.FlowLayout_flowLayoutTextSize, 16);
        textColor = typedArray.getColor(R.styleable.FlowLayout_flowLayoutTextColor, R.color.color_flow_text);
        textColorSelector = typedArray.getResourceId(R.styleable.FlowLayout_flowLayoutTextColorSelector, 0);
        shapeRadius = typedArray.getDimension(R.styleable.FlowLayout_flowLayoutRadius, 40f);
        shapeLineColor = typedArray.getColor(R.styleable.FlowLayout_flowLayoutLineColor, R.color.color_flow_line);
        shapeBackgroundColor = typedArray.getColor(R.styleable.FlowLayout_flowLayoutBackgroundColor, R.color.color_flow_bg);
        shapeBackgroundColorSelector = typedArray.getResourceId(R.styleable.FlowLayout_flowLayoutBackgroundColorSelector, 0);
        shapeLineWidth = typedArray.getDimension(R.styleable.FlowLayout_flowLayoutLineWidth, 4f);
        deleteBtnColor = typedArray.getColor(R.styleable.FlowLayout_flowLayoutDeleteBtnColor, Color.GRAY);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0, height = 0;
        int lineWidth = 0, lineHeight = 0;
        int count = getChildCount();
        mChildPos.clear();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (lineWidth + childWidth > widthSize - getPaddingLeft() - getPaddingRight()) {

                width = Math.max(width, lineWidth);
                height += lineHeight;
                lineWidth = childWidth;
                lineHeight = childHeight;
                mChildPos.add(new ChildPos(
                        getPaddingLeft() + lp.leftMargin,
                        getPaddingTop() + height + lp.topMargin,
                        getPaddingLeft() + childWidth - lp.rightMargin,
                        getPaddingTop() + height + childHeight - lp.bottomMargin));
            } else {
                mChildPos.add(new ChildPos(
                        getPaddingLeft() + lineWidth + lp.leftMargin,
                        getPaddingTop() + height + lp.topMargin,
                        getPaddingLeft() + lineWidth + childWidth - lp.rightMargin,
                        getPaddingTop() + height + childHeight - lp.bottomMargin));
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
            if (i == count - 1) {
                width = Math.max(lineWidth, width);
                height += lineHeight;
            }
        }

        int flowLayoutWidth = widthMode == MeasureSpec.AT_MOST ? width + getPaddingLeft() + getPaddingRight() : widthSize;
        int flowLayoutHeight = heightMode == MeasureSpec.AT_MOST ? height + getPaddingTop() + getPaddingBottom() : heightSize;

        realHeight = height + getPaddingTop() + getPaddingBottom();

        measuredHeight = heightSize;
        if (heightMode == MeasureSpec.EXACTLY) {
            realHeight = Math.max(measuredHeight, realHeight);
        }
        scrollable = realHeight > measuredHeight;

        setMeasuredDimension(flowLayoutWidth, flowLayoutHeight);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            ChildPos pos = mChildPos.get(i);
            child.layout(pos.left, pos.top, pos.right, pos.bottom);
        }
    }

    public void addItemView(LayoutInflater inflater, String tvName,  List<String> list , int position) {
        View view = inflater.inflate(R.layout.flow_layout, this, false);
        ImageView delete = view.findViewById(R.id.delete);
        if (isDeleteMode) {
            delete.setVisibility(VISIBLE);
            delete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i;
                    if (mAllSelectedNum.size() > 0 && (i = mAllSelectedNum.get(0)) > position){
                        mAllSelectedNum.clear();
                        mAllSelectedNum.add(i-1);
                    }else if (mAllSelectedNum.size() > 0 && mAllSelectedNum.get(0) == position){
                        mAllSelectedNum.clear();
                    }
                    list.remove(tvName);
                    showTag(list);
                    JsonUtil.updateHistoryJson(mContext, list, OthersUtil.SPSEARCHHISTORY, OthersUtil.SPSEARCHHISTORY_ITEM);
                }
            });
        } else {
            delete.setVisibility(GONE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            delete.setImageTintList(ColorStateList.valueOf(deleteBtnColor));
        }
        TextView textView = view.findViewById(R.id.value);
        textView.setTextSize(textSize / getContext().getResources().getDisplayMetrics().scaledDensity);
        if (textColorSelector != 0) {
            ColorStateList csl = getResources().getColorStateList(textColorSelector);
            textView.setTextColor(csl);
        } else {
            textView.setTextColor(textColor);
        }
        textView.setPadding(20, 4, 20, 4);
        textView.setText(tvName);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(shapeRadius);
        drawable.setStroke((int) shapeLineWidth, shapeLineColor);
        if (shapeBackgroundColorSelector != 0) {
            ColorStateList csl = getResources().getColorStateList(shapeBackgroundColorSelector);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable.setColor(csl);
            }
        } else {
            drawable.setColor(shapeBackgroundColor);
        }
        LinearLayout mItem = view.findViewById(R.id.flow_item);
        mItem.setBackground(drawable);

        this.addView(view);
    }


    public boolean isDeleteMode() {
        return isDeleteMode;
    }

    public void setDeleteMode(boolean deleteMode) {
        isDeleteMode = deleteMode;
    }

    public void setClickMode(boolean clickMode) {
        isClickMode = clickMode;
    }


    private boolean scrollable;
    private int measuredHeight;
    private int realHeight;
    private int scrolledHeight = 0;
    private int startY;
    private int offsetY;
    private boolean pointerDown;

    public int getRealHeight() {
        return realHeight;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (scrollable && isInterceptedTouch) {
            int currY = (int) event.getY();
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (!pointerDown) {
                        startY = currY;
                        pointerDown = true;
                    } else {
                        offsetY = startY - currY;
                        this.scrollTo(0, scrolledHeight + offsetY);
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    scrolledHeight += offsetY;
                    if (scrolledHeight + offsetY < 0) {
                        this.scrollTo(0, 0);
                        scrolledHeight = 0;
                    } else if (scrolledHeight + offsetY + measuredHeight > realHeight) {
                        this.scrollTo(0, realHeight - measuredHeight);
                        scrolledHeight = realHeight - measuredHeight;
                    }
                    pointerDown = false;
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(event);
    }


    private boolean isInterceptedTouch;
    private int startYY = 0;
    private boolean pointerDownY;
    private int minDistance = 10;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        int currY = (int) ev.getY();
        int offsetY = 0;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pointerDownY = true;
                intercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (pointerDownY) {
                    startYY = currY;
                } else {
                    offsetY = currY - startYY;
                }
                pointerDownY = false;
                intercepted = Math.abs(offsetY) > minDistance;
                break;
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
            default:
                break;
        }
        isInterceptedTouch = intercepted;
        return intercepted;
    }

    public void showTag(final List<String> list) {
        removeAllViews();
        mAllSelectedWords = list;
        for (int i = 0; i < list.size(); i++) {
            final String keywords = list.get(i);
            addItemView(LayoutInflater.from(getContext()), keywords, list , i);
            final int finalI = i;
            if (isClickMode){
                getChildAt(i).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mAllSelectedNum.size() != 0 && finalI != mAllSelectedNum.get(0)){
                            View lastSelectView = getChildAt(mAllSelectedNum.get(0));
                            lastSelectView.setSelected(!lastSelectView.isSelected());
                        }else if (mAllSelectedNum.size() != 0 && finalI == mAllSelectedNum.get(0)){
                            return;
                        }
                        View child = getChildAt(finalI);
                        child.setSelected(!child.isSelected());
                        if (child.isSelected()) {
                            mAllSelectedNum.clear();
                            mAllSelectedNum.add(finalI);
                        }
                        listener.onItemClick(keywords);
                    }
                });
            }
        }
        if (mAllSelectedNum.size() > 0){
            View lastSelectView = getChildAt(mAllSelectedNum.get(0));
            lastSelectView.setSelected(!lastSelectView.isSelected());
        }
    }

    private ItemClickListener listener;

    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(String currentSelectedkeywords);
    }

    public List<String> getHistoryList(){
        return mAllSelectedWords;
    }
}