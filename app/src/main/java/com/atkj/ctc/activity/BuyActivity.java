package com.atkj.ctc.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.api.ErrorCallback;
import com.atkj.ctc.api.LoadingCallback;
import com.atkj.ctc.bean.SellBean2;
import com.atkj.ctc.bean.SellDetailBean;
import com.atkj.ctc.bean.UserBean;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Arith;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.views.InputBox;
import com.atkj.ctc.views.ItemView;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/12/16 0016.
 */

public class BuyActivity extends ToobarActivity {


    private static final String TAG = "BuyActivity";
    private boolean isBuy;

    @BindView(R.id.tv_price)
    TextView tvPrice;//价格
    /*@BindView(R.id.rl_keyong)
    RelativeLayout rlKeyong;*/
    @BindView(R.id.tv_available)
    TextView tvAvailable;
    @BindView(R.id.tv_freeze)
    TextView tvFreeze;
    @BindView(R.id.bt_buy)
    TextView btBuy;
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.iv_payment)
    ItemView ivPayment;//付款方式
    @BindView(R.id.iv_aera)
    ItemView ivAera;//所在地
    @BindView(R.id.iv_time)
    ItemView ivTime;//付款期限
    @BindView(R.id.ib_price)
    InputBox ibPrice;//价格 输入框
    @BindView(R.id.ib_cont)
    InputBox ibCont;//数量 输入框


    private String orderid;
    //private String userId;
    private SellDetailBean detail;
    private LoadService loadService;
    private int loadDataCount = 0;
    //private SellBean2.ObjBean bean;
    private String paymentType;
    private AlertDialog dialog;
    private String ctid;
    private String currency;
    private String account;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_buy);
        ButterKnife.bind(this);
        loadService = LoadSir.getDefault().register(this, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                loadService.showCallback(LoadingCallback.class);
                initData();
            }
        });


        init();
        initEvent();
        initData();

    }

    private void initEvent() {
        //数量监听
        ibCont.setTextChangedListener(new InputBox.TextChangedListener() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (account == null || TextUtils.isEmpty(editable.toString())) {
                    return;
                }

                double inputCount = Double.parseDouble(editable.toString());
                double tradeCount = detail.getObj().getAccount();
                String price = ibPrice.getText();
                double availableCount = Double.parseDouble(account);

                if (isBuy) {
                    if (inputCount > tradeCount) {//输入大于交易数量
                        //最多输入交易数量
                        ibCont.setText(String.valueOf(tradeCount));
                    }
                } else {

                    if (availableCount > tradeCount && inputCount > availableCount) {
                        ibCont.setText(String.valueOf(tradeCount));
                    }

                    if (availableCount > tradeCount && inputCount < availableCount && inputCount > tradeCount) {
                        ibCont.setText(String.valueOf(tradeCount));
                    }


                    if (availableCount < tradeCount && inputCount > tradeCount) {
                        ibCont.setText(String.valueOf(availableCount));
                    }

                    if (availableCount < tradeCount && inputCount < tradeCount && inputCount > availableCount) {
                        ibCont.setText(String.valueOf(availableCount));
                    }

                }


            }
        });


    }


    private void initData() {
        getAvailableFreezeCont();

    }

    private void getAvailableFreezeCont() {
        String userId = AppUtils.getUserId();

        Map<String, String> param = new LinkedHashMap<>();
        param.put("userId", userId);
        param.put("type", "1");
        param.put("ctid", ctid);


        NetUtils.get(Url.availableFreezeCont, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getFreeze onError: " + e.toString());
                showToast(getString(R.string.a537));
                loadService.showCallback(ErrorCallback.class);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d(TAG, "getFreeze onResponse: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {

                        JSONObject obj1 = obj.getJSONObject("obj");
                        account = obj1.getString("account");
                        tvAvailable.setText(account + " " + currency);
                        tvFreeze.setText(obj1.getString("unaccount") + " " + currency);

                        getOrderDetail();

                        LogUtils.d(TAG, "onResponse: loadDataCount=" + loadDataCount);
                    } else {
                        showToast(obj.getString("msg"));
                        loadService.showCallback(ErrorCallback.class);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void setData() {
        SellDetailBean.ObjBean obj = detail.getObj();
        tvPrice.setText(String.valueOf(obj.getPrice()));
        paymentType = AppUtils.getPaymentType(obj.getPaytype());
        ivPayment.setInfo(paymentType);

        ivAera.setInfo(getString(R.string.a36));
        ivTime.setInfo(obj.getDurationtime() + getString(R.string.a38));

        String price = String.valueOf(obj.getPrice());
        double count = obj.getAccount();
        ibPrice.setText(price);
        double availableCount = Double.parseDouble(account);

        if (isBuy){
            ibCont.setText(String.valueOf(count));
        }else {
            ibCont.setText(count > availableCount ? String.valueOf(availableCount) : String.valueOf(count));
        }

        //ibCont.getEditText().setSelection(String.valueOf(obj.getAccount()).length());

    }


    public static void actionStart(Context context, boolean isbuy, String orderid, String ctid, String currency) {
        Intent intent = new Intent(context, BuyActivity.class);
        intent.putExtra("isbuy", isbuy);
        intent.putExtra("orderid", orderid);
        intent.putExtra("ctid", ctid);
        intent.putExtra("currency", currency);
        context.startActivity(intent);
    }

    private void init() {
        isBuy = getIntent().getBooleanExtra("isbuy", true);
        orderid = getIntent().getStringExtra("orderid");
        ctid = getIntent().getStringExtra("ctid");
        currency = getIntent().getStringExtra("currency");

        ibPrice.setTextColor(getResources().getColor(R.color.f888888));
        ibCont.setTextColor(getResources().getColor(R.color.f888888));
        ibCont.setCurrency(currency);
        ibPrice.setFocusable(false);

        int colorSell = getResources().getColor(R.color.textColor_green);
        int colorBuy = getResources().getColor(R.color.textColor2);

        tvPrice.setTextColor(isBuy ? colorBuy : colorSell);
        //llMairu.setVisibility(isBuy ? View.VISIBLE : View.GONE);
        //rlKeyong.setVisibility(isBuy ? View.GONE : View.VISIBLE);
        tvBuy.setText(isBuy ? getString(R.string.a39) : getString(R.string.a280));
        tvBuy.setTextColor(isBuy ? colorBuy : colorSell);
        Drawable drawableSell = getResources().getDrawable(R.drawable.selecter_login_bg_sell);
        Drawable drawableBuy = getResources().getDrawable(R.drawable.selecter_login_bg);
        btBuy.setBackground(isBuy ? drawableBuy : drawableSell);
        btBuy.setText(isBuy ? getString(R.string.a48, currency) : getString(R.string.a281, currency));
        setToobarTitle(isBuy ? getString(R.string.a39) : getString(R.string.a280));


    }


    @OnClick({R.id.bt_buy})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.bt_buy:
                if (detail != null) {
                    String phone = detail.getObj().getPhone();
                    String myPhone = AppUtils.getUser().getPhone();
                    String price = ibPrice.getText();
                    String count = ibCont.getText();
                    String alipayaccount = AppUtils.getUser().getAlipayaccount();
                    List<UserBean.ObjBean.BankcardBean> bankcard = AppUtils.getUser().getBankcard();
                    String paytype = detail.getObj().getPaytype();


                    if (TextUtils.isEmpty(price)) {
                        showToast(getString(R.string.a282));
                        return;
                    } else if (TextUtils.isEmpty(count)) {
                        showToast(getString(R.string.a283));
                        return;
                    } else if (phone.equals(myPhone)) {
                        showToast(isBuy ? getString(R.string.a284) : getString(R.string.a285));
                        return;
                    }

                    if ( paytype.contains("2")){
                        if (alipayaccount == null || TextUtils.isEmpty(alipayaccount)) {
                            showTips(Constants.BIND_TYPE_ZFB);
                            return;
                        }
                    }

                    if (paytype.contains("3")){
                        if (bankcard == null || bankcard.size() == 0){
                            showTips(Constants.BIND_TYPE_YL);
                            return;
                        }
                    }




                    ConfirmOrderActivity.actionStart(this, isBuy, price, count, detail);

                }


                break;
        }


    }

    private void showTips(final int bindType) {
        final android.app.AlertDialog.Builder customizeDialog = new android.app.AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_cancel_entrust, null);
        final TextView cancel = dialogView.findViewById(R.id.cancel);
        final TextView content = dialogView.findViewById(R.id.content);
        content.setGravity(Gravity.CENTER);
        String str = bindType == Constants.BIND_TYPE_ZFB ? getString(R.string.a286) : getString(R.string.a557);
        content.setText(str);
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

                if (bindType == Constants.BIND_TYPE_ZFB){
                    Intent intent = new Intent(BuyActivity.this, AccountNumberBindDetailActivity.class);
                    intent.putExtra("type", Constants.BIND_TYPE_ZFB);
                    startActivity(intent);
                }else if (bindType == Constants.BIND_TYPE_YL){
                    Intent intent = new Intent(BuyActivity.this, AddBankCardActivity.class);
                    intent.putExtra("type", Constants.BIND_TYPE_YL);
                    startActivity(intent);
                }

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


    public void getOrderDetail() {
        Map<String, String> param = new LinkedHashMap<>();
        param.put("orderid", orderid);



        String url = isBuy ? Url.sellDetail : Url.buyDetail;
        NetUtils.get(url, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "initData onError: " + e.toString());
                showToast(getString(R.string.a537));
                loadService.showCallback(ErrorCallback.class);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "initData onResponse: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        loadService.showSuccess();

                        detail = AppUtils.getGson().fromJson(response, SellDetailBean.class);
                        setData();
                    } else {
                        loadService.showCallback(ErrorCallback.class);
                        showToast(obj.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
