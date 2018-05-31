package com.atkj.ctc.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.atkj.ctc.MainActivity;
import com.atkj.ctc.R;
import com.atkj.ctc.bean.SellDetailBean;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Arith;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.views.ItemView;
import com.atkj.ctc.views.MyToolBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/12/18 0018.
 */

public class CompleteOrderActivity extends ToobarActivity {


    private static final String TAG = "CompleteOrderActivity";
    private boolean isBuy;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.iv_maijia)
    ItemView ivMaijia;
    @BindView(R.id.iv_alipay)
    ItemView ivAliAccount;
    @BindView(R.id.iv_name)
    ItemView ivAliName;
    @BindView(R.id.iv_bank_name)
    ItemView ivBankName;
    @BindView(R.id.iv_bank_id)
    ItemView ivBankId;
    @BindView(R.id.iv_bank)
    ItemView ivBank;
    @BindView(R.id.iv_bank_branch)
    ItemView ivBankBranch;




    @BindView(R.id.iv_pay_id)
    ItemView ivPayId;//付款参考号
    @BindView(R.id.bt_buy)
    TextView btBuy;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.tv_time)
    TextView tvTime;
    private AlertDialog dialog;
    private String orderid;
    private String phone;
    private Timer timer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_complete_order);
        ButterKnife.bind(this);


        init();
        initEvent();
    }

    private void initEvent() {
        getToolBar().setOnBackClickListener(new MyToolBar.OnBackClickListener() {
            @Override
            public void onBackClick() {
                goMain();
            }
        });

        ivMaijia.setOnInfoClickedListener(new ItemView.OnInfoClickedListener() {
            @Override
            public void onInfoclicked(ItemView view) {
                diallPhone(phone);
            }
        });

        ivAliAccount.setOnInfoClickedListener(new ItemView.OnInfoClickedListener() {
            @Override
            public void onInfoclicked(ItemView view) {
                AppUtils.copyClipboard(ivAliAccount.getInfo2Text());
                showToast(getString(R.string.a275));
            }
        });

        ivAliName.setOnInfoClickedListener(new ItemView.OnInfoClickedListener() {
            @Override
            public void onInfoclicked(ItemView view) {
                AppUtils.copyClipboard(ivAliName.getInfo2Text());
                showToast(getString(R.string.a275));
            }
        });

        ivBankName.setOnInfoClickedListener(new ItemView.OnInfoClickedListener() {
            @Override
            public void onInfoclicked(ItemView view) {
                AppUtils.copyClipboard(ivBankName.getInfo2Text());
                showToast(getString(R.string.a275));
            }
        });

        ivBankId.setOnInfoClickedListener(new ItemView.OnInfoClickedListener() {
            @Override
            public void onInfoclicked(ItemView view) {
                AppUtils.copyClipboard(ivBankId.getInfo2Text());
                showToast(getString(R.string.a275));
            }
        });

        ivBank.setOnInfoClickedListener(new ItemView.OnInfoClickedListener() {
            @Override
            public void onInfoclicked(ItemView view) {
                AppUtils.copyClipboard(ivBank.getInfo2Text());
                showToast(getString(R.string.a275));
            }
        });

        ivBankBranch.setOnInfoClickedListener(new ItemView.OnInfoClickedListener() {
            @Override
            public void onInfoclicked(ItemView view) {
                AppUtils.copyClipboard(ivBankBranch.getInfo2Text());
                showToast(getString(R.string.a275));
            }
        });






    }

    public void goMain(){
        Intent intent = new Intent(CompleteOrderActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void diallPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }



    public static void actionStart(Context context, boolean isBuy, String amount,
                                   SellDetailBean orderDetail){

        Intent intent = new Intent(context,CompleteOrderActivity.class);
        intent.putExtra("isBuy",isBuy);
        intent.putExtra("amount",amount);
        intent.putExtra("orderDetail",orderDetail);

        context.startActivity(intent);
    }


    private void init() {
        isBuy = getIntent().getBooleanExtra("isBuy", true);
        String amount = getIntent().getStringExtra("amount");
        SellDetailBean orderDetail = (SellDetailBean) getIntent().getSerializableExtra("orderDetail");

        orderid = orderDetail.getObj().getOrderid();
        phone = orderDetail.getObj().getPhone();
        String username = orderDetail.getObj().getName();
        int time = orderDetail.getObj().getDurationtime();


        timer = new Timer();
        timer.schedule(new MyTask(time),0,1000);

        setToobarTitle(getString(R.string.a287));
        int colorSell = getResources().getColor(R.color.textColor_green);
        int colorBuy = getResources().getColor(R.color.textColor2);


        tvTime.setText(getString(R.string.a288) + time + getString(R.string.a38));
        tvPrice.setText(amount);
        tvPrice.setTextColor(isBuy ? colorBuy : colorSell);
        ivMaijia.setInfoColor(isBuy ? colorBuy : colorSell);
        ivMaijia.setInfo(isBuy ? getString(R.string.a289) : getString(R.string.a290));
        ivMaijia.setTitle(isBuy ? getString(R.string.a291) : getString(R.string.a292));
        ivMaijia.setInfo2Text(username);

        tvRemark.setText(isBuy ? getString(R.string.a293) : getString(R.string.a294));
        Drawable drawableSell = getResources().getDrawable(R.drawable.selecter_login_bg_sell);
        Drawable drawableBuy = getResources().getDrawable(R.drawable.selecter_login_bg);
        btBuy.setBackground(isBuy ? drawableBuy : drawableSell);
        btBuy.setText(isBuy ? getString(R.string.a133) : getString(R.string.a56));
        //ivAliAccount.setInfo2Visible(isBuy);
        //ivPayment.setInfoColor(isBuy ? colorBuy : getResources().getColor(R.color.text_color));
        //ivPayment.setTitle(isBuy ? getString(R.string.a250) : getString(R.string.a34));


        String alipayaccount = orderDetail.getObj().getAlipayaccount();
        String alipayname = orderDetail.getObj().getAlipayname();
        if (alipayaccount != null && alipayname != null && !TextUtils.isEmpty(alipayaccount) && !TextUtils.isEmpty(alipayname)){
            ivAliAccount.setInfo2Text(alipayaccount);
            ivAliName.setInfo2Text(alipayname);
        }else {
            ivAliAccount.setVisibility(View.GONE);
            ivAliName.setVisibility(View.GONE);
        }


        List<SellDetailBean.ObjBean.BankcardBean> bankcard = orderDetail.getObj().getBankcard();
        if (bankcard != null && bankcard.size() > 0){
            SellDetailBean.ObjBean.BankcardBean bankcardBean = bankcard.get(0);

            ivBankName.setInfo2Text(bankcardBean.getBankrealname());
            ivBankId.setInfo2Text(bankcardBean.getBankcardid());
            ivBank.setInfo2Text(bankcardBean.getBankname());
            ivBankBranch.setInfo2Text(bankcardBean.getBankbranch());
        }else {
            ivBankName.setVisibility(View.GONE);
            ivBankId.setVisibility(View.GONE);
            ivBank.setVisibility(View.GONE);
            ivBankBranch.setVisibility(View.GONE);
        }


        //ivPayment.setInfo(isBuy ? getString(R.string.a49) : getString(R.string.a33));
    }





    @OnClick({R.id.bt_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_buy:
                if (isBuy){
                showTips();

                }else {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }

                break;


        }


    }

    private class MyTask extends TimerTask {

        int num;

        public MyTask (int time){
            num = time * 60;
        }

        @Override
        public void run() {
            num--;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    String str = num / 60 % 60 + "分" +  num % 60 + "秒";
                    tvTime.setText(getString(R.string.a288) + str);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null){
            timer.cancel();
        }

    }

    private void showTips() {
        final android.app.AlertDialog.Builder customizeDialog = new android.app.AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_cancel_entrust, null);
        final TextView cancel = dialogView.findViewById(R.id.cancel);
        final TextView content = dialogView.findViewById(R.id.content);
        content.setGravity(Gravity.CENTER);
        content.setText(getString(R.string.a295));
        TextView enter = dialogView.findViewById(R.id.enter);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complete();
                dialog.dismiss();
            }
        });

        TextView title = new TextView(this);
        title.setText("");
        customizeDialog.setCustomTitle(title);
        customizeDialog.setView(dialogView);

        customizeDialog.setPositiveButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog = customizeDialog.show();

    }

    private void complete() {
        String userId = AppUtils.getUserId();
        Map<String,String> param = new LinkedHashMap<>();


        param.put("orderid", orderid);
        param.put("userid",userId);

        showLoadingDialog();
        NetUtils.postString(Url.buyOrderConfirm, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(e.toString());
                showToast(getString(R.string.a537));
                dismissDialog();
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG,response);
                dismissDialog();
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200){
                        showToast(getString(R.string.a296));
                        goMain();
                    }else {
                        showToast(obj.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


}
