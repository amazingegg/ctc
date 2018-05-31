package com.atkj.ctc.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2018/1/4 0004.
 */

public  class CommonRecycleViewAdapter<K> extends BaseQuickAdapter<K,BaseViewHolder> {

    private String flag;


    public void setFlag(String i){
        flag = i;
    }

    public String getFlag(){
        return flag;
    }

    public CommonRecycleViewAdapter(int layoutResId, @Nullable List<K> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, K item) {

    }


}
