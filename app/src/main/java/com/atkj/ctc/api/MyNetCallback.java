package com.atkj.ctc.api;

import android.util.Log;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/12/18 0018.
 */

public abstract class MyNetCallback<T> extends Callback<T> {

   /* private static final String TAG = "MyNetCallback";
    private Class<T> clazz;

    @Override
    public T parseNetworkResponse(Response response, int id) throws IOException {
        Log.d(TAG, "parseNetworkResponse: "+response.body().string());
        String json = response.body().string();

        Gson gson = new Gson();
        BaseBean baseBean = gson.fromJson(json, BaseBean.class);

        Log.d(TAG, "BaseBean= : "+baseBean.toString());

         T bean = (T) baseBean.getObj();
        Log.d(TAG, "T=: "+ bean.toString());
        //T bean = AppUtils.getGson().fromJson(json,clazz.getGenericSuperclass());


        return bean;
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        onFailure(call,e,id);
    }

    @Override
    public void onResponse(T response, int id) {
        onSuccess(response,id);
    }

    public abstract void onFailure(Call call,Exception e,int id);
    public abstract void onSuccess(T response,int id);*/


}
