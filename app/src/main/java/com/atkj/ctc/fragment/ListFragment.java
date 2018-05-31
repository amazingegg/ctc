package com.atkj.ctc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.adapter.CommonRecycleViewAdapter;
import com.atkj.ctc.adapter.MyOrderActivityAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class ListFragment extends LazyFragment {
    private static final String TAG = "ListFragment";
    // 这里的参数只是一个举例可以根据需求更改
    private String name;
    private Unbinder unbinder;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rv)
    RecyclerView recyclerView;

    /**
     * 通过工厂方法来创建Fragment实例
     * 同时给Fragment来提供参数来使用
     * @param param1 参数1.
     * @return Fragment的实例.
     */
    public static ListFragment newInstance(String param1) {
        ListFragment fragment = new ListFragment();
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
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragement_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        initEvent();
        return view;
    }



    private void initEvent() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initData();

                refreshlayout.finishRefresh();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }


    public void setAdapter(RecyclerView.Adapter adapter){
        recyclerView.setAdapter(adapter);
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
