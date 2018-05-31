package com.atkj.ctc.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.bean.MarketListBean;
import com.atkj.ctc.utils.Arith;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Administrator on 2018/1/3 0003.
 */

public class HomeMarketAdapter extends BaseQuickAdapter<MarketListBean,BaseViewHolder> {
    public HomeMarketAdapter(int layoutResId, @Nullable List<MarketListBean> data) {
        super(layoutResId, data);
    }

    private double rate;

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    protected void convert(BaseViewHolder helper, MarketListBean item) {

        if (item.getData() != null && !TextUtils.isEmpty(item.getChannel())){
            String[] channel = item.getChannel().split("_");
            //(change/(last-change))*100
            double change = item.getData().getChange();
            double last = item.getData().getLast();
            double sub = Arith.sub(last, change);
            double div = Arith.div(change, sub);
            double present = Arith.mul(div, 100, 2);
            String format = new DecimalFormat("0.00").format(present);


            TextView tvPresent = helper.getView(R.id.tv_present);
            TextView newPrice = helper.getView(R.id.new_price);
            TextPaint paint = newPrice.getPaint();
            paint.setFakeBoldText(true);

            int red = mContext.getResources().getColor(R.color.textColor2);
            int green = mContext.getResources().getColor(R.color.textColor_green);
            tvPresent.setBackgroundColor(present > 0 ? green : red);
            tvPresent.setText(present > 0 ? "+"+format+"%" : format+"%");

            helper.setText(R.id.tv_currency,channel[3].toUpperCase() + "/")
                    .setText(R.id.tv_currency2,channel[4].toUpperCase())
                    .setText(R.id.new_price,String.valueOf(last))
                    .setText(R.id.tv_vol,mContext.getString(R.string.a447)+ String.valueOf(item.getData().getVol()))
                    .setText(R.id.new_price2,"¥ " + String.valueOf(rate == 0 ?
                    last : Arith.mul(last,rate,2)));
        }else {

            helper.setText(R.id.tv_currency,item.getChannel())
                    .setText(R.id.tv_currency2,"")
                    .setText(R.id.new_price,"---")
                    .setText(R.id.tv_vol,mContext.getString(R.string.a447)+ "---")
                    .setText(R.id.new_price2,"¥ " + "---");



        }


    }
}
