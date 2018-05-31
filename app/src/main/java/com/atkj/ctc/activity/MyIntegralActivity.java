package com.atkj.ctc.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.adapter.CommonRecycleViewAdapter;
import com.atkj.ctc.api.LoadingCallback;
import com.atkj.ctc.bean.MyIntegralBean;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Arith;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.JsonObject;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by Administrator on 2018/1/12 0012.
 */

public class MyIntegralActivity extends ToobarActivity {


    private static final String TAG = "MyIntegralActivity";
    @BindView(R.id.recycleview)
    RecyclerView recyclerView;
    @BindView(R.id.tv_available_integral)
    TextView total;
    @BindView(R.id.rate)
    TextView tvRate;





    private LoadService loadService;
    private double rate;
    private int luadcount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_my_integral);
        ButterKnife.bind(this);

        init();

    }

    private void init() {
        setToobarTitle(getString(R.string.a233));

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.layout_empty);

        loadService = LoadSir.getDefault().register(this, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                loadService.showCallback(LoadingCallback.class);
                luadcount = 0;
                initData();

            }
        });
        initData();


    }

    private void initData() {
        getPointDetail();
        getExchangeRate();

    }

    private void getExchangeRate() {
        Map<String,String> param = new LinkedHashMap<>();
        param.put("content","USDCNH");
        param.put("type","1");

        NetUtils.get(Url.getRate, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getExchangeRate onResponse: "+e.toString());
                showToast(getString(R.string.a537));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getExchangeRate onResponse: "+response);
                try {
                    JSONObject obj = new JSONObject(response);

                    JsonObject obj2 = new JsonObject();

                    if (obj.getInt("status") == 200) {
                        luadcount++;
                        if (luadcount == 2){
                            loadService.showSuccess();
                        }


                        JSONArray obj1 = obj.getJSONArray("obj");
                        JSONObject jsonObject = obj1.getJSONObject(0);
                        rate = jsonObject.getDouble("price");
                        tvRate.setText("1USD="+ rate + "CNY");

                    } else {
                        showToast(obj.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getPointDetail() {
        String userId = AppUtils.getUserId();
        Map<String,String> param = new LinkedHashMap<>();
        param.put("userid",userId);

        NetUtils.get(Url.pointDetails, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG,"getPointDetail onError "+e.toString());
                showToast(getString(R.string.a537));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG,"getPointDetail onResponse "+response);

                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getInt("status") == 200){
                        luadcount++;
                        if (luadcount == 2){
                            loadService.showSuccess();
                        }


                        JSONArray array = obj.getJSONArray("obj");
                        JSONObject obj1 = array.getJSONObject(0);
                        JSONArray array1 = array.getJSONArray(1);

                        total.setText(String.valueOf(obj1.getDouble("totalpoint")));
                        if (mListData == null) mListData = new ArrayList<>();
                        for (int i = 0; i < array1.length(); i++) {
                            MyIntegralBean bean = new MyIntegralBean();
                            JSONObject o = array1.getJSONObject(i);

                            bean.setRemake(o.getString("remake"));
                            bean.setContent(o.getString("content"));
                            bean.setPoint(o.get("point").toString());
                            bean.setStatus(o.getInt("status"));
                            mListData.add(bean);
                        }

                        adapter.replaceData(mListData);
                        adapter.notifyDataSetChanged();

                    }else {
                        showToast(obj.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private List<MyIntegralBean> mListData;
    private CommonRecycleViewAdapter adapter = new CommonRecycleViewAdapter
            <MyIntegralBean>(R.layout.list_item_integral,mListData){
        @Override
        protected void convert(BaseViewHolder helper, MyIntegralBean item) {
            int status = item.getStatus();
            //科学计数法转普通计数

            String point = AppUtils.convertCounting(item.getPoint());
            helper.setText(R.id.remark,item.getRemake())
                    .setText(R.id.content,item.getContent())
                    .setText(R.id.integral,status == 1 ? "+"+point : "-"+point)
                    .setText(R.id.remark2,"= "+point+"USD ≈ "+ Arith.mul(point,String.valueOf(rate),2)+"CNY");
        }
    };




}
