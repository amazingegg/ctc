package com.atkj.ctc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atkj.ctc.R;

import java.util.List;

/**
 * Created by Administrator on 2018/1/8 0008.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {


    private final LayoutInflater inflater;
    private  Context mContext;
    private  List<String> mDatas;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public MyRecyclerAdapter(Context context, List<String> datas){
        this. mContext=context;
        this. mDatas=datas;
        inflater= LayoutInflater. from(mContext);
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public MyViewHolder(View view) {
            super(view);
            //tv=(TextView) view.findViewById(R.id. tv_item);
        }

    }
}
