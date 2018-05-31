package com.atkj.ctc.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atkj.ctc.R;
import com.atkj.ctc.api.ErrorCallback;
import com.atkj.ctc.api.LoadingCallback;
import com.atkj.ctc.bean.OrderBean;
import com.atkj.ctc.bean.OrderDetailBean;
import com.atkj.ctc.bean.SellDetailBean;
import com.atkj.ctc.scancode.utils.Constant;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Arith;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.views.ItemView;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/12/27 0027.
 */

public class OrderDetailActivity extends ToobarActivity {

    @BindView(R.id.ll_2btn)
    LinearLayout btn2;

    @BindView(R.id.title1)
    TextView title1;
    @BindView(R.id.title2)
    TextView title2;
    @BindView(R.id.iv_image)
    ImageView headImage;
    @BindView(R.id.tv_username)
    TextView userName;
    @BindView(R.id.tv_conn_maijia)
    TextView tvPhone;
    @BindView(R.id.tv_info)
    TextView amount;
    @BindView(R.id.iv_buy_num)
    ItemView orderNo;
    @BindView(R.id.iv_payment)
    ItemView payment;
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
    @BindView(R.id.iv_time)
    ItemView time;
    @BindView(R.id.iv_remark)
    ItemView remark;
    @BindView(R.id.bt_enter2)
    TextView btCancel;
    @BindView(R.id.bt_enter3)
    TextView btEnter;

    private OrderDetailBean.ObjBean detail;
    private java.lang.String TAG = "OrderDetailActivity";
    private String orderid;
    private int type;
    private int orderStatus;
    private LoadService loadService;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        setToobarTitle(getString(R.string.a342));

        orderid = getIntent().getStringExtra("orderid");
        type = getIntent().getIntExtra("type",-1);
        orderStatus = getIntent().getIntExtra("orderStatus",-1);


