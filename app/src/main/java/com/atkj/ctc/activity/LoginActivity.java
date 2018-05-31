package com.atkj.ctc.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.tu.loadingdialog.LoadingDailog;
import com.atkj.ctc.MainActivity;
import com.atkj.ctc.R;
import com.atkj.ctc.adapter.CommonRecycleViewAdapter;
import com.atkj.ctc.bean.PostJsonBean;
import com.atkj.ctc.bean.UserBean;
import com.atkj.ctc.scancode.utils.Constant;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.views.EditView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by Administrator on 2017/12/14 0014.
 */

public class LoginActivity extends ToobarActivity implements EditView.OnInfoClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "LoginActivity";
    @BindView(R.id.cb_login)
    CheckBox login;
    @BindView(R.id.cb_register)
    CheckBox register;
    @BindView(R.id.ll_login)
    LinearLayout llLogin;
    @BindView(R.id.ll_register)
    LinearLayout llRegister;
    @BindView(R.id.ev_getVc)
    EditView getVc;//注册 验证码
    @BindView(R.id.ev_phone)
    EditText evPhone;//注册 手机
    @BindView(R.id.ev_pwd)
    EditView evRegisterPwd;//注册 密码
    @BindView(R.id.ev_login_pwd)
    EditView evPwd;//登录 密码
    @BindView(R.id.ev_login_phone)
    EditView evLoginPhone;//登录 Phone
    @BindView(R.id.ev_name)
    EditView evName;//注册 名称
    @BindView(R.id.ev_invite_code)
    EditView evInvite;//邀请码

   /* @BindView(R.id.yanzhengma)
    TextView yanzhengma;*/

    private long vipendtime;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_login);
        ButterKnife.bind(this);

        initEvent();

        boolean isRegist = getIntent().getBooleanExtra("isRegist", false);
        if (isRegist) register.setChecked(true);

    }

    @OnClick({R.id.login, R.id.bt_register})
    public void onViewClicked(View view) {
        int id = view.getId();
        String areacode = "86";
        switch (id) {
            case R.id.login:
                String loginPhone = evLoginPhone.getContentText().trim();
                String pwd = evPwd.getContentText().trim();

                if (TextUtils.isEmpty(loginPhone)) {
                    showToast(getString(R.string.a307));
                    return;
                } else if (TextUtils.isEmpty(pwd)) {
                    showToast(getString(R.string.a301));
                    return;
                }
                login(loginPhone, pwd, areacode);
                break;
            case R.id.bt_register:

                String password = evRegisterPwd.getContentText();
                String phone = evPhone.getText().toString().trim();

                String username = evName.getContentText();
                String invite = evInvite.getContentText();

                String pattern = "[\u4e00-\u9fa5\\w]+";

                if (TextUtils.isEmpty(username)) {
                    showToast(getString(R.string.a308));
                    return;
                } else if (username.length() < 4 || username.length() > 10) {
                    showToast(getString(R.string.a309));
                    return;
                } else if (!username.matches(pattern)){
                    showToast(getString(R.string.a538));
                    return;
                }else if (TextUtils.isEmpty(phone)) {
                    showToast(getString(R.string.a310));
                    return;
                } else if (phone.length() > 11 || phone.length() < 11) {
                    showToast(getString(R.string.a311));
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    showToast(getString(R.string.a312));
                    return;
                } else if (password.length() < 6) {
                    showToast(getString(R.string.a313));
                    return;
                } else if (!TextUtils.isEmpty(invite)){
                    if (invite.length() > 11 || invite.length() < 11){
                        showToast(getString(R.string.a315));
                        return;
                    }

                }
                verificationSMS();

                break;
        }
    }


    private void verificationSMS() {
        String code = getVc.getContentText();
        String phone = evPhone.getText().toString().trim();
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
                        String password = evRegisterPwd.getContentText();
                        String phone = evPhone.getText().toString().trim();

                        String username = evName.getContentText();
                        String invite = evInvite.getContentText();
                        String areacode = "86";

                        register(password, phone, areacode, username, invite);

                    } else {
                        showToast(obj.getString("msg"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    private void login(final String phone, final String pwd, String areacode) {
        Map<String,String> param = new LinkedHashMap<>();
        param.put("password",pwd);
        param.put("phone",phone);
        param.put("areacode",areacode);

        showLoadingDialog(getString(R.string.a306));
        NetUtils.postString(Url.login, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "login onError: "+e.toString());
                showToast(getString(R.string.a537));
                dismissDialog();
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "login onResponse: "+response);
                dismissDialog();
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        showToast(obj.getString("msg"));
                        AppUtils.userBean = AppUtils.getGson().fromJson(response, UserBean.class);
                        AppUtils.isLogin = true;
                        SharedPreferences sp = getSharedPreferences(Constants.SP_USER_INFO, Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putBoolean(Constants.IS_LOGIN,true);
                        edit.apply();

                        AppUtils.saveUserInfo(response);

                        Intent intent = new Intent();
                        intent.putExtra("vipendtime", vipendtime);
                        setResult(RESULT_OK, intent);

                        finish();
                    } else {
                        showToast(obj.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void register(final String password, final String phone,
                          final String areacode, String username, String invitePhone) {
        Map<String,String> param = new LinkedHashMap<>();
        param.put("password",password);
        param.put("phone",phone);
        param.put("areacode",areacode);
        param.put("username",username);
        param.put("invitePhone",invitePhone);

        showLoadingDialog();
        NetUtils.postString(Url.register, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                showToast(getString(R.string.a537));
                dismissDialog();
                LogUtils.d(TAG, "onError: "+e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d(TAG, "onResponse: " + response);
                dismissDialog();
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        showToast(getString(R.string.a316));

                        vipendtime = obj.getLong("obj");

                        login(phone, password, areacode);
                    } else {

                        String status = obj.getString("status");
                        if ("4041".equals(status)){
                            showToast(getString(R.string.a317));
                        }else {
                            showToast(obj.getString("msg"));
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void initEvent() {
        login.setOnCheckedChangeListener(this);
        register.setOnCheckedChangeListener(this);
        login.setChecked(true);
        getVc.setOnInfoClickListener(this);
        evPwd.setOnInfoClickListener(this);

        evLoginPhone.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        evInvite.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        getVc.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);

    }


    @Override
    public void onInfoClick(EditView view) {
        int id = view.getId();
        switch (id) {
            case R.id.ev_getVc:
                String phone = evPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    showToast(getString(R.string.a300));
                    return;
                } else if (phone.length() > 11 || phone.length() < 11){
                    showToast(getString(R.string.a311));
                    return;
                }
                sendVerificationCode(phone);
                new Timer().schedule(new MyTask(),0,1000);
                break;
            case R.id.ev_login_pwd:
                Intent intent = new Intent(this, FindPassWordActivity.class);
                startActivity(intent);
                break;
        }

    }


    /**
     * 发送验证码
     *
     * @param phone
     */
    private void sendVerificationCode(String phone) {
        Map<String, String> param = new LinkedHashMap<>();
        param.put("phone", phone);

        showLoadingDialog();
        NetUtils.get(Url.verificationCode, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "sendVerificationCode onError: "+e.toString());
                showToast(getString(R.string.a537));
                dismissDialog();
            }

            @Override
            public void onResponse(String response, int id) {
                dismissDialog();
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

    private class MyTask extends TimerTask{
        int num = 60;
        @Override
        public void run() {
            num--;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (num == 0){
                        getVc.setInfoEnable(true);
                        getVc.setInfo(getString(R.string.a18));
                        getVc.setInfoColor(getResources().getColor(R.color.textColor2));
                        cancel();
                    }else {
                        getVc.setInfo(getString(R.string.a437,num));
                        getVc.setInfoColor(getResources().getColor(R.color.text_color_gray));
                        getVc.setInfoEnable(false);
                    }
                }
            });
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();
        switch (id) {
            case R.id.cb_login:
                if (b) {
                    TextPaint loginPaint = login.getPaint();
                    loginPaint.setFakeBoldText(true);
                    login.setTextSize(24);
                    login.setTextColor(getResources().getColor(R.color.bg_color_red));
                    llLogin.setVisibility(View.VISIBLE);

                    TextPaint registerPaint = register.getPaint();
                    registerPaint.setFakeBoldText(false);
                    register.setTextSize(18);
                    register.setTextColor(getResources().getColor(R.color.text_color_gray));
                    llRegister.setVisibility(View.GONE);

                    register.setChecked(false);
                    setToobarTitle(getString(R.string.a80));
                }
                break;
            case R.id.cb_register:
                if (b) {
                    TextPaint registerPaint = register.getPaint();
                    registerPaint.setFakeBoldText(true);
                    register.setTextSize(24);
                    register.setTextColor(getResources().getColor(R.color.bg_color_red));
                    llRegister.setVisibility(View.VISIBLE);

                    TextPaint loginPaint = login.getPaint();
                    loginPaint.setFakeBoldText(false);
                    login.setTextSize(18);
                    login.setTextColor(getResources().getColor(R.color.text_color_gray));
                    llLogin.setVisibility(View.GONE);
                    login.setChecked(false);
                    setToobarTitle(getString(R.string.a81));
                }
                break;
        }
    }
}
