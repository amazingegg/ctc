package com.atkj.ctc.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.atkj.ctc.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/28 0028.
 */

public class CTSDetail extends ToobarActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_cts_detail);
        ButterKnife.bind(this);

        setToobarTitle(getString(R.string.a534));


    }




    public static void actionStart(Context context){
        Intent intent = new Intent(context,CTSDetail.class);
        context.startActivity(intent);
    }

    public static void actionStartForResult(Activity context){
        Intent intent = new Intent(context,CTSDetail.class);
        context.startActivityForResult(intent,4);
    }





    @OnClick({R.id.bt_buy})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.bt_buy:
                setResult(RESULT_OK);
                finish();
                break;

        }

    }







}
