package com.atguigu.stu_scrolltest;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.Scroller;

/**
 * Created by Fx on 2016/1/11.
 */
public class MyImageView extends ImageView {

    private final Scroller scroller;

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
    }


    public void smoothReset() {
        //开始滚动(并不真正滚动, 而保存了数据)
        // 第五个参数表示多少毫秒时间内完成
        scroller.startScroll(getScrollX(), getScrollY(), -getScrollX(), -getScrollY(), 1000);
        // 强制重绘 —> draw() —> computeScroll()
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        //计算此次item单元移动的偏移量并保存, 如果已经到达目标处, 直接返回false, 如果需要返回true
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY()); // 移动
            // 强制重绘 —> draw() —> computeScroll()
            invalidate();// 重绘
        }
    }
}
