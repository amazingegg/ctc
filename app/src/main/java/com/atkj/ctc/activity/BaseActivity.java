package com.atkj.ctc.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDailog;
import com.atkj.ctc.R;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public class BaseActivity extends AppCompatActivity {

    private FrameLayout container;
    private LoadingDailog loadingDailog;
    private boolean isExit;
    private Dialog tipsDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        container = findViewById(R.id.fl_container);
    }


    protected void setChildView(int layout) {
        View view = LayoutInflater.from(this).inflate(layout, null, false);
        setChildView(view);
    }

    protected void setChildView(View view) {
        container.removeAllViews();
        container.addView(view);
    }

    protected void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    public void showLoadingDialog(){
        showLoadingDialog(getString(R.string.a259));
    }

    public void showLoadingDialog(String msg){
        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(this)
                .setMessage(msg)
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





}
