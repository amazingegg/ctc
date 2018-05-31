package com.atkj.ctc.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidkun.xtablayout.XTabLayout;
import com.atkj.ctc.R;
import com.atkj.ctc.adapter.CommonRecycleViewAdapter;
import com.atkj.ctc.bean.AllCurrencyBean;
import com.atkj.ctc.bean.CurrencyIntroductionBean;
import com.atkj.ctc.bean.EntrustBean;
import com.atkj.ctc.bean.MarketListBean;
import com.atkj.ctc.bean.TestBean;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Arith;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.utils.kchart.DataHelper;
import com.atkj.ctc.utils.kchart.KChartAdapter;
import com.atkj.ctc.utils.kchart.KLineEntity;
import com.atkj.ctc.views.CustomLinearLayoutManager;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.tifezh.kchartlib.chart.KChartView;
import com.github.tifezh.kchartlib.chart.formatter.DateFormatter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2018/4/23 0023.
 */

public class CurrencyDetailActivity extends ToobarActivity implements TabLayout.OnTabSelectedListener {

    private static final java.lang.String TAG = "CurrencyDetailActivity";
    @BindView(R.id.tab_layout)
    XTabLayout tabLayout;
    @BindView(R.id.part_bottom)
    LinearLayout ll_part_bottom;
    @BindView(R.id.ll_part_introduction)
    LinearLayout ll_part_introduction;
    @BindView(R.id.buy)
    TextView tvBuy;
    @BindView(R.id.sell)
    TextView tvSell;
    @BindView(R.id.recycleview)
    RecyclerView recyclerView;
    @BindView(R.id.kchart_view)
    KChartView mKChartView;
    @BindView(R.id.tab_layout_time)
    TabLayout tabKTime;
    @BindView(R.id.tv_hight)
    TextView tvHight;
    @BindView(R.id.tv_low)
    TextView tvLow;
    @BindView(R.id.tv_vol)
    TextView tvVol;
    @BindView(R.id.last_price)
    TextView tvLastPrice;
    @BindView(R.id.last_price_rmb)
    TextView tvLastPriceRmb;
    @BindView(R.id.tv_extent)
    TextView tvExtent;
    @BindView(R.id.rl_top_2)
    RelativeLayout rl2;
    @BindView(R.id.ll_2)
    LinearLayout ll2;
    @BindView(R.id.ll_bottom)
    LinearLayout ll3;

