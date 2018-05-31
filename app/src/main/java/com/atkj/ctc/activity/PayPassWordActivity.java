package com.atkj.ctc.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.bean.PostJsonBean;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.views.EditView;
import com.atkj.ctc.views.MyItemView;
import com.atkj.ctc.views.PswInputView;
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
 * Created by Administrator on 2017/12/23 0023.
 */

public class PayPassWordActivity extends ToobarActivity implements EditView.OnInfoClickListener {


    private static final String TAG = "PayPassWordActivity";
    @BindView(R.id.ll_modify)
    LinearLayout llModify;
    @BindView(R.id.ll_add)
    LinearLayout llAdd;
    @BindView(R.id.ll_modify_pwd)
    LinearLayout llModifyPwd;

    @BindView(R.id.miv_modify_pwd)
    MyItemView modifyPwd;
    @BindView(R.id.miv_forget_pwd)
    MyItemView forgetPwd;
    @BindView(R.id.tv_pwd_title)
    TextView title;
    @BindView(R.id.piv)
    PswInputView inputView;
    @BindView(R.id.ev_verification_code)
    EditView evVerCode;
    @BindView(R.id.ev_new_pwd)
    EditView evNewPwd;
    @BindView(R.id.ev_new_pwd2)
    EditView evNewPwd2;


    private boolean isModifyPwd;
    private int type = TYPE_PWD_NEW;
    private String inputResult;
    private static final int TYPE_PWD_NEW = 0;
    private static final int TYPE_PWD_VERIFICATION = 1;
    private static final int TYPE_PWD_FORGET = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_paypassword);
        ButterKnife.bind(this);
        init();
        initEvent();

    }

    private void initEvent() {
        evVerCode.setOnInfoClickListener(this);


    }

    private void init() {
        evNewPwd.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        evNewPwd2.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        evVerCode.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);


        String paypassword = AppUtils.getUser().getPaypassword();
        isModifyPwd = paypassword != null && !"".equals(paypassword);

        inputView.setInputCallBack(new PswInputView.InputCallBack() {
            @Override
            public void onInputFinish(String result) {
                inputResult = result;
                LogUtils.d("onInputFinish -> result=="+result);
            }

            @Override
            public void onInputChange(String result) {
                inputResult = result;
                LogUtils.d("onInputChange -> result=="+result);

            }
        });


        llModify.setVisibility(isModifyPwd ? View.VISIBLE : View.GONE);
        llAdd.setVisibility(isModifyPwd ? View.GONE : View.VISIBLE);
        title.setText(getString(R.string.a366));
        setToobarTitle(isModifyPwd ? getString(R.string.a169) : getString(R.string.a367));
    }


    @OnClick({R.id.miv_modify_pwd, R.id.miv_forget_pwd, R.id.bt_enter,R.id.bt_forget_enter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.miv_modify_pwd://修改密码
                llModify.setVisibility(View.GONE);
                llModifyPwd.setVisibility(View.GONE);
                llAdd.setVisibility(View.VISIBLE);
                title.setText(getString(R.string.a368));
                type = TYPE_PWD_VERIFICATION;
                setToobarTitle(getString(R.string.a369));

                break;
            case R.id.miv_forget_pwd://忘记密码
                llModify.setVisibility(View.GONE);
                llAdd.setVisibility(View.GONE);
                llModifyPwd.setVisibility(View.VISIBLE);
                type = TYPE_PWD_FORGET;
                setToobarTitle(getString(R.string.a370));
                break;
            case R.id.bt_enter://确定 设置\修改
                modifyPwd();
                break;
            case R.id.bt_forget_enter://确定 忘记
                modifyPwd();
                break;

        }


    }

    private void modifyPwd() {
        String url = "";
        String userid =  AppUtils.getUserId();

        Map<String,String> param = new LinkedHashMap<>();
        param.put("id",userid);
        if (type == TYPE_PWD_NEW) {//新增
            title.setText(getString(R.string.a366));
            if (inputResult == null || inputResult.length() < 6) {
                showToast(getString(R.string.a329));
                return;
            }
            url = Url.paySetPwd;
            param.put("paypassword",inputResult);
        } else if (type == TYPE_PWD_VERIFICATION) {//修改
            title.setText(getString(R.string.a368));

            if (inputResult == null || inputResult.length() < 6) {
                showToast(getString(R.string.a329));
                return;
            }
            url = Url.verificationPwd;
            param.put("paypassword",inputResult);
        } else if (type == TYPE_PWD_FORGET){//忘记
            String pwd = evNewPwd.getContentText();
            String pwd2 = evNewPwd2.getContentText();

           if (pwd.length() < 6 || pwd.length() > 6){
                showToast(getString(R.string.a371));
                return;
            }else if (!pwd.equals(pwd2)) {
                showToast(getString(R.string.a336));
                return;
            }
            url = Url.payUpdatePwd;
            param.put("paypassword",evNewPwd2.getContentText());

            verificationSMS(url,param);
            return;
        }

        if (type == TYPE_PWD_VERIFICATION) {

            NetUtils.postString(url, param, new NetUtils.StringCallBack() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    showToast(getString(R.string.a537));
                    LogUtils.d(e.toString());
                }

                @Override
                public void onResponse(String response, int id) {
                    LogUtils.d(response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getInt("status") == 200) {

                            type = TYPE_PWD_NEW;
                            title.setText(getString(R.string.a366));
                            setToobarTitle(getString(R.string.a367));
                            inputView.clearResult();

                        } else {
                            showToast(obj.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } else {
            put(url,param);

        }

    }


    private void put(String url,Map<String,String> param){
        NetUtils.put(url, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(e.toString());
                showToast(getString(R.string.a537));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        showToast(type == TYPE_PWD_NEW ? getString(R.string.a372) : getString(R.string.a373));

                        AppUtils.getUser().setPaypassword(type == TYPE_PWD_NEW ? inputResult : evNewPwd2.getContentText());

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



    @Override
    public void onInfoClick(EditView editView) {
        sendVerificationCode(AppUtils.getUser().getPhone());
        new Timer().schedule(new MyTask(),0,1000);
    }


    private void verificationSMS(final String url, final Map<String, String> param1) {
        String code = evVerCode.getContentText();

        if (TextUtils.isEmpty(code)) {
            showToast(getString(R.string.a302));
            return;
        }

        final Map<String, String> param = new LinkedHashMap<>();
        param.put("phone", AppUtils.getUser().getPhone());
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

                        put(url,param1);
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

        showLoadingDialog();
        NetUtils.get(Url.verificationCode, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d("sendVerificationCode",e.toString());
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
                    if (num == 0){
                        evVerCode.setInfoEnable(true);
                        evVerCode.setInfo(getString(R.string.a18));
                        evVerCode.setInfoColor(getResources().getColor(R.color.textColor2));
                        cancel();
                    }else {
                        evVerCode.setInfo(getString(R.string.a437,num));
                        evVerCode.setInfoColor(getResources().getColor(R.color.text_color_gray));
                        evVerCode.setInfoEnable(false);
                    }
                }
            });
        }
    }


}
