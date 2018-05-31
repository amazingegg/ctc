package com.atkj.ctc.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.api.ErrorCallback;
import com.atkj.ctc.api.LoadingCallback;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.views.PswInputView;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadLayout;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2018/3/13 0013.
 */

public class PaiExtractActivity extends ToobarActivity{


    private static final java.lang.String TAG = "PaiExtractActivity";
    @BindView(R.id.ll_extract)
    LinearLayout llExtract;
    @BindView(R.id.ll_transfer_success)
    LinearLayout llSuccess;

    @BindView(R.id.tv_available)
    TextView tvAvailable;
    @BindView(R.id.tv_extract_all)
    TextView tvExtractAll;
    @BindView(R.id.et_cont)
    EditText etCont;
    @BindView(R.id.et_discovery)
    EditText etDiscovery;
    @BindView(R.id.et_phone)
    EditText etPhone;

    private LoadService loadService;
    private AlertDialog payPassWordDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setChildLayout(R.layout.activity_pai_extract);
        ButterKnife.bind(this);
        setToobarTitle(getString(R.string.a439));


        init();

    }

    private void init() {
        loadService = LoadSir.getDefault().register(this, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                loadService.showCallback(LoadingCallback.class);
                getAvailablePai();

            }
        });


        getAvailablePai();

    }



    @OnClick({R.id.tv_extract_all,R.id.bt_next})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.tv_extract_all:
                etCont.setText(tvAvailable.getText());
                etCont.setSelection(tvAvailable.getText().length());
                break;
            case R.id.bt_next:

                String count = etCont.getText().toString().trim();
                String discovery = etDiscovery.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String available = tvAvailable.getText().toString();

                if (TextUtils.isEmpty(count)){
                    showToast(getString(R.string.a283));
                    return;
                }else if (Double.parseDouble(count) > Double.parseDouble(available)){
                    showToast(getString(R.string.a442));
                    return;
                }else if (TextUtils.isEmpty(discovery)){
                    showToast(getString(R.string.a443));
                    return;
                }else if (TextUtils.isEmpty(phone)){
                    showToast(getString(R.string.a310));
                    return;
                }else if (phone.length() > 11 || phone.length() < 11){
                    showToast(getString(R.string.a311));
                    return;
                }


                String paypassword = AppUtils.getUser().getPaypassword();

                if (AppUtils.checkLogin(this)) {
                    return;
                } else if ("".equals(paypassword) || paypassword == null) {
                    Intent intent = new Intent(this, PayPassWordActivity.class);
                    startActivity(intent);
                    return;
                }

                showPayPassWordDialog();

                break;


        }



    }





    private void checkFundsPwd(String pwd) {

        if (pwd == null || pwd.length() < 6) {
            showToast(getString(R.string.a329));
            return;
        }

        String userId = AppUtils.getUserId();

        Map<String,String> param = new LinkedHashMap<>();
        param.put("id",userId);
        param.put("paypassword",pwd);

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

                        extractPai();

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

    private void extractPai() {
        String phone = etPhone.getText().toString().trim();
        String count = etCont.getText().toString().trim();
        String discovery = etDiscovery.getText().toString().trim();
        String userId = AppUtils.getUserId();
        Map<String,String> param = new LinkedHashMap<>();
        param.put("userid",userId);
        param.put("account",count);
        param.put("discoveryno",discovery);
        param.put("phone",phone);

        NetUtils.postString(Url.paiWithdraw, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                showToast(getString(R.string.a537));
                LogUtils.d(TAG, "extractPai onError: "+e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "extractPai onResponse: "+response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200){

                        llExtract.setVisibility(View.GONE);
                        llSuccess.setVisibility(View.VISIBLE);

                    }else {
                        showToast(obj.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });




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




    private void getAvailablePai() {

        String userId = AppUtils.getUserId();
        if (TextUtils.isEmpty(userId)) return;

        Map<String,String> param = new LinkedHashMap<>();
        param.put("userId",userId);
        param.put("type","1");
        param.put("ctid","0");

        NetUtils.get(Url.availableFreezeCont, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d("getAvailableCount ",e.toString());
                showToast(getString(R.string.a537));
                loadService.showCallback(ErrorCallback.class);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("getAvailableCount ","onResponse: " + response);
                loadService.showSuccess();

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        JSONObject obj1 = obj.getJSONObject("obj");
                        String availablePai = obj1.getString("account");
                        tvAvailable.setText(availablePai);
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
