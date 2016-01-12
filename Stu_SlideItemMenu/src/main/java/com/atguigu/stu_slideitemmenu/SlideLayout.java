package com.atguigu.stu_slideitemmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by Fx on 2016/1/11.
 * 滑动布局
 * <p/>
 * 1. 正常的初始显示item
 * 1). 得到子View对象(contentView, menuView) : onFinishInflate()
 * 2). 得到子View的宽高: onMeasure()
 * 3). 对菜单视图进行重新布局: onLayout()
 * 2. 通过手势拖动打开或关闭menu
 * 2.1). 响应用户操作: onTouchEvent() 返回true
 * 2.2). move时计算事件的移动, 对视图进行对应的滚动: scrollTo()
 * 2.3). up时, 得到总的偏移量, 判断是平滑打开/平滑关闭
 * 2.4). 实现平滑打开和关闭: 使用Scroller
 * 3. 使用ListView显示列表:
 * 3.1). 利用CommonBaseAdapter显示列表
 * 3.2). 发现问题:
 * 1).有时不能自动打开和关闭
 * 2).可以打开个多个
 * 4. 解决问题1: 有时不能自动打开和关闭
 * 本质: 当前视图(SlideLayout)与父视图(ListView)的事件冲突
 * 原因: 当用户在竖起方向有一定移动量时, ListView拦截了事件,
 * 一旦拦截了事件再也会传给当前视图(也就无法自动打开和关闭)
 * 解决: 不让父视图拦截(反拦截: 不让父View拦截事件)
 * 什么时候拦截? : 当得知用户是在做水平移动: totalDx>totalDy  & totalDx>10
 * 拦截: getParent().requestDisallowInterceptTouchEvent(true);
 * <p/>
 * 5. 解决当前视图与子视图的事件冲突
 * 解决: 拦截(不让子View消费事件)
 * 如何拦截? : onInterceptTouchEvent() 返回true
 * 什么条件下? : totalDx>10
 * <p/>
 * 6. 解决可以打开多个Item的问题
 * 解决办法: 自定义监听器
 */
public class SlideLayout extends FrameLayout {

    private View tv_item_content;
    private View tv_item_menu;

    private int contentWidth;
    private int menuWidth;
    private int viewHeight;
    private final Scroller scroller;

    public SlideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
    }

    /**
     * 完成解析后，获取子View
     */

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tv_item_content = getChildAt(0);
        tv_item_menu = getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取子View的宽高，为后面的布局做基础
        // 宽度
        contentWidth = tv_item_content.getMeasuredWidth();
        menuWidth = tv_item_menu.getMeasuredWidth();
        // 高度
        viewHeight = getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        tv_item_menu.layout(contentWidth, 0, contentWidth + menuWidth, viewHeight);
    }

    private int lastX, lastY, downX, downY;

    /**
     * 2.1). 响应用户操作: onTouchEvent() 返回true
     * 2.2). move时计算事件的移动, 对视图进行对应的滚动: scrollTo()
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventX = (int) event.getRawX();
        int eventY = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 因为 onInterceptTouchEvent() 这个方法，所以down事件不会再发生
                downX = lastX = eventX;
                downY = lastY = eventY;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = eventX - lastX;
                //滚动视图
                int toScrollX = getScrollX() - dx;
                //限制[0,menuWidth]
                if (toScrollX < 0) {
                    toScrollX = 0;
                } else if (toScrollX > menuWidth) {
                    toScrollX = menuWidth;
                }
                scrollTo(toScrollX, getScrollY());
                lastX = eventX;

                int totalX = Math.abs(eventX - downX);
                int totalY = Math.abs(eventY - downY);

                // 如果 totalX > totalY， 并且totalX > 10, 则反拦截
                if (totalX > totalY && totalX > 10) {
                    // 反拦截
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                //2.3). up时, 得到总的偏移量, 判断是平滑打开/平滑关闭
                int totalScrollX = getScrollX();
                if (totalScrollX < menuWidth / 2) {
                    // 关闭菜单
                    closeMenu();
                } else {
                    // 打开菜单
                    openMenu();
                }
                break;
        }

        return true;// *** 返回true，代表消费了此事件
    }

    /**
     * 滑动打开菜单
     */
    public void openMenu() {
        scroller.startScroll(getScrollX(), getScrollY(), menuWidth - getScrollX(), 0);
        invalidate();// 重新绘制 -> draw() -> computeScroll
        if (onStateChangeListener != null) {
            onStateChangeListener.onOpen(this);
        }
    }

    /**
     * 滑动关闭菜单
     */
    public void closeMenu() {
        scroller.startScroll(getScrollX(), getScrollY(), -getScrollX(), 0);
        invalidate();// 重新绘制 -> draw() -> computeScroll
        if (onStateChangeListener != null) {
            onStateChangeListener.onClose(this);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY()); // 在
            invalidate(); // 重新绘制 -> draw() -> computeScroll
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // totalX<10时，进行拦截
        boolean intercept = false;

        int eventX = (int) ev.getRawX();
        int eventY = (int) ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN :
                downX = lastX = eventX;
                downY = lastY = eventY;

                if (onStateChangeListener != null) {
                    onStateChangeListener.onDown(this);
                }
                break;
            case MotionEvent.ACTION_MOVE :
                int totalDx = Math.abs(eventX-downX);
                if (totalDx > 10) {
                    intercept = true;
                }
                break;
        }

        return intercept;
    }

    private OnStateChangeListener onStateChangeListener;

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    public interface OnStateChangeListener {
        //当打开时调用
        public void onOpen(SlideLayout layout);
        //当关闭时调用
        public void onClose(SlideLayout layout);
        //当down时调用
        public void onDown(SlideLayout layout);
    }
}
