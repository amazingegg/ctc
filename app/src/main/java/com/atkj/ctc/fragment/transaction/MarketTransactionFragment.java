package com.atkj.ctc.fragment.transaction;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atkj.ctc.R;
import com.atkj.ctc.adapter.MyViewPagerAdapter;
import com.atkj.ctc.fragment.BaseFragment;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class MarketTransactionFragment extends BaseFragment {
    private static final String TAG = "MarketTransactionFragment";
    // 这里的参数只是一个举例可以根据需求更改
    private String name;
    private Unbinder unbinder;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
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
    public static MarketTransactionFragment newInstance(String param1) {
        MarketTransactionFragment fragment = new MarketTransactionFragment();
        Bundle args = new Bundle();
        args.putString("name", param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString("name");
        }
    }


    @Override
    public void initData() {

    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragement_market_transaction, container, false);
        unbinder = ButterKnife.bind(this, view);

        initEvent();

        return view;
    }

    private void initEvent() {




        String[] marketTitles = {"π/usd","BTC/usd","LTC/usd","ETH/usd","ETC/usd","BCC/usd"};
        String[] c2cTitles = {"π/BTC","ETH/BTC","LTC/BTC","π/ETH","BTC/ETH","LTC/ETH"};


        fragments = new ArrayList<>();
        String[] titles;
        if (name.equals("MarketTransactionFragment")){
            titles = marketTitles;
        }else {
            titles = c2cTitles;
        }


        for (int i = 0; i < titles.length; i++) {
            int type = name.equals("MarketTransactionFragment")? 1 : 2;

            tabLayout.addTab(tabLayout.newTab().setText(titles[i]));
            MarketTransactionContentFragment fragment = MarketTransactionContentFragment.newInstance(titles[i]);
            fragments.add(fragment);

        }

        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getChildFragmentManager(),
                fragments,titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     *onAttach中连接监听接口 确保Activity支持该接口
     */
    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

    }


    /**
     * onDetach中注销接口
     */
    @Override
    public void onDetach() {
        super.onDetach();
    }



}
