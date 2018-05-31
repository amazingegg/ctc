package com.atkj.ctc.utils;


import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.OtherRequestBuilder;

import com.zhy.http.okhttp.callback.StringCallback;


import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class NetUtils {

    private static final java.lang.String TAG = "NetUtils";

    public static void get(final String url, Map<String,String> param, final StringCallBack callBack){
        String timestamp = AppUtils.getTimeStamp();
        Map<String,String> signParam = new LinkedHashMap<>();
        signParam.put("timestamp",timestamp);

        GetBuilder getBuilder = OkHttpUtils.get();
        getBuilder.url(url);
        for (Map.Entry<String, String> entry: param.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            getBuilder.addParams(key,value);
            signParam.put(key,value);
        }

        String sign = AppUtils.getSign(signParam);

        getBuilder.addParams("app_key",Constants.app_key)
                .addParams("sign",sign)
                .addParams("timestamp",timestamp)
                .addHeader("token",AppUtils.getUserToken())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        if (e.toString().contains("403")){
                            AppUtils.login();
                        }

                        callBack.onError(call,e,id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        callBack.onResponse(response,id);
                    }
                });


    }


    public static void postString(String url, Map<String,String> param, final StringCallBack callBack){

        String timestamp = AppUtils.getTimeStamp();
        Map<String,String> signParam = new LinkedHashMap<>();
        signParam.put("timestamp",timestamp);

        for (Map.Entry<String, String> entry: param.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            signParam.put(key,value);
        }

        String sign = AppUtils.getSign(signParam);
        signParam.put("sign",sign);
        signParam.put("app_key",Constants.app_key);

        String json = AppUtils.getGson().toJson(signParam);

        LogUtils.d(TAG, "postString: json ="+json);
        LogUtils.d(TAG, "postString: url ="+url);
        OkHttpUtils.postString()
                .url(url)
                .content(json)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .addHeader("token",AppUtils.getUserToken())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        if (e.getMessage().contains("403")){
                            AppUtils.login();
                        }

                        callBack.onError(call,e,id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        callBack.onResponse(response,id);
                    }
                });



    }

    public static void put(String url, Map<String,String> param, final StringCallBack callBack){
        OtherRequestBuilder put = OkHttpUtils.put();
        builder(put,url,param,callBack);

    }



    public static void delete(String url, Map<String,String> param, final StringCallBack callBack){
        OtherRequestBuilder delete = OkHttpUtils.delete();
        builder(delete,url,param,callBack);
    }


    private static void builder(OtherRequestBuilder builder,String url, Map<String,String> param, final StringCallBack callBack){
        String timestamp = AppUtils.getTimeStamp();
        Map<String,String> signParam = new LinkedHashMap<>();
        signParam.put("timestamp",timestamp);

        builder.url(url);

        for (Map.Entry<String, String> entry: param.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            signParam.put(key,value);
        }

        String sign = AppUtils.getSign(signParam);
        signParam.put("app_key",Constants.app_key);
        signParam.put("sign",sign);

        String json = AppUtils.getGson().toJson(signParam);
        LogUtils.d(TAG, "builder: json="+json);

        builder.requestBody(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json))
                .addHeader("token",AppUtils.getUserToken())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        if (e.getMessage().contains("403")){
                            AppUtils.login();
                        }


                        callBack.onError(call,e,id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        callBack.onResponse(response,id);
                    }
                });


    }



    public interface StringCallBack{

        void onError(Call call,Exception e, int id);
        void onResponse(String response, int id);

    }



}
