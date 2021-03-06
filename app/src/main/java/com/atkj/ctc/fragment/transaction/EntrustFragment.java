package com.atkj.ctc.fragment.transaction;

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
import com.atkj.ctc.fragment.BaseFragment;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class EntrustFragment extends BaseFragment {
    private static final String TAG = "EntrustFragment";
    // 这里的参数只是一个举例可以根据需求更改
    private String name;
    private Unbinder unbinder;

    @BindView(R.id.recycleview_entrust)
    RecyclerView recyclerView;


    /**
     * 通过工厂方法来创建Fragment实例
     * 同时给Fragment来提供参数来使用
     *
     * @param param1 参数1.
     * @return Fragment的实例.
     */
    public static EntrustFragment newInstance(String param1) {
        EntrustFragment fragment = new EntrustFragment();
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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragement_entrust, container, false);
        unbinder = ButterKnife.bind(this, view);

        initEvent();


        return view;
    }


    private List<String> mDataList;
    private CommonRecycleViewAdapter adapter = new CommonRecycleViewAdapter<String>(R.layout.list_item_entrust,mDataList){
        @Override
        protected void convert(BaseViewHolder helper, String item) {

        }
    };


    private void initEvent() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);

        mDataList = new ArrayList<>();
        mDataList.add("1");
        mDataList.add("1");
        mDataList.add("1");
        mDataList.add("1");
        mDataList.add("1");
        adapter.addData(mDataList);
        adapter.notifyDataSetChanged();


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
