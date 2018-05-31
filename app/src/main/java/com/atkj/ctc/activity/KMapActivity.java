package com.atkj.ctc.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.bean.MarketListBean;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Arith;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.utils.kchart.DataHelper;
import com.atkj.ctc.utils.kchart.KChartAdapter;
import com.atkj.ctc.utils.kchart.KLineEntity;
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

import java.net.URI;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by Administrator on 2018/1/5 0005.
 */

public class KMapActivity extends ToobarActivity implements TabLayout.OnTabSelectedListener {


    private static final String TAG = "KMapActivity";
    @BindView(R.id.kchart_view)
    KChartView mKChartView;
    @BindView(R.id.tab_layout_time)
    TabLayout tabLayout;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_extent)
    TextView tvExtent;
    @BindView(R.id.tv_vol)
    TextView tvVol;
    @BindView(R.id.tv_low)
    TextView tvLow;
    @BindView(R.id.tv_high)
    TextView tvHigh;



    private KChartAdapter mAdapter;
    private String symbol = "";
    private String type = "1day";
    private String size = "500";
    private WebSocketClient client;
    private String currencyName;
    private int colorRed;
    private int colorGreen;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_k_map);
        ButterKnife.bind(this);
        initView();
        initData();

    }

    private void initView() {
        colorRed = getResources().getColor(R.color.textColor2);
        colorGreen = getResources().getColor(R.color.textColor_green);
        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        if (ori == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏
            getToolBar().setVisibility(View.GONE);
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            getToolBar().setVisibility(View.VISIBLE);
        }

        currencyName = getIntent().getStringExtra("currencyName");
        setToobarTitle(currencyName+getString(R.string.a305));
        tabLayout.getTabAt(4).select();

        switch (currencyName.split("/")[0]){
            case "BTC":
                symbol = "btc_usd";
                break;
            case "LTC":
                symbol = "ltc_usd";
                break;
            case "ETH":
                symbol = "eth_usd";
                break;
            case "ETC":
                symbol = "etc_usd";
                break;
            case "BCC":
                symbol = "bcc_usd";
                break;
        }

        tabLayout.addOnTabSelectedListener(this);

        mAdapter = new KChartAdapter();
        mKChartView.setAdapter(mAdapter);
        mKChartView.setDateTimeFormatter(new DateFormatter());
        mKChartView.setGridRows(4);
        mKChartView.setGridColumns(3);
    }

    private void initData() {
        mKChartView.setRefreshListener(new KChartView.KChartRefreshListener() {
            @Override
            public void onLoadMoreBegin(KChartView chart) {
                //mKChartView.refreshComplete();
                //chart.refreshEnd();
            }
        });
        getKLineData(symbol,type,size);
        initWSCline();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeConn();
    }

    private void getKLineData(String symbol, String type,String size){


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
                .addParams("symbol",symbol)
                .addParams("type",type)
                .addParams("size",size)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.d(TAG, "getKLineData onError: "+e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d(TAG, "getKLineData onResponse: "+response);
                        List<KLineEntity> data = jsonToArray(response);

                        //第一次加载时开始动画
                        if(mAdapter.getCount()==0)
                        {
                            mKChartView.startAnimation();
                        }

                        mAdapter.addFooterData(data);
                        //加载完成，还有更多数据
                        if(data.size()>0)
                        {
                            mKChartView.refreshComplete();
                        }
                        //加载完成，没有更多数据
                        else {
                            mKChartView.refreshEnd();
                        }

                    }
                });

    }


    private  List<KLineEntity> jsonToArray(String jsonStr) {
        String[][] str = AppUtils.getGson().fromJson(jsonStr, new TypeToken<String[][]>(){}.getType());
        List<KLineEntity> kLineEntities = new ArrayList<>();
        for (String[] strings : str) {
            KLineEntity entity = new KLineEntity();
            entity.Date = AppUtils.timedate(Long.parseLong(strings[0]),"yyyy/MM/dd");
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

    //TabLayout 选择回调
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        LogUtils.d(TAG, "onTabSelected: ");
        switch (tab.getPosition()){
            case 1:
                type = "5min";
                size = "1000";
                getKLineData(symbol,type,size);
                break;
            case 2:
                type = "30min";
                size = "700";
                getKLineData(symbol,type,size);
                break;
            case 3:
                type = "1hour";
                size = "600";
                getKLineData(symbol,type,size);
                break;
            case 4:
                type = "1day";
                size = "500";
                getKLineData(symbol,type,size);
                break;
            case 0 :
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
                        tvExtent.setText(present > 0 ? "+"+format+"%" : format+"%");
                        tvHigh.setText(String.valueOf(data.getHigh()));
                        tvLow.setText(String.valueOf(data.getLow()));
                        tvPrice.setText(String.valueOf(last));
                        tvPrice.setTextColor( present > 0 ? colorRed : colorGreen);
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

    private void closeConn() {
        if (client != null){

            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                client = null;
            }
        }
    }



}
