package com.atkj.ctc.fragment.transaction;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.fragment.BaseFragment;
import com.atkj.ctc.views.InputBox;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class BuyFragment extends BaseFragment {
    private static final String TAG = "BuyFragment";
    // 这里的参数只是一个举例可以根据需求更改
    private String name;
    private Unbinder unbinder;

    @BindView(R.id.ib_price)
    InputBox ibPrice;
    @BindView(R.id.ib_cont)
    InputBox ibCount;
    @BindView(R.id.bt_buy)
    Button btBuy;
    private boolean isBuy;


    /**
     * 通过工厂方法来创建Fragment实例
     * 同时给Fragment来提供参数来使用
     *
     * @param param1 参数1.
     * @return Fragment的实例.
     */
    public static BuyFragment newInstance(String param1,boolean isBuy) {
        BuyFragment fragment = new BuyFragment();
        Bundle args = new Bundle();
        args.putString("name", param1);
        args.putBoolean("isBuy",isBuy);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString("name");
            isBuy = getArguments().getBoolean("isBuy");
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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragement_buy, container, false);
        unbinder = ButterKnife.bind(this, view);

        initEvent();

        return view;
    }

    private void initEvent() {
        String str1 = name.split("/")[0];
        String str2 = name.split("/")[1];
        ibPrice.setCurrency(str2);
        ibCount.setCurrency(str1);

        Drawable drawable = getResources().getDrawable(isBuy ? R.drawable.selecter_login_bg : R.drawable.selecter_login_bg_sell);
        btBuy.setBackground(drawable);
        btBuy.setText(isBuy ? getString(R.string.a39) + str1 : getString(R.string.a280) + str1);


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
