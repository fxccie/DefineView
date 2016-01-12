package com.atguigu.stu_circlebar;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

public class MainActivity extends Activity {

    private CircleBar cb_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cb_main = (CircleBar) findViewById(R.id.cb_main);
    }

    private boolean isRunning = false;

    public void download(View v) {

        if (isRunning)
            return; //如果正在运行，则return，保证一次只能运行一次线程
        new Thread() {
            public void run() {
                isRunning = true;
                cb_main.setProgress(0);//如不设置，当再次点击Button时，会出现200%

                int count = 55;
                //设置最大进度
                cb_main.setMax(count);
                for (int i = 0; i < count; i++) {
                    SystemClock.sleep(100);
                    cb_main.setProgress(cb_main.getProgress() + 1);
                }
                isRunning = false;
            }
        }.start();
    }
}
