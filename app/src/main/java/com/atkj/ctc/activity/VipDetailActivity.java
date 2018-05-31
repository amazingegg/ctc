package com.atkj.ctc.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.api.ErrorCallback;
import com.atkj.ctc.api.LoadingCallback;
import com.atkj.ctc.bean.PrivelegeBean;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by Administrator on 2018/1/19 0019.
 */

public class VipDetailActivity extends ToobarActivity {

    private static final java.lang.String TAG = "VipDetailActivity";
    @BindView(R.id.content1)
    TextView vip;
    @BindView(R.id.content2)
    TextView vip1;
    @BindView(R.id.content3)
    TextView vip2;
    @BindView(R.id.content4)
    TextView vip3;
    @BindView(R.id.title_zhekou)
    TextView tvTitle;
    @BindView(R.id.tv_jifen)
    TextView tvJifen;
    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.ll_table)
    LinearLayout llTabale;
    private LoadService loadService;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_vip_detail);
        ButterKnife.bind(this);

        init();


    }

    private void init() {
        setToobarTitle(getString(R.string.a104));

        int pos = getIntent().getIntExtra("pos", 0);
        loadService = LoadSir.getDefault().register(this, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                loadService.showCallback(LoadingCallback.class);
                init();
            }
        });

        getPrivelege(pos);

    }

    private void getPrivelege(final int pos) {
        String type = "";
        if (pos == 0 || pos == 1){
            type = "1";
        }else if (pos == 2){
            type = "2";
        }

        Map<String,String> param = new LinkedHashMap<>();
        param.put("type",type);

        NetUtils.get(Url.getPrivelege, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getPrivelege onError: ");
                showToast(getString(R.string.a537));
                loadService.showCallback(ErrorCallback.class);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getPrivelege onResponse: "+response);
                loadService.showSuccess();
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200){
                        PrivelegeBean bean = AppUtils.getGson().fromJson(response, PrivelegeBean.class);
                        switch (pos){
                            case 0:
                                tvTitle.setText(getString(R.string.a204));
                                title.setText(getString(R.string.a410));
                                vip.setText(String.format(getString(R.string.a411),bean.getObj().get(0).getDiscount()));
                                vip1.setText(String.format(getString(R.string.a411),bean.getObj().get(1).getDiscount()));
                                vip2.setText(String.format(getString(R.string.a411),bean.getObj().get(2).getDiscount()));
                                vip3.setText(String.format(getString(R.string.a411),bean.getObj().get(3).getDiscount()));
                                break;
                            case 1:
                                tvTitle.setText(getString(R.string.a412));
                                title.setText(getString(R.string.a413));
                                vip.setText(String.format(getString(R.string.a414),bean.getObj().get(0).getRebate()));
                                vip1.setText(String.format(getString(R.string.a414),bean.getObj().get(1).getRebate()));
                                vip2.setText(String.format(getString(R.string.a414),bean.getObj().get(2).getRebate()));
                                vip3.setText(String.format(getString(R.string.a414),bean.getObj().get(3).getRebate()));
                                break;
                            case 2:
                                llTabale.setVisibility(View.GONE);
                                String total = bean.getObj().get(0).getTotal();
                                tvTitle.setText(String.format(getString(R.string.a415),total));
                                tvJifen.setText(String.format(getString(R.string.a416),total,
                                        bean.getObj().get(0).getEvery(),bean.getObj().get(0).getAccount()));
                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


}
