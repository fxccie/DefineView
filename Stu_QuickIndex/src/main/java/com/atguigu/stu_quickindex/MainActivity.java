package com.atguigu.stu_quickindex;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.ListView;
import android.widget.TextView;

import com.example.adapter.base.CommonBaseAdapter;
import com.example.adapter.base.ViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {

    private ListView lv_main;
    private TextView tv_main_word;
    private QuickIndexView qiv_main_words;

    private List<Person> data;
    private CommonBaseAdapter<Person> adapter;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 ListView、TextView和QiuckIndexView
        lv_main = (ListView)findViewById(R.id.lv_main);
        tv_main_word = (TextView)findViewById(R.id.tv_main_word);
        qiv_main_words = (QuickIndexView)findViewById(R.id.qiv_main_words);

        //给qiv_main_words设置自定义的下标改变的监听
        qiv_main_words.setOnIndexChangeListener(new QuickIndexView.OnIndexChangeListener() {
            @Override
            public void onIndexChange(String word) {//操作的下标已经改变了
                //移除未处理的消息
                handler.removeCallbacksAndMessages(null);

                tv_main_word.setVisibility(View.VISIBLE);
                tv_main_word.setText(word);

                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).getPinYin().substring(0, 1).equals(word)) {
                        // ListView 定位到指定下标处
                        lv_main.setSelection(i);
                        return;
                    }
                }
            }

            @Override
            public void onUp() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {//只有通过new Thread().start()导致run()执行才是分线程执行
                        // 透明度 从 1 → 0
                        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0);
                        alphaAnimation.setDuration(500);
                        AnimationSet animationSet = new AnimationSet(false);
                        animationSet.addAnimation(alphaAnimation);
                        // 为视图添加动画，调用 startAnimation()，而不是setAnimation()
                        tv_main_word.startAnimation(animationSet);
                        tv_main_word.setVisibility(View.GONE);
                    }
                }, 2000);
            }
        });

        initData();

        // 初始化 Adapter
        adapter = new CommonBaseAdapter<Person>(this, data, R.layout.item_main) {
            @Override
            public void convert(ViewHolder holder, int position) {
                Person person = data.get(position);
                String pinYin = person.getPinYin().substring(0, 1);
                if (position == 0) {
                    holder.setVisibility(R.id.tv_item_pinyin, View.VISIBLE);
                } else {
                    Person perPerson = data.get(position-1);
                    String perPinYin = perPerson.getPinYin().substring(0, 1);
                    if (pinYin.equals(perPinYin)) {// 如果当前拼音和前一个Itemd的pinYin相等，那么不显示
                        holder.setVisibility(R.id.tv_item_pinyin, View.GONE);
                    } else {
                        holder.setVisibility(R.id.tv_item_pinyin, View.VISIBLE);
                    }
                }
                holder.setText(R.id.tv_item_pinyin, pinYin);
                holder.setText(R.id.tv_item_name, person.getName());
            }
        };

        // 为 ListView 设置 adapter
        lv_main.setAdapter(adapter);
    }

    private void initData() {
        data = new ArrayList<>();
        // 虚拟数据
        data.add(new Person("张晓飞"));
        data.add(new Person("杨光福"));
        data.add(new Person("胡继群"));
        data.add(new Person("刘畅"));

        data.add(new Person("钟泽兴"));
        data.add(new Person("尹革新"));
        data.add(new Person("安传鑫"));
        data.add(new Person("张骞壬"));

        data.add(new Person("温松"));
        data.add(new Person("李凤秋"));
        data.add(new Person("刘甫"));
        data.add(new Person("娄全超"));
        data.add(new Person("张猛"));

        data.add(new Person("王英杰"));
        data.add(new Person("李振南"));
        data.add(new Person("孙仁政"));
        data.add(new Person("唐春雷"));
        data.add(new Person("牛鹏伟"));
        data.add(new Person("姜宇航"));

        data.add(new Person("刘挺"));
        data.add(new Person("张洪瑞"));
        data.add(new Person("张建忠"));
        data.add(new Person("侯亚帅"));
        data.add(new Person("刘帅"));

        data.add(new Person("乔竞飞"));
        data.add(new Person("徐雨健"));
        data.add(new Person("吴亮"));
        data.add(new Person("王兆霖"));

        data.add(new Person("阿三"));

        //对集合中的对象进行排序
        Collections.sort(data);
    }


}
