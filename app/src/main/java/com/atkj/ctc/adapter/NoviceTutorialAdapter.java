package com.atkj.ctc.adapter;

import android.support.annotation.Nullable;

import com.atkj.ctc.R;
import com.atkj.ctc.bean.NoviceBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/12/27 0027.
 */

public class NoviceTutorialAdapter extends BaseQuickAdapter<String,BaseViewHolder>{
    public NoviceTutorialAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {


        helper.setText(R.id.content,item)
        .setText(R.id.title,String.valueOf(helper.getLayoutPosition()+1));


    }
}
