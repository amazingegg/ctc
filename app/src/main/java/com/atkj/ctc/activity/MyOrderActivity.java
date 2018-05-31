package com.atkj.ctc.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.androidkun.xtablayout.XTabLayout;
import com.atkj.ctc.R;
import com.atkj.ctc.adapter.CommonRecycleViewAdapter;
import com.atkj.ctc.adapter.MyViewPagerAdapter;
import com.atkj.ctc.bean.CurrencyBean;
import com.atkj.ctc.fragment.order.OrderAllFragment;
import com.atkj.ctc.fragment.order.OrderAppearFragment;
import com.atkj.ctc.fragment.order.OrderCompletedFragment;
import com.atkj.ctc.fragment.order.OrderPaidFragment;
import com.atkj.ctc.fragment.order.OrderUnpaidFragment;
import com.atkj.ctc.fragment.order.PublishAllFragment;
import com.atkj.ctc.fragment.order.PublishCompletedFragment;
import com.atkj.ctc.fragment.order.PublishPendingFragment;
import com.atkj.ctc.fragment.order.PublishTransactionFragment;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.views.GridDivider;
import com.atkj.ctc.views.MyToolBar;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/12/19 0019.
 */

public class MyOrderActivity extends ToobarActivity {
    private static final java.lang.String TAG = "MyOrderActivity";

    @BindView(R.id.vp)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    XTabLayout tabLayout;
    private List<Fragment> mFragments;
    private boolean isMyOrder;
    private PopupWindow currencyPopu;
    private MyToolBar toolBar;
    private int ctid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_my_roder);
        ButterKnife.bind(this);
        isMyOrder = getIntent().getBooleanExtra("isMyOrder", true);
        setToobarTitle(isMyOrder ? getString(R.string.a231) : getString(R.string.a232));

        initEvent();
        initData();


    }

    private void initData() {
        getCurrencyType(Constants.CURRENCY_P2P);

    }


    public void initEvent() {
        toolBar = getToolBar();
        TextView textView = toolBar.getRightTextView();
        toolBar.setTextRightVisibility(true);
        toolBar.setRightText("BTC");
        Drawable drawable = getResources().getDrawable(R.drawable.qiehuan);
        drawable.setBounds(0, 0, AppUtils.dip2px(this, 12),
                AppUtils.dip2px(this, 12));

        textView.setCompoundDrawables(null,null,drawable,null);
        textView.setCompoundDrawablePadding(5);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrencyPop();
            }
        });


        viewPager.addOnAdapterChangeListener(new ViewPager.OnAdapterChangeListener() {
            @Override
            public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {

            }
        });


        mFragments = new ArrayList<>();

        if (isMyOrder) {
            mFragments.add(OrderAllFragment.newInstance("OrderAllFragment"));
            mFragments.add(OrderUnpaidFragment.newInstance("OrderUnpaidFragment"));
            mFragments.add(OrderPaidFragment.newInstance("OrderPaidFragment"));
            mFragments.add(OrderCompletedFragment.newInstance("OrderCompletedFragment"));
            mFragments.add(OrderAppearFragment.newInstance("OrderAppearFragment"));
        } else {
            mFragments.add(PublishAllFragment.newInstance("PublishAllFragment"));
            mFragments.add(PublishPendingFragment.newInstance("PublishPendingFragment"));
            mFragments.add(PublishTransactionFragment.newInstance("PublishTransactionFragment"));
            mFragments.add(PublishCompletedFragment.newInstance("PublishCompletedFragment"));
        }

        String[] orderTitles = {getString(R.string.a120), getString(R.string.a121),
                getString(R.string.a122), getString(R.string.a123), getString(R.string.a124)};

        String[] publishTitles = {getString(R.string.a27), getString(R.string.a340),
                getString(R.string.a341), getString(R.string.a123)};
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager(), mFragments, isMyOrder ? orderTitles : publishTitles);
        viewPager.setAdapter(adapter);

        int len = isMyOrder ? orderTitles.length : publishTitles.length;
        for (int i = 0; i < len; i++) {
            XTabLayout.Tab tab = tabLayout.newTab();
            tab.setText(isMyOrder ? orderTitles[i] : publishTitles[i]);
            tabLayout.addTab(tab);
        }

        tabLayout.setupWithViewPager(viewPager);

        //tabLayout.setOnTabSelectedListener(this);


        //币种选择监听
        currencyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                currencyPopu.dismiss();
                CurrencyBean.ObjBean bean = p2pCurrencyList.get(position);

                toolBar.setRightText(bean.getSymbol());
                ctid = bean.getCtid();
                /*if (listener != null){
                    listener.onClick(0);
                }*/

                EventBus.getDefault().post(bean.getCtid());

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


    private void showCurrencyPop() {

        //初始化POPU
        View content = LayoutInflater.from(this).inflate(R.layout.pop_currency_list, null);
        currencyPopu = new PopupWindow(content, ViewGroup.LayoutParams.MATCH_PARENT, AppUtils.dip2px(this, 200), true);
        RecyclerView currencyList = content.findViewById(R.id.rv_currency_list);
        currencyList.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        currencyList.setAdapter(currencyAdapter);
        int[] attr = new int[]{android.R.attr.listDivider};
        currencyList.addItemDecoration(new GridDivider(this, GridDivider.DIRECTION_VERTICAL, 3, attr));

        currencyPopu.setFocusable(true);
        currencyPopu.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));

        if (currencyPopu.isShowing()) {
            currencyPopu.dismiss();
        } else {
            currencyPopu.showAsDropDown(toolBar);
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

    public int getCtid() {
        return ctid;
    }




    public interface OnCurrencyClickListener{
        void onClick(int ctid);
    }

    private OnCurrencyClickListener listener;
    public void setOnCurrencyClickListener(OnCurrencyClickListener listener){
        this.listener = listener;
    }

    private void parsJson(String str) {
        if (str != null) {
            CurrencyBean currencyBean = AppUtils.getGson().fromJson(str, CurrencyBean.class);
            p2pCurrencyList = currencyBean.getObj();

            if (p2pCurrencyList.size() > 0) {
                CurrencyBean.ObjBean bean = p2pCurrencyList.get(0);
                bean.setSelect(true);
                ctid = bean.getCtid();
                /*if (listener != null){
                    listener.onClick(0);
                }*/
                EventBus.getDefault().post(bean.getCtid());
                toolBar.setRightText(bean.getSymbol());

                currencyAdapter.replaceData(p2pCurrencyList);
                currencyAdapter.notifyDataSetChanged();
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


}
