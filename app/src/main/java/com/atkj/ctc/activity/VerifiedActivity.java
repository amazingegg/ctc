package com.atkj.ctc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.atkj.ctc.R;
import com.atkj.ctc.utils.Constants;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/30 0030.
 */

public class VerifiedActivity extends ToobarActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_verified);
        setToobarTitle(getString(R.string.a170));
        ButterKnife.bind(this);
    }

    @OnClick({R.id.miv_info,R.id.miv_bind})
    public void OnViewClicked(View view){

        Intent intent;

        switch (view.getId()){
            case R.id.miv_info://证件信息
                intent = new Intent(this,VerifiedDetailActivity.class);
                intent.putExtra("type", Constants.ACTIVITY_PAPERWORK_INFO);
                startActivity(intent);

                break;
            case R.id.miv_bind://账号绑定

                AccountNumberBindActivity.actionStart(this);

                break;
        }



    }


























}
