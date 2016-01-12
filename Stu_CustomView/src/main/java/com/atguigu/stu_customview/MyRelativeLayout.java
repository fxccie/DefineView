package com.atguigu.stu_customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

/**
 * Created by furong-pc on 2015/12/30.
 */
public class MyRelativeLayout extends RelativeLayout {

    private MyTextView childView;
    public MyRelativeLayout(Context context) {
        super(context);
        Log.e("TAG", "MyRelativeLayout(Context context)");
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e("TAG", "MyRelativeLayout(Context context, AttributeSet attrs)");
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.e("TAG", "MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr)");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.e("TAG", "MyRelativeLayout onFinishInflate()");
        childView = (MyTextView) getChildAt(0);
        String viewName = childView.getClass().getName();
        Log.e("TAG","childView:"+viewName);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e("TAG", "MyRelativeLayout onAttachedToWindow()");
    }
}
