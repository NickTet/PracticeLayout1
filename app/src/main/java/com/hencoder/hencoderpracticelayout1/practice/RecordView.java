package com.hencoder.hencoderpracticelayout1.practice;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.hencoder.hencoderpracticelayout1.R;

public class RecordView extends View{
    //录制时间
    private long mRecordTime=10*1000;
    private int mWidth;
    private int mHeight;
    private int mcenterX,mcenterY;
    private int mRadius;
    private Paint mPaint;
    //外环圆的颜色，进度颜色
    private int mRoundColor;
    //外环圆的宽度
    private int mRoundWidth;
    //外环圆进度的宽度,颜色
    private int mProgressWidth=20;
    private int mProgressColor;
    //内环圆的颜色，进度颜色
    private int mInnerRoundColor;
    //外环圆的宽度
    private float progress;
    //进度条动画
    ValueAnimator valueAnimator;
    private RecordResultListener mDefaultRecordResultListener;

    public void setDefaultRecordResultListener(RecordResultListener defaultRecordResultListener) {
        mDefaultRecordResultListener = defaultRecordResultListener;
    }

    public RecordView(Context context) {
        this(context,null);
    }

    public RecordView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public RecordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs,defStyleAttr);
    }

    private void init(@Nullable AttributeSet attrs, int defStyleAttr) {
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        TypedArray typedArray=getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.RecordView,defStyleAttr,0);
        if (typedArray==null)return;
        mRoundColor=typedArray.getColor(R.styleable.RecordView_OutRoundColor,Color.RED);
        mRoundWidth=typedArray.getInt(R.styleable.RecordView_OutRoundWidth,20);
        mInnerRoundColor=typedArray.getColor(R.styleable.RecordView_InnerRoundColor,Color.WHITE);
        mProgressColor=typedArray.getColor(R.styleable.RecordView_ProgressCircleColor,Color.RED);
        mRadius=typedArray.getInteger(R.styleable.RecordView_RadiusWidth,200);
        mProgressWidth=typedArray.getInteger(R.styleable.RecordView_ProgressCircleWidth,20);
        mRadius=typedArray.getInteger(R.styleable.RecordView_RadiusWidth,200);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measurewidth=MeasureSpec.getSize(widthMeasureSpec);
        int measurewidthMode=MeasureSpec.getMode(widthMeasureSpec);
        int measureheight=MeasureSpec.getMode(heightMeasureSpec);
        int measureheightMode=MeasureSpec.getMode(heightMeasureSpec);
        int width = 0,height=0;
        switch (measurewidthMode){
            case MeasureSpec.EXACTLY:
                width=measurewidth;
                break;
            case MeasureSpec.AT_MOST:
                width=(int) (mRadius*2/0.7)+2*mRoundWidth+getPaddingTop()+getPaddingBottom();
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        switch (measureheightMode){
            case MeasureSpec.EXACTLY:
                height=measureheight;
                break;
            case MeasureSpec.AT_MOST:
                height=(int) (mRadius*2/0.7)+2*mRoundWidth+getPaddingTop()+getPaddingBottom();
//                height=2*mRadius+mRoundWidth+getPaddingTop()+getPaddingBottom();
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        setMeasuredDimension(width,height);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRoundCircle(canvas);
        drawInnerRoundCircle(canvas);
        drawProgressCircle(canvas);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth=w;
        mHeight=h;
        mcenterX=mWidth/2;
        mcenterY=mHeight/2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                cancelAnimation();
                startScaleAnimation();
                break;
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
                reset();
                progress=0;
                invalidate();
                cancelAnimation();
        }
        return true;
    }

    /**
     * 绘制外圆环
     * @param canvas
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void drawRoundCircle(Canvas canvas){
        canvas.translate(mcenterX,mcenterY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mRoundWidth);
        mPaint.setColor(mRoundColor);
        RectF rectF=new RectF(-mRadius,-mRadius,mRadius,mRadius);
        canvas.drawArc(rectF,-90,360,false,mPaint);
    }
    /**
     * 绘制外圆环
     * @param canvas
     */
    private void drawInnerRoundCircle(Canvas canvas){
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mInnerRoundColor);
        canvas.drawCircle(0,0,mRadius,mPaint);
    }
    /**
     * 绘制外圆进度条
     * @param canvas
     */
    private void drawProgressCircle(Canvas canvas){
        mPaint.setColor(mProgressColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mProgressWidth);
        //绘制进度调
        RectF rectF=new RectF(-mRadius-mRoundWidth/2+mProgressWidth/2,-mRadius-mRoundWidth/2+mProgressWidth/2,mRadius+mRoundWidth/2-mProgressWidth/2,mRadius+mRoundWidth/2-mProgressWidth/2);
        canvas.drawArc(rectF,-90,progress,false,mPaint);
    }

    private void startAnimation(){
        valueAnimator=ValueAnimator.ofFloat(0,1.f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value= (float) animation.getAnimatedValue();
                progress=360*value;
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                progress=0;
                invalidate();
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(mRecordTime);
        valueAnimator.setRepeatCount(0);
        valueAnimator.start();
    }
    private void cancelAnimation(){
        if (valueAnimator!=null&&valueAnimator.isRunning()){
            valueAnimator.cancel();
            valueAnimator=null;
        }
    }
    public interface RecordResultListener{
        void complete();
    }

    /**
     * 放大缩小动画
     */
    private void startScaleAnimation(){
        mRadius= (int) (0.7*mRadius);
        mRoundWidth=mRoundWidth*2;
        invalidate();
        startAnimation();
    }

    private void reset(){
        mRadius= (int) (mRadius/0.7);
        mRoundWidth=mRoundWidth/2;
    }
}
