package com.atguigu.stu_slideitemmenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adapter.base.CommonBaseAdapter;
import com.example.adapter.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    // 哈哈哈
    // 嘻嘻嘻
    private ListView lv_main;
    private CommonBaseAdapter<MyBean> adapter;
    private List<MyBean> data = new ArrayList<>();
    private SlideLayout openedLayout;//打开的
    private SlideLayout.OnStateChangeListener onStateChangeListener = new SlideLayout.OnStateChangeListener() {
        @Override
        public void onOpen(SlideLayout layout) {
            openedLayout = layout;
        }

        @Override
        public void onClose(SlideLayout layout) {
            if (openedLayout == layout) {
                openedLayout = null;
            }
        }

        @Override
        public void onDown(SlideLayout layout) {
            if(openedLayout!=null && layout!=openedLayout) {
                openedLayout.closeMenu();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化
        lv_main = (ListView) findViewById(R.id.lv_main);
        initData();
        adapter = new CommonBaseAdapter<MyBean>(this, data, R.layout.item_main) {
            @Override
            public void convert(ViewHolder holder, final int position) {
                final MyBean myBean = data.get(position);
                holder.setText(R.id.tv_item_content, myBean.content)
                        .setOnclickListener(R.id.tv_item_content, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this, myBean.content, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setOnclickListener(R.id.tv_item_menu, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                data.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        });

                SlideLayout slideLayout = (SlideLayout) holder.getConvertView();
                slideLayout.setOnStateChangeListener(onStateChangeListener);
            }
        };
        lv_main.setAdapter(adapter);
    }

    private void initData() {
        for (int i = 1; i < 30; i++) {
            data.add(new MyBean("content" + i));
        }
    }

}
