package com.atkj.ctc.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atkj.ctc.R;
import com.atkj.ctc.api.ErrorCallback;
import com.atkj.ctc.scancode.zxing.encode.EncodingHandler;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.DownloadService;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.google.zxing.WriterException;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2018/1/31 0031.
 */

public class BTCRechargeActivity extends ToobarActivity {


    private static final java.lang.String TAG = "BTCRechargeActivity";
    @BindView(R.id.tips)
    TextView tips;
    @BindView(R.id.btc_address)
    TextView btcAddress;
    @BindView(R.id.erweima)
    ImageView image;

    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private AlertDialog dialog;
    private LoadService loadService;
    private String currency;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_btc_recharge);
        ButterKnife.bind(this);


        init();


    }

    private void init() {
        currency = getIntent().getStringExtra("currency");
        setToobarTitle(getString(R.string.a273,currency));


        tips.setText(getString(R.string.a274,currency,currency));

        loadService = LoadSir.getDefault().register(this, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                getWalletAddress();
            }
        });

        getWalletAddress();

    }

    public static void startActivity(String currency, Activity activity){
        Intent intent = new Intent(activity,BTCRechargeActivity.class);
        intent.putExtra("currency",currency);
        AppUtils.getContext().startActivity(intent);
    }


    private void getWalletAddress() {
        String userId = AppUtils.getUserId();
        Map<String,String> param = new LinkedHashMap<>();
        param.put("symbol",currency);
        param.put("userid",userId);

        NetUtils.get(Url.getWalletAddress, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getWalletAddress onError: "+e.toString());
                showToast(getString(R.string.a537));
                loadService.showCallback(ErrorCallback.class);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getWalletAddress onResponse: "+response);

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200){
                        loadService.showSuccess();

                        btcAddress.setText(obj.getString("obj"));
                        Bitmap code = create2Code(btcAddress.getText().toString());
                        image.setImageBitmap(code);

                    }else {
                        loadService.showCallback(ErrorCallback.class);
                        showToast(obj.getString("msg"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


    }

    /**
     * 生成二维码
     * @param key
     */
    private Bitmap create2Code(String key) {
        Bitmap qrCode=null;
        try {
            qrCode= EncodingHandler.create2Code(key,AppUtils.dip2px(this,200));
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return qrCode;
    }

    @OnClick({R.id.save, R.id.copy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.save:

                // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 检查该权限是否已经获取
                    int i = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        // 如果没有授予该权限，就去提示用户请求
                        showDialogTipUserRequestPermission();

                    } else {
                        //Bitmap bitmap = ((BitmapDrawable) (image).getDrawable()).getBitmap();
                        //MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", "description");
                        //sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File("/sdcard/Boohee/image.jpg"))));
                        // sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
                        saveImage();


                    }
                }

                break;
            case R.id.copy:
                AppUtils.copyClipboard(btcAddress.getText().toString());
                showToast(getString(R.string.a275));
                break;
        }


    }

    private void saveImage() {

        boolean b = saveImageToGallery2(this, ((BitmapDrawable) (image).getDrawable()).getBitmap());
        if (b) showToast(getString(R.string.a276));

    }


    //保存文件到指定路径
    public boolean saveImageToGallery2(Context context, Bitmap bmp) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ctcimage";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
           // MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //===================================================================权限申请====================================================================//
    // 提示用户该请求权限的弹出框
    private void showDialogTipUserRequestPermission() {

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.a277))
                .setMessage(getString(R.string.a278))
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
                    saveImage();
                }
            }
        }
    }

    // 提示用户去应用设置界面手动开启权限
    private void showDialogTipUserGoToAppSettting() {

        dialog = new AlertDialog.Builder(this)
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

                    Toast.makeText(this, getString(R.string.a271), Toast.LENGTH_SHORT).show();
                    saveImage();
                }
            }
        }

    }

//===========================================================================权限申请==============================================================//



}
