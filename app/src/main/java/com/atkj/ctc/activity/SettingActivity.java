package com.atkj.ctc.activity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atkj.ctc.MainActivity;
import com.atkj.ctc.R;
import com.atkj.ctc.bean.VersionBean;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.DownloadService;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.views.MyItemView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/12/22 0022.
 */

public class SettingActivity extends ToobarActivity {


    @BindView(R.id.new_version)
    TextView tvNew;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_sign_out)
    TextView singOut;

    @BindView(R.id.miv_paypassword)
    MyItemView payPassWord;
    @BindView(R.id.miv_phone)
    MyItemView mivPhone;

    // 要申请的权限
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private AlertDialog dialog;
    private String appName;
    private AlertDialog downLoadDialog;
    private ProgressBar progressBar;
    private DownloadManager downloadManager;
    private long mTaskId;
    private DownloadService.DownloadBinder downloadBinder;
    private java.lang.String TAG = "SettingActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_setting);
        ButterKnife.bind(this);
        setToobarTitle(getString(R.string.a235));

        init();
    }


    private void init() {
        if (AppUtils.isLogin) {
            String paypassword = AppUtils.getUser().getPaypassword();
            payPassWord.isShowInfo(paypassword == null || "".equals(paypassword));
            String phone = AppUtils.getUser().getPhone();
            mivPhone.setInfo(AppUtils.splitPhone(phone));
            singOut.setText(getString(R.string.a176));

        } else {
            payPassWord.isShowInfo(true);
            mivPhone.setInfo("");
            singOut.setText(getString(R.string.a80));
        }

        tvNew.setVisibility(AppUtils.isUpdate ? View.VISIBLE : View.GONE);
        tvVersion.setText(AppUtils.getVerName(this));

    }


    @OnClick({/*R.id.miv_language,*/ R.id.tv_sign_out, R.id.rl_about, R.id.rl_update,
            R.id.miv_paypassword, R.id.miv_phone,
            R.id.miv_verified, R.id.miv_modifypwd
            , R.id.miv_personal_info})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {

            /*case R.id.miv_language://语言选择
                intent = new Intent(this,LanguageActivity.class);
                startActivity(intent);

                break;*/

            case R.id.miv_paypassword://设置支付密码
                if (AppUtils.checkLogin(this)) return;

                intent = new Intent(this, PayPassWordActivity.class);
                startActivity(intent);
                break;
            case R.id.miv_verified://实名认证
                if (AppUtils.checkLogin(this)) return;

                intent = new Intent(this, VerifiedActivity.class);
                startActivity(intent);
                break;

            case R.id.miv_modifypwd://修改密码
                if (AppUtils.checkLogin(this)) return;

                intent = new Intent(this, ModifyPwdActivity.class);
                startActivity(intent);
                break;
            case R.id.miv_personal_info://个人信息
                if (AppUtils.checkLogin(this)) return;

                intent = new Intent(this, PersonalInfoActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_sign_out:
                signOut();
                break;
            case R.id.rl_about:
                AboutActivity.actionStart(this);
                break;

            case R.id.rl_update:
                checkVersion();
                break;
        }


    }

    private void checkVersion() {

        Map<String, String> param = new LinkedHashMap<>();
        param.put("clienttype", Constants.CLIENT_TYPE_ANDROID);
        NetUtils.get(Url.checkVersion, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "checkVersion onError: " + e.toString());
                showToast(getString(R.string.a537));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d("checkVersion onResponse== ", response);

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        VersionBean bean = AppUtils.getGson().fromJson(response, VersionBean.class);

                        float currentCode = AppUtils.getVersionCode(SettingActivity.this);
                        float serverCode = bean.getObj().getVersion();

                        AppUtils.isUpdate = serverCode > currentCode;
                        AppUtils.updateInfo = bean;
                        if (!AppUtils.isUpdate) {
                            showToast(getString(R.string.a385));
                        } else {
                            showDialog();
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtils.d(TAG, "checkVersion catch: " + e.getMessage());
                }
            }
        });

    }



    private void showDialog() {
        AlertDialog.Builder customizeDialog = new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_download, null);
        LinearLayout update = dialogView.findViewById(R.id.rl_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 检查该权限是否已经获取
                    int i = ContextCompat.checkSelfPermission(SettingActivity.this, permissions[0]);
                    // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        // 如果没有授予该权限，就去提示用户请求
                        showDialogTipUserRequestPermission();

                        downLoadDialog.dismiss();
                    } else {
                        showToast(getString(R.string.a386));

                        Intent intent = new Intent(SettingActivity.this, DownloadService.class);
                        startService(intent);
                        downLoadDialog.dismiss();
                    }
                }

            }
        });


        TextView title = new TextView(this);
        title.setGravity(Gravity.CENTER_HORIZONTAL);
        title.setPadding(30, 30, 30, 30);
        String version = AppUtils.updateInfo.getObj().getTitle();
        title.setText(getString(R.string.a387) + version);
        title.setTextColor(getResources().getColor(R.color.f000000));
        title.setTextSize(24);
        customizeDialog.setCustomTitle(title);
        customizeDialog.setView(dialogView);

        downLoadDialog = customizeDialog.show();

    }

    //===================================================================权限申请====================================================================//
    // 提示用户该请求权限的弹出框
    private void showDialogTipUserRequestPermission() {

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.a277))
                .setMessage(getString(R.string.a388))
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
                    showToast(getString(R.string.a389));
                    Intent intent = new Intent(SettingActivity.this, DownloadService.class);
                    startService(intent);
                }
            }
        }
    }

    // 提示用户去应用设置界面手动开启权限
    private void showDialogTipUserGoToAppSettting() {

        dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.a277))
                .setMessage(getString(R.string.a390))
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

                    Toast.makeText(this, "权限获取成功,开始下载APP", Toast.LENGTH_SHORT).show();
                    //showToast("权限获取成功,开始下载APP");
                    Intent intent = new Intent(SettingActivity.this, DownloadService.class);
                    startService(intent);
                }
            }
        } else if (requestCode == 22) {

            singOut.setText(getString(R.string.a176));


        }


    }

//===========================================================================权限申请==============================================================//

    private void signOut() {
        if (AppUtils.isLogin) {
            String userid = AppUtils.getUserId();

            Map<String,String> param = new LinkedHashMap<>();
            param.put("userid",userid);

            showLoadingDialog();
            NetUtils.put(Url.loginOut, param, new NetUtils.StringCallBack() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    LogUtils.d(TAG, "signOut onError: "+e.toString());
                    dismissDialog();
                }

                @Override
                public void onResponse(String response, int id) {
                    LogUtils.d(TAG, "signOut onResponse: "+response);
                    LogUtils.d(response);
                    dismissDialog();
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getInt("status") == 200) {
                            AppUtils.isLogin = false;

                            SharedPreferences sp = getSharedPreferences(Constants.SP_USER_INFO, Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = sp.edit();
                            edit.putBoolean(Constants.IS_LOGIN, false);

                            edit.apply();

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


        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, 22);
        }

    }


}
