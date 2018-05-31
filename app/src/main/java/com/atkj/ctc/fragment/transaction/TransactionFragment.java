package com.atkj.ctc.fragment.transaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidkun.xtablayout.XTabLayout;
import com.atkj.ctc.MainActivity;
import com.atkj.ctc.R;
import com.atkj.ctc.adapter.MyViewPagerAdapter;
import com.atkj.ctc.adapter.TransactionFragmentAdapter;
import com.atkj.ctc.bean.CurrencyBean;
import com.atkj.ctc.bean.SellBean;
import com.atkj.ctc.fragment.BaseFragment;
import com.atkj.ctc.scancode.utils.Constant;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Currency;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public class TransactionFragment extends BaseFragment {
    private static final String TAG = "TransactionFragment";
    // 这里的参数只是一个举例可以根据需求更改
    private String name;
    private Unbinder unbinder;

    private MainActivity mContext;

    @BindView(R.id.tab_layout)
    XTabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private ArrayList<Fragment> fragments;


    /**
     * 通过工厂方法来创建Fragment实例
     * 同时给Fragment来提供参数来使用
     *
     * @param param1 参数1.
     * @return Fragment的实例.
     */
    public static TransactionFragment newInstance(String param1) {
        TransactionFragment fragment = new TransactionFragment();
        Bundle args = new Bundle();
        args.putString("name", param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d(TAG, "onResume: ");

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        MarketTransactionContentFragment fragment = (MarketTransactionContentFragment) fragments.get(0);
        //fragment.parentHiddenChanged(hidden);
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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragement_transaction, container, false);
        unbinder = ButterKnife.bind(this, view);

        initEvent();
        LogUtils.d(TAG, "initView: ");


        return view;
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }


    private void initEvent() {

        fragments = new ArrayList<>();
        fragments.add(MarketTransactionContentFragment.newInstance("C2CTransactionFragment"));

        //fragments.add(P2PTransactionFragment.newInstance("P2PTransactionFragment"));

        String[] fragmentTitles = {getString(R.string.a417), getString(R.string.a219), /*getString(R.string.a436)*/};

        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getActivity().getSupportFragmentManager(),
                fragments, fragmentTitles);
        viewPager.setAdapter(adapter);

        for (String fragmentTitle : fragmentTitles) {
            XTabLayout.Tab tab = tabLayout.newTab();
            tab.setText(fragmentTitle);
            tabLayout.addTab(tab);
        }
        tabLayout.setupWithViewPager(viewPager);


    }

    private List<CurrencyBean.ObjBean> c2cCurrencyList;
    private List<CurrencyBean.ObjBean> marketCurrencyList;





    /*private void send(int type, String response) {

        CurrencyBean currencyBean = AppUtils.getGson().fromJson(response, CurrencyBean.class);
        if (type == Constants.CURRENCY_MARKET) {
            marketCurrencyList = currencyBean.getObj();

            if (marketCurrencyList.size() > 0) marketCurrencyList.get(0).setSelect(true);

            MarketTransactionContentFragment fragment = (MarketTransactionContentFragment) fragments.get(1);
            fragment.setCurrency(marketCurrencyList);
        } else {
            c2cCurrencyList = currencyBean.getObj();
            if (c2cCurrencyList.size() > 0) c2cCurrencyList.get(0).setSelect(true);
            MarketTransactionContentFragment fragment = (MarketTransactionContentFragment) fragments.get(0);
            fragment.setCurrency(c2cCurrencyList);
        }

    }*/

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


    public void checkFragment(int i) {
        tabLayout.getTabAt(1).select();
        MarketTransactionContentFragment fragment = (MarketTransactionContentFragment) fragments.get(1);
    }
}
