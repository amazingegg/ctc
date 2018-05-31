package com.atkj.ctc.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidkun.xtablayout.XTabLayout;
import com.atkj.ctc.MainActivity;
import com.atkj.ctc.R;
import com.atkj.ctc.activity.BuyActivity;
import com.atkj.ctc.activity.CTSPurchaseActivity;
import com.atkj.ctc.activity.CurrencyDetailActivity;
import com.atkj.ctc.activity.CustomerServiceActivity;
import com.atkj.ctc.activity.MyBonusActivity;
import com.atkj.ctc.activity.QuotationActivity;
import com.atkj.ctc.activity.ShareActivity;
import com.atkj.ctc.activity.WebViewActivity;
import com.atkj.ctc.adapter.CommonRecycleViewAdapter;
import com.atkj.ctc.adapter.HomeFragmentAdapter;
import com.atkj.ctc.adapter.HomeMarketAdapter;
import com.atkj.ctc.adapter.TransactionFragmentAdapter;
import com.atkj.ctc.bean.AllCurrencyBean;
import com.atkj.ctc.bean.BannerBean;
import com.atkj.ctc.bean.CurrencyBean;
import com.atkj.ctc.bean.MarketListBean;
import com.atkj.ctc.bean.NoticeBean;
import com.atkj.ctc.bean.SellBean;
import com.atkj.ctc.bean.SellBean2;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.GlideImageLoader;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.views.CustomLinearLayoutManager;
import com.atkj.ctc.views.GridDivider;
import com.atkj.ctc.views.VerticalTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.internal.Excluder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public class HomeFragment extends BaseFragment implements OnRefreshListener, OnBannerListener {

    private static final int TYEP_SELL = 1;
    private static final int TYEP_BUY = 2;
    private static final String TAG = HomeFragment.class.getSimpleName();
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.rv_buy)
    RecyclerView rvMarket;
    /*@BindView(R.id.rv_p2p_sell)
    RecyclerView rvP2PSell;*/
    @BindView(R.id.rv_p2p_buy)
    RecyclerView rvP2PBuy;
    @BindView(R.id.rv_p2p_sell)
    RecyclerView rvP2PSell;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLaout;
    @BindView(R.id.scroll_view)
    NestedScrollView scrollView;
    @BindView(R.id.ll_group)
    LinearLayout llGroup;
    @BindView(R.id.rl_notice)
    RelativeLayout notice;
    @BindView(R.id.tv_notice)
    VerticalTextView tvNotice;
    @BindView(R.id.tv_currency)
    TextView tvCurrency;
    @BindView(R.id.rg_group)
    RadioGroup radioGroup;

    @BindView(R.id.cb_buy_pai)
    RadioButton rbBuyPai;
    @BindView(R.id.cb_sell_pai)
    RadioButton rbSellPai;
    @BindView(R.id.tab_layout)
    XTabLayout currencyPopTab;

    @BindView(R.id.rv_buy_2)
    RecyclerView rvMarket2;

    // 这里的参数只是一个举例可以根据需求更改
    private String name;
    private MainActivity mContext;
    private Unbinder unbinder;

    private List<String> images = new ArrayList<>();
    private TransactionFragmentAdapter p2pBuyAdapter;

    private HomeMarketAdapter mMarketAdapter;
    private List<SellBean.ObjBean.ListBean> mBuyListData;
    private List<SellBean.ObjBean.ListBean> mSellListData;

    private List<MarketListBean> mMarketListData;
    private WebSocketClient client;
    private double rate;
    private List<BannerBean.ObjBean> bannerList;
    private List<NoticeBean.ObjBean> noticeList;
    private Timer timer;
    private MyTask timerTask;
    private PopupWindow currencyPopu;
    private int ctid;
    private String currency;
    //private List<AllCurrencyBean.ObjBean> allCurrencyBeanList;
    private ArrayList<MarketListBean> mMarketListData2;
    private HomeMarketAdapter mMarketAdapter2;
    private TransactionFragmentAdapter p2pSellAdapter;


    /**
     * 通过工厂方法来创建Fragment实例
     * 同时给Fragment来提供参数来使用
     *
     * @param param1 参数1.
     * @return Fragment的实例.
     */
    public static HomeFragment newInstance(String param1) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("name", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * onAttach中连接监听接口 确保Activity支持该接口
     */
    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mContext = (MainActivity) activity;
    }


    @Override
    public void onStart() {
        super.onStart();
        startTimer();
        initWSCline();
        banner.startAutoPlay();

    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d(TAG, "onStop: ");
        banner.stopAutoPlay();
        stopTimer();
        removeChannel();
        closeConn();
    }


    @Override
    public void onResume() {
        super.onResume();
        //LogUtils.d("HomeFragment--onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        //LogUtils.d("HomeFragment--onPause");

    }

    /**
     * onDetach中注销接口
     */
    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragement_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (getArguments() != null) {
            name = getArguments().getString("name");
        }

        LogUtils.d("initView");
        initEvent();

        return view;
    }

    private class MyTask extends TimerTask {
        @Override
        public void run() {
            if (rbBuyPai.isChecked()) {
                getSellListData();
            } else {
                getBuyListData();
            }
        }
    }

    private void startTimer() {
        timer = null;
        timerTask = null;

        if (timer == null) {
            timer = new Timer();
        }
        if (timerTask == null) {
            timerTask = new MyTask();
        }
        if (timer != null && timerTask != null)
            timer.schedule(timerTask, 0, 15000);
    }


    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    private void initEvent() {

        refreshLaout.setOnRefreshListener(this);
        banner.setOnBannerListener(this);
        banner.setDelayTime(4000);

        //公告
        tvNotice.setTextStillTime(5000);//设置停留时长间隔
        tvNotice.setAnimTime(300);//设置进入和退出的时间间隔
        tvNotice.setText(12, 5, getResources().getColor(R.color.textColor2));//设置属性
        tvNotice.startAutoScroll();
        tvNotice.setOnItemClickListener(new VerticalTextView.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                String path = noticeList.get(pos).getPath();


                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra(WebViewActivity.URL, path);
                startActivity(intent);
            }
        });


        currencyPopTab.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    rvMarket.setVisibility(View.VISIBLE);
                    rvMarket2.setVisibility(View.GONE);
                } else {
                    rvMarket.setVisibility(View.GONE);
                    rvMarket2.setVisibility(View.VISIBLE);

                    List<MarketListBean> list1 = new ArrayList<>();
                    List<AllCurrencyBean.ObjBean.ListBean> list = AppUtils.allCurrencyBeanList.get(tab.getPosition()).getList();
                    for (int i = 0; i < list.size(); i++) {
                        MarketListBean bean = new MarketListBean();
                        bean.setChannel(list.get(i).getSymbol());
                        list1.add(bean);
                    }

                    mMarketAdapter2.replaceData(list1);
                    mMarketAdapter2.notifyDataSetChanged();
                }


            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {


            }
        });

        //动态设置图标大小
        int[] drawables = {R.drawable.jiangjin1, R.drawable.xinshou, R.drawable.kefu, R.drawable.fenxiang};
        for (int i = 0; i < 4; i++) {
            Drawable drawable = getResources().getDrawable(drawables[i]);
            drawable.setBounds(0, 0, AppUtils.dip2px(getContext(), 30),
                    AppUtils.dip2px(getContext(), 30));

            TextView childAt = (TextView) llGroup.getChildAt(i);
            childAt.setCompoundDrawables(null, drawable, null, null);
        }


        //大盘交易
        rvMarket.setLayoutManager(new CustomLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mMarketListData = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            mMarketListData.add(new MarketListBean());
        }
        mMarketAdapter = new HomeMarketAdapter(R.layout.list_item_market, mMarketListData);
        mMarketAdapter.bindToRecyclerView(rvMarket);
        mMarketAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                TextView currency = (TextView) mMarketAdapter.getViewByPosition(position, R.id.tv_currency);
                TextView currency2 = (TextView) mMarketAdapter.getViewByPosition(position, R.id.tv_currency2);
                String str = currency.getText().toString() + currency2.getText().toString();
                List<AllCurrencyBean.ObjBean.ListBean> list = AppUtils.allCurrencyBeanList.get(0).getList();
                for (int i = 0; i < list.size(); i++) {
                    AllCurrencyBean.ObjBean.ListBean bean = list.get(i);
                    if (str.equals(bean.getSymbol())) {
                        CurrencyDetailActivity.actionStart(mContext, bean, 3);
                    }
                }

            }
        });
        rvMarket.setAdapter(mMarketAdapter);


        //大盘交易2
        rvMarket2.setLayoutManager(new CustomLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mMarketListData2 = new ArrayList<>();

        mMarketAdapter2 = new HomeMarketAdapter(R.layout.list_item_market, mMarketListData2);
        mMarketAdapter2.bindToRecyclerView(rvMarket2);
        rvMarket2.setAdapter(mMarketAdapter2);
        mMarketAdapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                AllCurrencyBean.ObjBean.ListBean bean = AppUtils.allCurrencyBeanList
                        .get(currencyPopTab.getSelectedTabPosition())
                        .getList()
                        .get(position);
                CurrencyDetailActivity.actionStart(mContext, bean, 3);
            }
        });

        //点对点交易
        rbBuyPai.setChecked(true);
        rbBuyPai.getPaint().setFakeBoldText(true);
        //点对点交易
        rvP2PBuy.setLayoutManager(new CustomLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        p2pBuyAdapter = new TransactionFragmentAdapter(R.layout.list_item_transaction_fragement, mBuyListData);
        p2pBuyAdapter.bindToRecyclerView(rvP2PBuy);
        p2pBuyAdapter.setEmptyView(R.layout.layout_empty);
        p2pBuyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (AppUtils.checkLogin(getActivity())) return;

                boolean isBuy = p2pBuyAdapter.isBuy();
                String orderid = mBuyListData.get(position).getOrderid();

                BuyActivity.actionStart(getContext()
                        , isBuy
                        , orderid
                        , String.valueOf(ctid)
                        , currency);
            }
        });
        rvP2PBuy.setAdapter(p2pBuyAdapter);

        //点对点交易
        rvP2PSell.setLayoutManager(new CustomLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        p2pSellAdapter = new TransactionFragmentAdapter(R.layout.list_item_transaction_fragement, mSellListData);
        p2pSellAdapter.bindToRecyclerView(rvP2PSell);
        p2pSellAdapter.setEmptyView(R.layout.layout_empty);
        p2pSellAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (AppUtils.checkLogin(getActivity())) return;

                boolean isBuy = p2pSellAdapter.isBuy();
                String orderid = mSellListData.get(position).getOrderid();

                BuyActivity.actionStart(getContext()
                        , isBuy
                        , orderid
                        , String.valueOf(ctid)
                        , currency);
            }
        });
        rvP2PSell.setAdapter(p2pSellAdapter);


        //币种选择监听
        currencyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                currencyPopu.dismiss();
                CurrencyBean.ObjBean bean = p2pCurrencyList.get(position);
                tvCurrency.setText(bean.getSymbol());
                ctid = bean.getCtid();
                currency = bean.getSymbol();

                if (rbBuyPai.isChecked()) {
                    getSellListData();
                } else {
                    getBuyListData();
                }

                for (int i = 0; i < p2pCurrencyList.size(); i++) {
                    CurrencyBean.ObjBean bean1 = p2pCurrencyList.get(i);
                    if (i == position) {
                        bean1.setSelect(true);
                    } else {
                        bean1.setSelect(false);
                    }

                }
                currencyAdapter.notifyDataSetChanged();
            }
        });

        //买卖按钮
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                TextPaint paint = rbBuyPai.getPaint();
                TextPaint paint1 = rbSellPai.getPaint();
                switch (checkedId) {
                    case R.id.cb_buy_pai:

                        paint.setFakeBoldText(true);
                        paint1.setFakeBoldText(false);

                        rvP2PBuy.setVisibility(View.GONE);
                        rvP2PSell.setVisibility(View.VISIBLE);


                        getSellListData();

                        break;
                    case R.id.cb_sell_pai:
                        paint.setFakeBoldText(false);
                        paint1.setFakeBoldText(true);

                        rvP2PBuy.setVisibility(View.VISIBLE);
                        rvP2PSell.setVisibility(View.GONE);

                        getBuyListData();

                        break;
                }
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


    }

    @OnClick({R.id.tv_my_bonus, R.id.tv_new_teach, R.id.tv_cus_service,
            R.id.tv_shear, /*R.id.tv_market_more,*/ R.id.rl_notice, R.id.ll_currency,R.id.more})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_my_bonus://我的资产
                if (AppUtils.checkLogin(getActivity())) return;

                intent = new Intent(getActivity(), MyBonusActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_new_teach://新手教程
                intent = new Intent(getActivity(), WebViewActivity.class);

                String url;
                SharedPreferences sp = getContext().getSharedPreferences(Constants.LANGUAGE, Context.MODE_PRIVATE);
                String country = sp.getString("COUNTRY", "");
                switch (country) {
                    case "zh":
                        url = Url.tutorial;
                        break;
                    case "en":
                        url = Url.tutorial_en;
                        break;
                    default:
                        url = Url.tutorial;
                }
                intent.putExtra("url", url);
                startActivity(intent);
                break;
            case R.id.tv_cus_service://联系客服
                intent = new Intent(getActivity(), CustomerServiceActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_shear://分享有奖
                if (AppUtils.checkLogin(getActivity())) return;

                intent = new Intent(getActivity(), ShareActivity.class);
                startActivity(intent);
                break;
            /*case R.id.tv_market_more://大盘交易 更多
                intent = new Intent(getActivity(), QuotationActivity.class);

                startActivity(intent);
                break;*/
            case R.id.rl_notice:


                break;
            case R.id.ll_currency:
                showCurrencyPop();
                break;
            case R.id.more:
                mContext.check(1,R.id.rb_p2p);
                break;

        }

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden){
            initData();
        }
    }

    private List<CurrencyBean.ObjBean> p2pCurrencyList;
    CommonRecycleViewAdapter currencyAdapter = new CommonRecycleViewAdapter<CurrencyBean.ObjBean>
            (R.layout.list_item_currencylist, p2pCurrencyList) {
        @Override
        protected void convert(BaseViewHolder helper, CurrencyBean.ObjBean item) {
            boolean itemSelect = item.isSelect();
            int colorSelect = getResources().getColor(R.color.textColor2);
            int colorNormal = getResources().getColor(R.color.f484848);

            TextView content = helper.getView(R.id.tv_content);
            content.setText(item.getSymbol());
            content.setTextColor(itemSelect ? colorSelect : colorNormal);


        }
    };

    private void showCurrencyPop() {

        //初始化POPU
        View content = LayoutInflater.from(getContext()).inflate(R.layout.pop_currency_list, null);
        currencyPopu = new PopupWindow(content, ViewGroup.LayoutParams.MATCH_PARENT, AppUtils.dip2px(getContext(), 200), true);
        RecyclerView currencyList = content.findViewById(R.id.rv_currency_list);
        currencyList.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
        currencyList.setAdapter(currencyAdapter);
        int[] attr = new int[]{android.R.attr.listDivider};
        currencyList.addItemDecoration(new GridDivider(getContext(), GridDivider.DIRECTION_VERTICAL, 3, attr));

        currencyPopu.setFocusable(true);
        currencyPopu.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));

        if (currencyPopu.isShowing()) {
            currencyPopu.dismiss();
        } else {
            currencyPopu.showAsDropDown(radioGroup);
        }

    }

    @Override
    public void initData() {
        getAllCurrency();
        getCurrencyType(Constants.CURRENCY_P2P);

        getBannerImage();
        getExchangeRate();
        getNotice();




    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    public void getSellListData() {
        Map<String, String> param = new LinkedHashMap<>();
        param.put("pageSize", "10");
        param.put("pageNum", "1");
        param.put("ctid", String.valueOf(ctid));

        NetUtils.get(Url.sellList, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getSellListData onError: " + e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getSellListData onResponse: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        //if (!rbSellPai.isChecked()) return;

                        SellBean bean = AppUtils.getGson().fromJson(response, SellBean.class);

                        if (bean.getObj().getList().size() > 10) {
                            mSellListData = bean.getObj().getList().subList(0, 10);
                        } else {
                            mSellListData = bean.getObj().getList();
                        }

                        p2pSellAdapter.replaceData(mSellListData);
                        p2pSellAdapter.setBuy(true);
                        p2pSellAdapter.notifyDataSetChanged();
                    } else {
                        showToast(obj.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void getBuyListData() {
        Map<String, String> param = new LinkedHashMap<>();
        //TODO 分页显示 下拉加载更多
        param.put("pageSize", "10");
        param.put("pageNum", "1");
        param.put("ctid", String.valueOf(ctid));

        NetUtils.get(Url.buyList, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getBuyListData onError: " + e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getBuyListData onResponse: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        //if (!rbBuyPai.isChecked()) return;

                        SellBean bean = AppUtils.getGson().fromJson(response, SellBean.class);

                        if (bean.getObj().getList().size() > 10) {
                            mBuyListData = bean.getObj().getList().subList(0, 10);
                        } else {
                            mBuyListData = bean.getObj().getList();
                        }

                        p2pBuyAdapter.replaceData(mBuyListData);
                        p2pBuyAdapter.setBuy(false);
                        p2pBuyAdapter.notifyDataSetChanged();

                    } else {
                        showToast(obj.getString("msg"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private String getJson(int type) {
        //从缓存获取
        SharedPreferences sp = getContext().getSharedPreferences("ctid", Context.MODE_PRIVATE);
        return sp.getString(getKey(type), null);
    }

    private String getKey(int type) {
        String key;
        switch (type) {
            case 1:
                key = "ctid_market_json";
                break;
            case 4:
                key = "ctid_c2c_json";
                break;
            case 3:
                key = "ctid_p2p_json";
                break;
            default:
                key = "";
                break;
        }
        return key;
    }

    private void parsJson(String str) {
        if (str != null) {
            CurrencyBean currencyBean = AppUtils.getGson().fromJson(str, CurrencyBean.class);
            p2pCurrencyList = currencyBean.getObj();

            if (p2pCurrencyList.size() > 0) {
                CurrencyBean.ObjBean bean = p2pCurrencyList.get(0);
                bean.setSelect(true);
                ctid = bean.getCtid();
                currency = bean.getSymbol();

                getSellListData();

                tvCurrency.setText(bean.getSymbol());
                currencyAdapter.replaceData(p2pCurrencyList);
                currencyAdapter.notifyDataSetChanged();
            }
        }
    }

    private void getCurrencyType(final int type) {
        //1 大盘交易title 2 币币交易title
        Map<String, String> param = new LinkedHashMap<>();
        param.put("type", String.valueOf(type));

        NetUtils.get(Url.ctData, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getCurrencyType onError: " + e.toString());

                String str = getJson(type);
                parsJson(str);

                if (rbBuyPai.isChecked()){
                    getSellListData();
                }else {
                    getBuyListData();
                }

            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getCurrencyType onResponse: " + response);
                try {
                    JSONObject obj = new JSONObject(response);

                    String str;
                    if (obj.getInt("status") == 200) {
                        //缓存一下
                        SharedPreferences sp = getContext().getSharedPreferences("ctid", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString(getKey(type), response);
                        edit.apply();
                        str = response;
                    } else {
                        //从缓存获取
                        str = getJson(type);
                    }

                    parsJson(str);

                    if (rbBuyPai.isChecked()){
                        getSellListData();
                    }else {
                        getBuyListData();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private void getNotice() {
        Map<String, String> param = new LinkedHashMap<>();

        NetUtils.get(Url.noticeList, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getNotice onError: " + e.toString());
                showToast(getString(R.string.a537));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getNotice onResponse: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        NoticeBean noticeBean = AppUtils.getGson().fromJson(response, NoticeBean.class);
                        noticeList = noticeBean.getObj();

                        ArrayList<String> list = new ArrayList<>();

                        for (int i = 0; i < noticeList.size(); i++) {
                            list.add(noticeList.get(i).getContent());
                        }
                        tvNotice.setTextList(list);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public void initWSCline() {
        client = new WebSocketClient(URI.create(Url.wsMarket)) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                LogUtils.d("onOpen==========");
                client.send("[{'event':'addChannel','channel':'ok_sub_spot_btc_usd_ticker'}," +
                        "{'event':'addChannel','channel':'ok_sub_spot_ltc_usd_ticker'}," +

                        "{'event':'addChannel','channel':'ok_sub_spot_eth_usd_ticker'}]");
                LogUtils.d("onOpen", handshakedata.getHttpStatus() + "");
                LogUtils.d("onOpen", handshakedata.getHttpStatusMessage());
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

                //LogUtils.w("message=="+message);
                EventBus.getDefault().post(bean);


                double last = bean.getData().getLast();
                switch (channel[3]) {
                    case "btc":
                        //LogUtils.w(message);
                        mMarketListData.set(0, bean);
                        AppUtils.lastPrice.set(0,last );
                        break;
                    case "ltc":
                        mMarketListData.set(1, bean);
                        AppUtils.lastPrice.set(1, last);
                        break;
                    case "eth":
                        mMarketListData.set(2, bean);
                        AppUtils.lastPrice.set(2, last);
                        break;
                   /* case "etc":
                        mMarketListData.set(3, bean);
                        AppUtils.lastPrice.set(3, bean.getData().getLast());
                        break;
                    case "bcc":
                        mMarketListData.set(4, bean);
                        AppUtils.lastPrice.set(4, bean.getData().getLast());
                        break;*/
                }

                getActivity().runOnUiThread(new Runnable() {
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
        client.connect();
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

    //取消接收推送
    public void removeChannel() {

        if (client != null) {
            if (client.isConnecting()) {
                client.send("[{'event':'removeChannel','channel':'ok_sub_spot_btc_usd_ticker'}," +
                        "{'event':'removeChannel','channel':'ok_sub_spot_ltc_usd_ticker'}," +
                        "{'event':'removeChannel','channel':'ok_sub_spot_eth_usd_ticker'}," +
                        "{'event':'removeChannel','channel':'ok_sub_spot_etc_usd_ticker'}," +
                        "{'event':'removeChannel','channel':'ok_sub_spot_bcc_usd_ticker'}]");
            }
        }

    }

    private void getBannerImage() {
        Map<String, String> param = new LinkedHashMap<>();
        NetUtils.get(Url.getIndexImg, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getBannerImage onError: " + e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getBannerImage onResponse: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        BannerBean bannerBean = AppUtils.getGson().fromJson(response, BannerBean.class);
                        bannerList = bannerBean.getObj();
                        images.clear();
                        for (int i = 0; i < bannerList.size(); i++) {
                            images.add(bannerList.get(i).getUrl());
                        }
                        banner.setImages(images).setImageLoader(new GlideImageLoader()).start();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void getAllCurrency() {
        Map<String, String> param = new LinkedHashMap<>();
        param.put("type", String.valueOf(Constants.CURRENCY_TYPE_C2C));

        NetUtils.get(Url.ctData, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getAllCurrency onError: " + e.toString());

                //从缓存获取
                SharedPreferences sp = mContext.getSharedPreferences("ctid", Context.MODE_PRIVATE);
                String key = getKey(Constants.CURRENCY_TYPE_C2C);
                String str = sp.getString(key, null);
                if (str != null) {
                    AllCurrencyBean currencyBean = AppUtils.getGson().fromJson(str, AllCurrencyBean.class);
                    AppUtils.allCurrencyBeanList = currencyBean.getObj();

                    //默认第一个选中
                    AppUtils.ctid = AppUtils.allCurrencyBeanList.get(0).getList().get(0).getCtid();

                    currencyPopTab.removeAllTabs();

                    for (int i = 0; i < AppUtils.allCurrencyBeanList.size(); i++) {
                        String symbol = AppUtils.allCurrencyBeanList.get(i).getSymbol();
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
                        SharedPreferences sp = mContext.getSharedPreferences("ctid", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString(getKey(Constants.CURRENCY_TYPE_C2C), response);
                        edit.apply();
                        str = response;
                    } else {
                        //从缓存获取
                        SharedPreferences sp = mContext.getSharedPreferences("ctid", Context.MODE_PRIVATE);
                        str = sp.getString(getKey(Constants.CURRENCY_TYPE_C2C), null);
                    }

                    AllCurrencyBean currencyBean = AppUtils.getGson().fromJson(str, AllCurrencyBean.class);
                    AppUtils.allCurrencyBeanList = currencyBean.getObj();
                    //默认第一个选中
                    AppUtils.ctid = AppUtils.allCurrencyBeanList.get(0).getList().get(0).getCtid();

                    currencyPopTab.removeAllTabs();

                    for (int i = 0; i < AppUtils.allCurrencyBeanList.size(); i++) {
                        String symbol = AppUtils.allCurrencyBeanList.get(i).getSymbol();
                        currencyPopTab.addTab(currencyPopTab.newTab().setText(symbol));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void getExchangeRate() {
        Map<String, String> param = new LinkedHashMap<>();
        param.put("content", "USDCNH");
        param.put("type", "0");

        NetUtils.get(Url.getRate, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getExchangeRate onError: " + e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getExchangeRate onResponse=" + response);
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
    public void onRefresh(RefreshLayout refreshlayout) {
        initData();

        if (client != null) {
            removeChannel();
            closeConn();
            initWSCline();
        } else {
            initWSCline();
        }

        refreshlayout.finishRefresh();
    }

    @Override
    public void OnBannerClick(int position) {

        String path = bannerList.get(position).getPath();
        if ("cts".equals(path)) {
            CTSPurchaseActivity.actionStart(getContext());
        } else {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("url", path);
            startActivity(intent);
        }


    }
}
