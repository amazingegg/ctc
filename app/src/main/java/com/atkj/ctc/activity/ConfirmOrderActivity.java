package com.atkj.ctc.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.bean.OrderConfirmBean;
import com.atkj.ctc.bean.PostJsonBean;
import com.atkj.ctc.bean.SellDetailBean;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Arith;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.views.ItemView;
import com.atkj.ctc.views.PswInputView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by Administrator on 2017/12/18 0018.
 */

 public class ConfirmOrderActivity extends ToobarActivity{


    private static final String TAG = "ConfirmOrderActivity";
    private boolean isBuy;

    @BindView(R.id.tv_conn_maijia)
    TextView tvMaijia;
    @BindView(R.id.iv_buy_num)
    ItemView ivBuyNum;
    @BindView(R.id.iv_price)
    ItemView ivPrice;
    @BindView(R.id.iv_payment)
    ItemView ivPayment;
    @BindView(R.id.iv_aera)
    ItemView ivAera;
    @BindView(R.id.iv_time)
    ItemView ivTime;
    @BindView(R.id.et_input)
    EditText etRemark;
    @BindView(R.id.bt_enter)
    TextView btEnter;
    @BindView(R.id.tv_username)
    TextView tvUserName;
    private String price;
    private String quantity;
    private SellDetailBean orderDetail;
    private AlertDialog payPassWordDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_confirm_order);
        ButterKnife.bind(this);




        init();

    }


    public static void actionStart(Context context, boolean isbuy,String price, String quantity, SellDetailBean orderDetail){
        Intent intent = new Intent(context,ConfirmOrderActivity.class);
        intent.putExtra("isbuy",isbuy);
        intent.putExtra("price",price);
        intent.putExtra("quantity",quantity);
        intent.putExtra("orderDetail",orderDetail);

        context.startActivity(intent);

    }

    private void init() {
        isBuy = getIntent().getBooleanExtra("isbuy", true);
        price = getIntent().getStringExtra("price");
        quantity = getIntent().getStringExtra("quantity");
        orderDetail = (SellDetailBean) getIntent().getSerializableExtra("orderDetail");

        String username = orderDetail.getObj().getName();
        int durationtime = orderDetail.getObj().getDurationtime();
        String paypent = AppUtils.getPaymentType(orderDetail.getObj().getPaytype());

        tvUserName.setText(username);
        ivTime.setInfo(durationtime +getString(R.string.a38));

        ivPayment.setInfo(paypent);

        setToobarTitle(getString(R.string.a297));
        int colorSell = getResources().getColor(R.color.textColor_green);
        int colorBuy = getResources().getColor(R.color.textColor2);
        tvMaijia.setTextColor(isBuy ? colorBuy : colorSell);
        tvMaijia.setText(isBuy ? getString(R.string.a289) : getString(R.string.a290));
        ivBuyNum.setTitle(isBuy ? getString(R.string.a158) : getString(R.string.a298));

        ivBuyNum.setInfoColor(isBuy ? colorBuy : colorSell);
        ivBuyNum.setInfo(quantity);
        ivPrice.setInfoColor(isBuy ? colorBuy : colorSell);
        ivPrice.setInfo(price + " CNY");

        Drawable drawableSell = getResources().getDrawable(R.drawable.selecter_login_bg_sell);
        Drawable drawableBuy = getResources().getDrawable(R.drawable.selecter_login_bg);
        btEnter.setBackground(isBuy ? drawableBuy : drawableSell);
    }


    @OnClick({R.id.bt_enter,R.id.tv_conn_maijia})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.bt_enter:
                if (isBuy){
                    postOrder();
                }else {
                    //检查是否绑定支付密码
                    String paypassword = AppUtils.getUser().getPaypassword();
                    if ("".equals(paypassword) || paypassword == null) {
                        Intent payPwdIntent = new Intent(this, PayPassWordActivity.class);
                        startActivity(payPwdIntent);
                        return;
                    }

                    showPayPassWordDialog();
                }
                break;
            case R.id.tv_conn_maijia:
                AppUtils.diallPhone(this,orderDetail.getObj().getPhone());
                break;
        }
    }


    private void showPayPassWordDialog() {
        AlertDialog.Builder customizeDialog = new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_input_pwd, null);
        final PswInputView inputView = dialogView.findViewById(R.id.piv);
        inputView.setInputCallBack(new PswInputView.InputCallBack() {
            @Override
            public void onInputFinish(String result) {
                checkFundsPwd(result);
                inputView.hideSoftInput();
            }

            @Override
            public void onInputChange(String result) {
            }
        });
        TextView title = new TextView(this);
        title.setGravity(Gravity.CENTER_HORIZONTAL);
        title.setPadding(30, 30, 30, 30);
        title.setText(getString(R.string.a328));
        title.setTextColor(getResources().getColor(R.color.f000000));
        title.setTextSize(24);
        customizeDialog.setCustomTitle(title);
        customizeDialog.setView(dialogView);

        customizeDialog.setPositiveButton("",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        payPassWordDialog = customizeDialog.show();

    }

    private void checkFundsPwd(String pwd) {

        if (pwd == null || pwd.length() < 6) {
            showToast(getString(R.string.a329));
            return;
        }
        String userid = AppUtils.getUserId();
        String timestamp = AppUtils.getTimeStamp();

        Map<String, String> param = new LinkedHashMap<>();
        param.put("timestamp", timestamp);
        param.put("paypassword", pwd);
        param.put("id", userid);

        String sign = AppUtils.getSign(param);

        PostJsonBean bean = new PostJsonBean();
        bean.setPaypassword(pwd);
        bean.setId(userid);
        bean.setApp_key(Constants.app_key);
        bean.setSign(sign);
        bean.setTimestamp(timestamp);

        String url = Url.verificationPwd;
        String json = AppUtils.getGson().toJson(bean);

        OkHttpUtils.postString()
                .url(url)
                .content(json)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d(e.toString());
                        dismissPwdDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d(response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getInt("status") == 200) {

                                postOrder();

                                dismissPwdDialog();
                            } else {
                                showToast(obj.getString("msg"));
                                dismissPwdDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });

    }

    private void dismissPwdDialog() {
        if (payPassWordDialog != null) {
            if (payPassWordDialog.isShowing()) {

                payPassWordDialog.dismiss();
            }
        }
    }


    private void postOrder() {
        String userid = AppUtils.getUserId();
        Map<String,String> param = new LinkedHashMap<>();

        param.put("userid",userid);
        param.put("price",price);
        param.put("account",quantity);
        param.put("orderid", orderDetail.getObj().getOrderid());
        String url = isBuy ? Url.sellConfirm : Url.buyConfirm;

        showLoadingDialog();
        NetUtils.postString(url, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(e.toString());
                showToast(getString(R.string.a537));
                dismissDialog();
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "postOrder onResponse: "+response);
                dismissDialog();

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200){
                        OrderConfirmBean bean = AppUtils.getGson().fromJson(response, OrderConfirmBean.class);

                        String amount = Arith.mul(price,quantity,2);
                       // String username = orderDetail.getObj().getName();
                        //String durationtime = String.valueOf(orderDetail.getObj().getDurationtime());
                        //String phone = orderDetail.getObj().getPhone();
                       // String orderid = orderDetail.getObj().getOrderid();
                        //String alipayaccount = bean.getObj().getAlipayaccount();
                        //String realname = bean.getObj().getRealname();



                        CompleteOrderActivity.actionStart(ConfirmOrderActivity.this
                        ,isBuy,amount,orderDetail);

                    }else {
                        showToast(obj.getString("msg"));
                        dismissDialog();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


}
