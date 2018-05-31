package com.atkj.ctc.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.atkj.ctc.R;
import com.atkj.ctc.adapter.HomeMarketAdapter;
import com.atkj.ctc.bean.MarketListBean;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by Administrator on 2018/1/3 0003.
 */

public class QuotationActivity extends ToobarActivity implements OnRefreshListener {


    private static final java.lang.String TAG = "QuotationActivity";
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.srl)
    SmartRefreshLayout refreshLayout;

    private HomeMarketAdapter mMarketAdapter;
    private ArrayList<MarketListBean> mMarketListData;
    private WebSocketClient client;
    private double rate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_quotation);
        ButterKnife.bind(this);
        setToobarTitle(getString(R.string.a305));

        initEvent();

    }

    private void initEvent() {

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mMarketListData = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mMarketListData.add(new MarketListBean());
        }

        mMarketAdapter = new HomeMarketAdapter(R.layout.list_item_market,mMarketListData);

        recyclerView.setAdapter(mMarketAdapter);

        refreshLayout.setOnRefreshListener(this);

    }

    public void initWSCline() {
        if (client == null) {
            client = new WebSocketClient(URI.create(Url.wsMarket)) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    LogUtils.d("onOpen==========");
                    client.send("[{'event':'addChannel','channel':'ok_sub_spot_btc_usd_ticker'}," +
                            "{'event':'addChannel','channel':'ok_sub_spot_ltc_usd_ticker'}," +
                            "{'event':'addChannel','channel':'ok_sub_spot_eth_usd_ticker'}," +
                            "{'event':'addChannel','channel':'ok_sub_spot_etc_usd_ticker'}," +
                            "{'event':'addChannel','channel':'ok_sub_spot_bcc_usd_ticker'}]");
                    LogUtils.d("onOpen",handshakedata.getHttpStatus()+"");
                    LogUtils.d("onOpen",handshakedata.getHttpStatusMessage());
                    // client.send("{'event':'addChannel','channel':'ok_sub_spot_btc_usd_ticker'}");

                }

                @Override
                public void onMessage(String message) {
                    //Json的解析类对象
                    JsonParser parser = new JsonParser();
                    //将JSON的String 转成一个JsonArray对象
                    JsonArray jsonArray = parser.parse(message).getAsJsonArray();
                    JsonElement jsonElement = jsonArray.get(0);

                    MarketListBean bean = AppUtils.getGson().fromJson(jsonElement, MarketListBean.class);
                    String[] channel = bean.getChannel().split("_");

                    switch (channel[3]) {
                        case "btc":
                            //LogUtils.w(message);
                            mMarketListData.set(0, bean);
                            break;
                        case "ltc":
                            mMarketListData.set(1, bean);
                            break;
                        case "eth":
                            mMarketListData.set(2, bean);
                            break;
                        case "etc":
                            mMarketListData.set(3, bean);
                            break;
                        case "bcc":
                            mMarketListData.set(4, bean);
                            break;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMarketAdapter.setRate(rate);
                            mMarketAdapter.notifyDataSetChanged();
                        }
                    });


                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    LogUtils.d("Connection closed by " + (remote ? "remote peer" : "us") + ", info=" + reason);
                    //closeConn();
                }

                @Override
                public void onError(Exception ex) {

                }
            };
        }

        client.connect();
    }

    private void getExchangeRate() {
        Map<String,String> param = new LinkedHashMap<>();
        param.put("content","USDCNH");
        param.put("type","0");

        NetUtils.get(Url.getRate, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getExchangeRate onError: " + e.toString());
                showToast(getString(R.string.a537));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getExchangeRate onResponse: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        JSONArray obj1 = obj.getJSONArray("obj");
                        JSONObject jsonObject = obj1.getJSONObject(0);
                        rate = jsonObject.getDouble("price");

                    } else {
                        showToast(obj.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void closeConn() {
        try {
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client = null;
        }
    }

    //取消接收推送
    public void removeChannel() {
        if (client.isConnecting()){
            client.send("[{'event':'removeChannel','channel':'ok_sub_spot_btc_usd_ticker'}," +
                    "{'event':'removeChannel','channel':'ok_sub_spot_ltc_usd_ticker'}," +
                    "{'event':'removeChannel','channel':'ok_sub_spot_eth_usd_ticker'}," +
                    "{'event':'removeChannel','channel':'ok_sub_spot_etc_usd_ticker'}," +
                    "{'event':'removeChannel','channel':'ok_sub_spot_bcc_usd_ticker'}]");
        }

    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {

        getExchangeRate();

        if (client != null){
            removeChannel();
            closeConn();
            initWSCline();
        }else {
            initWSCline();
        }

        refreshlayout.finishRefresh();
    }


    @Override
    public void onResume() {
        super.onResume();
        initWSCline();
    }

    @Override
    public void onPause() {
        super.onPause();
        removeChannel();
        closeConn();
    }




}
