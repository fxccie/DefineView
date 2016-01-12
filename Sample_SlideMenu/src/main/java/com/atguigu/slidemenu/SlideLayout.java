package com.atguigu.slidemenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * 自定义ViewGroup: 侧滑菜单
 * @author 张晓飞
 *
 * 1. 正常的初始化显示
 *	1). 重写onFinishInflate(): 得到菜单视图
 *  2). 重写onMeasure(): 得到菜单视图的宽高
 *	3). 重写OnLayout(): 对菜单进行重新布局
 * 2. 拖动菜单
 *  2.1). 重写onTouchEvent(): 响应用户的操作
 *  2.2). 在move时计算事件的偏移, 对当前布局进行滚动
 *  2.3). 在up时, 根据布局决的偏移量, 来判断是打开/关闭菜单
 *  2.4). 使用Scoller实现平滑打开/关闭
 */
public class SlideLayout extends FrameLayout {

	private View menuView;
	private int menuWidth, menuHeight;
	private Scroller scroller;
	private boolean isOpen = false;//标识初始为关闭
	public SlideLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		scroller = new Scroller(context);
	}

	//1.1). 重写onFinishInflate(): 得到菜单视图
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		menuView = getChildAt(1);
	}

	//1.2). 重写onMeasure(): 得到菜单视图的宽高
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		menuWidth = menuView.getMeasuredWidth();
		menuHeight = menuView.getMeasuredHeight();
	}

	//1.3). 重写OnLayout(): 对菜单进行重新布局
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		menuView.layout(-menuWidth, 0, 0, menuHeight);
	}

	//2.1). 重写onTouchEvent(): 响应用户的操作
	//2.2). 在move时计算事件的偏移, 对当前布局进行滚动
	//2.3). 在up时, 根据布局的偏移量, 来判断是打开/关闭菜单
	private int lastX;
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int eventX = (int) event.getRawX();
		switch (event.getAction()) {
		    case MotionEvent.ACTION_DOWN :
				lastX = eventX;
		        break;
		    case MotionEvent.ACTION_MOVE :
				int dx = eventX-lastX;
				int scrollX = getScrollX()-dx;
				//限制scrollX : [-menuWidth, 0]
				if(scrollX<-menuWidth) {
					scrollX = -menuWidth;
				} else if(scrollX>0) {
					scrollX = 0;
				}

				scrollTo(scrollX, getScrollY());
				lastX = eventX;
		        break;
		    case MotionEvent.ACTION_UP :
				//得到布局的偏移量
				int totalScrollX = getScrollX();
				if(totalScrollX<=-menuWidth/2) {
					openMenu();
				} else {
					closeMenu();
				}
				break;
		}
		return true;
	}

	/**
	 * 平滑的关闭菜单-->0
	 */
	private void closeMenu() {
		scroller.startScroll(getScrollX(), getScrollY(), -getScrollX(), -getScrollY());
		invalidate();
		isOpen = false;
	}

	/**
	 * 平滑的打开菜单-->-menuWidth
	 */
	private void openMenu() {
		scroller.startScroll(getScrollX(), getScrollY(), -menuWidth-getScrollX(), -getScrollY());
		invalidate();
		isOpen = true;
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		if(scroller.computeScrollOffset()) {
			scrollTo(scroller.getCurrX(), scroller.getCurrY());
			invalidate();
		}
	}

	public void switchState() {
		if (isOpen) {
			closeMenu();
		} else {
			openMenu();
		}
	}
}