        loadService = LoadSir.getDefault().register(this, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                loadService.showCallback(LoadingCallback.class);
                initData();
            }
        });


        initEvent();
        initData();




    }

    public static void actionStart(Context context,String orderid,int type,int orderStatus,String selluserid,String buyuserid){
        Intent intent = new Intent(context,OrderDetailActivity.class);
        intent.putExtra("orderid",orderid);
        intent.putExtra("type",type);
        intent.putExtra("orderStatus",orderStatus);
        context.startActivity(intent);
    }

    private void initData(){

        getOrderDetail();
    }

    private void getOrderDetail() {
        Map<String,String> param = new LinkedHashMap<>();
        param.put("orderid",orderid);
        param.put("userid",AppUtils.getUserId());

        NetUtils.get(Url.myOrderDetails, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getOrderDetail onError: "+e.toString());
                showToast(getString(R.string.a537));
                loadService.showCallback(ErrorCallback.class);

            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getOrderDetail onResponse: "+response);

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200){
                        loadService.showSuccess();

                        OrderDetailBean bean = AppUtils.getGson().fromJson(response, OrderDetailBean.class);
                        detail = bean.getObj();
                        init();

                    }else {
                        loadService.showCallback(ErrorCallback.class);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });




    }

    private void initEvent() {
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




    private void init() {
        int durationtime = detail.getMinute();
        String paytype = detail.getPaytype();
        String userId = AppUtils.getUserId();
        String selluserid = detail.getSelluserid();
        String buyuserid = detail.getBuyuserid();

        String str1 = "";
        String str2 = "";

        if (type == 1){//卖单
            switch (orderStatus){
                case Constants.ORDER_STATUS_UNPAID://未付款
                    str1 = getString(R.string.a343);
                    str2 = "<font color='#FF5D5A'>"+durationtime+getString(R.string.a344)+"</font>"+getString(R.string.a345);
                    title2.setText(Html.fromHtml(str2));

                    /*btn2.setVisibility(View.VISIBLE);
                    if (userId.equals(detail.getSelluserid())){//卖单-卖家确认收款
                        btEnter.setText("确认收款");
                    }else {
                        btEnter.setText("确认付款");
                    }*/

                    break;
                case Constants.ORDER_STATUS_PAID://已付款
                    str1 = getString(R.string.a346);
                    str2 = getString(R.string.a346);
                    title2.setText(str2);
                    break;
                case Constants.ORDER_STATUS_COMLETED://已完成
                    str1 = getString(R.string.a347);
                    str2 = getString(R.string.a347);
                    title2.setText(str2);

                    break;
                case Constants.ORDER_STATUS_APPEAR://申诉中
                    str1 = getString(R.string.a348);
                    str2 = getString(R.string.a349);
                    title2.setText(str2);
                    break;

            }
            if (userId.equals(selluserid)){//卖单 卖家
                tvPhone.setText(getString(R.string.a352)+detail.getBuyphone());
                userName.setText(detail.getBuyname());


                ivAliAccount.setVisibility(View.GONE);
                ivAliName.setVisibility(View.GONE);
                ivBankName.setVisibility(View.GONE);
                ivBankId.setVisibility(View.GONE);
                ivBank.setVisibility(View.GONE);
                ivBankBranch.setVisibility(View.GONE);

            }else {//卖单 买家
                tvPhone.setText(getString(R.string.a350)+detail.getSellphone());
                userName.setText(detail.getSellname());
            }



        }else {
            switch (orderStatus){
                case Constants.ORDER_STATUS_UNPAID://未付款
                    str1 = getString(R.string.a343);
                    str2 = "<font color='#FF5D5A'>"+durationtime+getString(R.string.a344)+"</font>"+getString(R.string.a345);
                    title2.setText(Html.fromHtml(str2));
                    /*btn2.setVisibility(View.VISIBLE);

                    if (userId.equals(detail.getSelluserid())){//买单-买家确认付款
                        btEnter.setText("确认付款");
                    }else {
                        btEnter.setText("确认收款");
                    }*/

                    break;
                case Constants.ORDER_STATUS_PAID://已付款
                    str1 = getString(R.string.a351);
                    str2 = getString(R.string.a351);
                    title2.setText(str2);

                    break;
                case Constants.ORDER_STATUS_COMLETED://已完成
                    str1 = getString(R.string.a347);
                    str2 = getString(R.string.a347);
                    title2.setText(str2);

                    break;
                case Constants.ORDER_STATUS_APPEAR://申诉中
                    str1 = getString(R.string.a348);
                    str2 = getString(R.string.a349);
                    title2.setText(str2);
                    btn2.setVisibility(View.GONE);
                    break;

            }

            if (userId.equals(buyuserid)){//买单 买家
                tvPhone.setText(getString(R.string.a350)+detail.getSellphone());
                userName.setText(detail.getSellname());

            }else {//买单 卖家
                tvPhone.setText(getString(R.string.a352)+detail.getBuyphone());
                userName.setText(detail.getBuyname());

                ivAliAccount.setVisibility(View.GONE);
                ivAliName.setVisibility(View.GONE);
                ivBankName.setVisibility(View.GONE);
                ivBankId.setVisibility(View.GONE);
                ivBank.setVisibility(View.GONE);
                ivBankBranch.setVisibility(View.GONE);

            }
        }



        /*switch (paytype){
            case Constants.PAYMENT_WEICHAT:
                payment2.setTitle("微信账号");
                break;
            case Constants.PAYMENT_ALIPAY:
                payment2.setTitle("支付宝账号");
                break;
            case Constants.PAYMENT_YINLIAN:
                payment2.setTitle("银行账号");
                break;
        }*/
        String alipayaccount = detail.getAlipayaccount();
        String alipayname = detail.getAlipayname();
        if (alipayaccount != null && alipayname != null && !TextUtils.isEmpty(alipayaccount) && !TextUtils.isEmpty(alipayname)){
            ivAliAccount.setInfo2Text(alipayaccount);
            ivAliName.setInfo2Text(alipayname);
        }else {
            ivAliAccount.setVisibility(View.GONE);
            ivAliName.setVisibility(View.GONE);
        }


        List<OrderDetailBean.ObjBean.BankcardBean> bankcard = detail.getBankcard();
        if (bankcard != null && bankcard.size() > 0){
            OrderDetailBean.ObjBean.BankcardBean bankcardBean = bankcard.get(0);

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



        title1.setText(str1);

        amount.setText(detail.getTotal()+"CNY");
        orderNo.setInfo(detail.getOrderno());
        payment.setInfo(AppUtils.getPaymentType(paytype));
        time.setInfo(AppUtils.timedate(detail.getCreatetime()));
        remark.setInfo(detail.getRemake());


    }






    @OnClick({R.id.tv_conn_maijia,R.id.bt_enter2,R.id.bt_enter3})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.tv_conn_maijia:
                diallPhone(type == 1 ? detail.getBuyphone() : detail.getSellphone());
                break;
            case R.id.bt_enter2:


                break;
            case R.id.bt_enter3:


                break;
        }


    }

    public void diallPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }





}
