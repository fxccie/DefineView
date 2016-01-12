package com.atguigu.stu_customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by furong-pc on 2015/12/30.
 */
public class MyTextView extends TextView {
    public MyTextView(Context context) {
        super(context);
        Log.e("TAG", "MyTextView(Context context)");
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e("TAG", "MyTextView(Context context, AttributeSet attrs)");
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.e("TAG", "MyTextView(Context context, AttributeSet attrs, int defStyleAttr)");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.e("TAG", "MyTextView onFinishInflate()");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e("TAG", "MyTextView onAttachedToWindow()");
    }
}
