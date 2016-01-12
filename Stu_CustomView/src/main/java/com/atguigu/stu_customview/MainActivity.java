package com.atguigu.stu_customview;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 显示ContentView的3中方式
         */

        // 方式1：
        // setContentView(R.layout.activity_main);

        // 方式2：
        /*View view = View.inflate(this, R.layout.activity_main, null); // View是什么？布局文件的根布局文件
        setContentView(view); // 加载布局文件，创建试图对象树
        String viewName = view.getClass().getName();
        Log.e("TAG","viewName:"+viewName); // viewName:android.widget.RelativeLayout*/

       //方式3：
        /*TextView textView = new TextView(this);
        textView.setText("Hello World!");
        setContentView(textView);
        String viewName = textView.getClass().getName();
        Log.e("TAG","viewName:"+viewName); // viewName:android.widget.TextView*/

//        MyTextView myTextView = new MyTextView(this);
//        myTextView.setText("Hello World!");
//        myTextView.setTextColor(Color.RED);
//        setContentView(myTextView);
        // TAG标签：
        // MyTextView(Context context)
        // onAttachedToWindow()


        setContentView(R.layout.activity_main);

        //得到根视图
        /*Window window = getWindow();
        View decorView = window.getDecorView();
        String rootView = decorView.getClass().getSuperclass().getName();
        Log.e("TAG", "rootView："+rootView); // rootView：android.widget.FrameLayout*/
    }
}
