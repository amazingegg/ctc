package com.atkj.ctc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.atkj.ctc.R;
import com.atkj.ctc.utils.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/20 0020.
 */

public class TranslucentActivity extends BaseActivity{


    @BindView(R.id.iv_sell)
    ImageView ivSell;
    @BindView(R.id.iv_buy)
    ImageView ivBuy;
    @BindView(R.id.iv_close)
    ImageView ivClose;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translucent_activity);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.iv_sell,R.id.iv_buy,R.id.iv_close})
    public void onViewClicked(View view){
        switch (view.getId()){

            case R.id.iv_buy:
                if (AppUtils.checkLogin(this)) return;
                Intent buyIntent = new Intent(this, PostBuyActivity.class);
                buyIntent.putExtra("isBuy", true);
                startActivity(buyIntent);
                finish();
                break;
            case R.id.iv_sell:
                if (AppUtils.checkLogin(this)) return;

                Intent sellIntent = new Intent(this, PostBuyActivity.class);
                sellIntent.putExtra("isBuy", false);
                startActivity(sellIntent);
                finish();
                break;
            case R.id.iv_close:
                finish();
                overridePendingTransition(0,0);
                break;
        }

    }
}
