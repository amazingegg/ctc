package com.atkj.ctc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.bean.PostJsonBean;
import com.atkj.ctc.bean.UserBean;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by Administrator on 2018/1/10 0010.
 */

public class AddBankCardActivity extends ToobarActivity {

    private static final java.lang.String TAG = "AddBankCardActivity";
    @BindView(R.id.et_card_num)
    EditText etCardNum;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_bank)
    EditText etBank;
    @BindView(R.id.et_vc)
    EditText vc;
    @BindView(R.id.tv_tips)
    TextView tips;
    @BindView(R.id.tv_get_vc)
    TextView verCode;
    @BindView(R.id.et_bank_branch)
    EditText etBankBranch;
    @BindView(R.id.bt_next)
    TextView tvNext;
    @BindView(R.id.rl_vc)
    RelativeLayout rlVc;

    private String verificationCode;
    private String cardNum;
    private String bank;
    private String name;
    private String bankbranch;
    private boolean isModify;
    private boolean isFirstAdd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_add_bankcard);
        ButterKnife.bind(this);

        init();
    }


    public static void actionStart(Context context){
        Intent intent = new Intent(context,AddBankCardActivity.class);
        context.startActivity(intent);
    }

    private void init() {
        setToobarTitle(getString(R.string.a178));
        tips.setText(getString(R.string.a449));

        UserBean.ObjBean user = AppUtils.getUser();
        List<UserBean.ObjBean.BankcardBean> bankcard = user.getBankcard();
        if (bankcard != null && bankcard.size() > 0){
            isModify = true;
            rlVc.setVisibility(View.GONE);
            UserBean.ObjBean.BankcardBean bankcardBean = bankcard.get(0);
            etCardNum.setText(bankcardBean.getBankcardid());
            etCardNum.setEnabled(false);

            etName.setText(bankcardBean.getRealname());
            etName.setEnabled(false);

            etBank.setText(bankcardBean.getBankname());
            etBank.setEnabled(false);

            etBankBranch.setText(bankcardBean.getBankbranch());
            etBankBranch.setEnabled(false);

            tvNext.setText(getString(R.string.a251));

        }else {
            isModify = false;
            isFirstAdd = true;
            rlVc.setVisibility(View.GONE);
        }





    }


    @OnClick({R.id.bt_next,R.id.tv_get_vc})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.bt_next:
                if (isModify){
                    etCardNum.setEnabled(true);
                    etName.setEnabled(true);
                    etBank.setEnabled(true);
                    etBankBranch.setEnabled(true);

                    rlVc.setVisibility(View.VISIBLE);
                    tvNext.setText(getString(R.string.next));
                    isModify = false;
                }else {
                    if (isFirstAdd){
                        postBankCard2Server();
                    }else {
                        verificationSMS();
                    }
                }

                break;
            case R.id.tv_get_vc:
                sendVerificationCode(AppUtils.getUser().getPhone());
                new Timer().schedule(new MyTask(),0,1000);
                break;

        }
    }

    private void postBankCard2Server() {
        cardNum = etCardNum.getText().toString().trim();
        name = etName.getText().toString().trim();
        bank = etBank.getText().toString().trim();
        bankbranch = etBankBranch.getText().toString().trim();



        if (TextUtils.isEmpty(cardNum)){
            showToast(getString(R.string.a551));
            return;
        }else if (TextUtils.isEmpty(name)){
            showToast(getString(R.string.a552));
            return;
        }else if (TextUtils.isEmpty(bank)){
            showToast(getString(R.string.a553));
            return;
        }else if (TextUtils.isEmpty(bankbranch)){
            showToast(getString(R.string.a554));
        }


        String userId = AppUtils.getUserId();
        Map<String, String> param = new LinkedHashMap<>();
        param.put("userid", userId);
        param.put("realname",name);
        param.put("bankcardid",cardNum);
        param.put("bankname",bank);
        param.put("bankbranch",bankbranch);

        showLoadingDialog();

        NetUtils.postString(Url.setBankCard, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(e.toString());
                showToast(getString(R.string.a537));
                dismissDialog();
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200){
                        showToast("添加成功");
                        UserBean.ObjBean userBean = AppUtils.getUser();
                        List<UserBean.ObjBean.BankcardBean> list = new ArrayList<>();
                        UserBean.ObjBean.BankcardBean bean = new UserBean.ObjBean.BankcardBean();
                        bean.setBankcardid(cardNum);
                        bean.setBankname(bank);
                        bean.setRealname(name);
                        bean.setBankbranch(bankbranch);
                        bean.setId(obj.getString("obj"));
                        list.add(bean);
                        userBean.setBankcard(list);


                        AppUtils.saveUserInfo(AppUtils.getGson().toJson(AppUtils.userBean));

                        Intent intent = new Intent();
                        intent.putExtra("cardNum",cardNum);
                        intent.putExtra("bank",bank);
                        intent.putExtra("type",Constants.BIND_TYPE_YL);

                        setResult(RESULT_OK,intent);
                        dismissDialog();
                        finish();
                    }else {
                        showToast(obj.getString("msg"));
                        dismissDialog();
                    }
                } catch (JSONException e) {
                    dismissDialog();
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
                        verificationCode = obj1.getString("obj");
                        LogUtils.d("onResponse: verificationCode=" + verificationCode);

                    } else {
                        showToast(obj.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private void verificationSMS(){

        String code = vc.getText().toString().trim();
        if (TextUtils.isEmpty(code)){
            showToast(getString(R.string.a302));
            return;
        }

        Map<String,String> param = new LinkedHashMap<>();
        param.put("phone",AppUtils.getUser().getPhone());
        param.put("code",code);

        showLoadingDialog();
        NetUtils.postString(Url.verificationSMS, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "verificationSMS onError: "+e);
                showToast(getString(R.string.a537));
                dismissDialog();

            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "verificationSMS onResponse: "+response);
                dismissDialog();
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200){

                        postBankCard2Server();

                    }else {
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
                        verCode.setClickable(true);
                        verCode.setText(getString(R.string.a18));
                        verCode.setTextColor(getResources().getColor(R.color.textColor2));
                        cancel();
                    }else {
                        verCode.setText(getString(R.string.a437,num));
                        verCode.setTextColor(getResources().getColor(R.color.text_color_gray));
                        verCode.setClickable(false);
                    }
                }
            });
        }
    }

}
