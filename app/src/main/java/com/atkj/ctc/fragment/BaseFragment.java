package com.atkj.ctc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDailog;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public abstract class BaseFragment extends Fragment {

    private LoadingDailog loadingDailog;

    /**
     * 当该类被系统创建的时候回调
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView(inflater,container,savedInstanceState);
    }
    //抽象类，由孩子实现，实现不同的效果
    public abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }
    /**
     * 当子类需要联网请求数据的时候，可以重写该方法，该方法中联网请求
     */
    public void initData() {

    }



    public void showToast(String msg){
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }

    public void showLoadingDialog(Context context){
        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(context)
                .setMessage("加载中...")
                .setCancelable(true)
                .setCancelOutside(true);
        loadingDailog = loadBuilder.create();
        loadingDailog.show();
    }

    public void dismissDialog(){
        if (loadingDailog != null){
            if (loadingDailog.isShowing()){
                loadingDailog.dismiss();
            }

        }
    }


    public abstract String getFragmentTag();


}
