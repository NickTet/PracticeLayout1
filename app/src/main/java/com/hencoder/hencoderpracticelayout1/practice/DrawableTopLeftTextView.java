package com.hencoder.hencoderpracticelayout1.practice;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.hencoder.hencoderpracticelayout1.R;

public class DrawableTopLeftTextView extends android.support.v7.widget.AppCompatTextView{
    //左侧图片
    private Bitmap bitmap;
    private int mHeight;
    private int mWidth;
    private Paint paint;
    private int drawableresId;
    private int drawablePadding;
    public DrawableTopLeftTextView(Context context) {
        this(context,null);
    }

    public DrawableTopLeftTextView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public DrawableTopLeftTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray=context.getTheme().obtainStyledAttributes(attrs,R.styleable.DrawableTopLeftTextview,defStyleAttr,0);
        drawableresId=typedArray.getResourceId(R.styleable.DrawableTopLeftTextview_drawableTopLeftLeft,R.mipmap.ic_launcher);
        drawablePadding=typedArray.getInteger(R.styleable.DrawableTopLeftTextview_drawableTopLeftPadding,10);
        bitmap= BitmapFactory.decodeResource(getResources(),drawableresId);
        setIncludeFontPadding(false);
        paint=new Paint();
        setPadding(getPaddingLeft()+drawablePadding+bitmap.getWidth(),getPaddingTop(),getPaddingRight(),getPaddingBottom());
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight=getMeasuredHeight();
        mWidth=getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap,0,0,paint);
        super.onDraw(canvas);
    }
}
