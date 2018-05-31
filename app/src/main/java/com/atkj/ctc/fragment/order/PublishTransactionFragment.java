package com.atkj.ctc.fragment.order;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDailog;
import com.atkj.ctc.R;
import com.atkj.ctc.activity.MyOrderActivity;
import com.atkj.ctc.adapter.MyOrderActivityAdapter;
import com.atkj.ctc.bean.OrderBean;
import com.atkj.ctc.fragment.BaseFragment;
import com.atkj.ctc.fragment.LazyFragment;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class PublishTransactionFragment extends LazyFragment implements OnRefreshListener, BaseQuickAdapter.OnItemChildClickListener, MyOrderActivity.OnCurrencyClickListener {
    private static final String TAG = "PublishTransactionFragment";
    // 这里的参数只是一个举例可以根据需求更改
    private String name;
    private Unbinder unbinder;


    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private MyOrderActivityAdapter adapter;
    private List<OrderBean.ObjBean.ListBean> mListData;
    private LoadingDailog dialog;
    private MyOrderActivity myOrderActivity;
    private int ctid;


    /**
     * 通过工厂方法来创建Fragment实例
     * 同时给Fragment来提供参数来使用
     *
     * @param param1 参数1.
     * @return Fragment的实例.
     */
    public static PublishTransactionFragment newInstance(String param1) {
        PublishTransactionFragment fragment = new PublishTransactionFragment();
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
    protected void onFragmentFirstVisible() {
        getListData();
    }


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragement_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        initEvent();

        return view;
    }

    private void initEvent() {
        refreshLayout.setOnRefreshListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new MyOrderActivityAdapter(R.layout.list_item_my_publish, mListData,false);
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.layout_empty);
        adapter.setOnItemChildClickListener(this);
        recyclerView.setAdapter(adapter);

        myOrderActivity.setOnCurrencyClickListener(this);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    /**
     *onAttach中连接监听接口 确保Activity支持该接口
     */
    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        myOrderActivity = (MyOrderActivity) activity;
    }


    @Override
    public void onClick(int ctid) {
        this.ctid = ctid;
        getListData();
    }

    private void getListData() {

        Map<String, String> param = new LinkedHashMap<>();
        String userid =  AppUtils.getUserId();

        param.put("userid", userid);
        param.put("status", Integer.toString(Constants.PUBLISH_TYPE_TRANSACTION));
        param.put("ctid",String.valueOf(myOrderActivity.getCtid()));
        LogUtils.d(TAG, "getListData: ctid=="+myOrderActivity.getCtid());


        NetUtils.get(Url.myPublish, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(e.toString());
                showToast(getString(R.string.a537));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        OrderBean bean = AppUtils.getGson().fromJson(response, OrderBean.class);
                        mListData = bean.getObj().getList();

                        adapter.replaceData(mListData);
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * onDetach中注销接口
     */
    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        getListData();
        refreshlayout.finishRefresh();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (view.getId() == R.id.tv_status_bottom){
            confirm(position);
        }
    }

    private void confirm(int position) {

        String orderId = mListData.get(position).getOrderid();
        String selluserid = mListData.get(position).getSelluserid();
        String buyuserid = mListData.get(position).getBuyuserid();
        String url = "";
        String userId;
        String id = AppUtils.getUserId();

        int type = mListData.get(position).getType();
        switch (type){
            case Constants.ORDER_SELL:
                if (id.equals(selluserid)){//卖单-卖家确认收款
                    userId = selluserid;
                    url = Url.sellOrderConfirm;
                }else {//卖单 -买家确认付款
                    userId = buyuserid;
                    url = Url.buyOrderConfirm;
                }
                break;
            case Constants.ORDER_BUY:
                if (id.equals(buyuserid)){//买单 - 买家确认付款
                    userId = buyuserid;
                    url = Url.buyOrderConfirm;
                }else {//买单 卖家确认收款
                    userId = selluserid;
                    url = Url.sellOrderConfirm;
                }
                break;
            default:
                userId = "";
        }
        Map<String,String> param = new LinkedHashMap<>();
        param.put("orderid",orderId);
        param.put("userid",userId);

        showLoadingDialog(getContext());
        NetUtils.postString(url, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(e.toString());
                dismissDialog();
                showToast(getString(R.string.a537));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG,response);
                dismissDialog();
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200){
                        getListData();
                    }else {
                        Toast.makeText(getContext(),obj.getString("msg"),Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCurrencyEvent(Integer event) {
        this.ctid = event;
        getListData();
    }












}
