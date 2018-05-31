package com.atkj.ctc.adapter;

import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.view.View;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.bean.SellBean;
import com.atkj.ctc.bean.SellBean2;
import com.atkj.ctc.utils.AppUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/12/26 0026.
 */

public class HomeFragmentAdapter extends BaseQuickAdapter<SellBean2.ObjBean,BaseViewHolder>{

    private boolean isBuy;

    public void setBuy(boolean isBuy){
        this.isBuy = isBuy;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public HomeFragmentAdapter(int layoutResId, @Nullable List<SellBean2.ObjBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SellBean2.ObjBean item) {

        TextView price = helper.getView(R.id.tv_price);
        TextView user = helper.getView(R.id.tv_user);
        TextView num = helper.getView(R.id.tv_num);
        TextPaint paint = price.getPaint();
        TextPaint paint2 = user.getPaint();
        TextPaint paint3 = num.getPaint();
        paint.setFakeBoldText(true);
        paint2.setFakeBoldText(true);
        paint3.setFakeBoldText(true);

        int color = mContext.getResources().getColor(R.color.textColor2);
        int color2 = mContext.getResources().getColor(R.color.textColor_green);
        price.setTextColor(isBuy ? color : color2 );

        price.setText(String.valueOf(item.getPrice()));
        user.setText(isBuy ? item.getSellname() : item.getBuyname());
        num.setText(String.valueOf(item.getAccount()));
        helper.setText(R.id.tv_payment, AppUtils.getPaymentType(item.getPaytype()));

        int layoutPosition = helper.getLayoutPosition();
        View view = helper.getView(R.id.line);
        if (layoutPosition == getData().size()-1){
            view.setVisibility(View.GONE);
        }


    }
}
