package com.atkj.ctc.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.android.tu.loadingdialog.LoadingDailog;
import com.atkj.ctc.R;
import com.atkj.ctc.views.MyToolBar;

/**
 * Created by Administrator on 2017/12/14 0014.
 */

public class ToobarActivity extends BaseActivity {

    private static final String TAG = "ToobarActivity";
    private FrameLayout container;
    private MyToolBar toolBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildView(R.layout.activity_toobar);

        initView();
        initListener();


    }

    private void initListener() {
        toolBar.setOnBackClickListener(new MyToolBar.OnBackClickListener() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
    }

    private void initView() {
        container = findViewById(R.id.container);
        toolBar = findViewById(R.id.toobar);
    }


    protected void setChildLayout(int layout) {
        View view = LayoutInflater.from(this).inflate(layout, null, false);
        setChildLayout(view);
    }

    protected void setChildLayout(View view) {
        container.removeAllViews();
        container.addView(view);
    }

    protected void setToobarTitle(String title){
        toolBar.setTitle(title);
    }

    public MyToolBar getToolBar(){
        return toolBar;
    }




}
