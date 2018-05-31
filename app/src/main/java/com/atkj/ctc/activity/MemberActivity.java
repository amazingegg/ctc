package com.atkj.ctc.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.api.ErrorCallback;
import com.atkj.ctc.api.LoadingCallback;
import com.atkj.ctc.bean.PostJsonBean;
import com.atkj.ctc.bean.VipBean;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.views.PswInputView;
import com.atkj.ctc.views.VIPView;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/1/12 0012.
 */

public class MemberActivity extends ToobarActivity {

    private static final java.lang.String TAG = "MemberActivity";
    @BindView(R.id.vipview)
    VIPView vipView;
    @BindView(R.id.tv_pay)
    TextView tvPay;


    @BindView(R.id.content1)
    TextView content1;
    @BindView(R.id.content2)
    TextView content2;
    @BindView(R.id.content3)
    TextView content3;
    @BindView(R.id.content4)
    TextView content4;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;


    private boolean isVip;
    private int progress = 0;
    private AlertDialog payPassWordDialog;
    private String availableUsd;
    private LoadService loadService;
    private AlertDialog tipsDialog;
    private int loadCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_member);
        ButterKnife.bind(this);
        init();


    }

    private void init() {

        setToobarTitle(getString(R.string.a318));
        String username = AppUtils.getUser().getUsername();
        vipView.setName(username);


        String[] contents = {getString(R.string.a319), getString(R.string.a320), getString(R.string.a321), getString(R.string.a322)};
        content1.setText(contents[0]);
        content2.setText(contents[1]);
        content3.setText(contents[2]);
        content4.setText(contents[3]);

        loadService = LoadSir.getDefault().register(this, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                loadService.showCallback(LoadingCallback.class);
                getAvailableCount();
                getVipInfo();
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getAvailableCount();
                getVipInfo();

                refreshlayout.finishRefresh();
            }
        });

        getAvailableCount();
        getVipInfo();
    }

    private void getVipInfo() {
        String userId = AppUtils.getUserId();
        Map<String, String> param = new LinkedHashMap<>();
        param.put("userid", userId);

        NetUtils.get(Url.userCore, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getVipInfo onError: " + e.toString());
                showToast(getString(R.string.a537));
                loadService.showCallback(ErrorCallback.class);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getVipInfo onResponse: " + response);

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        loadCount++;
                        if (loadCount == 2) {
                            loadService.showSuccess();
                            loadCount = 0;
                        }


                        VipBean bean = AppUtils.getGson().fromJson(response, VipBean.class);
                        VipBean.ObjBean vipBean = bean.getObj().get(0);
                        isVip = vipBean.getVipstatus() == 2 || vipBean.getVipstatus() == 3;

                        vipView.isVip(isVip);
                        vipView.setTime(isVip ? AppUtils.timedate(vipBean.getVipendtime()) : getString(R.string.a324));
                        tvPay.setText(isVip ? getString(R.string.a323) : getString(R.string.a106));
                        vipView.setMaxProgress(vipBean.getInviteno() + vipBean.getPoorno());
                        vipView.setProgress(vipBean.getInviteno());

                        switch (vipBean.getLevel()) {
                            case "VIP":
                                vipView.setTips(getString(R.string.a325) + vipBean.getPoorno() + getString(R.string.a326) + "VIP1");
                                vipView.setVipImage(getResources().getDrawable(R.drawable.vip), VIPView.LEFT);
                                vipView.setVipImage(getResources().getDrawable(R.drawable.vip1), VIPView.RIGHT);
                                break;
                            case "VIP1":
                                vipView.setTips(getString(R.string.a325) + vipBean.getPoorno() + getString(R.string.a326) + "VIP2");
                                vipView.setVipImage(getResources().getDrawable(R.drawable.vip1), VIPView.LEFT);
                                vipView.setVipImage(getResources().getDrawable(R.drawable.vip2), VIPView.RIGHT);
                                break;
                            case "VIP2":
                                vipView.setTips(getString(R.string.a325) + vipBean.getPoorno() + getString(R.string.a326) + "VIP3");
                                vipView.setVipImage(getResources().getDrawable(R.drawable.vip2), VIPView.LEFT);
                                vipView.setVipImage(getResources().getDrawable(R.drawable.vip3), VIPView.RIGHT);
                                break;
                            case "VIP3":
                                vipView.setTips(getString(R.string.a327));
                                vipView.setVipImage(getResources().getDrawable(R.drawable.vip3), VIPView.LEFT);
                                vipView.setVipImage(getResources().getDrawable(R.drawable.vip3), VIPView.RIGHT);
                                break;
                        }
                    }else {
                        showToast(obj.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    @OnClick({R.id.tv_pay, R.id.ll1, R.id.ll2, R.id.ll3, R.id.ll4})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_pay:
                if (AppUtils.checkLogin(this)) {
                    finish();
                    return;
                }

                String paypassword = AppUtils.getUser().getPaypassword();
                if (TextUtils.isEmpty(paypassword)) {
                    intent = new Intent(this, PayPassWordActivity.class);
                    startActivity(intent);
                    return;
                }

                showPayPassWordDialog();
                break;

            case R.id.ll1:
                intent = new Intent(this, VipDetailActivity.class);
                intent.putExtra("pos", 0);
                startActivity(intent);
                break;

            case R.id.ll2:
                intent = new Intent(this, VipDetailActivity.class);
                intent.putExtra("pos", 1);
                startActivity(intent);

                break;
            case R.id.ll3:
                intent = new Intent(this, VipDetailActivity.class);
                intent.putExtra("pos", 2);
                startActivity(intent);
                break;
            case R.id.ll4:

                break;
        }


    }


    private void showPayPassWordDialog() {
        AlertDialog.Builder customizeDialog = new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_input_pwd, null);
        final PswInputView inputView = dialogView.findViewById(R.id.piv);
        inputView.setInputCallBack(new PswInputView.InputCallBack() {
            @Override
            public void onInputFinish(String result) {
                checkFundsPwd(result);
                inputView.hideSoftInput();
            }

            @Override
            public void onInputChange(String result) {
            }
        });
        TextView title = new TextView(this);
        title.setGravity(Gravity.CENTER_HORIZONTAL);
        title.setPadding(30, 30, 30, 30);
        title.setText(getString(R.string.a328));
        title.setTextColor(getResources().getColor(R.color.f000000));
        title.setTextSize(24);
        customizeDialog.setCustomTitle(title);
        customizeDialog.setView(dialogView);

        customizeDialog.setPositiveButton("",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取EditView中的输入内容
                    }
                });

        payPassWordDialog = customizeDialog.show();

    }


    private void checkFundsPwd(String pwd) {

        if (pwd == null || pwd.length() < 6) {
            showToast(getString(R.string.a329));
            return;
        }

        String userId = AppUtils.getUserId();

        Map<String, String> param = new LinkedHashMap<>();
        param.put("id", userId);
        param.put("paypassword", pwd);

        NetUtils.postString(Url.verificationPwd, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d("checkFundsPwd onError", e.toString());
                showToast(getString(R.string.a537));
                if (payPassWordDialog != null) {
                    if (payPassWordDialog.isShowing()) {

                        payPassWordDialog.dismiss();
                    }
                }
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("checkFundsPwd onResponse", response);

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {

                        openMember();

                        if (payPassWordDialog != null) {
                            if (payPassWordDialog.isShowing()) {

                                payPassWordDialog.dismiss();
                            }
                        }

                    } else {
                        showToast(obj.getString("msg"));
                        if (payPassWordDialog != null) {
                            if (payPassWordDialog.isShowing()) {

                                payPassWordDialog.dismiss();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void openMember() {
        //可用USD数量不够,提示去充值USD
        if (Double.parseDouble(availableUsd) < Integer.parseInt(Constants.MEMBER_PRICE)) {

            //showTips2();
            showToast(getString(R.string.a332));
            return;
        }
        String userId = AppUtils.getUserId();
        Map<String, String> param = new LinkedHashMap<>();
        param.put("userid", userId);
        param.put("account", Constants.MEMBER_PRICE);

        //执行网络请求
        showLoadingDialog();
        NetUtils.put(Url.memberOpen, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "openMember onResponse" + e.toString());
                showToast(getString(R.string.a537));
                dismissDialog();
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "openMember onResponse" + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {

                        showToast(getString(R.string.a330));
                        AppUtils.getUser().setVipstatus(Constants.MEMBER_VIP);
                        AppUtils.getUser().setVipendtime(obj.getLong("obj"));
                        getVipInfo();
                        dismissDialog();
                    } else {
                        showToast(obj.getString("msg"));
                        dismissDialog();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void showTips2() {
        final AlertDialog.Builder customizeDialog = new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_cancel_entrust, null);
        final TextView cancel = dialogView.findViewById(R.id.cancel);
        final TextView content = dialogView.findViewById(R.id.content);
        final TextView enter = dialogView.findViewById(R.id.enter);
        enter.setTextColor(getResources().getColor(R.color.textColor2));
        enter.setText(getString(R.string.a331));
        content.setText(getString(R.string.a332));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipsDialog.dismiss();
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberActivity.this, USDRechargeActivity.class);
                startActivityForResult(intent, 2);
                tipsDialog.dismiss();
            }
        });

        TextView title = new TextView(this);
        title.setText("");
        customizeDialog.setCustomTitle(title);
        customizeDialog.setView(dialogView);

        customizeDialog.setPositiveButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        tipsDialog = customizeDialog.show();

    }

    //可用资产
    private void getAvailableCount() {
        String userId = AppUtils.getUserId();
        if (TextUtils.isEmpty(userId)) return;

        Map<String, String> param = new LinkedHashMap<>();
        param.put("userId", userId);
        param.put("type", "2");
        param.put("ctid", "0");

        NetUtils.get(Url.availableFreezeCont, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                showToast(getString(R.string.a537));
                LogUtils.d("getAvailableCount ", e.toString());
                loadService.showCallback(ErrorCallback.class);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("getAvailableCount ", "onResponse: " + response);
                loadCount++;

                if (loadCount == 2) {
                    loadService.showSuccess();
                }

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        JSONObject obj1 = obj.getJSONObject("obj");
                        availableUsd = obj1.getString("account");
                    } else {
                        showToast(obj.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


}
