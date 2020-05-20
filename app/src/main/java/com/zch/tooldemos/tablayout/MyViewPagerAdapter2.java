package com.zch.tooldemos.tablayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.zch.tooldemos.demochannel.ChannelEntity;

/**
 * 重新创建fragments 的方案
 * Created by Zch on 2019/4/12 15:19.
 */

public class MyViewPagerAdapter2 extends FragmentStatePagerAdapter {
    private List<ChannelEntity> list = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private FragmentManager fm;

    public MyViewPagerAdapter2(FragmentManager fm, List<ChannelEntity> data) {
        super(fm);
        this.fm = fm;
        this.list = data;
        this.fragments = new ArrayList<>();
        initFragments();
//        for (ChannelEntity entity : data){
//            this.fragments.add(new TabFragment());
//        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }


    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).getName();

    }

    /**
     * 解决bug
     * Java.lang.NullPointerException: Attempt to invoke virtual method 'java.lang.Object android.util.SparseArray.get(int)' on a null object reference
//     * @param state
//     * @param loader
     */
//    @Override
//    public void restoreState(Parcelable state, ClassLoader loader) {
////        super.restoreState(state, loader);
//    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    /**
     * 根据频道信息，添加对应的页面
     */
    public void initFragments(){
        for (ChannelEntity entity : list){
            Log.e("entity.getid", "getid = " + entity.getId());
            Log.e("entity.getid", "getname = " + entity.getName());
            if(!entity.getName().equals("频道0")){
                fragments.add(new TabFragment());
            }else {
                fragments.add(new Fragment1());
            }
        }
    }

    public void RefreshFragments(List<ChannelEntity> datalist){
        //避免重复创建fragment
        if(this.fragments != null){
            FragmentTransaction ft = fm.beginTransaction();
            for(Fragment f:this.fragments){
                ft.remove(f);
            }
            ft.commit();
            ft=null;
            fm.executePendingTransactions();
        }
        this.list = datalist;
        this.fragments = new ArrayList<>();
        initFragments();
        notifyDataSetChanged();
    }
}
