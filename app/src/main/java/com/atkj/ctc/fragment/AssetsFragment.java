package com.atkj.ctc.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.atkj.ctc.MainActivity;
import com.atkj.ctc.R;
import com.atkj.ctc.activity.AssetsActivity;
import com.atkj.ctc.activity.LoginActivity;
import com.atkj.ctc.activity.USDRechargeActivity;
import com.atkj.ctc.adapter.AssetsFragmentAdapter;
import com.atkj.ctc.bean.AssetsListBean;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Arith;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.views.AssetsItemView;
import com.chad.library.adapter.base.BaseQuickAdapter;
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
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public class AssetsFragment extends BaseFragment implements OnRefreshListener, AssetsItemView.OnViewClickListener {
    private static final java.lang.String TAG = "AssetsFragment";
    // 这里的参数只是一个举例可以根据需求更改
    private String name;
    private Unbinder unbinder;


    @BindView(R.id.ll_assets)
    LinearLayout llAssets;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;


    private List<AssetsListBean.ObjBean> assetsList;
    private PopupWindow pop;
    private MainActivity mContext;


    /**
     * 通过工厂方法来创建Fragment实例
     * 同时给Fragment来提供参数来使用
     *
     * @param param1 参数1.
     * @return Fragment的实例.
     */
    public static AssetsFragment newInstance(String param1) {
        AssetsFragment fragment = new AssetsFragment();
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
        LogUtils.d(TAG, "initData: ");
        getAssetsData();
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


    public void getAssetsData() {

        if (!AppUtils.isLogin) {
            for (int i = 0; i < llAssets.getChildCount(); i++) {
                AssetsItemView view = (AssetsItemView) llAssets.getChildAt(i);
                view.clearData();
            }
        }


        Map<String, String> param = new LinkedHashMap<>();
        String userid = AppUtils.getUserId();

        param.put("userid", userid);
        param.put("type", "1");

        NetUtils.get(Url.myAssets, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getAssetsData onError: "+e.toString());
                showToast(getString(R.string.a537));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getAssetsData onResponse: "+response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        AssetsListBean bean = AppUtils.getGson().fromJson(response, AssetsListBean.class);
                        assetsList = bean.getObj();
                        llAssets.removeAllViews();
                        for (int i = 0; i < assetsList.size(); i++) {
                            AssetsItemView childAt = new AssetsItemView(getContext());
                            String currency = assetsList.get(i).getCurrency();
                            childAt.setCurrency(currency);
                            childAt.setAvailable(assetsList.get(i).getAccount());
                            childAt.setFreeze(assetsList.get(i).getUnaccount());
                            childAt.setPosition(i);
                            if ("π".equals(currency)) {
                                childAt.setCurrencyTextSize(25);
                            } else {
                                childAt.setCurrencyTextSize(18);
                            }
                            childAt.setOnViewClickListener(AssetsFragment.this);
                            llAssets.addView(childAt);

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragement_assets, container, false);

        unbinder = ButterKnife.bind(this, view);
        initEvent();
        return view;
    }

    private void initEvent() {
        refreshLayout.setOnRefreshListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d(TAG, "onResume: ");
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
    public void onRefresh(RefreshLayout refreshlayout) {
        getAssetsData();
        refreshlayout.finishRefresh();

    }



    @Override
    public void onViewClick(AssetsItemView view) {

        if (!AppUtils.isLogin) return;

       /* AssetsActivity.actionStart(getContext(),view.getAvailable()
        ,view.getFreeze()
        ,view.getCurrency()
        ,assetsList.get(view.getPosition()).getStatus());*/
        AssetsActivity.actionStart(mContext,view.getAvailable()
                ,view.getFreeze()
                ,view.getCurrency()
                ,assetsList.get(view.getPosition()).getStatus(),4);
    }
}
