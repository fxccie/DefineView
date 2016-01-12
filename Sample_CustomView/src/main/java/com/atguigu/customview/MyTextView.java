package com.atguigu.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by xfzhang on 2015/12/28.
 */
public class MyTextView extends TextView {

    //自己 new
    public MyTextView(Context context) {
        super(context);
        Log.e("TAG", "MyTextView(context)");
    }

    //布局文件(系统反射调用)
    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e("TAG", "MyTextView(context,attrs)");
    }

    //布局文件+引入样式(系统反射调用)
    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    //当加载完布局并创建好整个视图对象树才调用
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.e("TAG", "onFinishInflate()");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e("TAG", "onAttachedToWindow()");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("TAG", "onMeasure()");
        //得到当前视图或子View的测量宽高
        this.getMeasuredHeight();
        this.getMeasuredWidth();
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        Log.e("TAG", "layout()");
        super.layout(l, t, r, b);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.e("TAG", "onLayout() "+changed);
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void draw(Canvas canvas) {
        Log.e("TAG", "draw()");
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e("TAG", "onDraw()");
        super.onDraw(canvas);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("TAG", "onDetachedFromWindow()");
    }
}
