package com.zch.tooldemos.tablayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zch.tooldemos.R;

/**
 * Created by Zch on 2019/4/15 9:47.
 */

public class Fragment1 extends Fragment {
    private final static String TAG = "Fragment1";

    public Fragment1(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "fragment1 oncreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, null);
        TextView tv = view.findViewById(R.id.textView);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TabManyActivity.myReciver.sendEmptyMessage(0);
            }
        });
        Log.e(TAG, "fragment1 oncreateview");
        return view;

    }
}
