package com.atkj.ctc.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.api.ErrorCallback;
import com.atkj.ctc.api.LoadingCallback;
import com.atkj.ctc.bean.CurrencyBean;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.views.InputBox;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;

import org.greenrobot.eventbus.EventBus;
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

public class PostBuyActivity extends ToobarActivity {

    private static final String TAG = "PostBuyActivity";
    private boolean isBuy;

    @BindView(R.id.ll_mairu)
    LinearLayout llMairu;
    @BindView(R.id.rl_keyong)
    RelativeLayout rlKeyong;
    @BindView(R.id.bt_buy)
    TextView btBuy;
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.ib_price)
    InputBox ibPrice;
    @BindView(R.id.ib_cont)
    InputBox ibCont;
    @BindView(R.id.tv_available)
    TextView tvAvailable;//可用数量
    @BindView(R.id.tv_freeze)
    TextView tvFreeze;//冻结数量
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private String pwdResult;
    private AlertDialog payPassWordDialog;
    private String available;
    private LoadService loadService;
    private AlertDialog rechargeDialog;
    private PopupWindow pop;
    private List<CurrencyBean.ObjBean> p2pCurrencyList;
    private int ctid;
    private String currency;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_post_buy);
        ButterKnife.bind(this);

        isBuy = getIntent().getBooleanExtra("isBuy", true);
        init(isBuy);


    }

    private void init(boolean isBuy) {
        setToobarTitle(isBuy ? getString(R.string.a39) : getString(R.string.a280));
        int colorSell = getResources().getColor(R.color.textColor_green);
        int colorBuy = getResources().getColor(R.color.textColor2);

        tvBuy.setText(isBuy ? getString(R.string.a39) : getString(R.string.a280));
        tvBuy.setTextColor(isBuy ? colorBuy : colorSell);
        Drawable drawableSell = getResources().getDrawable(R.drawable.selecter_login_bg_sell);
        Drawable drawableBuy = getResources().getDrawable(R.drawable.selecter_login_bg);
        btBuy.setBackground(isBuy ? drawableBuy : drawableSell);


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

    private void initEvent() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ctid = p2pCurrencyList.get(tab.getPosition()).getCtid();
                currency = p2pCurrencyList.get(tab.getPosition()).getSymbol();
                ibCont.setCurrency(p2pCurrencyList.get(tab.getPosition()).getSymbol());
                btBuy.setText(isBuy ? getString(R.string.a48, currency) : getString(R.string.a281, currency));
                getAvailableFreezeCont(ctid);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void initData() {
        getCurrencyType(Constants.CURRENCY_P2P);

    }

    private void getCurrencyType(final int type) {

        //1 大盘交易title 2 币币交易title
        Map<String, String> param = new LinkedHashMap<>();
        param.put("type", String.valueOf(type));

        NetUtils.get(Url.ctData, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getCurrencyType onError: " + e.toString());
                showToast(getString(R.string.a537));
                String str = getJson(type);
                parsJson(str);

            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getCurrencyType onResponse: " + response);
                try {
                    JSONObject obj = new JSONObject(response);

                    String str;
                    if (obj.getInt("status") == 200) {
                        //缓存一下
                        SharedPreferences sp = getSharedPreferences("ctid", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString(getKey(type), response);
                        edit.apply();
                        str = response;
                    } else {
                        //从缓存获取
                        str = getJson(type);
                    }

                    parsJson(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void parsJson(String str) {
        if (str != null) {
            CurrencyBean currencyBean = AppUtils.getGson().fromJson(str, CurrencyBean.class);
            p2pCurrencyList = currencyBean.getObj();

            getAvailableFreezeCont(p2pCurrencyList.get(0).getCtid());

            if (p2pCurrencyList.size() > 0) {
                CurrencyBean.ObjBean bean = p2pCurrencyList.get(0);
                bean.setSelect(true);
                ctid = bean.getCtid();
                currency = bean.getSymbol();
                btBuy.setText(isBuy ? getString(R.string.a48, currency) : getString(R.string.a281, currency));
                ibCont.setCurrency(currency);
            }

            for (int i = 0; i < p2pCurrencyList.size(); i++) {
                tabLayout.addTab(tabLayout.newTab().setText(p2pCurrencyList.get(i).getSymbol()));
            }


        }
    }

    private String getJson(int type) {
        //从缓存获取
        SharedPreferences sp = getSharedPreferences("ctid", Context.MODE_PRIVATE);
        return sp.getString(getKey(type), null);
    }

    private String getKey(int type) {
        String key;
        switch (type) {
            case 1:
                key = "ctid_market_json";
            case 2:
                key = "ctid_c2c_json";
            case 3:
                key = "ctid_p2p_json";
            default:
                key = "";
        }
        return key;
    }

    private void getAvailableFreezeCont(int ctid) {
        String userid = AppUtils.getUserId();

        Map<String, String> param = new LinkedHashMap<>();
        param.put("userId", userid);
        param.put("type", "1");
        param.put("ctid", String.valueOf(ctid));

        NetUtils.get(Url.availableFreezeCont, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d("getAvailableFreezeCont", e.toString());
                showToast(getString(R.string.a537));
                loadService.showCallback(ErrorCallback.class);
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d(TAG, "getAvailableFreezeCont onResponse: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        loadService.showSuccess();

                        JSONObject obj1 = obj.getJSONObject("obj");
                        available = obj1.getString("account");
                        tvAvailable.setText(available);
                        tvFreeze.setText(obj1.getString("unaccount"));
                    } else {
                        showToast(obj.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @OnClick({R.id.bt_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_buy:
                if (TextUtils.isEmpty(ibPrice.getText())) {
                    showToast(getString(R.string.a376));
                    return;
                } else if (Double.parseDouble(ibPrice.getText()) > Constants.PRICE_MAX) {
                    showToast(getString(R.string.a377));
                    return;
                } else if (TextUtils.isEmpty(ibCont.getText())) {
                    showToast(getString(R.string.a283));
                    return;
                } else if (Double.parseDouble(ibCont.getText()) > Constants.PRICE_MAX) {
                    showToast(getString(R.string.a378));
                    return;
                }else if (!isBuy){
                    if (Double.parseDouble(ibCont.getText()) > Double.parseDouble(available)) {
                        /*String symbol = p2pCurrencyList.get(tabLayout.getSelectedTabPosition()).getSymbol();
                        showRechargeTips(symbol);*/

                        showToast(getString(R.string.a382, currency));
                        return;
                    }
                }

                Intent intent = new Intent(this, PostBuyConfirmActivity.class);
                intent.putExtra("isBuy", isBuy);
                intent.putExtra("price", ibPrice.getText());
                intent.putExtra("cont", ibCont.getText());
                intent.putExtra("currency", currency);
                intent.putExtra("ctid", String.valueOf(ctid));
                startActivity(intent);

                break;

        }
    }


    private void showRechargeTips(final String currency) {
        final AlertDialog.Builder customizeDialog = new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_cancel_entrust, null);
        final TextView cancel = dialogView.findViewById(R.id.cancel);
        final TextView enter = dialogView.findViewById(R.id.enter);
        final TextView content = dialogView.findViewById(R.id.content);

        content.setText(getString(R.string.a382, currency));
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
                Intent intent;
                switch (currency) {
                    case "π":
                        intent = new Intent(PostBuyActivity.this, PaiRechargeActivity.class);
                        startActivity(intent);
                        break;
                    case "USD":
                        intent = new Intent(PostBuyActivity.this, USDRechargeActivity.class);
                        startActivity(intent);

                        break;
                    default:
                        BTCRechargeActivity.startActivity(currency,PostBuyActivity.this);
                        break;

                }
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

}
