package com.atkj.ctc.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.atkj.ctc.BuildConfig;
import com.atkj.ctc.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

/**
 * Created by Administrator on 2017/12/29 0029.
 */

public class DownloadService extends Service {


    // 要申请的权限
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private DownloadManager downloadManager;
    private long mTaskId;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




    @Override
    public void onCreate() {
        super.onCreate();

        String url = AppUtils.getUpdateInfo().getObj().getUrl();
        String appName = AppUtils.getUpdateInfo().getObj().getFilename();
        LogUtils.d("downloadAPK==" + "url==" + url + "          appname==" + appName);


        downloadAPK(url,appName);
    }





    public class DownloadBinder extends Binder {

        public void startDownLoad(){

        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }




    //使用系统下载器下载
    private void downloadAPK(String versionUrl, String versionName) {
        //创建下载任务
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(versionUrl));
        request.setAllowedOverRoaming(false);//漫游网络是否可以下载

        //设置文件类型，可以在下载结束后自动打开该文件
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(versionUrl));
        request.setMimeType(mimeString);

        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setVisibleInDownloadsUi(true);

        //sdcard的目录下的download文件夹，必须设置
        request.setDestinationInExternalPublicDir("/Download/", versionName);
        //request.setDestinationInExternalFilesDir(),也可以自己制定下载路径

        //将下载请求加入下载队列
        downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        //加入下载队列后会给该任务返回一个long型的id，
        //通过该id可以取消任务，重启任务等等，看上面源码中框起来的方法
        mTaskId = downloadManager.enqueue(request);

        //注册广播接收者，监听下载状态
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }


    //广播接受者，接收下载状态
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkDownloadStatus();//检查下载状态
        }
    };


    //检查下载状态
    private void checkDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(mTaskId);//筛选下载任务，传入任务ID，可变参数
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                    LogUtils.i(">>>下载暂停");
                case DownloadManager.STATUS_PENDING:
                    LogUtils.i(">>>下载延迟");
                case DownloadManager.STATUS_RUNNING:
                    LogUtils.i(">>>正在下载");
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    LogUtils.i(">>>下载完成");
                    //下载完成安装APK
                    String downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            .getAbsolutePath() + File.separator + AppUtils.getUpdateInfo().getObj().getFilename();





                    installAPK(new File(downloadPath));

                    stopSelf();
                    break;
                case DownloadManager.STATUS_FAILED:
                    LogUtils.i(">>>下载失败");
                    break;
            }
        }
    }



    //下载到本地后执行安装
    private void installAPK(File file) {
        if (!file.exists()) return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Uri uri = Uri.parse("file://" + file.toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileProvider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");

        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        //intent.setDataAndType(uri, "application/vnd.android.package-archive");
        //intent.addCategory("android.intent.category.DEFAULT");
        //在服务中开启activity必须设置flag,后面解释
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
