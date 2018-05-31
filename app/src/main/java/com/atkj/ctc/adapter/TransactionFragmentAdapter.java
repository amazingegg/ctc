package com.atkj.ctc.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.bean.SellBean;
import com.atkj.ctc.utils.AppUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/12/16 0016.
 */

public class TransactionFragmentAdapter extends BaseQuickAdapter<SellBean.ObjBean.ListBean,BaseViewHolder>{

    private boolean isBuy;


    public TransactionFragmentAdapter(int layoutResId, @Nullable List<SellBean.ObjBean.ListBean> data) {
        super(layoutResId, data);
    }

    public void setBuy(boolean isBuy){
        this.isBuy = isBuy;
    }

    public boolean isBuy() {
        return isBuy;
    }

    @Override
    protected void convert(BaseViewHolder helper, SellBean.ObjBean.ListBean item) {

        TextView price = helper.getView(R.id.tv_price);
        int colorRed = mContext.getResources().getColor(R.color.textColor2);
        int colorGreen = mContext.getResources().getColor(R.color.textColor_green);
        price.setTextColor(isBuy ? colorRed : colorGreen );

        helper.setText(R.id.tv_user,item.getName())
                .setText(R.id.tv_num,String.valueOf(item.getAccount()))
                .setText(R.id.tv_price,String.valueOf(item.getPrice()))
                .setText(R.id.tv_payment, AppUtils.getPaymentType(item.getPaytype()));

        /*int layoutPosition = helper.getLayoutPosition();
        View view = helper.getView(R.id.line);
        int size = getData().size();
        if (layoutPosition == size-1){
            view.setVisibility(View.GONE);
        }*/

    }
}
