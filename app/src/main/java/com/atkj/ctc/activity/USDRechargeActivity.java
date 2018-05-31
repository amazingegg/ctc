package com.atkj.ctc.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.api.ErrorCallback;
import com.atkj.ctc.api.LoadingCallback;
import com.atkj.ctc.bean.PostJsonBean;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Arith;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.views.ItemView;
import com.atkj.ctc.views.MyToolBar;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/1/9 0009.
 */

public class USDRechargeActivity extends ToobarActivity {


    private static final java.lang.String TAG = "USDRechargeActivity";
    @BindView(R.id.ll_first)
    LinearLayout llFirst;
    @BindView(R.id.ll_recharge_amount)
    LinearLayout llSecond;
    @BindView(R.id.ll_last)
    LinearLayout llLast;
    @BindView(R.id.et_name)
    EditText etName;//姓名
    @BindView(R.id.bt_next)
    TextView btNext;
    @BindView(R.id.iv_account)
    ItemView ivAccount;//账户名
    @BindView(R.id.iv_bank_account)
    ItemView ivBankAccount;//银行账号
    @BindView(R.id.iv_bank)
    ItemView ivBank;//收款银行
    @BindView(R.id.iv_create_bank)
    ItemView ivCreateBank;//开户行
    @BindView(R.id.tv_tips1)
    TextView tvTips1;//充值金额
    @BindView(R.id.tv_tips2)
    TextView tvTips2;//转账持卡人
    @BindView(R.id.recharge_count)
    TextView recharge_count;
    @BindView(R.id.et_count)
    EditText etcount;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_tips)
    TextView tvTips;


    private MyToolBar toolBar;
    private LoadService loadService;
    private double rate;
    private AlertDialog dialog;
    private String symbol;
    private int orderId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_usd_recharge);
        ButterKnife.bind(this);

        init();

        initEvent();


    }

    private void initEvent() {
        ivAccount.setOnInfoClickedListener(new ItemView.OnInfoClickedListener() {
            @Override
            public void onInfoclicked(ItemView view) {
                AppUtils.copyClipboard(ivAccount.getInfo2Text());
                showToast(getString(R.string.a275));
            }
        });

        ivBankAccount.setOnInfoClickedListener(new ItemView.OnInfoClickedListener() {
            @Override
            public void onInfoclicked(ItemView view) {
                AppUtils.copyClipboard(ivBankAccount.getInfo2Text());
                showToast(getString(R.string.a275));
            }
        });

        etcount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!TextUtils.isEmpty(s.toString())) {
                    double count = Double.valueOf(s.toString());
                    if (count == 0) {
                        tvAmount.setText("0.00");
                    } else {

                        try {
                            tvAmount.setText(String.valueOf(Arith.div(count, rate, 2)));
                        }catch (Exception e){
                            LogUtils.d(TAG, "afterTextChanged: "+e.toString());
                        }


                    }
                } else {
                    tvAmount.setText("0.00");
                }
            }
        });


    }

    private void init() {
        setToobarTitle(getString(R.string.a262));
        toolBar = getToolBar();
        toolBar.setRightText(getString(R.string.a399));
        toolBar.setTextRightVisibility(true);
        toolBar.setOnRightClickListener(new MyToolBar.OnRightClickListener() {
            @Override
            public void onRightClick() {
                //TODO 充值记录
                showToast(getString(R.string.a248));
            }
        });

        /*ivAccount.setInfo2Text("王汉杰");//账户名
        ivBankAccount.setInfo2Text("6226 2206 3465 1953");
        ivBank.setInfo("民生银行");
        ivCreateBank.setInfo("深圳宝城支行");*/

        tvTips.setText(getString(R.string.a400));

        tvAmount.setText("0.00");
        ivAccount.setOnInfoClickedListener(new ItemView.OnInfoClickedListener() {
            @Override
            public void onInfoclicked(ItemView view) {
                AppUtils.copyClipboard(ivAccount.getInfo2Text());
                showToast(getString(R.string.a275));
            }
        });
        ivBankAccount.setOnInfoClickedListener(new ItemView.OnInfoClickedListener() {
            @Override
            public void onInfoclicked(ItemView view) {
                AppUtils.copyClipboard(ivBankAccount.getInfo2Text());
                showToast(getString(R.string.a275));
            }
        });


        //如果是第一次充值,设置真实姓名
        String realname = AppUtils.getUser().getRealname();
        if (TextUtils.isEmpty(realname) || realname == null) {
            llFirst.setVisibility(View.VISIBLE);
            llSecond.setVisibility(View.GONE);
            llLast.setVisibility(View.GONE);
        }else {
            llSecond.setVisibility(View.VISIBLE);
        }

        loadService = LoadSir.getDefault().register(this, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                loadService.showCallback(LoadingCallback.class);
                getExchangeRate();
            }
        });

        getExchangeRate();

    }

    @OnClick({R.id.bt_next, R.id.bt_next2, R.id.bt_cancel, R.id.bt_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_next:
                postRealName2Server();
                break;
            case R.id.bt_next2:

                next();

                break;
            case R.id.bt_cancel:
                finish();

                break;
            case R.id.bt_complete:

                showTips();

                break;


        }


    }

    private void showTips() {
        final android.app.AlertDialog.Builder customizeDialog = new android.app.AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_cancel_entrust, null);
        final TextView cancel = dialogView.findViewById(R.id.cancel);
        final TextView content = dialogView.findViewById(R.id.content);
        content.setText(getString(R.string.a401));
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
                comfirmOrder();
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

    private void comfirmOrder() {
        String userId = AppUtils.getUserId();
        String id = String.valueOf(orderId);

        Map<String,String> param = new LinkedHashMap<>();
        param.put("userid",userId);
        param.put("id",id);

        showLoadingDialog();


        NetUtils.put(Url.usdRechargeConfirm, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "onError: " + e.toString());
                showToast(getString(R.string.a537));
                dismissDialog();
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "comfirmOrder onResponse: " + response);
                dismissDialog();
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200){
                        showToast(getString(R.string.a296));
                        finish();
                    }else {
                        showToast(obj.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void next() {
        String phone = etPhone.getText().toString().trim();
        String strCount = etcount.getText().toString().trim();
        String amount = tvAmount.getText().toString();

        if (TextUtils.isEmpty(strCount)) {
            showToast(getString(R.string.a283));
            return;
        }

        double count = Double.valueOf(strCount);
        if (count < 100) {
            showToast(String.format(getString(R.string.a402),100));
            return;
        }else if (Double.valueOf(amount) == 0){
            showToast(getString(R.string.a529));
            return;
        }else if (TextUtils.isEmpty(phone)) {
            showToast(getString(R.string.a403));
            return;
        }else if (phone.length() > 11 || phone.length() < 11){
            showToast(getString(R.string.a404));
            return;
        }

        String subPhone = phone.substring(phone.length() - 2, phone.length());
        subPhone = "." + subPhone;

        String userId = AppUtils.getUserId();

        Map<String,String> param = new LinkedHashMap<>();
        param.put("userid",userId);
        param.put("phone",phone);
        param.put("amount",amount);
        param.put("ratemoney",strCount + subPhone);
        //param.put("rate",symbol);

        AppUtils.closeSoftKeybord(etcount,this);

        showLoadingDialog();

        NetUtils.postString(Url.usdRecharge, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "onError: " + e.toString());
                dismissDialog();
                showToast(getString(R.string.a537));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "onResponse: " + response);
                dismissDialog();
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        llSecond.setVisibility(View.GONE);
                        llLast.setVisibility(View.VISIBLE);

                        JSONObject obj1 = obj.getJSONObject("obj");

                        ivAccount.setInfo2Text(obj1.getString("accountname"));
                        ivBankAccount.setInfo2Text(obj1.getString("bankcount"));
                        ivBank.setInfo(obj1.getString("bank"));
                        ivCreateBank.setInfo(obj1.getString("bankname"));
                        orderId = obj1.getInt("id");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        tvTips1.setText(getString(R.string.a405) + etcount.getText() + subPhone);

        String realname = AppUtils.getUser().getRealname();
        String subName = realname.substring(realname.length() - 1, realname.length());
        tvTips2.setText(String.format(getString(R.string.a406),subName,subPhone));
        //tvTips2.setText("转账持卡人: **" + subName + ", 汇款时请务必包含小数点\" " + subPhone + "\"");

    }

    private void postRealName2Server() {
        final String realName = etName.getText().toString();
        if (TextUtils.isEmpty(realName)) {
            showToast(getString(R.string.a407));
            return;
        }

        String userId = AppUtils.getUserId();

        Map<String,String> param = new LinkedHashMap<>();
        param.put("id",userId);
        param.put("realname",realName);

        showLoadingDialog();
        NetUtils.put(Url.setRealName, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(e.toString());
                dismissDialog();
                showToast(getString(R.string.a537));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(response);
                dismissDialog();
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        llFirst.setVisibility(View.GONE);
                        llSecond.setVisibility(View.VISIBLE);
                        String realname = obj.getString("obj");

                        AppUtils.getUser().setRealname(realname);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private void getExchangeRate() {
        Map<String,String> param = new LinkedHashMap<>();
        param.put("content","USDCNH");
        param.put("type","1");

        NetUtils.get(Url.getRate, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getExchangeRate onError: " + e.toString());
                showToast(getString(R.string.a537));
                loadService.showCallback(ErrorCallback.class);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getExchangeRate onResponse: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        loadService.showSuccess();

                        JSONArray obj1 = obj.getJSONArray("obj");
                        JSONObject jsonObject = obj1.getJSONObject(0);
                        rate = jsonObject.getDouble("price");
                        //symbol = jsonObject.getString("symbol");

                        recharge_count.setText(String.format(getString(R.string.a408),String.valueOf(rate)));

                    } else {
                        showToast(obj.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}
