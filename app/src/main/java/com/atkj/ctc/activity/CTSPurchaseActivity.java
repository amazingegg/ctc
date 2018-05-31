package com.atkj.ctc.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.adapter.CommonRecycleViewAdapter;
import com.atkj.ctc.bean.CtsBean;
import com.atkj.ctc.bean.CurrencyBean;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Arith;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.views.MyToolBar;
import com.atkj.ctc.views.PswInputView;
import com.atkj.ctc.views.TipsDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
 * Created by Administrator on 2018/3/28 0028.
 */

public class CTSPurchaseActivity extends ToobarActivity {


    private static final java.lang.String TAG = "CTSPurchaseActivity";
    @BindView(R.id.recycleview)
    RecyclerView recyclerView;
    @BindView(R.id.part1)
    RelativeLayout part1;
    @BindView(R.id.part2)
    LinearLayout part2;
    @BindView(R.id.part3)
    LinearLayout part3;
    @BindView(R.id.part4)
    LinearLayout part4;
    @BindView(R.id.tv_quantity)
    TextView tvQuantity;
    @BindView(R.id.cb_eth)
    CheckBox cbEth;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.characteristic)
    TextView tvCharacteristic;
    @BindView(R.id.introduction)
    TextView tvIntroduction;
    @BindView(R.id.et_invite)
    EditText etInvite;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.rl_eth)
    RelativeLayout rlEth;
    @BindView(R.id.tv_Remaining)
    TextView tvRemaining;


    private String[] cts = {"1000CTS", "2000CTS", "3000CTS", "5000CTS", "8000CTS", "10000CTS"};
    private String quantity = "1000";
    //private String usd;
    private String eth;
    private String inviteno;
    private AlertDialog payPassWordDialog;
    private AlertDialog rechargeDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_cts_purchase);

        ButterKnife.bind(this);
        init();

        getCurrentCts();

    }

    private void init() {

        setToobarTitle(getString(R.string.a471));
        cbEth.setChecked(true);
        tvCharacteristic.getPaint().setFakeBoldText(true);
        tvIntroduction.getPaint().setFakeBoldText(true);


        getToolBar().setTextRightVisibility(true);
        getToolBar().setRightText(getString(R.string.a533));
        getToolBar().setOnRightClickListener(new MyToolBar.OnRightClickListener() {
            @Override
            public void onRightClick() {

                CTSDetail.actionStartForResult(CTSPurchaseActivity.this);
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        int[] attr = new int[]{android.R.attr.listDivider};
        //recyclerView.addItemDecoration(new GridDivider(this,GridDivider.DIRECTION_VERTICAL,3,attr));
        listData = new ArrayList<>();

        for (int i = 0; i < cts.length; i++) {
            CurrencyBean.ObjBean bean = new CurrencyBean.ObjBean();
            bean.setSymbol(cts[i]);
            if (i == 0) {
                bean.setSelect(true);
            }
            listData.add(bean);
        }
        adapter.replaceData(listData);
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String symbol = listData.get(position).getSymbol();

                quantity = symbol.substring(0, symbol.length() - 3);

                for (int i = 0; i < listData.size(); i++) {
                    CurrencyBean.ObjBean bean = listData.get(i);

                    if (i == position) {
                        bean.setSelect(true);
                    } else {
                        bean.setSelect(false);
                    }

                }
                adapter.notifyDataSetChanged();
            }
        });


    }

    private List<CurrencyBean.ObjBean> listData;
    CommonRecycleViewAdapter adapter = new CommonRecycleViewAdapter<CurrencyBean.ObjBean>(R.layout.list_item_cts_purchase, listData) {
        @Override
        protected void convert(BaseViewHolder helper, CurrencyBean.ObjBean item) {
            TextView content = helper.getView(R.id.content);
            Drawable drawable1 = mContext.getResources().getDrawable(R.drawable.shap_cts_text_bg);
            Drawable drawable2 = mContext.getResources().getDrawable(R.drawable.shap_cts_text_bg_select);

            if (item.isSelect()) {
                content.setBackground(drawable2);
                content.setTextColor(mContext.getResources().getColor(R.color.textColor2));
            } else {
                content.setBackground(drawable1);
                content.setTextColor(mContext.getResources().getColor(R.color.f999999));
            }

            content.setText(item.getSymbol());

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 4) {

            if (resultCode == RESULT_OK) {
                part1.setVisibility(View.GONE);
                part2.setVisibility(View.VISIBLE);
                getToolBar().setTextRightVisibility(false);
            }

        }

    }


    private void getCurrentCts() {
        Map<String, String> param = new LinkedHashMap<>();
        param.put("userid", AppUtils.getUserId());

        showLoadingDialog();
        NetUtils.get(Url.getCurrentCts, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getCurrentCts onError: " + e.toString());
                showToast(getString(R.string.a537));
                dismissDialog();

            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getCurrentCts onResponse: " + response);
                dismissDialog();
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {

                        JSONObject obj1 = obj.getJSONObject("obj");
                        String surplus = obj1.getString("surplus");


                        tvRemaining.setText(surplus);

                    } else {
                        showToast(obj.getString("msg"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    @OnClick({R.id.bt_next, R.id.bt_buy, R.id.pay, R.id.cb_eth, R.id.rl_eth})
    public void onViewClicked(View view) {


        switch (view.getId()) {
            case R.id.bt_buy://认购CTS

                part1.setVisibility(View.GONE);
                part2.setVisibility(View.VISIBLE);
                getToolBar().setTextRightVisibility(false);
                break;
            case R.id.bt_next:

                checkPhone();
                getToolBar().setTextRightVisibility(false);

                break;
            case R.id.pay:

                String paypassword = AppUtils.getUser().getPaypassword();
                 if ("".equals(paypassword) || paypassword == null) {
                    Intent intent = new Intent(this, PayPassWordActivity.class);
                    startActivity(intent);
                    return;
                }
                showPayPassWordDialog();

                break;
            /*case R.id.cb_usd:
                cbUsd.setChecked(true);
                cbEth.setChecked(false);
                tvTotal.setText(Arith.mul(quantity,usd,2)+"USD");
                break;*/
            case R.id.cb_eth:
                cbEth.setChecked(true);
                tvTotal.setText(Arith.mul(quantity, eth, 2) + "ETH");
                break;
            /*case R.id.rl_usd:
                cbUsd.setChecked(true);
                cbEth.setChecked(false);
                tvTotal.setText(Arith.mul(quantity,usd,2)+"USD");
                break;*/
            case R.id.rl_eth:
                cbEth.setChecked(true);
                tvTotal.setText(Arith.mul(quantity, eth, 2) + "ETH");
                break;

        }


    }

    private void showNormalDialog() {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setTitle("提示:");
        normalDialog.setMessage("CTS股智能合约活动北京时间5月5日14点正式开放尽请期待");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        normalDialog.show();
    }

    //检查支付密码
    private void checkFundsPwd(String pwd) {
        if (pwd == null || pwd.length() < 6) {
            showToast(getString(R.string.a329));
            return;
        }
        String userid = AppUtils.getUserId();
        Map<String, String> param = new LinkedHashMap<>();
        param.put("paypassword", pwd);
        param.put("id", userid);

        NetUtils.postString(Url.verificationPwd, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(e.toString());
                showToast(getString(R.string.a537));
                if (payPassWordDialog != null) {
                    if (payPassWordDialog.isShowing()) {
                        payPassWordDialog.dismiss();
                    }
                }
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {

                        buyCTS();
                        if (payPassWordDialog != null) {
                            if (payPassWordDialog.isShowing()) {
                                payPassWordDialog.dismiss();
                            }
                        }

                    } else {
                        showToast(obj.getString("msg"));
                        if (payPassWordDialog != null) {
                            if (payPassWordDialog.isShowing()) {
                                payPassWordDialog.dismiss();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    //支付密码Dialog
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

        payPassWordDialog = customizeDialog.show();


    }

    private void buyCTS() {

        double d = Double.parseDouble(eth);
        double d2 = Double.parseDouble(quantity);
        Map<String, String> param = new LinkedHashMap<>();
        param.put("userid", AppUtils.getUserId());
        param.put("inviteno", inviteno);
        param.put("amount", quantity);
        param.put("price", eth);
        param.put("paymoney", String.valueOf(Arith.mul(d2, d)));

        NetUtils.postString(Url.buyCts, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "buyCTS onError: " + e.toString());
                showToast(getString(R.string.a537));

            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "buyCTS onResponse: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        part3.setVisibility(View.GONE);
                        part4.setVisibility(View.VISIBLE);
                        new Timer().schedule(task, 0, 1000);
                    } else {
                        if (obj.getInt("status") == 20514) {
                            showRechargeTips("ETH");
                        } else {
                            showToast(obj.getString("msg"));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


    }

    //充值Dialog
    private void showRechargeTips(final String currency) {
        final AlertDialog.Builder customizeDialog = new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_cancel_entrust, null);
        final TextView cancel = dialogView.findViewById(R.id.cancel);
        final TextView enter = dialogView.findViewById(R.id.enter);
        final TextView content = dialogView.findViewById(R.id.content);

        content.setText(getString(R.string.a422, currency));
        enter.setText(getString(R.string.a331));
        enter.setTextColor(getResources().getColor(R.color.textColor2));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rechargeDialog.dismiss();
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                switch (currency) {
                    case "ETH":
                        intent = new Intent(CTSPurchaseActivity.this, BTCRechargeActivity.class);
                        intent.putExtra("currency", currency);
                        break;
                    case "USD":
                        intent = new Intent(CTSPurchaseActivity.this, USDRechargeActivity.class);
                        break;

                }
                startActivity(intent);
                rechargeDialog.dismiss();
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
        rechargeDialog = customizeDialog.show();
    }


    private void checkPhone() {
        String phone = etInvite.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            showToast(getString(R.string.a300));
            return;
        } else if (phone.length() > 11 || phone.length() < 11) {
            showToast(getString(R.string.a311));
            return;
        }

        Map<String, String> param = new LinkedHashMap<>();
        param.put("userid", AppUtils.getUserId());
        param.put("inviteno", phone);
        param.put("areacode", "86");

        NetUtils.postString(Url.checkPhone, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                showToast(getString(R.string.a537));
                LogUtils.d(TAG, "checkPhone onError: " + e.toString());

            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "checkPhone onResponse: " + response);
                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getInt("status") == 200) {
                        part2.setVisibility(View.GONE);
                        part3.setVisibility(View.VISIBLE);
                        tvQuantity.setText(quantity + "CTS");

                        CtsBean ctsBean = AppUtils.getGson().fromJson(response, CtsBean.class);

                        //usd = ctsBean.getObj().getUsd();
                        eth = ctsBean.getObj().getEth();
                        inviteno = ctsBean.getObj().getInviteno();

                        tvTotal.setText(Arith.mul(quantity, eth, 6) + "ETH");

                    } else {
                        showToast(obj.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


    }


    private TimerTask task = new TimerTask() {

        int num = 4;

        @Override
        public void run() {
            num--;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (num == 0) {
                        CTSPurchaseActivity.this.finish();
                        cancel();
                    }
                    tvTime.setText(String.format(getString(R.string.a384), num));
                }
            });


        }
    };


    public static void actionStart(Context context) {
        Intent intent = new Intent(context, CTSPurchaseActivity.class);
        context.startActivity(intent);

    }
}
