package com.atkj.ctc.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atkj.ctc.R;
import com.atkj.ctc.scancode.CommonScanActivity;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.views.EditView;
import com.atkj.ctc.views.PswInputView;

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

/**
 * Created by Administrator on 2018/1/31 0031.
 */

public class BTCExtractActivity extends ToobarActivity {


    private static final java.lang.String TAG = "BTCExtractActivity";
    @BindView(R.id.tips)
    TextView tips;
    @BindView(R.id.ll_transfer_success)
    LinearLayout transfer_success;
    @BindView(R.id.ll_transfer)
    LinearLayout transfer;
    @BindView(R.id.et_btc_address)
    EditText etAddress;
    @BindView(R.id.quantity)
    EditText etQuantity;
    @BindView(R.id.tv_available)
    TextView tvAvailable;
    @BindView(R.id.et_vc)
    EditText etVc;
    @BindView(R.id.get_vc)
    TextView getVc;
    @BindView(R.id.ll_vc)
    LinearLayout llVc;


    // 要申请的权限
    private String[] permissions = {Manifest.permission.CAMERA};
    private AlertDialog dialog;
    private String currency;
    private String available;
    private android.app.AlertDialog payPassWordDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_btc_extract);
        ButterKnife.bind(this);

        init();

    }

    private void init() {
        currency = getIntent().getStringExtra("currency");
        available = getIntent().getStringExtra("available");
        etQuantity.setHint(getString(R.string.a546,Constants.MINI_EXTRACT));


        etVc.setInputType(InputType.TYPE_CLASS_NUMBER);

        setToobarTitle(getString(R.string.a267, currency));
        tvAvailable.setText(available + currency);

        tips.setText(getString(R.string.a266, Constants.MINI_EXTRACT,currency));


    }


    @OnClick({R.id.iv_scan_code, R.id.bt_commit, R.id.all, R.id.get_vc,R.id.bt_commit2})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_scan_code:
                // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 检查该权限是否已经获取
                    int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                    // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        // 如果没有授予该权限，就去提示用户请求
                        showDialogTipUserRequestPermission();


                    } else {
                        intent = new Intent(this, CommonScanActivity.class);
                        intent.putExtra(Constants.REQUEST_SCAN_MODE, Constants.REQUEST_SCAN_MODE_QRCODE_MODE);
                        startActivityForResult(intent, 11);
                    }
                }

                break;
            case R.id.bt_commit:



                String address = etAddress.getText().toString();

                String quantity = etQuantity.getText().toString();


                if (TextUtils.isEmpty(address)) {
                    showToast(getString(R.string.a450));
                    return;
                } else if (TextUtils.isEmpty(quantity)) {
                    showToast(getString(R.string.a283));
                    return;
                }

                try {
                    double d = Double.parseDouble(quantity);

                    if (d < Double.parseDouble(Constants.MINI_EXTRACT)) {
                        showToast(getString(R.string.a546,Constants.MINI_EXTRACT));
                        return;
                    }


                } catch (Exception e) {
                    showToast(getString(R.string.a545));
                    return;
                }

                llVc.setVisibility(View.VISIBLE);
                transfer.setVisibility(View.GONE);


                break;
            case R.id.all:
                etQuantity.setText(available);
                etQuantity.setSelection(available.length());
                break;

            case R.id.get_vc:
                String phone = AppUtils.getUser().getPhone();
                sendVerificationCode(phone);
                new Timer().schedule(new MyTask(), 0, 1000);
                break;

            case R.id.bt_commit2:
                verificationSMS();
                break;

        }


    }


    private void verificationSMS() {
        String code = etVc.getText().toString();

        if (TextUtils.isEmpty(code)) {
            showToast(getString(R.string.a302));
            return;
        }

        Map<String, String> param = new LinkedHashMap<>();
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


                        String paypassword = AppUtils.getUser().getPaypassword();
                        if ("".equals(paypassword) || paypassword == null) {
                           Intent intent = new Intent(BTCExtractActivity.this, PayPassWordActivity.class);
                            startActivity(intent);
                            return;
                        }

                        showPayPassWordDialog();


                    } else {
                        showToast(obj.getString("msg"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    private void showPayPassWordDialog() {
        android.app.AlertDialog.Builder customizeDialog = new android.app.AlertDialog.Builder(this);
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

    private void sendVerificationCode(String phone) {
        Map<String, String> param = new LinkedHashMap<>();
        param.put("phone", phone);

        showLoadingDialog();
        NetUtils.get(Url.verificationCode, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "sendVerificationCode onError: " + e.toString());
                showToast(getString(R.string.a537));
                dismissDialog();
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "onResponse: "+response);
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

    private class MyTask extends TimerTask {
        int num = 60;

        @Override
        public void run() {
            num--;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (num == 0) {
                        getVc.setClickable(true);
                        getVc.setText(getString(R.string.a18));
                        getVc.setTextColor(getResources().getColor(R.color.textColor2));
                        cancel();
                    } else {
                        getVc.setText(getString(R.string.a437, num));
                        getVc.setTextColor(getResources().getColor(R.color.text_color_gray));
                        getVc.setClickable(false);
                    }
                }
            });
        }
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

                        extract();

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

    private void extract() {
        String userId = AppUtils.getUserId();
        String address = etAddress.getText().toString();
        String quantity = etQuantity.getText().toString();

        Map<String, String> param = new LinkedHashMap<>();
        param.put("address", address);
        param.put("symbol", currency);
        param.put("account", quantity);
        param.put("comment", "");
        param.put("userid", userId);

        showLoadingDialog();
        NetUtils.postString(Url.transferToAddress, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "extract onError: " + e.toString());
                showToast(getString(R.string.a537));
                dismissDialog();
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "extract onResponse: " + response);
                dismissDialog();
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        showToast(obj.getString("msg"));

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


    //===================================================================权限申请====================================================================//
    // 提示用户该请求权限的弹出框
    private void showDialogTipUserRequestPermission() {

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.a268))
                .setMessage(getString(R.string.a269))
                .setPositiveButton(getString(R.string.a270), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermission();
                    }
                })
                .setNegativeButton(getString(R.string.a199), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(false).show();
    }

    // 开始提交请求权限
    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 321);
    }

    // 用户权限 申请 的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSettting();
                    }
                } else {
                    showToast(getString(R.string.a271));
                    Intent intent = new Intent(this, CommonScanActivity.class);
                    intent.putExtra(Constants.REQUEST_SCAN_MODE, Constants.REQUEST_SCAN_MODE_QRCODE_MODE);
                    startActivity(intent);
                }
            }
        }
    }

    // 提示用户去应用设置界面手动开启权限
    private void showDialogTipUserGoToAppSettting() {

        dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.a268))
                .setMessage(getString(R.string.a272))
                .setPositiveButton(getString(R.string.a270), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        goToAppSetting();
                    }
                })
                .setNegativeButton(getString(R.string.a199), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(false).show();
    }

    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);

        startActivityForResult(intent, 123);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 提示用户应该去应用设置界面手动开启权限
                    showDialogTipUserGoToAppSettting();
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }

                    showToast(getString(R.string.a271));
                    Intent intent = new Intent(this, CommonScanActivity.class);
                    intent.putExtra(Constants.REQUEST_SCAN_MODE, Constants.REQUEST_SCAN_MODE_QRCODE_MODE);
                    startActivity(intent);
                }
            }
        } else if (requestCode == 11) {
            if (data != null) {
                String codeResult = data.getStringExtra("result");
                etAddress.setText(codeResult);
                etAddress.setSelection(codeResult.length());
            }

        }


    }

//===========================================================================权限申请==============================================================//


}
