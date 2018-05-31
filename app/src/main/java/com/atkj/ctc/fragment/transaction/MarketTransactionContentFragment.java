package com.atkj.ctc.fragment.transaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidkun.xtablayout.XTabLayout;
import com.atkj.ctc.MainActivity;
import com.atkj.ctc.R;
import com.atkj.ctc.activity.BTCRechargeActivity;
import com.atkj.ctc.activity.KMapActivity;
import com.atkj.ctc.activity.LoginActivity;
import com.atkj.ctc.activity.MemberActivity;
import com.atkj.ctc.activity.PaiRechargeActivity;
import com.atkj.ctc.activity.PayPassWordActivity;
import com.atkj.ctc.activity.USDRechargeActivity;
import com.atkj.ctc.adapter.CommonRecycleViewAdapter;
import com.atkj.ctc.bean.AllCurrencyBean;
import com.atkj.ctc.bean.EntrustBean;
import com.atkj.ctc.bean.FeeBean;
import com.atkj.ctc.bean.MarketListBean;
import com.atkj.ctc.fragment.LazyFragment;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Arith;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.views.GridDivider;
import com.atkj.ctc.views.InputBox;
import com.atkj.ctc.views.PswInputView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.java_websocket.client.WebSocketClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;


public class MarketTransactionContentFragment extends LazyFragment implements RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "MarketTransactionContentFragment";
    private static final String SECOND = "2";
    private static final String FIRST = "1";
    private static final String BUY = "2";
    private static final String SELL = "1";
    private static final String ENTRUST = "entrust";
    private static final String HISTORY = "history";
    // 这里的参数只是一个举例可以根据需求更改
    private String currencyName;
    private Unbinder unbinder;

    @BindView(R.id.price)
    TextView topPrice;
    @BindView(R.id.cny_price)
    TextView cnyPrice;
    @BindView(R.id.rg_group)
    RadioGroup rg_group;
    @BindView(R.id.cb_buy_pai)
    RadioButton rbBuy;


    @BindView(R.id.recycleview_buy)
    RecyclerView rvBuy;//买单交易 委托信息
    @BindView(R.id.recycleview_sell)
    RecyclerView rvSell;//卖单交易 委托信息
    @BindView(R.id.price_entrust)
    TextView tvPrice;
    @BindView(R.id.recycleview_entrust)
    RecyclerView rvEntrust;//委托
    /*@BindView(R.id.ll_empty_entrust)
    LinearLayout enptyEntrust;*/
    /*@BindView(R.id.recycleview_bottom)
    RecyclerView rvBottom;//下拉最新数据*/
   /* @BindView(R.id.ll_empty)
    LinearLayout emptyView;*/

    @BindView(R.id.ib_price)
    InputBox ibPrice;
    @BindView(R.id.ib_cont)
    InputBox ibCount;
    @BindView(R.id.bt_buy)
    TextView btBuy;
    /* @BindView(R.id.buy_count_1)
     TextView tvBuyCount;*/
    /*@BindView(R.id.part_buy)
    LinearLayout partBuy;*/
    @BindView(R.id.part_entrust)
    LinearLayout partEntrust;
    @BindView(R.id.k_line_map)
    ImageView kLineMap;
   /* @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.scroll_view1)
    NestedScrollView scrollView1;*/

    @BindView(R.id.available)
    TextView tvAvailable;//可用
    /*@BindView(R.id.buy_count_2)
    TextView tvAvailableCount;//可用数量*/
    @BindView(R.id.title4)
    TextView title4;
    @BindView(R.id.tv_currency)
    TextView currency;
    @BindView(R.id.ll_currency)
    LinearLayout ll_Currency;
    @BindView(R.id.rl_top_title)
    RelativeLayout rl_TopTitle;
    @BindView(R.id.iv_down_allow)
    ImageView downArrow;
    /* @BindView(R.id.tv_latest_trades)
     TextView tvLatestTrades;*/
   /* @BindView(R.id.currency)
    TextView tvCurrency;*/
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tab_layout2)
    XTabLayout tabLayout2;
    @BindView(R.id.tv_cny_price)
    TextView tvCnyPrice;
    @BindView(R.id.tv_percent)
    TextView tvPercent;
    @BindView(R.id.seek_bar)
    SeekBar seekBar;
    @BindView(R.id.transaction_count)
    TextView tvTransactionCount;


    private String availableUsd;
    private String availablePai;
    private boolean isBuy = true;
    private String freezePai;
    //private String availableCount = "0.00";
    private AlertDialog payPassWordDialog;
    //private WebSocketClient webSocketClient;
    private double rate;
    //private double newPrice;
    private AlertDialog cancelEntrustDialog;
    private Timer timer;
    private MyTask timerTask;
    private AlertDialog rechargeDialog;
    private PopupWindow tradesPopu;
    private View view;
    private MainActivity mContext;
    private AlertDialog loginTipsDialog;

    private PopupWindow currencyPopu;
    private int ctid;
    //private String name;
    //private ArrayList<Double> mMarketListData;
    private View currencyPopContent;
    private XTabLayout currencyPopTab;
    //private List<AllCurrencyBean.ObjBean> allCurrencyBeanList;
    //private List<Double> lastPriceList;

    /**
     * 通过工厂方法来创建Fragment实例
     * 同时给Fragment来提供参数来使用
     *
     * @param name 参数1.
     * @return Fragment的实例.
     */
    public static MarketTransactionContentFragment newInstance(String name) {
        MarketTransactionContentFragment fragment = new MarketTransactionContentFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            name = getArguments().getString("name");
        }*/
    }


    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d(TAG, "onPause: =====");


        //removeChannel();
        //closeConn();


    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        LogUtils.d(TAG, "onHiddenChanged: ==" + hidden);

        if (AppUtils.allCurrencyBeanList != null && !hidden) {


            //TODO 在这个页面选择币种后,跳到主页再返回会更改选择
            changeCurrency();

        }


    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        stopTimer();
    }

    @Override
    public void onStart() {
        super.onStart();
        startTimer();
        EventBus.getDefault().register(this);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (MainActivity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d(TAG, "onResume: ");


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMarketEvent(MarketListBean event) {
        String[] channel = event.getChannel().split("_");
        String title = currencyName.split("/")[0];
        double newPrice = event.getData().getLast();

        switch (channel[3]) {
            case "btc":
                //mMarketListData.set(0, newPrice);
                if (title.equals("BTC")) {
                    setPrice(newPrice);
                    //LogUtils.w("BTC=="+newPrice);
                }
                break;
            case "ltc":
                //mMarketListData.set(1, newPrice);
                if (title.equals("LTC")) {
                    setPrice(newPrice);
                    //LogUtils.w("LTC=="+newPrice);
                }
                break;
            case "eth":
                //mMarketListData.set(2, newPrice);
                if (title.equals("ETH")) {
                    setPrice(newPrice);
                    //LogUtils.w("ETH=="+newPrice);
                }
                break;
            case "etc":
                //mMarketListData.set(3, newPrice);
                if (title.equals("ETC")) {
                    setPrice(newPrice);
                }
                break;
            case "bcc":
                //mMarketListData.set(4, newPrice);
                if (title.equals("BCC")) {
                    setPrice(newPrice);
                }
                break;
        }

    }


    private void changeCurrency() {
        //noinspection ConstantConditions
        //tabLayout.getTabAt(AppUtils.isBuy ? 0 : 1).select();

        currencyPopTab.removeAllTabs();
        for (int i = 0; i < AppUtils.allCurrencyBeanList.size(); i++) {
            String symbol = AppUtils.allCurrencyBeanList.get(i).getSymbol();
            currencyPopTab.addTab(currencyPopTab.newTab().setText(symbol));


            List<AllCurrencyBean.ObjBean.ListBean> list1 = AppUtils.allCurrencyBeanList.get(i).getList();
            for (int j = 0; j < list1.size(); j++) {
                AllCurrencyBean.ObjBean.ListBean listBean = list1.get(j);

                if (listBean.getCtid() == AppUtils.ctid) {
                    setCurrency(listBean);
                    //noinspection ConstantConditions
                    currencyPopTab.getTabAt(i).select();
                }
            }
        }

    }

    private void startTimer() {
        LogUtils.d(TAG, "startTimer: ");
        stopTimer();

        timer = new Timer();
        timerTask = new MyTask();
        timer.schedule(timerTask, 0, 15000);
    }


    private void stopTimer() {
        LogUtils.d(TAG, "stopTimer: ");
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
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

    @Override
    protected void onFragmentFirstVisible() {

    }

    private void getData() {
        //π不显示K线图
        String name = currencyName.split("/")[0];
        if (name.equals("π") || name.equals("CTS")) {
            kLineMap.setVisibility(View.GONE);
        } else {
            kLineMap.setVisibility(View.VISIBLE);
        }

        //noinspection ConstantConditions
        if (tabLayout2.getTabAt(0).isSelected()) {

            getEntrustList(ENTRUST);

        } else {
            getEntrustList(HISTORY);


        }
        getAvailableCount(FIRST);
        getAvailableCount(SECOND);
        getExchangeRate();
        getEntrust(BUY);
        getEntrust(SELL);


    }


    @SuppressLint("SetTextI18n")
    public void setCurrency(AllCurrencyBean.ObjBean.ListBean bean) {

        AppUtils.clearCurrencySelect();

        bean.setSelect(true);

        currencyName = bean.getSymbol();
        currency.setText(currencyName);
        ctid = bean.getCtid();

        //币种显示
        String[] splitTitle = currencyName.split("/");
        ibPrice.setCurrency(splitTitle[1]);
        ibCount.setCurrency(splitTitle[0]);
        btBuy.setText(getString(R.string.a432, splitTitle[0]));

        //noinspection ConstantConditions
        if (isBuy) {
            btBuy.setText(getString(R.string.a432, splitTitle[0]));
        } else {

            btBuy.setText(getString(R.string.a280) + splitTitle[0]);
        }


        /*List<AllCurrencyBean.ObjBean.ListBean> list = allCurrencyBeanList
                .get(currencyPopTab.getSelectedTabPosition()).getList();

        for (int i = 0; i < list.size(); i++) {
            AllCurrencyBean.ObjBean.ListBean listBean = list.get(i);
            if (i == position) {
                listBean.setSelect(true);
            } else {
                listBean.setSelect(false);
            }

        }*/

        switch (currencyName) {
            case "BTC/USD":
                setPrice(AppUtils.lastPrice.get(0));
                break;
            case "LTC/USD":
                setPrice(AppUtils.lastPrice.get(1));
                break;
            case "ETH/USD":
                setPrice(AppUtils.lastPrice.get(2));
                break;
            case "ETC/USD":
                setPrice(AppUtils.lastPrice.get(3));
                break;
            default:
                setPrice(0);
        }

        currencyAdapter.notifyDataSetChanged();
        getData();


        /*for (int i = 0; i < currencyListData.size(); i++) {
            CurrencyBean.ObjBean bean = currencyListData.get(i);

            if (bean.isSelect()) {
                currencyName = data.get(i).getSymbol();
                ctid = data.get(i).getCtid();
            }
        }*/


        /*//π不显示K线图
        String name = currencyName.split("/")[0];
        if (name.equals("π") || name.equals("CTS")) {
            kLineMap.setVisibility(View.GONE);
        } else {
            kLineMap.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        String[] splitTitle = currencyName.split("/");

        switch (checkedId) {
            case R.id.cb_buy_pai://买入
                isBuy = true;
                //partBuy.setVisibility(View.VISIBLE);
                //partEntrust.setVisibility(View.GONE);

                Drawable drawableSell = getResources().getDrawable(R.drawable.selecter_login_bg_sell);
                //tvBuyCount.setText(getString(R.string.a210));
                btBuy.setText(getString(R.string.a432, splitTitle[0]));
                btBuy.setBackground(drawableSell);

                if (ctid != 0) {
                    getAvailableCount(SECOND);
                }
                /*if (availableUsd != null) {
                    tvAvailable.setText(availableUsd + " " + currencyName.split("/")[1]);
                }*/

                /*//重新计算可买数量
                String price = ibPrice.getText();
                ibPrice.setText(price);
                ibPrice.getEditText().setSelection(price.length());*/


                break;
            case R.id.cb_sell_pai://卖出
                isBuy = false;

                Drawable drawableBuy = getResources().getDrawable(R.drawable.selecter_login_bg);
                btBuy.setText(getString(R.string.a280) + splitTitle[0]);
                btBuy.setBackground(drawableBuy);

                getAvailableCount(FIRST);


               /* if (availablePai != null) {
                    tvAvailable.setText(availablePai + " " + splitTitle[0]);
                }*/


                break;


        }

    }

    /*public void check(int ctid, String currencyName, boolean isBuy) {
        if (isBuy) {
            tabLayout.getTabAt(0).select();
        } else {
            tabLayout.getTabAt(1).select();
        }

        AllCurrencyBean.ObjBean.ListBean bean = new AllCurrencyBean.ObjBean.ListBean();
        bean.setCtid(ctid);
        bean.setSymbol(currencyName);
        setCurrency(bean);

    }*/


    private class MyTask extends TimerTask {
        @Override
        public void run() {
            getEntrust(BUY);
            getEntrust(SELL);
            //getLatestTransactions();
        }
    }


    //最新委托买卖列表
    private void getEntrust(final String type) {
        Map<String, String> param = new LinkedHashMap<>();
        param.put("type", type);
        param.put("ctid", String.valueOf(ctid));

        NetUtils.get(Url.pendingOrder, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getEntrust onError: " + e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, currencyName + " getEntrust onResponse: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        EntrustBean bean = AppUtils.getGson().fromJson(response, EntrustBean.class);
                        List<EntrustBean.ObjBean> list = bean.getObj();

                        /*Collections.sort(list, new Comparator<EntrustBean.ObjBean>() {
                            @Override
                            public int compare(EntrustBean.ObjBean o1, EntrustBean.ObjBean o2) {
                                Double a = Double.parseDouble(o1.getPrice());
                                Double b = Double.parseDouble(o2.getPrice());
                                return b.compareTo(a);
                            }
                        });*/

                        List<EntrustBean.ObjBean> data;
                        if (list.size() > 6) {
                            data = list.subList(0, 6);
                        } else {
                            data = list;
                        }

                        if (type.equals(BUY)) {
                            mBuyListData = data;
                            buyAdapter.replaceData(mBuyListData);
                            buyAdapter.notifyDataSetChanged();
                        } else {
                            mSellListData = data;
                            sellAdapter.replaceData(mSellListData);
                            sellAdapter.notifyDataSetChanged();
                        }

                    } else {
                        showToast(obj.getString("msg"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragement_market_transaction_content, container, false);
        unbinder = ButterKnife.bind(this, view);

        initEvent();


        return view;
    }

    @Override
    public void initData() {
        changeCurrency();
    }

    private List<EntrustBean.ObjBean> mBuyListData;
    CommonRecycleViewAdapter buyAdapter = new CommonRecycleViewAdapter<EntrustBean.ObjBean>
            (R.layout.list_item_market_entrust_buy, mBuyListData) {

        @Override
        protected void convert(BaseViewHolder helper, EntrustBean.ObjBean item) {

            int num = helper.getLayoutPosition()+1;

            helper.setText(R.id.count, new BigDecimal(item.getAccount()).setScale(4).toPlainString())
                    .setText(R.id.price, new BigDecimal(item.getPrice()).setScale(4).toPlainString())
            .setText(R.id.tv_pankou,getString(R.string.a227)+num);
        }
    };

    private List<EntrustBean.ObjBean> mSellListData;
    CommonRecycleViewAdapter sellAdapter = new CommonRecycleViewAdapter<EntrustBean.ObjBean>
            (R.layout.list_item_market_entrust_buy, mSellListData) {

        @Override
        protected void convert(BaseViewHolder helper, EntrustBean.ObjBean item) {
            int num = helper.getLayoutPosition()+1;

            helper.setText(R.id.count, new BigDecimal(item.getAccount()).setScale(4).toPlainString())
                    .setText(R.id.price, new BigDecimal(item.getPrice()).setScale(4).toPlainString())
                    .setText(R.id.tv_pankou,getString(R.string.a228)+num);
            TextView price = helper.getView(R.id.price);
            TextView pankou = helper.getView(R.id.tv_pankou);

            int color = getResources().getColor(R.color.textColor2);
            price.setTextColor(color);
            pankou.setTextColor(color);


        }
    };


    private List<EntrustBean.ObjBean> mListDataEntrust = new ArrayList<>();
    private List<EntrustBean.ObjBean> mListDataHistory = new ArrayList<>();
    CommonRecycleViewAdapter entrustAdapter = new CommonRecycleViewAdapter<EntrustBean.ObjBean>(R.layout.list_item_entrust, mListDataEntrust) {

        @Override
        protected void convert(BaseViewHolder helper, EntrustBean.ObjBean item) {
            TextView price = helper.getView(R.id.price);
            if (item.getType() == Constants.ORDER_BUY) {
                price.setTextColor(getResources().getColor(R.color.textColor_green));
            } else {

                price.setTextColor(getResources().getColor(R.color.textColor2));
            }

            if (getFlag().equals(ENTRUST)) {
                helper.setText(R.id.total, getString(R.string.a199));
            } else {
                helper.setText(R.id.total, getString(R.string.a287));

            }
            helper.setText(R.id.time, AppUtils.timedate(item.getCreatetime(), "MM-dd HH:mm:ss"))
                    .setText(R.id.price, new BigDecimal(item.getPrice()).setScale(4).toPlainString())
                    .setText(R.id.count, new BigDecimal(item.getAccount()).setScale(4).toPlainString());
            helper.addOnClickListener(R.id.total);

        }
    };

    private List<AllCurrencyBean.ObjBean.ListBean> currencyListData;
    CommonRecycleViewAdapter currencyAdapter = new CommonRecycleViewAdapter<AllCurrencyBean.ObjBean.ListBean>
            (R.layout.list_item_currencylist, currencyListData) {
        @Override
        protected void convert(BaseViewHolder helper, AllCurrencyBean.ObjBean.ListBean item) {
            boolean itemSelect = item.isSelect();
            int colorSelect = getResources().getColor(R.color.textColor2);
            int colorNormal = getResources().getColor(R.color.f484848);

            TextView content = helper.getView(R.id.tv_content);
            content.setText(item.getSymbol());
            content.setTextColor(itemSelect ? colorSelect : colorNormal);

        }

    };


    private void initEvent() {
        tvPrice.setText(getString(R.string.a420));
        rbBuy.setChecked(true);
        Drawable drawableSell = getResources().getDrawable(R.drawable.selecter_login_bg_sell);
        btBuy.setBackground(drawableSell);
        //tabLayout.addOnTabSelectedListener(this);
        rg_group.setOnCheckedChangeListener(this);
        //rg_group.getChildAt(0).setSelected(true);
        //rg_group.check(R.id.cb_buy_pai);
        //rbBuy.setChecked(true);
        topPrice.getPaint().setFakeBoldText(true);
        currency.getPaint().setFakeBoldText(true);
        currencyName = currency.getText().toString();

        //初始化币种关系POP
        currencyPopContent = LayoutInflater.from(getContext()).inflate(R.layout.pop_currency_list_c2c, null);
        final RecyclerView currencyList = currencyPopContent.findViewById(R.id.rv_currency_list);
        currencyPopTab = currencyPopContent.findViewById(R.id.pop_tab);
        currencyList.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
        currencyList.setAdapter(currencyAdapter);
        int[] attr = new int[]{android.R.attr.listDivider};
        currencyList.addItemDecoration(new GridDivider(getContext(), GridDivider.DIRECTION_VERTICAL, 3, attr));

        currencyPopTab.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                currencyListData = AppUtils.allCurrencyBeanList.get(tab.getPosition()).getList();
                currencyAdapter.replaceData(currencyListData);
                currencyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });


        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getData();
                refreshlayout.finishRefresh();
            }
        });

        //价格改变监听
        ibPrice.setTextChangedListener(new InputBox.TextChangedListener()

        {
            @Override
            public void afterTextChanged(Editable editable) {


                String price = editable.toString();
                String ibCountText = ibCount.getText();
                if (TextUtils.isEmpty(price) || Double.parseDouble(price) == 0) return;

                //单价对应的人民币价格
                String cnyPrice = Arith.mul(price, String.valueOf(rate), 2);
                tvCnyPrice.setText("≈" + cnyPrice + "CNY");
                seekBar.setProgress(0);

                //数量为空时设置交易额为空
                if (TextUtils.isEmpty(ibCountText)) {
                    tvTransactionCount.setText(getString(R.string.a535, "---"));
                    return;
                }

                //交易额等于单价乘以数量
                String amount = Arith.mul(price, ibCountText, 8);
                tvTransactionCount.setText(getString(R.string.a535, amount + currencyName.split("/")[1]));
            }
        });


        //数量监听
        ibCount.setTextChangedListener(new InputBox.TextChangedListener()

        {
            @Override
            public void afterTextChanged(Editable editable) {

                String input = editable.toString();
                String price = ibPrice.getText();
                if (TextUtils.isEmpty(input) || availableUsd == null || TextUtils.isEmpty(price)) {
                    tvTransactionCount.setText(getString(R.string.a535, "---"));
                    return;
                }

                double inputCount = Double.parseDouble(input);
                if (isBuy) {
                    //可交易数量 = 可用USD / 价格
                    double availableCount = Double.parseDouble(Arith.div(availableUsd, price, 8));
                    availableCount = Arith.mul(availableCount, 0.99, 8);

                    //输入大于交易数量,最多输入可交易数量
                    if (inputCount > availableCount) {
                        ibCount.setText(new BigDecimal(availableCount).toPlainString());
                        String amount = Arith.mul(String.valueOf(availableCount), price, 8);
                        tvTransactionCount.setText(getString(R.string.a535, amount + currencyName.split("/")[1]));

                        Toast toast = Toast.makeText(getContext(),
                                getString(R.string.a544, currencyName.split("/")[1]),
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                } else {
                    //可交易数量 = 可用PAI
                    if (inputCount > Double.parseDouble(availablePai)) {

                        availablePai = Arith.mul(availablePai, "0.99", 8);

                        //输入大于交易数量,最多输入可交易数量
                        ibCount.setText(availablePai);
                        String amount = Arith.mul(availablePai, price, 8);
                        tvTransactionCount.setText(getString(R.string.a535, amount + currencyName.split("/")[1]));

                        Toast toast = Toast.makeText(getContext(),
                                getString(R.string.a544, currencyName.split("/")[0]),
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        return;
                    }

                }

                String amount = Arith.mul(input, price, 8);

                //TODO 增加输入数量时更改滚动条百分比,目前有冲突
                /*String percent = Arith.div(amount, availableUsd, 2);
                int p = (int) (Float.parseFloat(percent) * 100);
                seekBar.setProgress(p);*/

                tvTransactionCount.setText(getString(R.string.a535, amount + currencyName.split("/")[1]));

            }
        });

        //滑动条
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                String price = ibPrice.getText();
                if (TextUtils.isEmpty(price) || availableUsd == null || availablePai == null) {
                    return;
                }


                tvPercent.setText(progress + "%");
                String percent = String.valueOf(Arith.div(progress, 100, 2));

                String transactionCount;
                String quantity;

                if (isBuy) {
                    transactionCount = Arith.mul(availableUsd, percent, 8);//交易额
                    quantity = Arith.div(transactionCount, price, 8);//数量
                    quantity = Arith.mul(quantity, "0.99", 8);

                } else {
                    quantity = Arith.mul(availablePai, percent, 8);//数量
                    quantity = Arith.mul(quantity, "0.99", 8);

                    transactionCount = Arith.mul(quantity, price, 8);//交易额
                }

                //tvTransactionCount.setText(getString(R.string.a535, transactionCount + currencyName.split("/")[1]));
                if (Double.parseDouble(quantity) == 0) {
                    ibCount.setText("");
                } else {
                    ibCount.setText(quantity);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //委托 历史
        tabLayout2.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0://委托
                        //partBuy.setVisibility(View.GONE);
                        //partEntrust.setVisibility(View.VISIBLE);
                        title4.setText(getString(R.string.a215));
                        getEntrustList(ENTRUST);
                        break;
                    case 1://历史
                        //partBuy.setVisibility(View.GONE);
                        //partEntrust.setVisibility(View.VISIBLE);
                        title4.setText(getString(R.string.a433));
                        getEntrustList(HISTORY);
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


        //币种选择监听
        currencyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                currencyPopu.dismiss();
                AllCurrencyBean.ObjBean.ListBean bean = AppUtils.allCurrencyBeanList.get(currencyPopTab.
                        getSelectedTabPosition()).getList().get(position);
                setCurrency(bean);
            }
        });

        //买单列表
        rvBuy.setLayoutManager(new

                LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvBuy.setAdapter(buyAdapter);
        buyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()

        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String price = String.valueOf(mBuyListData.get(position).getPrice());
                ibPrice.setText(price);
                //ibPrice.getEditText().setSelection(price.length());

            }
        });

        //卖单列表
        rvSell.setLayoutManager(new

                LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvSell.setAdapter(sellAdapter);
        sellAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()

        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String price = String.valueOf(mSellListData.get(position).getPrice());
                ibPrice.setText(price);
                //ibPrice.getEditText().setSelection(price.length());

            }
        });

        //委托列表
        rvEntrust.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvEntrust.setAdapter(entrustAdapter);
        entrustAdapter.bindToRecyclerView(rvEntrust);
        entrustAdapter.setEmptyView(R.layout.layout_empty);
        entrustAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()

        {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                String flag = entrustAdapter.getFlag();
                if (ENTRUST.equals(flag)) {
                    showCancelEntrustTips(position);
                }


            }
        });

        //初始化买卖Title
       /* String[] titles = {getString(R.string.a39), getString(R.string.a280)};
        for (String title : titles) {
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }*/


        //更改Indicater长度
       /* tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Field mTabStripField = tabLayout.getClass().getDeclaredField("mTabStrip");
                    mTabStripField.setAccessible(true);

                    LinearLayout mTabStrip = (LinearLayout) mTabStripField.get(tabLayout);

                    int dp10 = 40;

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });*/


    }


    //取消委托Dialog
    private void showCancelEntrustTips(final int position) {
        final AlertDialog.Builder customizeDialog = new AlertDialog.Builder(getContext());
        final View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_cancel_entrust, null);
        final TextView cancel = dialogView.findViewById(R.id.cancel);
        final TextView enter = dialogView.findViewById(R.id.enter);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelEntrustDialog.dismiss();
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelEntrust(position);
                cancelEntrustDialog.dismiss();
            }
        });

        TextView title = new TextView(getContext());
        title.setText("");
        customizeDialog.setCustomTitle(title);
        customizeDialog.setView(dialogView);

        customizeDialog.setPositiveButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        cancelEntrustDialog = customizeDialog.show();
    }

    //充值Dialog
    private void showRechargeTips(final String currency) {
        final AlertDialog.Builder customizeDialog = new AlertDialog.Builder(getContext());
        final View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_cancel_entrust, null);
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
                Intent intent;
                switch (currency) {
                    case "π":
                        intent = new Intent(getActivity(), PaiRechargeActivity.class);
                        break;
                    case "usd":
                        intent = new Intent(getActivity(), USDRechargeActivity.class);
                        break;
                    default:
                        intent = new Intent(getActivity(), BTCRechargeActivity.class);
                        intent.putExtra("currency", currency);
                }
                startActivity(intent);
                rechargeDialog.dismiss();
            }
        });

        TextView title = new TextView(getContext());
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

    //登录Dialog
    private void showLoginTips() {
        final AlertDialog.Builder customizeDialog = new AlertDialog.Builder(getContext());
        final View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_cancel_entrust, null);
        final TextView cancel = dialogView.findViewById(R.id.cancel);
        final TextView enter = dialogView.findViewById(R.id.enter);
        final TextView content = dialogView.findViewById(R.id.content);

        content.setText(getString(R.string.a424));
        enter.setText(getString(R.string.a425));
        enter.setTextColor(getResources().getColor(R.color.textColor2));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginTipsDialog.dismiss();
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                loginTipsDialog.dismiss();
            }
        });

        TextView title = new TextView(getContext());
        title.setText("");
        customizeDialog.setCustomTitle(title);
        customizeDialog.setView(dialogView);

        customizeDialog.setPositiveButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        loginTipsDialog = customizeDialog.show();
    }

    //获取手续费
    private void getFee(final String url, final Map<String, String> param1, String count, String price) {
        showLoadingDialog(getContext());
        Map<String, String> param = new LinkedHashMap<>();
        param.put("userid", AppUtils.getUserId());
        param.put("account", count);
        param.put("price", price);
        param.put("ctid", String.valueOf(ctid));


        NetUtils.get(Url.getHallFee, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, " getFee onError: " + e.toString());
                showToast(getString(R.string.a537));
                dismissDialog();
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getFee onResponse: " + response);
                dismissDialog();
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        FeeBean feeBean = AppUtils.getGson().fromJson(response, FeeBean.class);

                        String fee = feeBean.getObj().getFee();
                        String discount = feeBean.getObj().getDiscount();

                        showPopu(url, param1, fee, discount);

                    } else {
                        showToast(obj.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    //交易时的弹窗
    private void showPopu(final String url, final Map<String, String> param, final String fee, String discount) {
        final View content = LayoutInflater.from(getContext()).inflate(R.layout.pop_transaction, null);
        tradesPopu = new PopupWindow(content, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);

        ImageView close = content.findViewById(R.id.iv_close);
        TextView openMember = content.findViewById(R.id.tv_open_member);
        TextView vip = content.findViewById(R.id.tv_vip);
        final TextView tvContent = content.findViewById(R.id.tv_content);
        final TextView tvNext = content.findViewById(R.id.tv_next);
        int vipstatus = AppUtils.getUser().getVipstatus();
        if (vipstatus == Constants.MEMBER_VIP) {
            String str = getString(R.string.a429, "VIP", 8);
            switch (discount) {
                case "8":
                    str = getString(R.string.a429, "VIP", 8);
                    break;
                case "6":
                    str = getString(R.string.a429, "VIP1", 6);
                    break;
                case "4":
                    str = getString(R.string.a429, "VIP2", 4);
                    break;
                case "2":
                    str = getString(R.string.a429, "VIP3", 2);
                    break;
            }
            vip.setText(str);
            openMember.setText("");
        } else if (vipstatus == Constants.MEMBER_ORDINARY) {
            vip.setText(getString(R.string.a245, 2));
            openMember.setText(getString(R.string.a246));
        } else if (vipstatus == Constants.MEMBER_SPECIAL_VIP) {
            vip.setText(getString(R.string.a381));
            openMember.setText("");
        }

        String currency = currencyName.split("/")[1];
        tvContent.setText(getString(R.string.a244, fee, " " + currency));
        final TextPaint paint = tvContent.getPaint();
        paint.setFakeBoldText(true);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tradesPopu.dismiss();
            }
        });
        openMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MemberActivity.class);
                startActivity(intent);
                tradesPopu.dismiss();
            }
        });
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPayPassWordDialog(url, param);
                tradesPopu.dismiss();
            }
        });

        tradesPopu.setFocusable(true);
        tradesPopu.setOutsideTouchable(true);
        tradesPopu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams attributes = getActivity().getWindow().getAttributes();
                attributes.alpha = 1f;
                getActivity().getWindow().setAttributes(attributes);
            }
        });
        tradesPopu.showAtLocation(mContext.getNavigationBottom(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        WindowManager.LayoutParams attributes = getActivity().getWindow().getAttributes();
        attributes.alpha = 0.5f;
        getActivity().getWindow().setAttributes(attributes);

    }


    private void cancelEntrust(int position) {

        if (mListDataEntrust != null) {
            EntrustBean.ObjBean order = mListDataEntrust.get(position);

            Map<String, String> param = new LinkedHashMap<>();
            param.put("orderid", order.getId());
            param.put("type", String.valueOf(order.getType()));
            param.put("ctid", String.valueOf(ctid));
            param.put("userid",AppUtils.getUserId());

            showLoadingDialog(getContext());
            NetUtils.put(Url.cancelTrust, param, new NetUtils.StringCallBack() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    LogUtils.d(TAG, "onError: " + e.toString());
                    showToast(getString(R.string.a537));
                    dismissDialog();
                }

                @Override
                public void onResponse(String response, int id) {
                    LogUtils.d(TAG, "onResponse: " + response);
                    dismissDialog();
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getInt("status") == 200) {
                            getEntrustList(ENTRUST);
                            getEntrust(BUY);
                            getEntrust(SELL);
                            showToast(obj.getString("msg"));

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


    //点击事件
    @OnClick({R.id.k_line_map, R.id.bt_buy, R.id.ll_currency})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_currency:
                showCurrencyPop();
                break;
            case R.id.k_line_map://K线图
                Intent intent = new Intent(getContext(), KMapActivity.class);
                intent.putExtra("currencyName", currency.getText().toString());
                startActivity(intent);
                break;
            case R.id.bt_buy://买卖按钮
                if (AppUtils.checkLogin(getActivity())) return;
                String price = ibPrice.getText();
                String count = ibCount.getText();
                String currency1 = currencyName.split("/")[0];
                String currency2 = currencyName.split("/")[1];
                String url = Url.hallTransfer;

                if (TextUtils.isEmpty(price)) {
                    showToast(getString(R.string.a282));
                    return;
                } else if (TextUtils.isEmpty(count)) {
                    showToast(getString(R.string.a283));
                    return;
                } else if (Double.parseDouble(price) == 0 || Double.parseDouble(count) == 0) {
                    showToast(getString(R.string.a524));
                    return;
                } else if (availableUsd == null || availablePai == null) {
                    showToast(getString(R.string.a421));
                    return;
                }


                Map<String, String> param = new LinkedHashMap<>();
                param.put("userid", AppUtils.getUserId());
                param.put("account", count);
                param.put("price", price);
                param.put("ctid", String.valueOf(ctid));
                if (isBuy) {//买单
                    //判断2资金不足
                    if (Double.parseDouble(Arith.mul(count, price, 2)) > Double.parseDouble(availableUsd)) {
                        showRechargeTips(currency2);
                        return;
                    }

                    param.put("type", "2");
                    transaction(url, param);
                } else {
                    //判断1资金不足
                    if (Double.parseDouble(count) > Double.parseDouble(availablePai)) {
                        showRechargeTips(currency1);
                        return;
                    }

                    String paypassword = AppUtils.getUser().getPaypassword();
                    if ("".equals(paypassword) || paypassword == null) {
                        Intent payPwdIntent = new Intent(getContext(), PayPassWordActivity.class);
                        startActivity(payPwdIntent);
                        return;
                    }
                    param.put("type", "1");
                    getFee(url, param, count, price);
                }


                break;
        }

    }

    private void showCurrencyPop() {
        currencyPopu = new PopupWindow(currencyPopContent,
                ViewGroup.LayoutParams.MATCH_PARENT, AppUtils.dip2px(getContext(),
                200), true);

        currencyPopu.setFocusable(true);
        currencyPopu.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));

        if (currencyPopu.isShowing()) {
            currencyPopu.dismiss();
        } else {
            currencyPopu.showAsDropDown(rl_TopTitle);
        }

    }

    //支付密码Dialog
    private void showPayPassWordDialog(final String url, final Map<String, String> param) {
        AlertDialog.Builder customizeDialog = new AlertDialog.Builder(getContext());
        final View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_input_pwd, null);
        final PswInputView inputView = dialogView.findViewById(R.id.piv);
        inputView.setInputCallBack(new PswInputView.InputCallBack() {
            @Override
            public void onInputFinish(String result) {
                checkFundsPwd(result, url, param);
                inputView.hideSoftInput();
            }

            @Override
            public void onInputChange(String result) {
            }
        });
        TextView title = new TextView(getContext());
        title.setGravity(Gravity.CENTER_HORIZONTAL);
        title.setPadding(30, 30, 30, 30);
        title.setText(getString(R.string.a328));
        title.setTextColor(getResources().getColor(R.color.f000000));
        title.setTextSize(24);
        customizeDialog.setCustomTitle(title);
        customizeDialog.setView(dialogView);


        payPassWordDialog = customizeDialog.show();


    }

    //检查支付密码
    private void checkFundsPwd(String pwd, final String url, final Map<String, String> param1) {

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

                        transaction(url, param1);
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

    //大盘交易 买入卖出
    private void transaction(String url, Map<String, String> param) {

        showLoadingDialog(getContext());

        NetUtils.postString(url, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "transaction onError: " + e.toString());
                showToast(getString(R.string.a537));
                dismissDialog();
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "transaction onResponse: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        showToast(getString(R.string.a431));

                        getData();

                        ibPrice.clearText();
                        ibCount.clearText();
                        tvTransactionCount.setText("---");
                        dismissDialog();
                    } else {
                        showToast(obj.getString("msg"));
                        dismissDialog();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    //可用 冻结 资产
    private void getAvailableCount(final String type) {
        String userId = AppUtils.getUserId();
        if (TextUtils.isEmpty(userId)) return;

        Map<String, String> param = new LinkedHashMap<>();
        param.put("userId", userId);
        param.put("type", type);
        param.put("ctid", String.valueOf(ctid));

        LogUtils.d(TAG, "getAvailableCount: param==" + param.toString());

        final String url = Url.availableFreezeCont;
        NetUtils.get(url, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, currencyName + " getAvailableCount onError " + e.toString());
                showToast(getString(R.string.a537));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, currencyName + " getAvailableCount onResponse: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        JSONObject obj1 = obj.getJSONObject("obj");
                        if (type.equals(SECOND)) {
                            availableUsd = obj1.getString("account");
                        } else {
                            availablePai = obj1.getString("account");
                            freezePai = obj1.getString("unaccount");
                        }

                        if (isBuy) {
                            tvAvailable.setText(getString(R.string.a46) + " " + availableUsd + currencyName.split("/")[1]);
                        } else {
                            tvAvailable.setText(getString(R.string.a46) + " " + availablePai + currencyName.split("/")[0]);

                        }


                    } else {
                        showToast(obj.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    /**
     * onDetach中注销接口
     */
    @Override
    public void onDetach() {
        super.onDetach();
    }


    //交易委托 和 历史
    private void getEntrustList(final String type) {
        String url;
        if (ENTRUST.equals(type)) {
            url = Url.myTrust;
        } else {
            url = Url.myHistory;
        }

        String userId = AppUtils.getUserId();

        if (TextUtils.isEmpty(userId)) {
            showLoginTips();
            return;
        }

        Map<String, String> param = new LinkedHashMap<>();
        param.put("userid", userId);
        param.put("ctid", String.valueOf(ctid));


        NetUtils.get(url, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getEntrustList onError: " + e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getEntrustList onResponse: " + response);
                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getInt("status") == 200) {
                        EntrustBean bean = AppUtils.getGson().fromJson(response, EntrustBean.class);

                        if (type.equals(ENTRUST) && tabLayout2.getTabAt(0).isSelected()) {
                            mListDataEntrust = bean.getObj();
                            entrustAdapter.setFlag(ENTRUST);

                            entrustAdapter.replaceData(mListDataEntrust);


                        } else {
                            mListDataHistory = bean.getObj();
                            entrustAdapter.setFlag(HISTORY);
                            entrustAdapter.replaceData(mListDataHistory);

                        }

                        entrustAdapter.notifyDataSetChanged();

                    } else {
                        showToast(obj.getString("msg"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }


    private void setPrice(double price) {
        if (rate != 0 && topPrice != null && cnyPrice != null) {
            topPrice.setText(String.valueOf(price));
            cnyPrice.setText("≈￥" + String.valueOf(Arith.mul(price, rate, 2)));
        }

    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    //===============================================================================================================


    /*private void getAllCurrency() {
        Map<String, String> param = new LinkedHashMap<>();
        param.put("type", String.valueOf(Constants.CURRENCY_TYPE_C2C));

        NetUtils.get(Url.ctData, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getAllCurrency onError: " + e.toString());

                //从缓存获取
                SharedPreferences sp = getContext().getSharedPreferences("ctid", Context.MODE_PRIVATE);
                String key = getKey(Constants.CURRENCY_TYPE_C2C);
                String str = sp.getString(key, null);
                if (str != null) {
                    AllCurrencyBean currencyBean = AppUtils.getGson().fromJson(str, AllCurrencyBean.class);
                    allCurrencyBeanList = currencyBean.getObj();
                    if (allCurrencyBeanList.size() > 0)
                        setCurrency(allCurrencyBeanList.get(0).getList().get(0));

                    for (int i = 0; i < allCurrencyBeanList.size(); i++) {
                        String symbol = allCurrencyBeanList.get(i).getSymbol();
                        currencyPopTab.addTab(currencyPopTab.newTab().setText(symbol));
                    }


                }

            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getAllCurrency onResponse: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String str;
                    if (obj.getInt("status") == 200) {
                        //缓存一下
                        SharedPreferences sp = getContext().getSharedPreferences("ctid", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString(getKey(Constants.CURRENCY_TYPE_C2C), response);
                        edit.apply();
                        str = response;
                    } else {
                        //从缓存获取
                        SharedPreferences sp = getContext().getSharedPreferences("ctid", Context.MODE_PRIVATE);
                        str = sp.getString(getKey(Constants.CURRENCY_TYPE_C2C), null);
                    }

                    AllCurrencyBean currencyBean = AppUtils.getGson().fromJson(str, AllCurrencyBean.class);
                    allCurrencyBeanList = currencyBean.getObj();

                    if (allCurrencyBeanList.size() > 0)
                        setCurrency(allCurrencyBeanList.get(0).getList().get(0));

                    for (int i = 0; i < allCurrencyBeanList.size(); i++) {
                        String symbol = allCurrencyBeanList.get(i).getSymbol();
                        currencyPopTab.addTab(currencyPopTab.newTab().setText(symbol));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }*/


}
