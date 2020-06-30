package com.zc.xxj.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import com.zc.xxj.R;

public class VerticalProgress extends View {

    private int mRadius;
    private boolean mBorderEnable;
    private boolean mGradientEnable;

    private int mStartResId;
    private int mEndResId;
    private int mBorderColorResId;
    private int mProgressBgColorId;
    private int mBorderWidth;

    private int mProgress = 0;
    private int max = 100;

    private int mWidth;
    private int mHeight;

    private RectF mRectF;
    private Paint mPaint;

    public VerticalProgress(Context context) {
        super(context);
        init(context, null);
    }

    public VerticalProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth() - 1;
        mHeight = getMeasuredHeight() - 1;
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = null;
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.verticalProgress);

            mRadius = typedArray.getInt(R.styleable.verticalProgress_progress_radius, 0);
            mBorderEnable = typedArray.getBoolean(R.styleable.verticalProgress_progress_border_enable, false);
            mGradientEnable = typedArray.getBoolean(R.styleable.verticalProgress_progress_gradient_enable, true);
            mStartResId = typedArray.getResourceId(R.styleable.verticalProgress_progress_start_color, R.color.colorPrimary);
            mProgressBgColorId = typedArray.getResourceId(R.styleable.verticalProgress_progress_solid_color, R.color.white);
            mEndResId = typedArray.getResourceId(R.styleable.verticalProgress_progress_end_color, R.color.color_4EA6FD);
            mBorderColorResId = typedArray.getResourceId(R.styleable.verticalProgress_progress_border_color, R.color.color_4EA6FD);
            mBorderWidth = typedArray.getResourceId(R.styleable.verticalProgress_progress_border_width, 10);
        }

        if (typedArray != null) {
            typedArray.recycle();
        }

        mRectF = new RectF();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mRadius == 0) {
            mRadius = mWidth / 2;
        }

        if (mBorderEnable) {
            mRectF.set(0, 0, mWidth, mHeight);
            mPaint.setColor(getResources().getColor(mBorderColorResId));
            canvas.drawRoundRect(mRectF, mRadius, mRadius, mPaint);
            mPaint.setColor(getResources().getColor(mProgressBgColorId));
            mRectF.set(mBorderWidth, mBorderWidth, mWidth - mBorderWidth, mHeight - mBorderWidth);
            canvas.drawRoundRect(mRectF, mRadius, mRadius, mPaint);
        }

        if (mProgress == 0)
            return;

        float section = mProgress / max;

        mRectF.set(+8, mHeight - mProgress / 100f * mHeight + 10, mWidth - 8, mHeight - 8);

        if (mGradientEnable) {
            LinearGradient shader = new LinearGradient(0, 0, mWidth * section, mHeight,
                    getResources().getColor(mStartResId), getResources().getColor(mEndResId), Shader.TileMode.CLAMP);

            mPaint.setShader(shader);
        }

        canvas.drawRoundRect(mRectF, mRadius, mRadius, mPaint);

        mPaint.setShader(null);
    }

    public void setProgress(int currentCount) {

        this.mProgress = currentCount > max ? max : currentCount;

        postInvalidate();

    }
}