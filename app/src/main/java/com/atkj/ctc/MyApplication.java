package com.atkj.ctc;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import com.atkj.ctc.api.EmptyCallback;
import com.atkj.ctc.api.ErrorCallback;
import com.atkj.ctc.api.LoadingCallback;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.views.MyClassicsHeader;
import com.google.gson.Gson;
import com.kingja.loadsir.core.LoadSir;
import com.mob.MobSDK;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by yuanpan on 2017/7/19.
 */

public class MyApplication extends Application {

    public static Gson mGson;
    public static Handler mHandler;
    public static Context mContext;

    static {
        /*//设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });*/
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                //指定为经典Header，默认是 贝塞尔雷达Header

                return new MyClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });

        /*//设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });*/
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mGson = new Gson();
        mHandler = new Handler();
        mContext = getApplicationContext();

        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())//添加各种状态页
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                /*.addCallback(new TimeoutCallback())
                .addCallback(new CustomCallback())*/
                .setDefaultCallback(LoadingCallback.class)//设置默认状态页
                .commit();
        //分享
        MobSDK.init(this);
        //Bugly 崩溃检测
        CrashReport.initCrashReport(getApplicationContext(), "3fff0eb88a", true);

        for (int i = 0; i < 3; i++) {
            AppUtils.lastPrice.add(0.0);
        }


    }

}
