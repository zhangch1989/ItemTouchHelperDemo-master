package com.zch.tooldemos.tablayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.zch.tooldemos.R;
import com.zch.tooldemos.demochannel.ChannelEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zq on 2017/1/12.
 */

public class TabManyActivity extends FragmentActivity {
    private TabLayout tabLayout = null;
    private ViewPager vp_pager;
    private MyViewPagerAdapter adapter;

    private List<ChannelEntity> selecte_channel = new ArrayList<ChannelEntity>();

    public static MyReciver myReciver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tab_layout);
        myReciver = new MyReciver();

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        vp_pager = (ViewPager) findViewById(R.id.tab_viewpager);
        vp_pager.setOffscreenPageLimit(1);
        initView();
    }
    private void initView() {
        initData();
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
//        vp_pager.setAdapter(new MorePagerAdapter(selecte_channel));
        adapter = new MyViewPagerAdapter(getSupportFragmentManager(), selecte_channel);
        vp_pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(vp_pager);
    }

    private void initData(){
        selecte_channel = (List<ChannelEntity>) getIntent().getSerializableExtra("list");;
        if(selecte_channel == null){
            selecte_channel = new ArrayList<>();
            selecte_channel.add(new ChannelEntity("测试频道1"));
            selecte_channel.add(new ChannelEntity("测试频道2"));
            selecte_channel.add(new ChannelEntity("测试频道3"));
            selecte_channel.add(new ChannelEntity("测试频道4"));
        }
    }

    final class MorePagerAdapter extends PagerAdapter {
        private List<ChannelEntity> list = new ArrayList<>();

        public MorePagerAdapter(List<ChannelEntity> data){
            this.list = data;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TextView tv = new TextView(TabManyActivity.this);
            tv.setText("布局" + position);
            tv.setTextSize(30.0f);
            tv.setGravity(Gravity.CENTER);
            (container).addView(tv);
            return tv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            (container).removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return list.get(position).getName();
        }
    }

    public class MyReciver extends Handler{
        @Override
        public void handleMessage(Message msg) {
//            selecte_channel.remove(0);
            selecte_channel.add(1,new ChannelEntity((long) 0, "新增频道"));
            adapter.RefreshFragments(selecte_channel);
            vp_pager.setCurrentItem(0);
            tabLayout.scrollTo(0,0);
        }
    }

}