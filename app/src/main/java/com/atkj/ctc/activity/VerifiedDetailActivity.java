package com.atkj.ctc.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/2 0002.
 */

public class VerifiedDetailActivity extends ToobarActivity{


    @BindView(R.id.tips)
    TextView tips;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setChildLayout( R.layout.activity_verified_detail);

        setToobarTitle(getString(R.string.a201));

        ButterKnife.bind(this);

        init();

    }

    private void init() {
        String str = getString(R.string.a409);
        tips.setText(str);



    }


    @OnClick({R.id.bt_enter})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.bt_enter:
                showToast(getString(R.string.a248));//TODO

                break;
        }
    }







}
