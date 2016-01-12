package com.atguigu.customview;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class MainActivity extends Activity {

    private MyTextView mtv_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //加载布局文件, 创建对应的视图对象树
        mtv_main = (MyTextView)findViewById(R.id.mtv_main);

        /*
        View view = View.inflate(this, R.layout.activity_main, null);//view是什么布局文件的根布局类型
        setContentView(view);
        */
/*

        MyTextView view = new MyTextView(this);
        view.setText("atguigu");
        setContentView(view);
*/


        //得到整个界面的根视图对象
        Window window = getWindow();
        View decorView = window.getDecorView();
        String rootTypeName = decorView.getClass().getSuperclass().getName();
        //Log.e("TAG", "rootTypeName="+rootTypeName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("TAG", "Activity onResume()");
    }
    
    public void forceLayout(View v) {
        mtv_main.requestLayout();
    }

    public void forceDraw(View v) {
        //mtv_main.invalidate();//只能在主线程执行
        //mtv_main.postInvalidate(); //可以在分线程执行

        new Thread(){
            public void run(){
                //mtv_main.invalidate();
                mtv_main.postInvalidate();
            }
        }.start();
    }

    public void removeView(View v) {
        ViewGroup viewGroup = (ViewGroup) mtv_main.getParent();
        viewGroup.removeView(mtv_main);
    }

}
