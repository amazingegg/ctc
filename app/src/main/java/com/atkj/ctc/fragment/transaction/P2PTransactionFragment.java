package com.atkj.ctc.fragment.transaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atkj.ctc.MainActivity;
import com.atkj.ctc.R;
import com.atkj.ctc.activity.BuyActivity;
import com.atkj.ctc.activity.TranslucentActivity;
import com.atkj.ctc.adapter.CommonRecycleViewAdapter;
import com.atkj.ctc.adapter.TransactionFragmentAdapter;
import com.atkj.ctc.bean.CurrencyBean;
import com.atkj.ctc.bean.SellBean;
import com.atkj.ctc.fragment.BaseFragment;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.views.GridDivider;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public class P2PTransactionFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener, OnRefreshListener,
        RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "P2PTransactionFragment";
    // 这里的参数只是一个举例可以根据需求更改
    private String name;
    private Unbinder unbinder;


    @BindView(R.id.recycleview_buy)
    RecyclerView mRecycleView_Buy;
    @BindView(R.id.recycleview_sell)
    RecyclerView mRecycleView_Sell;
    @BindView(R.id.cb_buy_pai)
    RadioButton rbBuyPai;
    @BindView(R.id.cb_sell_pai)
    RadioButton rbSellPai;
    @BindView(R.id.iv_add)
    CheckBox ivAdd;
    @BindView(R.id.rl_root)
    RelativeLayout rlRootView;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rg_group)
    RadioGroup radioGroup;
    @BindView(R.id.tv_currency)
    TextView tvCurrency;


    private MainActivity mContext;
    private List<SellBean.ObjBean.ListBean> mBuyListData;
    private List<SellBean.ObjBean.ListBean> mSellListData;
    private View emptyView;
    private PopupWindow currencyPopu;
    private List<CurrencyBean.ObjBean> p2pCurrencyList;
    private int ctid;
    private String currency;
    private TransactionFragmentAdapter mBuyAdapter;
    private TransactionFragmentAdapter mSellAdapter;


    /**
     * 通过工厂方法来创建Fragment实例
     * 同时给Fragment来提供参数来使用
     *
     * @param param1 参数1.
     * @return Fragment的实例.
     */
    public static P2PTransactionFragment newInstance(String param1) {
        P2PTransactionFragment fragment = new P2PTransactionFragment();
        Bundle args = new Bundle();
        args.putString("name", param1);
        fragment.setArguments(args);
        return fragment;
    }


    @OnClick({R.id.iv_add, R.id.ll_currency})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
                Intent intent = new Intent(getActivity(), TranslucentActivity.class);
                startActivityForResult(intent, 0);
                ivAdd.setVisibility(View.GONE);
                getActivity().overridePendingTransition(0, 0);

                break;
            case R.id.ll_currency:


                showCurrencyPop();

                break;

        }

    }

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
    public void onResume() {
        super.onResume();
        LogUtils.d(TAG, "TransactionFragment onResume");
        ivAdd.setVisibility(View.VISIBLE);

        if (rbBuyPai.isChecked()) {
            getSellListData();
        } else {
            getBuyListData();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString("name");
        }
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragement_p2ptransaction, container, false);
        unbinder = ButterKnife.bind(this, view);

        initEvent();
        rbBuyPai.setChecked(true);

        return view;
    }


    private void initEvent() {

        radioGroup.setOnCheckedChangeListener(this);
        //买单列表
        mRecycleView_Buy.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mBuyAdapter = new TransactionFragmentAdapter(R.layout.list_item_transaction_fragement, mBuyListData);
        mBuyAdapter.bindToRecyclerView(mRecycleView_Buy);
        mBuyAdapter.setEmptyView(R.layout.layout_empty);
        mBuyAdapter.setOnItemClickListener(this);
        refreshLayout.setOnRefreshListener(this);
        mRecycleView_Buy.setAdapter(mBuyAdapter);

        //卖单列表
        mRecycleView_Sell.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mSellAdapter = new TransactionFragmentAdapter(R.layout.list_item_transaction_fragement, mSellListData);
        mSellAdapter.bindToRecyclerView(mRecycleView_Sell);
        mSellAdapter.setEmptyView(R.layout.layout_empty);
        mSellAdapter.setOnItemClickListener(this);
        refreshLayout.setOnRefreshListener(this);
        mRecycleView_Sell.setAdapter(mSellAdapter);


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


    }


    @Override
    public void initData() {
        getCurrencyType(Constants.CURRENCY_P2P);


        //refreshLayout.setEnableRefresh(true);
        //refreshLayout.autoRefresh();

    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden){
            initData();
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
                showToast(getString(R.string.a537));
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

    private String getJson(int type) {
        //从缓存获取
        Context context = AppUtils.getContext();
        SharedPreferences sp = context.getSharedPreferences("ctid", Context.MODE_PRIVATE);
        return sp.getString(getKey(type), null);
    }

    private String getKey(int type) {
        String key;
        switch (type) {
            case 1:
                key = "ctid_market_json";
                break;
            case 2:
                key = "ctid_c2c_json";
                break;
            case 3:
                key = "ctid_p2p_json";
                break;
            default:
                key = "";
        }
        return key;
    }

    private void getBuyListData() {
        Map<String, String> param = new LinkedHashMap<>();
        //TODO 分页显示 下拉加载更多
        param.put("pageSize", "20");
        param.put("pageNum", "1");
        param.put("ctid", String.valueOf(ctid));


        NetUtils.get(Url.buyList, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getBuyListData onError: " + e.toString());
                showToast(getString(R.string.a537));

            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getBuyListData onResponse: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        //if (!rbBuyPai.isChecked()) return;

                        SellBean bean = AppUtils.getGson().fromJson(response, SellBean.class);
                        mBuyListData = bean.getObj().getList();

                        mBuyAdapter.replaceData(mBuyListData);
                        mBuyAdapter.setBuy(false);
                        mBuyAdapter.notifyDataSetChanged();

                    } else {
                        showToast(obj.getString("msg"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    public void getSellListData() {
        Map<String, String> param = new LinkedHashMap<>();
        param.put("pageSize", "20");
        param.put("pageNum", "1");
        param.put("ctid", String.valueOf(ctid));


        NetUtils.get(Url.sellList, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getSellListData onError: " + e.toString());
                showToast(getString(R.string.a537));

            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getSellListData onResponse: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        //if (!rbSellPai.isChecked()) return;

                        SellBean bean = AppUtils.getGson().fromJson(response, SellBean.class);
                        mSellListData = bean.getObj().getList();

                        mSellAdapter.replaceData(mSellListData);
                        mSellAdapter.setBuy(true);
                        mSellAdapter.notifyDataSetChanged();
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
     * onAttach中连接监听接口 确保Activity支持该接口
     */
    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.mContext = (MainActivity) activity;

    }


    /**
     * onDetach中注销接口
     */
    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (AppUtils.checkLogin(getActivity())) return;

        boolean isBuy = rbBuyPai.isChecked();
        String orderid = isBuy ? mSellListData.get(position).getOrderid() : mBuyListData.get(position).getOrderid();

        BuyActivity.actionStart(getContext()
                , isBuy
                , orderid
                , String.valueOf(ctid)
                , currency);
    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {

        if (rbBuyPai.isChecked()) {
            getSellListData();
        } else {
            getBuyListData();
        }
        getCurrencyType(Constants.CURRENCY_P2P);

        refreshlayout.finishRefresh();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        TextPaint paint = rbBuyPai.getPaint();
        TextPaint paint1 = rbSellPai.getPaint();
        switch (i) {
            case R.id.cb_buy_pai:

                paint.setFakeBoldText(true);
                paint1.setFakeBoldText(false);

                mRecycleView_Buy.setVisibility(View.GONE);
                mRecycleView_Sell.setVisibility(View.VISIBLE);
                getSellListData();

                break;
            case R.id.cb_sell_pai:
                paint.setFakeBoldText(false);
                paint1.setFakeBoldText(true);

                mRecycleView_Buy.setVisibility(View.VISIBLE);
                mRecycleView_Sell.setVisibility(View.GONE);

                getBuyListData();
                break;
        }
    }


}
