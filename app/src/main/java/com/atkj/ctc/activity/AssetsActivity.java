package com.atkj.ctc.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.adapter.CommonRecycleViewAdapter;
import com.atkj.ctc.api.ErrorCallback;
import com.atkj.ctc.api.LoadingCallback;
import com.atkj.ctc.bean.AssetsDetailBean;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Arith;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.views.MyToolBar;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
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
 * Created by Administrator on 2017/12/19 0019.
 */

public class AssetsActivity extends ToobarActivity {


    private static final java.lang.String TAG = "AssetsActivity";
    @BindView(R.id.tv_assets)
    TextView tvAssets;
    @BindView(R.id.available)
    TextView tvAvailable;
    @BindView(R.id.freeze)
    TextView tvFreeze;
    @BindView(R.id.bt_recharge)
    TextView btRecharge;
    @BindView(R.id.bt_extract)
    TextView btExtract;
    @BindView(R.id.tv_available_cny)
    TextView tvAvailableCNY;
    @BindView(R.id.tv_freeze_cny)
    TextView tvFreezeCNY;
    @BindView(R.id.recycleview)
    RecyclerView recyclerView;
    @BindView(R.id.tv_currency)
    TextView tvCurrency;


    private String assetsType;
    private double rate;
    private String account;
    private String unAccount;
    private LoadService loadService;
    private int status;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_assets);
        ButterKnife.bind(this);

        loadService = LoadSir.getDefault().register(this, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                loadService.showCallback(LoadingCallback.class);
                init();
            }
        });

        init();
        initEvent();
        initData();
    }

    private void initData() {
        getExchangeRate(assetsType);
        getAssetsDetail(assetsType);
    }


    private void initEvent() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.layout_empty);


    }

    private List<AssetsDetailBean.ObjBean.ListBean> mDataList;
    private CommonRecycleViewAdapter adapter = new CommonRecycleViewAdapter<AssetsDetailBean.ObjBean.ListBean>
            (R.layout.list_item_assets_detail, mDataList) {
        @Override
        protected void convert(BaseViewHolder helper, AssetsDetailBean.ObjBean.ListBean item) {

            String type;
            switch (item.getType()) {
                case 1:
                    type = getString(R.string.a265);
                    break;
                case 2:
                    type = getString(R.string.a264);
                    break;
                case 3:
                    type = getString(R.string.a502);
                    break;
                case 4:
                    type = getString(R.string.a541);
                    break;
                case 5:
                    type = getString(R.string.a542);
                    break;
                case 6:
                    type = getString(R.string.a543);
                    break;
                default:
                    type = "";
            }

            TextView price = helper.getView(R.id.tv_amount);
            if (item.getStatus() == 2)
                price.setTextColor(mContext.getResources().getColor(R.color.textColor2));


            String str = item.getStatus() == 1 ? "+" + item.getAccount() : item.getAccount() < 0 ? String.valueOf(item.getAccount()) : "-" + item.getAccount();


            helper.setText(R.id.tv_date, AppUtils.timedate(item.getCreatetime(), "yyyy-MM-dd"))
                    .setText(R.id.tv_type, type)
                    .setText(R.id.tv_amount, str);
        }
    };


    public static void actionStart(Context context, String account, String unAccount, String assetsType, int status) {
        Intent intent = new Intent(context, AssetsActivity.class);
        intent.putExtra("account", account);
        intent.putExtra("unAccount", unAccount);
        intent.putExtra("assetsType", assetsType);
        intent.putExtra("status", status);
        context.startActivity(intent);

    }

    public static void actionStart(Activity context, String account, String unAccount, String assetsType, int status,int requestCode) {
        Intent intent = new Intent(context, AssetsActivity.class);
        intent.putExtra("account", account);
        intent.putExtra("unAccount", unAccount);
        intent.putExtra("assetsType", assetsType);
        intent.putExtra("status", status);
        context.startActivityForResult(intent,requestCode);

    }



    private void init() {

        account = getIntent().getStringExtra("account");
        unAccount = getIntent().getStringExtra("unAccount");
        assetsType = getIntent().getStringExtra("assetsType");
        status = getIntent().getIntExtra("status", -1);
        String title = assetsType + " " + getString(R.string.a90);
        tvAssets.setText(title);
        setToobarTitle(title);

        MyToolBar toolBar = getToolBar();
        toolBar.setRightText(getString(R.string.a515));
        toolBar.setTextRightVisibility(true);
        toolBar.setOnRightClickListener(new MyToolBar.OnRightClickListener() {
            @Override
            public void onRightClick() {
                Intent intent = new Intent(AssetsActivity.this, FreezeInstructionsActivity.class);
                startActivity(intent);
            }
        });


        tvAvailable.setText(account);
        tvFreeze.setText(unAccount);

        if (assetsType.equals("USD")){
            btRecharge.setText(getString(R.string.a39));
            btExtract.setText(getString(R.string.a280));
        }else {
            btRecharge.setText(getString(R.string.a264));
            btExtract.setText(getString(R.string.a265));
        }

        tvCurrency.setText(getString(R.string.a523, assetsType));


    }


    private void getExchangeRate(final String type) {
        Map<String, String> param = new LinkedHashMap<>();
        param.put("content", "USDCNH");
        param.put("type", "0");

        NetUtils.get(Url.getRate, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getExchangeRate onResponse: " + e.toString());
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
                        switch (type) {
                            case "BTC":
                                Double last = AppUtils.lastPrice.get(0);
                                rate = Arith.mul(rate, last);
                                break;
                            case "LTC":
                                rate = Arith.mul(rate, AppUtils.lastPrice.get(1));
                                break;
                            case "ETH":
                                rate = Arith.mul(rate, AppUtils.lastPrice.get(2));
                                break;

                        }

                        tvAvailableCNY.setText("=" + Arith.mul(account, String.valueOf(rate), 2) + " CNY");
                        tvFreezeCNY.setText("=" + Arith.mul(unAccount, String.valueOf(rate), 2) + " CNY");

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

    private void getAssetsDetail(String assetsType) {

        Map<String, String> param = new LinkedHashMap<>();
        param.put("symbol", assetsType);
        param.put("userid", AppUtils.getUserId());

        LogUtils.d(TAG, "getAssetsDetail: symbol=="+assetsType + "  userid=="+AppUtils.getUserId());

        NetUtils.get(Url.myAssetsDetails, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getAssetsDetail onError: " + e.toString());
                showToast(getString(R.string.a537));

            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getAssetsDetail onResponse: " + response);

                AssetsDetailBean bean = AppUtils.getGson().fromJson(response, AssetsDetailBean.class);

                if (bean.getStatus() == 200) {
                    mDataList = bean.getObj().getList();
                    adapter.replaceData(mDataList);
                    adapter.notifyDataSetChanged();
                }


            }
        });


    }


    @OnClick({R.id.bt_recharge, R.id.bt_extract})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.bt_recharge://C2C充值

                if ("USD".equals(assetsType)) {
                    //intent = new Intent(this, USDRechargeActivity.class);
                    //startActivity(intent);

                    setResult(RESULT_OK);
                    finish();


                } else if ("π".equals(assetsType)) {
                    intent = new Intent(this, PaiRechargeActivity.class);
                    startActivity(intent);
                } else if ("CTS".equals(assetsType)) {
                    showToast(getString(R.string.a248));
                } else if (status == 2) {
                    showToast(getString(R.string.a248));
                } else {
                    intent = new Intent(this, BTCRechargeActivity.class);
                    intent.putExtra("currency", assetsType);
                    startActivity(intent);
                }
                break;
            case R.id.bt_extract://C2C提取
                if ("USD".equals(assetsType)) {
                    /*intent = new Intent(this, USDExtractActivity.class);
                    startActivity(intent);*/
                    setResult(RESULT_OK);
                    finish();
                } else if ("π".equals(assetsType)) {
                    intent = new Intent(this, PaiExtractActivity.class);
                    startActivity(intent);
                } else if ("CTS".equals(assetsType)) {
                    showToast(getString(R.string.a248));
                } else {
                    intent = new Intent(this, BTCExtractActivity.class);
                    intent.putExtra("currency", assetsType);
                    intent.putExtra("available", account);
                    startActivity(intent);
                }

                break;
        }
    }


}
