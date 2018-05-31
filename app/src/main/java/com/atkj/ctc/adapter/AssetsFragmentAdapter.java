package com.atkj.ctc.adapter;

import android.support.annotation.Nullable;

import com.atkj.ctc.R;
import com.atkj.ctc.bean.AssetsListBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/12/19 0019.
 */

public class AssetsFragmentAdapter extends BaseQuickAdapter<AssetsListBean,BaseViewHolder>{

    public AssetsFragmentAdapter(int layoutResId, @Nullable List<AssetsListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AssetsListBean item) {

       /* helper.setText(R.id.tv_currency,"π")
                .setText(R.id.tv_available,Double.toString(item.getObj().getAccount()))
                .setText(R.id.tv_freeze,Double.toString(item.getObj().getUnaccount()));*/

    }
}
