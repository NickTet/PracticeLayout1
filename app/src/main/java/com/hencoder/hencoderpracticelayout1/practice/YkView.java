package com.hencoder.hencoderpracticelayout1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class YkView extends View {

    private int mWidth;
    private int mHeight;
    private Point mCenterPoint;
    private int mRadius;
    private Context mContext;
    private Paint mPaint;
    private boolean mIsDrawInside = false;

    public YkView(Context context) {
        this(context, null);
    }

    public YkView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YkView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dp2px(4));
        mRadius = dp2px(20);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        if (!mIsDrawInside)
        drawInside(canvas);

        drawOutSide(canvas);
    }

    private void drawInside(Canvas canvas) {
        mIsDrawInside = true;
        canvas.drawArc(mCenterPoint.x - mRadius, mCenterPoint.y - mRadius,
                mCenterPoint.x + mRadius, mCenterPoint.x + mRadius, 360, 360, false, mPaint);
        mPaint.setStrokeWidth(0);
        mPaint.setColor(Color.BLACK);
    }

    private void drawOutSide(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(1);
        mRadius += dp2px(20);
        canvas.drawArc(mCenterPoint.x - mRadius, mCenterPoint.y - mRadius,
                mCenterPoint.x + mRadius, mCenterPoint.x + mRadius, 360, 360, false, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mWidth == 0) {
            mWidth = getMeasuredWidth();
            mHeight = getMeasuredHeight();
            mCenterPoint = new Point(mWidth / 2, mHeight / 2);
        }
    }

    public int dp2px(float dipValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
