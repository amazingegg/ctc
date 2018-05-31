package com.atkj.ctc;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atkj.ctc.activity.BaseActivity;
import com.atkj.ctc.activity.LoginActivity;
import com.atkj.ctc.bean.UserBean;
import com.atkj.ctc.bean.VersionBean;
import com.atkj.ctc.fragment.AssetsFragment;
import com.atkj.ctc.fragment.BaseFragment;
import com.atkj.ctc.fragment.HomeFragment;
import com.atkj.ctc.fragment.InformationFragment;
import com.atkj.ctc.fragment.MyFragment;
import com.atkj.ctc.fragment.transaction.MarketTransactionContentFragment;
import com.atkj.ctc.fragment.transaction.P2PTransactionFragment;
import com.atkj.ctc.fragment.transaction.TransactionFragment;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.DownloadService;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class MainActivity extends BaseActivity {

    private static final int REQUEST_LOGIN = 1;
    private static final String TAG = "MainActivity";
    @BindView(R.id.rg_main)
    RadioGroup rgMain;


    //装fragment的实例集合
    private ArrayList<BaseFragment> fragments;

    private int position = 0;
    //缓存Fragment或上次显示的Fragment
    private Fragment tempFragment;
    private PopupWindow pop;
    private AlertDialog versionUpdateDialog;
    // 要申请的权限
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private AlertDialog tipUserDialog;
    private AlertDialog integralDialog;
    private android.app.AlertDialog exitDialog;
    private int isExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //初始化Fragment
        initFragment(savedInstanceState);
        //设置RadioGroup的监听
        initListener();

        login();
        checkVersion();

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isExit++;
            if (isExit == 1) {
                showToast(getString(R.string.a469));
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        isExit = 0;
                    }
                }, 2500);
            }

            if (isExit == 2) {
                exit();
            }

            return false;
        }

        return super.onKeyDown(keyCode, event);

    }


    private void exit() {
        finish();
        System.exit(0);
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

                        float currentCode = AppUtils.getVersionCode(MainActivity.this);
                        float serverCode = bean.getObj().getVersion();

                        AppUtils.isUpdate = serverCode > currentCode;
                        AppUtils.updateInfo = bean;

                        if (AppUtils.isUpdate) {
                            showVersionUpdateDialog();
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtils.d(TAG, "checkVersion catch: " + e.getMessage());
                }
            }
        });

    }

    private void checkIsAndroidO() {
        if (Build.VERSION.SDK_INT >= 26) {
            boolean b = getPackageManager().canRequestPackageInstalls();
            if (b) {
                showToast(getString(R.string.a386));

                Intent intent = new Intent(MainActivity.this, DownloadService.class);
                startService(intent);
            } else {
                //请求安装未知应用来源的权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 26);
            }
        } else {
            showToast(getString(R.string.a386));

            Intent intent = new Intent(MainActivity.this, DownloadService.class);
            startService(intent);
        }

    }

    private void showVersionUpdateDialog() {
        AlertDialog.Builder customizeDialog = new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_download, null);
        TextView update = dialogView.findViewById(R.id.tv_update);
        TextView cancel = dialogView.findViewById(R.id.tv_cancel);
        TextView tvRemark = dialogView.findViewById(R.id.remark);
        TextView tvRemark1 = dialogView.findViewById(R.id.remark1);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 检查该权限是否已经获取
                    int i = ContextCompat.checkSelfPermission(MainActivity.this, permissions[0]);
                    // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        // 如果没有授予该权限，就去提示用户请求
                        showDialogTipUserRequestPermission();

                        versionUpdateDialog.dismiss();
                    } else {
                        checkIsAndroidO();
                        versionUpdateDialog.dismiss();
                    }
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
        TextView title = new TextView(this);
        title.setGravity(Gravity.CENTER_HORIZONTAL);
        title.setPadding(30, 30, 30, 30);
        String title1 = AppUtils.updateInfo.getObj().getTitle();
        String remark = AppUtils.updateInfo.getObj().getRemake();
        String remark1 = AppUtils.updateInfo.getObj().getPrompt();

        tvRemark.setText(remark);
        tvRemark1.setText(remark1);
        title.setText(getString(R.string.a387) + title1);
        title.setTextColor(getResources().getColor(R.color.f000000));
        title.setTextSize(24);

        customizeDialog.setCustomTitle(title);
        customizeDialog.setView(dialogView);
        customizeDialog.setCancelable(false);

        versionUpdateDialog = customizeDialog.show();

    }

    private void checkLogin() {
        if (!AppUtils.isLogin) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public View getNavigationBottom() {
        return findViewById(R.id.ll_daohang);
    }


    private void login() {
        UserBean userBean = AppUtils.getUserBean();
        SharedPreferences sp = getSharedPreferences(Constants.SP_USER_INFO, Context.MODE_PRIVATE);
        boolean isLogin = sp.getBoolean(Constants.IS_LOGIN, false);
        if (userBean != null && isLogin) {
            AppUtils.userBean = userBean;
            AppUtils.isLogin = true;

            SharedPreferences.Editor edit = sp.edit();
            edit.putBoolean(Constants.IS_LOGIN,true);
            edit.apply();

        } else {
            checkLogin();
        }
    }

    private void showLoginPopu() {

        if (!AppUtils.isLogin) {
            View content = LayoutInflater.from(this).inflate(R.layout.pop_assets, null);
            pop = new PopupWindow(content, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);

            TextView login = content.findViewById(R.id.bt_login);
            TextView register = content.findViewById(R.id.bt_register);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    pop.dismiss();
                }
            });
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtra("isRegist", true);
                    startActivity(intent);
                    pop.dismiss();
                }
            });
            pop.setFocusable(false);
            pop.setOutsideTouchable(false);
            //pop.setAnimationStyle(R.style.animTranslate);
            pop.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));

            pop.showAtLocation(getNavigationBottom(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,
                    getNavigationBottom().getMeasuredHeight());
           /* WindowManager.LayoutParams attributes = getActivity().getWindow().getAttributes();
            attributes.alpha = 0.5f;
            getActivity().getWindow().setAttributes(attributes);*/

        }

    }

    private void initListener() {
        check(0, R.id.rb_home);

        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_home: //首页
                        position = 0;
                        if (pop != null) {
                            if (pop.isShowing()) pop.dismiss();
                        }
                        break;
                    case R.id.rb_p2p://点对点
                        position = 1;
                        if (pop != null) {
                            if (pop.isShowing()) pop.dismiss();
                        }
                        break;
                    case R.id.rb_transaction: //币币交易
                        position = 2;
                        if (pop != null) {
                            if (pop.isShowing()) pop.dismiss();
                        }
                        break;
                    case R.id.rb_assets: //资产
                        position = 3;
                        showLoginPopu();
                        break;
                    case R.id.rb_my: //我的
                        position = 4;
                        if (pop != null) {
                            if (pop.isShowing()) pop.dismiss();
                        }
                        break;
                    default:
                        position = 0;
                        if (pop != null) {
                            if (pop.isShowing()) pop.dismiss();
                        }
                        break;
                }
                //根据位置得到相应的Fragment
                BaseFragment baseFragment = getFragment(position);
                /**
                 * 第一个参数: 上次显示的Fragment
                 * 第二个参数: 当前正要显示的Fragment
                 */
                switchFragment(tempFragment, baseFragment);
            }
        });


        //修改底部导航栏图标大小
        int[] drawables = {R.drawable.home_button_selector, R.drawable.p2p_button_selector
                , R.drawable.transaction_button_selector,
                R.drawable.assets_button_selector, R.drawable.my_button_selector};
        for (int i = 0; i < drawables.length; i++) {
            Drawable drawable = getResources().getDrawable(drawables[i]);
            drawable.setBounds(0, 0, AppUtils.dip2px(this, 22),
                    AppUtils.dip2px(this, 22));

            RadioButton childAt = (RadioButton) rgMain.getChildAt(i);
            childAt.setCompoundDrawables(null, drawable, null, null);
        }


    }

    /**
     * 添加的时候按照顺序
     *
     * @param savedInstanceState
     */
    private void initFragment(Bundle savedInstanceState) {
        //FragmentManager fragmentManager = getSupportFragmentManager();

        fragments = new ArrayList<>();

        fragments.add(HomeFragment.newInstance("HomeFragment"));
        fragments.add(P2PTransactionFragment.newInstance("P2PTransactionFragment"));
        //fragments.add(TransactionFragment.newInstance("TransactionFragment"));
        fragments.add(MarketTransactionContentFragment.newInstance("C2CTransactionFragment"));
        fragments.add(AssetsFragment.newInstance("AssetsFragment"));
        fragments.add(MyFragment.newInstance("MyFragment"));

        /*LogUtils.d(TAG, "initFragment: savedInstanceState");
        fragments.add((HomeFragment)fragmentManager.findFragmentByTag("HomeFragment"));
        fragments.add((P2PTransactionFragment)fragmentManager.findFragmentByTag("P2PTransactionFragment"));
        fragments.add((MarketTransactionContentFragment)fragmentManager.findFragmentByTag("MarketTransactionContentFragment"));
        fragments.add((AssetsFragment)fragmentManager.findFragmentByTag("AssetsFragment"));
        fragments.add((MyFragment)fragmentManager.findFragmentByTag("MyFragment"));*/

    }

    /**
     * 根据位置得到对应的 Fragment
     *
     * @param position
     * @return
     */
    private BaseFragment getFragment(int position) {
        if (fragments != null && fragments.size() > 0) {
            return fragments.get(position);
        }
        return null;
    }


    /**
     * 切换Fragment
     *
     * @param fragment
     * @param nextFragment
     */
    private void switchFragment(Fragment fragment, BaseFragment nextFragment) {
        if (tempFragment != nextFragment) {
            tempFragment = nextFragment;
            if (nextFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //判断nextFragment是否添加成功
                if (!nextFragment.isAdded()) {
                    //隐藏当前的Fragment
                    if (fragment != null) {
                        transaction.hide(fragment);
                        //transaction.remove(fragment);
                    }
                    //添加Fragment
                    String fragmentTag = nextFragment.getFragmentTag();
                    transaction.add(R.id.frameLayout, nextFragment, fragmentTag).commit();
                } else {
                    //隐藏当前Fragment
                    if (fragment != null) {
                        transaction.hide(fragment);
                        // transaction.remove(fragment);
                    }
                    transaction.show(nextFragment).commit();
                    //transaction.add(R.id.frameLayout, nextFragment).commit();
                }
            }
        }
    }

    //========================================================权限代码==========================================================================//

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
                    Intent intent = new Intent(MainActivity.this, DownloadService.class);
                    startService(intent);
                }
            }
        }
    }

    // 提示用户去应用设置界面手动开启权限
    private void showDialogTipUserGoToAppSettting() {

        tipUserDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle(getString(R.string.a277))
                .setMessage(getString(R.string.a279))
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
                        finish();
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


        if (resultCode == RESULT_OK) {
            if (requestCode == 123) {

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 检查该权限是否已经获取
                    int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                    // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        // 提示用户应该去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSettting();
                    } else {

                        showToast(getString(R.string.a389));
                        Intent intent = new Intent(MainActivity.this, DownloadService.class);
                        startService(intent);
                    }
                }
            } else if (requestCode == REQUEST_LOGIN) {
                if (data != null) {
                    long vipendtime = data.getLongExtra("vipendtime", 0);
                    if (vipendtime != 0) {
                        shouIntegralDialog(vipendtime);
                    }
                }
            } else if (requestCode == 3) {
                check(2, R.id.rb_transaction);
            } else if (requestCode == 4) {
                check(1, R.id.rb_p2p);
            } else if (requestCode == 26) {
                showToast(getString(R.string.a386));

                Intent intent = new Intent(MainActivity.this, DownloadService.class);
                startService(intent);
            }
        }


    }

    private void shouIntegralDialog(long time) {
        final AlertDialog.Builder customizeDialog = new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_main_integral, null);
        TextView endtime = dialogView.findViewById(R.id.tv_endtime);
        endtime.setText(getString(R.string.a435) + AppUtils.timedate(time, "yyyy-MM-dd"));
        TextView confirm = dialogView.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (integralDialog != null) {
                    if (integralDialog.isShowing()) {
                        integralDialog.dismiss();
                    }
                }
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
        integralDialog = customizeDialog.show();

    }


    public void check(int position, int resid) {
        rgMain.check(resid);
        switchFragment(tempFragment, getFragment(position));
    }


}
