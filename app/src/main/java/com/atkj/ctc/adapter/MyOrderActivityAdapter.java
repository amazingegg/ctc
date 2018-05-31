package com.atkj.ctc.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.atkj.ctc.MainActivity;
import com.atkj.ctc.R;
import com.atkj.ctc.bean.OrderBean;
import com.atkj.ctc.scancode.utils.Constant;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Arith;
import com.atkj.ctc.utils.Constants;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/12/19 0019.
 */

public class MyOrderActivityAdapter extends BaseQuickAdapter<OrderBean.ObjBean.ListBean, BaseViewHolder> {


    private final boolean isMyOrder;

    public MyOrderActivityAdapter(int layoutResId, @Nullable List<OrderBean.ObjBean.ListBean> data, boolean isMyOrder) {
        super(layoutResId, data);
        this.isMyOrder = isMyOrder;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderBean.ObjBean.ListBean item) {


        int orderStatus = item.getOrderStatus();
        int type = item.getType();//1 卖单 2买单
        int redColor = mContext.getResources().getColor(R.color.textColor2);
        int grayColor = mContext.getResources().getColor(R.color.fbbbbbb);
        Drawable redBg = mContext.getResources().getDrawable(R.drawable.shap_input_box_red);
        Drawable grayBg = mContext.getResources().getDrawable(R.drawable.shap_input_box_gray);
        String selluserid = item.getSelluserid();
        String buyuserid = item.getBuyuserid();
        String id = AppUtils.getUserId();

        TextView waitingConfirm = helper.getView(R.id.tv_waiting_confirm);
        TextView statusBottom = helper.getView(R.id.tv_status_bottom);
        TextView tvOrderStatus = helper.getView(R.id.tv_status);

        //用户名
        if (type == Constants.ORDER_SELL && selluserid.equals(id)) {//卖单-卖家确认收款
            helper.setText(R.id.tv_name, item.getBuyname());
        } else if (type == Constants.ORDER_SELL && !selluserid.equals(id)) {//卖单-买家确认付款
            helper.setText(R.id.tv_name, item.getSellname());
        } else if (type == Constants.ORDER_BUY && buyuserid.equals(id)) {//买单-买家确认付款
            helper.setText(R.id.tv_name, item.getSellname());
        } else if (type == Constants.ORDER_BUY && !buyuserid.equals(id)) {//买单-卖家确认收款
            helper.setText(R.id.tv_name, item.getBuyname());
        }


        if (isMyOrder) {//我的订单
            if (orderStatus == Constants.ORDER_STATUS_PAID) {
                String str = "";
                String sellstatus = item.getSellstatus();
                String buystatus = item.getBuystatus();
                if (type == Constants.ORDER_SELL && selluserid.equals(id)) {//卖单-卖家确认收款

                    str = buystatus.equals(Constants.ORDER_TRANSATION) ?
                            mContext.getString(R.string.a457) : mContext.getString(R.string.a458);

                    if (sellstatus.equals(Constants.ORDER_COMPLETE)){
                        statusBottom.setVisibility(View.GONE);
                    }

                } else if (type == Constants.ORDER_SELL && !selluserid.equals(id)) {//卖单-买家确认付款

                    str = sellstatus.equals(Constants.ORDER_COMPLETE) ?
                            mContext.getString(R.string.a459) : mContext.getString(R.string.a460);

                } else if (type == Constants.ORDER_BUY && buyuserid.equals(id)) {//买单-买家确认付款

                    str = sellstatus.equals(Constants.ORDER_COMPLETE) ?
                            mContext.getString(R.string.a459) : mContext.getString(R.string.a460);

                } else if (type == Constants.ORDER_BUY && !buyuserid.equals(id)) {//买单-卖家确认收款

                    str = buystatus.equals(Constants.ORDER_TRANSATION) ?
                            mContext.getString(R.string.a457) : mContext.getString(R.string.a458);
                    statusBottom.setVisibility(sellstatus.equals(Constants.ORDER_COMPLETE) ? View.GONE : View.VISIBLE);

                }
                waitingConfirm.setText(str);
            }else {
                statusBottom.setVisibility(View.VISIBLE);
            }

            waitingConfirm.setVisibility(orderStatus == Constants.ORDER_STATUS_PAID ? View.VISIBLE : View.GONE);//等待卖家确认
            statusBottom.setBackground(orderStatus == Constants.ORDER_STATUS_COMLETED ? grayBg : redBg);
            statusBottom.setTextColor(orderStatus == Constants.ORDER_STATUS_COMLETED ? grayColor : redColor);
            tvOrderStatus.setTextColor(orderStatus == Constants.ORDER_STATUS_COMLETED ? grayColor : redColor);


        } else {//我的发布

            statusBottom.setBackground(orderStatus == Constants.ORDER_STATUS_PAID ? grayBg : redBg);
            statusBottom.setTextColor(orderStatus == Constants.ORDER_STATUS_PAID ? grayColor : redColor);
            tvOrderStatus.setTextColor(orderStatus == Constants.ORDER_STATUS_PAID ? grayColor : redColor);

            TextView tvCont = helper.getView(R.id.tv_cont);
            tvCont.setText(String.valueOf(item.getAccount()));//数量
            tvCont.setVisibility(View.VISIBLE);

            TextView tvCurrency = helper.getView(R.id.tv_currency);
            tvCurrency.setText(item.getSymbol());//币种
            tvCurrency.setVisibility(View.VISIBLE);

            //如果是交易中,隐藏确认按钮
            if (orderStatus == Constants.PUBLISH_TYPE_TRANSACTION){
                statusBottom.setVisibility(View.GONE);
            }

            //我的发布,所有订单状态提示都隐藏
            waitingConfirm.setVisibility(View.GONE);

        }

        String s = AppUtils.getOrderStatus(item.getOrderStatus(), isMyOrder);
        tvOrderStatus.setText(s);//订单状态

        helper.setText(R.id.tv_order_time, AppUtils.timedate(item.getCreatetime()));//下单时间
        helper.setText(R.id.tv_orderid, item.getOrderno());//订单号

        String totalPrice = item.getTotal() + " CNY";
        helper.setText(R.id.tv_amount_price, totalPrice);//总价

        String status = AppUtils.getOrderStatus(orderStatus, type, type == Constants.ORDER_SELL ? selluserid : buyuserid, isMyOrder,
                item.getSellstatus(),item.getBuystatus());
        statusBottom.setText(status);//确认按钮

        helper.addOnClickListener(R.id.tv_status_bottom);
    }
}
