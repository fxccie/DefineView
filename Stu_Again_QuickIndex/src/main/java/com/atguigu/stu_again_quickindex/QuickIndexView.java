package com.atguigu.stu_again_quickindex;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Fx on 2016/1/7.
 */
public class QuickIndexView extends View {

    // 画笔对象
    private Paint paint;

    // Item的 宽度 和 高度
    private float itemWidth;
    private float itemHeight;

    // 26个字母的数组
    private String[] words = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };

    public QuickIndexView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setTextSize(30);
        paint.setColor(Color.WHITE);
    }

    /**
     * 获取当前视图的宽度和高度
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取当前视图的 宽度 和 高度
        itemWidth = getMeasuredWidth();
        itemHeight = getMeasuredHeight()/26;
    }

    /**
     * 在此方法中绘制26个大写的英文字母
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < words.length; i++) {
            if (currentIndex == i) {
                paint.setTextSize(45);
                paint.setColor(Color.GRAY);
            } else {
                paint.setTextSize(30);
                paint.setColor(Color.WHITE);
            }
            Rect bounds = new Rect();
            paint.getTextBounds(words[i], 0, words[i].length(), bounds);
            // 文本的宽高
            int wordWidth = bounds.width();
            int wordHeight = bounds.height();
            // 文本的坐标
            float wordX = itemWidth/2 - wordWidth/2;
            float wordY = itemHeight/2 + wordHeight/2 + i * itemHeight;

            // 绘制数据
            canvas.drawText(words[i], wordX , wordY, paint);
        }
    }

    private int currentIndex = -1;

    /**
     * 触摸时，字母变大，颜色为灰色
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // 判断当前的索引值
                float eventY = event.getY();
                currentIndex = (int) (eventY / itemHeight);
                // 重新绘制
                invalidate();

                //当事件发生时调用监听器的回调方法
                if (onIndexChangeListen != null) {
                    onIndexChangeListen.onIndexChange(words[currentIndex]);
                }

                break;
            case MotionEvent.ACTION_UP:
                currentIndex = -1;
                invalidate();
                if (onIndexChangeListen != null) {
                    onIndexChangeListen.onUp();
                }
                break;
        }
        return true;
    }

    private OnIndexChangeListen onIndexChangeListen;

    public void setOnIndexChangeListen(OnIndexChangeListen onIndexChangeListen) {
        this.onIndexChangeListen = onIndexChangeListen;
    }
    // 定义自定义监听
    public interface OnIndexChangeListen {
        // 索引发生改变
        void onIndexChange(String word);
        // 触摸事件 UP 时
        void onUp();
    }
}
