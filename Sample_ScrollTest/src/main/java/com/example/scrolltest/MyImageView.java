package com.example.scrolltest;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.Scroller;

public class MyImageView extends ImageView {

	private Scroller scroller;

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);

		scroller = new Scroller(context);
	}

	/**
	 * 平滑的复位
	 */
	public void smoothReset() {
		//开始滚动(并不真正滚动, 而保存了数据)
		scroller.startScroll(getScrollX(), getScrollY(), -getScrollX(), -getScrollY(), 1000);

		//强制重绘制-->draw()-->computeScroll()
		invalidate();
	}

	@Override
	public void computeScroll() {
		//计算此次item单元移动的偏移量并保存, 如果已经到达目标处, 直接返回false, 如果需要返回true
		if(scroller.computeScrollOffset()) {
			//偏移(瞬间)
			scrollTo(scroller.getCurrX(), scroller.getCurrY()); // 这就调用了ondraw()方法
			//强制重绘制-->draw()-->computeScroll()
			invalidate();
		}
	}
}
