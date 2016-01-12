package com.atguigu.slideitemmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * 1. 正常的初始显示item
 *  1). 得到子View对象(contentView, menuView) : onFinishInflate()
 *  2). 得到子View的宽高: onMeasure()
 *  3). 对菜单视图进行重新布局: onLayout()
 * 2. 通过手势拖动打开或关闭menu
 *  2.1). 响应用户操作: onTouchEvent() 返回true
 *  2.2). move时计算事件的移动, 对视图进行对应的滚动: scrollTo()
 *  2.3). up时, 得到总的偏移量, 判断是平滑打开/平滑关闭
 *  2.4). 实现平滑打开和关闭: 使用Scroller
 * 3. 使用ListView显示列表:
 *  3.1). 利用CommonBaseAdapter显示列表
 *  3.2). 发现问题:
 *      1).有时不能自动打开和关闭
 *      2).可以打开个多个
 * 4. 解决问题1: 有时不能自动打开和关闭
 *  本质: 当前视图(SlideLayout)与父视图(ListView)的事件冲突
 *  原因: 当用户在竖起方向有一定移动量时, ListView拦截了事件,
 *      一旦拦截了事件再也会传给当前视图(也就无法自动打开和关闭)
 *  解决: 不让父视图拦截(反拦截: 不父View拦截事件)
 *      什么时候拦截? : 当得知用户是在做水平移动: totalDx>totalDy  & totalDx>10
 *      拦截: getParent().requestDisallowInterceptTouchEvent(true);
 *
 * 5. 解决当前视图与子视图的事件冲突
 *    解决: 拦截(不让子View消费事件)
 *    如何拦截? : onInterceptTouchEvent() 返回true
 *    什么条件下? : totalDx>8
 *
 * 6. 解决可以打开多个Item的问题
 *    解决办法: 自定义监听器
 *
 */
public class SlideLayout extends FrameLayout {

    private View contentView, menuView;
    private int contentWidth, menuWidth, viewHeight;
    private Scroller scroller;

    public SlideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
    }

    //1). 得到子View对象(contentView, menuView) : onFinishInflate()
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        menuView = getChildAt(1);
    }

    //2). 得到子View的宽高: onMeasure()

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        contentWidth = contentView.getMeasuredWidth();
        //contentWidth = getMeasuredWidth(); //可以
        menuWidth = menuView.getMeasuredWidth();
        //menuWidth = getMeasuredWidth(); //不可以
        viewHeight = getMeasuredHeight();
    }

    //3). 对菜单视图进行重新布局: onLayout()
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //只对menuView进行布局
        menuView.layout(contentWidth, 0, contentWidth+menuWidth, viewHeight);
    }

    //2.1). 响应用户操作: onTouchEvent() 返回true
    //2.2). move时计算事件的移动, 对视图进行对应的滚动: scrollTo()
    private int downX, lastX, downY, lastY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventX = (int) event.getRawX();
        int eventY = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                downX = lastX = eventX;
                downY = lastY = eventY;
                break;
            case MotionEvent.ACTION_MOVE :
                int dx = eventX-lastX;
                //滚动视图
                int toScrollX = getScrollX()-dx;
                //限制[0,menuWidth]
                if (toScrollX < 0) {
                    toScrollX = 0;
                } else if (toScrollX > menuWidth) {
                    toScrollX = menuWidth;
                }
                scrollTo(toScrollX, getScrollY());
                lastX = eventX;

                //什么时候拦截? : 当得知用户是在做水平移动: totalDx>totalDy  & totalDx>10
                int totalDx = Math.abs(eventX-downX);
                int totalDy = Math.abs(eventY-downY);
                if (totalDx > totalDy && totalDx > 8) {
                    //不父视图不要拦截事件
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP :
                //2.3). up时, 得到总的偏移量, 判断是平滑打开/平滑关闭
                int totalScrollX = getScrollX();
                if (totalScrollX < menuWidth / 2) {
                    closeMenu();
                } else {
                    openMenu();
                }
                break;
        }
        //return super.onTouchEvent(event);
        return true;
    }

    //2.4). 实现平滑打开和关闭
    public void openMenu() {//-->menuWidth
        scroller.startScroll(getScrollX(), getScrollY(), menuWidth - getScrollX(), 0);
        invalidate();
        if (onStateChangeListener != null) {
            onStateChangeListener.onOpen(this);
        }
    }
    public void closeMenu() {//-->0
        scroller.startScroll(getScrollX(), getScrollY(),-getScrollX(), 0);
        invalidate();

        if (onStateChangeListener != null) {
            onStateChangeListener.onClose(this);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }

    //什么条件下? : totalDx>8
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
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
                if (totalDx > 8) {
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