    @BindView(R.id.tv_title1)
    TextView tvTitle;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_total2)
    TextView tvTotal2;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_web)
    TextView tvWeb;
    @BindView(R.id.tv_block)
    TextView tvBlock;
    @BindView(R.id.tv_introduction)
    TextView tvIntroduction;


    private String currencyName;
    private String symbol = "";
    private KChartAdapter mAdapter;
    private WebSocketClient client;
    private String type = "1day";
    private String size = "500";
    private int colorRed;
    private int colorGreen;
    private double rate;
    private int ctid;
    private AllCurrencyBean.ObjBean.ListBean currencyData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_currency_detail);
        ButterKnife.bind(this);


        init();
        initEvent();
        initData();


    }


    private void init() {

        Configuration mConfiguration = getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        ViewGroup.LayoutParams lp = mKChartView.getLayoutParams();

        if (ori == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏

            getToolBar().setVisibility(View.GONE);
            rl2.setVisibility(View.GONE);
            ll2.setVisibility(View.GONE);
            ll3.setVisibility(View.GONE);
            Point size = new Point();
            getWindowManager().getDefaultDisplay().getSize(size);
            lp.height = size.y - tabKTime.getLayoutParams().height;


        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //显示状态栏
            getToolBar().setVisibility(View.VISIBLE);
            rl2.setVisibility(View.VISIBLE);
            ll2.setVisibility(View.VISIBLE);
            ll3.setVisibility(View.VISIBLE);
            lp.height = AppUtils.dip2px(this, 300);
        }

        mKChartView.setLayoutParams(lp);


        currencyData = (AllCurrencyBean.ObjBean.ListBean) getIntent().getSerializableExtra("currencyData");
        if (currencyData != null){
            currencyName = currencyData.getSymbol();
            ctid = currencyData.getCtid();
        }


        tvLastPrice.getPaint().setFakeBoldText(true);


        colorRed = getResources().getColor(R.color.textColor2);
        colorGreen = getResources().getColor(R.color.textColor_green);
        tabKTime.getTabAt(4).select();

        setToobarTitle(currencyName);
        if (currencyName != null) {
            String currency = currencyName.split("/")[0];
            tvBuy.setText(getString(R.string.a48, currency));
            tvSell.setText(getString(R.string.a281, currency));

            switch (currencyName) {
                case "BTC/USD":
                    symbol = "btc_usd";
                    break;
                case "LTC/USD":
                    symbol = "ltc_usd";
                    break;
                case "ETH/USD":
                    symbol = "eth_usd";
                    break;
                case "ETC/USD":
                    symbol = "etc_usd";
                    break;
                case "BCC/USD":
                    symbol = "bch_usd";
                    break;
                default:
                    symbol = "btc_usd";
            }


        }

        mAdapter = new KChartAdapter();
        mKChartView.setAdapter(mAdapter);
        mKChartView.setDateTimeFormatter(new DateFormatter());
        mKChartView.setGridRows(4);
        mKChartView.setGridColumns(3);


    }

    public static void actionStart(Context context, AllCurrencyBean.ObjBean.ListBean bean) {
        Intent intent = new Intent(context, CurrencyDetailActivity.class);
        intent.putExtra("currencyData", bean);
        context.startActivity(intent);
    }

    public static void actionStart(Activity context, AllCurrencyBean.ObjBean.ListBean bean ,int requestCode) {
        Intent intent = new Intent(context, CurrencyDetailActivity.class);
        intent.putExtra("currencyData", bean);
        context.startActivityForResult(intent,requestCode);
    }

    private void initData() {
        mKChartView.setRefreshListener(new KChartView.KChartRefreshListener() {
            @Override
            public void onLoadMoreBegin(KChartView chart) {
                //mKChartView.refreshComplete();
                //chart.refreshEnd();
            }
        });
        getKLineData(symbol, type, size);

        initWSCline();
        getLatestTransactions();
        getCTIntroduction();
        getExchangeRate();



    }

    private void getCTIntroduction() {
        Map<String, String> param = new LinkedHashMap<>();
        param.put("ctid", String.valueOf(ctid));

        NetUtils.get(Url.currencyDetails, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getCTIntroduction onError: " + e.toString());
                showToast(getString(R.string.a537));

            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getCTIntroduction onResponse: " + response);

                CurrencyIntroductionBean bean = AppUtils.getGson().fromJson(response, CurrencyIntroductionBean.class);
                CurrencyIntroductionBean.ObjBean obj = bean.getObj();


                if (obj == null) return;
                if (bean.getStatus() == 200){

                    tvTitle.setText(obj.getTitle());
                    tvTime.setText(obj.getIssuedtime());
                    tvTotal.setText(obj.getCirculationaccount());
                    tvTotal2.setText(obj.getIssuedaccount());
                    tvPrice.setText(obj.getPrice());
                    tvWeb.setText(obj.getUrl());
                    tvBlock.setText(obj.getBlockurl());
                    //tvIntroduction.setText(obj.getRemake());
                    tvIntroduction.setText(Html.fromHtml(obj.getRemake()));

                }

            }
        });


    }


    private void initEvent() {

        //最新交易列表
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(bottomAdapter);
        recyclerView.setFocusable(false);
        bottomAdapter.bindToRecyclerView(recyclerView);
        bottomAdapter.setEmptyView(R.layout.layout_empty);

        tabKTime.addOnTabSelectedListener(this);

        tabLayout.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        ll_part_bottom.setVisibility(View.VISIBLE);
                        ll_part_introduction.setVisibility(View.GONE);
                        break;
                    case 1:
                        ll_part_bottom.setVisibility(View.GONE);
                        ll_part_introduction.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {





            }
        });

    }


    private List<EntrustBean.ObjBean> mListDataBottom;
    CommonRecycleViewAdapter bottomAdapter = new CommonRecycleViewAdapter<EntrustBean.ObjBean>(R.layout.list_item_last_trade, mListDataBottom) {
        @Override
        protected void convert(BaseViewHolder helper, EntrustBean.ObjBean item) {
            helper.setText(R.id.time, AppUtils.timedate(item.getCreatetime(), "HH:mm:ss"))
                    .setText(R.id.price, String.valueOf(item.getPrice()))
                    .setText(R.id.count, String.valueOf(item.getAccount()));
        }
    };

    //最新交易
    private void getLatestTransactions() {
        Map<String, String> param = new LinkedHashMap<>();
        param.put("ctid", String.valueOf(ctid));

        NetUtils.get(Url.dealOrder, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, " getLatestTransactions onError: " + e.toString());
                showToast(getString(R.string.a537));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, " getLatestTransactions onResponse: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        EntrustBean bean = AppUtils.getGson().fromJson(response, EntrustBean.class);
                        mListDataBottom = bean.getObj();
                        bottomAdapter.replaceData(mListDataBottom);
                        bottomAdapter.notifyDataSetChanged();

                    } else {
                        showToast(obj.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @OnClick({R.id.buy, R.id.sell, R.id.iv_full_screen})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.buy:
                intent.putExtra("ctid",ctid);
                intent.putExtra("isBuy",true);
                intent.putExtra("currencyName",currencyName);

                AppUtils.ctid = ctid;
                AppUtils.isBuy = true;

                setResult(RESULT_OK,intent);
                finish();
                break;

            case R.id.sell:
                intent.putExtra("ctid",ctid);
                intent.putExtra("isBuy",false);
                intent.putExtra("currencyName",currencyName);

                AppUtils.ctid = ctid;
                AppUtils.isBuy = false;
               /* AppUtils.clearCurrencySelect();
                currencyData.setSelect(true);*/

                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.iv_full_screen:
                requestScreen();

                break;

        }


    }

    private void requestScreen() {
        boolean isVertical = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        if (isVertical) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }

    }


    private void getKLineData(String symbol, String type, String size) {

        switch (currencyName) {
            case "BTC/USD":
                break;
            case "LTC/USD":
                break;
            case "ETH/USD":
                break;
            case "ETC/USD":
                break;
            case "BCC/USD":
                break;
            default:
                return;
        }

        mKChartView.showLoading();
        OkHttpUtils.get().url(Url.kLine)
                .addParams("symbol", symbol)
                .addParams("type", type)
                .addParams("size", size)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d(TAG, "getKLineData onError: " + e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d(TAG, "getKLineData onResponse: " + response);
                        List<KLineEntity> data = jsonToArray(response);

                        //第一次加载时开始动画
                        if (mAdapter.getCount() == 0) {
                            mKChartView.startAnimation();
                        }

                        mAdapter.addFooterData(data);
                        //加载完成，还有更多数据
                        if (data.size() > 0) {
                            mKChartView.refreshComplete();
                        }
                        //加载完成，没有更多数据
                        else {
                            mKChartView.refreshEnd();
                        }

                    }
                });

    }

    public void initWSCline() {
        client = null;
        if (client == null) {
            client = new WebSocketClient(URI.create(Url.wsMarket)) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    LogUtils.d("onOpen==========");
                    LogUtils.d("onOpen", handshakedata.getHttpStatus() + "");
                    LogUtils.d("onOpen", handshakedata.getHttpStatusMessage());

                    switch (currencyName) {
                        case "BTC/USD":
                            client.send("{'event':'addChannel','channel':'ok_sub_spot_btc_usd_ticker'}");
                            break;
                        case "LTC/USD":
                            client.send("{'event':'addChannel','channel':'ok_sub_spot_ltc_usd_ticker'}");
                            break;
                        case "ETH/USD":
                            client.send("{'event':'addChannel','channel':'ok_sub_spot_eth_usd_ticker'}");
                            break;
                        case "ETC/USD":
                            client.send("{'event':'addChannel','channel':'ok_sub_spot_etc_usd_ticker'}");
                            break;
                        case "BCC/USD":
                            client.send("{'event':'addChannel','channel':'ok_sub_spot_bch_usd_ticker'}");
                            break;
                    }

                }

                @Override
                public void onMessage(String message) {
                    //Json的解析类对象
                    JsonParser parser = new JsonParser();
                    //将JSON的String 转成一个JsonArray对象
                    JsonArray jsonArray = parser.parse(message).getAsJsonArray();
                    JsonElement jsonElement = jsonArray.get(0);

                    final MarketListBean bean = AppUtils.getGson().fromJson(jsonElement, MarketListBean.class);
                    final MarketListBean.DataBean data = bean.getData();
                    //(change/(last-change))*100
                    double change = data.getChange();
                    final double last = data.getLast();
                    double sub = Arith.sub(last, change);
                    double div = Arith.div(change, sub);
                    final double present = Arith.mul(div, 100, 2);
                    final String format = new DecimalFormat("0.00").format(present);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvExtent.setText(present > 0 ? "+" + format + "%" : format + "%");
                            tvHight.setText(String.valueOf(data.getHigh()));
                            tvLow.setText(String.valueOf(data.getLow()));
                            tvLastPrice.setText(String.valueOf(last));
                            tvLastPrice.setTextColor(present > 0 ? colorRed : colorGreen);
                            if (rate != 0) {
                                double rmbPrice = Arith.mul(last, rate, 2);
                                tvLastPriceRmb.setText("=￥" + String.valueOf(rmbPrice));
                            }

                            tvVol.setText(String.valueOf(data.getVol()));
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


    //TabLayout 选择回调
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        LogUtils.d(TAG, "onTabSelected: ");
        switch (tab.getPosition()) {
            case 1:
                type = "5min";
                size = "1000";
                getKLineData(symbol, type, size);
                break;
            case 2:
                type = "30min";
                size = "700";
                getKLineData(symbol, type, size);
                break;
            case 3:
                type = "1hour";
                size = "600";
                getKLineData(symbol, type, size);
                break;
            case 4:
                type = "1day";
                size = "500";
                getKLineData(symbol, type, size);
                break;
            case 0:
                showToast(getString(R.string.a248));
                break;
        }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    private List<KLineEntity> jsonToArray(String jsonStr) {
        String[][] str = AppUtils.getGson().fromJson(jsonStr, new TypeToken<String[][]>() {
        }.getType());
        List<KLineEntity> kLineEntities = new ArrayList<>();
        for (String[] strings : str) {
            KLineEntity entity = new KLineEntity();
            entity.Date = AppUtils.timedate(Long.parseLong(strings[0]), "yyyy/MM/dd");
            entity.Open = Float.parseFloat(strings[1]);
            entity.Close = Float.parseFloat(strings[4]);
            entity.High = Float.parseFloat(strings[2]);
            entity.Low = Float.parseFloat(strings[3]);
            entity.Volume = Float.parseFloat(strings[5]);
            kLineEntities.add(entity);
        }
        DataHelper.calculate(kLineEntities);
        return kLineEntities;
    }


    private void closeConn() {
        if (client != null) {

            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                client = null;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeConn();
    }


    private void getExchangeRate() {
        Map<String, String> param = new LinkedHashMap<>();
        param.put("content", "USDCNH");
        param.put("type", "0");

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


}
