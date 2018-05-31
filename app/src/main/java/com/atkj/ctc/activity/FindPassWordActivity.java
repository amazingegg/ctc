package com.atkj.ctc.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.tu.loadingdialog.LoadingDailog;
import com.atkj.ctc.R;
import com.atkj.ctc.bean.PostJsonBean;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.views.EditView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class FindPassWordActivity extends ToobarActivity implements EditView.OnInfoClickListener {
    private static final String TAG = "FindPassWordActivity";

    @BindView(R.id.ev_phone)
    EditView evPhone;//电话
    @BindView(R.id.ev_vc)
    EditView evVc;//验证码
    @BindView(R.id.ev_new_pwd)
    EditView evPwd;
    @BindView(R.id.ev_new_pwd2)
    EditView evPwd2;
    @BindView(R.id.ll_modify_pwd)
    LinearLayout llModifyPwd;
    @BindView(R.id.ll_modify_success)
    LinearLayout llModifySuccess;
    private LoadingDailog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_find_password);
        setToobarTitle(getString(R.string.a299));
        ButterKnife.bind(this);

        initEvent();

    }

    private void initEvent() {
        evVc.setOnInfoClickListener(this);

        evVc.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        evPhone.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);


    }


    @OnClick({R.id.enter, R.id.enter2})
    public void onViewClicked(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.enter:
                String phone = evPhone.getContentText().trim();
                String pwd = evPwd.getContentText().trim();
                String areacode = "86";
                String pwd2 = evPwd2.getContentText().trim();

                if (TextUtils.isEmpty(phone)) {
                    showToast(getString(R.string.a300));
                    return;
                } else if (TextUtils.isEmpty(pwd)) {
                    showToast(getString(R.string.a301));
                    return;
                } else if (pwd.length() < 6 || pwd2.length() < 6) {
                    showToast(getString(R.string.a313));
                    return;
                } else if (!pwd.equals(pwd2)) {
                    showToast(getString(R.string.a304));
                    return;
                }
                verificationSMS();

                break;
            case R.id.enter2:
                finish();
                break;
        }
    }

    private void modifyPwd(final String phone, final String pwd, String areacode) {
        Map<String, String> param = new LinkedHashMap<>();
        param.put("password", pwd);
        param.put("phone", phone);
        param.put("areacode", areacode);

        showLoadingDialog();
        NetUtils.put(Url.updatePwd, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dismissDialog();
                showToast(getString(R.string.a537));
                LogUtils.d(e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d(TAG, "onResponse: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        dismissDialog();
                        llModifyPwd.setVisibility(View.GONE);
                        llModifySuccess.setVisibility(View.VISIBLE);

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


    private void verificationSMS() {
        String code = evVc.getContentText();
        String phone = evPhone.getContentText().trim();
        if (TextUtils.isEmpty(code)) {
            showToast(getString(R.string.a302));
            return;
        }

        Map<String, String> param = new LinkedHashMap<>();
        param.put("phone", phone);
        param.put("code", code);

        showLoadingDialog();
        NetUtils.postString(Url.verificationSMS, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "verificationSMS onError: " + e);
                showToast(getString(R.string.a537));
                dismissDialog();

            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "verificationSMS onResponse: " + response);
                dismissDialog();
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        String phone = evPhone.getContentText().trim();
                        String pwd = evPwd.getContentText().trim();
                        String areacode = "86";
                        modifyPwd(phone, pwd, areacode);

                    } else {
                        showToast(obj.getString("msg"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }


    /**
     * 发送验证码
     *
     * @param phone
     */
    private void sendVerificationCode(String phone) {
        Map<String, String> param = new LinkedHashMap<>();
        param.put("phone", phone);

        NetUtils.get(Url.verificationCode, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                showToast(getString(R.string.a537));
                LogUtils.d(TAG, "sendVerificationCode onError: " + e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getInt("status") == 200) {
                        showToast(getString(R.string.a261));
                        JSONObject obj1 = obj.getJSONObject("obj");

                    } else {
                        showToast(obj.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private class MyTask extends TimerTask {
        int num = 60;

        @Override
        public void run() {
            num--;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (num == 0) {
                        evVc.setInfoEnable(true);
                        evVc.setInfo(getString(R.string.a18));
                        evVc.setInfoColor(getResources().getColor(R.color.textColor2));
                        cancel();
                    } else {
                        evVc.setInfo(getString(R.string.a437, num));
                        evVc.setInfoColor(getResources().getColor(R.color.text_color_gray));
                        evVc.setInfoEnable(false);
                    }
                }
            });
        }
    }

    @Override
    public void onInfoClick(EditView editView) {
        String phone = evPhone.getContentText().trim();
        if (TextUtils.isEmpty(phone)) {
            showToast(getString(R.string.a300));
        } else if (phone.length() > 11 || phone.length() < 11) {
            showToast(getString(R.string.a311));
            return;
        }

        sendVerificationCode(phone);
        new Timer().schedule(new MyTask(), 0, 1000);
    }
}
