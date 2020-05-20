package com.zch.tooldemos.maplist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.zch.tooldemos.R;

/**
 * Created by Zch on 2019/3/22 11:12.
 */

public class GaodeAdapter extends RecyclerView.Adapter<GaodeAdapter.MyViewHolder> {
    private LayoutInflater mInflater;
    private List<String> datalist = new ArrayList<>();

    public GaodeAdapter(Context context,List<String> datalist){
        this.datalist = datalist;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mInflater.inflate(R.layout.adapter_schedule_view, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv.setText(datalist.get(position));
        holder.tv_time.setText("10:00");
        holder.tv_day.setText("03月25日");
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        TextView tv_time;
        TextView tv_day;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_schedule_adapter_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_schedule_adapter_time);
            tv_day = (TextView) itemView.findViewById(R.id.tv_schedule_adapter_mmdd);
        }
    }
}
