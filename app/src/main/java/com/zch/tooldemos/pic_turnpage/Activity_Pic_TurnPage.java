package com.zch.tooldemos.pic_turnpage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zch.tooldemos.R;
import com.zch.tooldemos.pageturn.FileUtils;

import se.emilsjolander.flipview.FlipView;
import se.emilsjolander.flipview.OverFlipMode;

/**
 * Created by Zch on 2020/5/7 10:42.
 * 图片翻页效果
 */

public class Activity_Pic_TurnPage extends Activity {
    private final static String TAG = "Activity_Pic_TurnPage";
    private Context context;

    private static final String[] pages = {"1.png","2.png","3.png","4.png"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_turnpage);
        context = this;
        initView();
    }

    private void initView(){
        FlipView flipView = (FlipView) findViewById(R.id.flip_view);
        MyAdapter adapter = new MyAdapter();
        flipView.setAdapter(adapter);
    }

    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return pages.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if(convertView==null){
                convertView=new NumberTextVeiw(context);
            }
            Bitmap btmap = FileUtils.getImageFromAssetsFile(context, pages[position]);
            ((ImageView)(convertView.findViewById(R.id.tv_number))).setImageBitmap(btmap);
            return convertView;
        }



    }
    private class NumberTextVeiw extends LinearLayout {
        ViewHolder viewHolder;
        public NumberTextVeiw(Context context) {
            super(context);
            inflate(context, R.layout.item_pic_turnpage, this);
            viewHolder=new ViewHolder();
            viewHolder.tv_number=(ImageView)findViewById(R.id.tv_number);
        }
    }
    private class ViewHolder {
        public ImageView tv_number;

    }
}
