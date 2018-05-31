package com.atkj.ctc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.bean.UserBean;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.views.MyItemView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/2 0002.
 */

public class AccountNumberBindActivity extends ToobarActivity {

    @BindView(R.id.miv_zfb)
    MyItemView zfb;
    @BindView(R.id.miv_wx)
    MyItemView wx;
    @BindView(R.id.miv_yl)
    MyItemView yl;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_account_number_bind);
        ButterKnife.bind(this);
        setToobarTitle(getString(R.string.a202));

        init();

    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context,AccountNumberBindActivity.class);
        context.startActivity(intent);

    }

    private void init() {
        zfb.setDrawableLeft(R.drawable.zfb);
        wx.setDrawableLeft(R.drawable.weixin);
        yl.setDrawableLeft(R.drawable.yinlian);

        String alipayaccount = AppUtils.getUser().getAlipayaccount();
        if (TextUtils.isEmpty(alipayaccount)){
            zfb.setInfo(getString(R.string.a4));
        }else {
            zfb.setInfo("");
        }

        String wechatid = AppUtils.getUser().getWechatid();
        if (TextUtils.isEmpty(wechatid)){
            wx.setInfo(getString(R.string.a4));
        }else {
            wx.setInfo("");
        }

        List<UserBean.ObjBean.BankcardBean> bankcard = AppUtils.getUser().getBankcard();
        if (bankcard == null || bankcard.size() == 0){
            yl.setInfo(getString(R.string.a4));
        }else {
            yl.setInfo("");
        }

    }

    @OnClick({R.id.miv_zfb,R.id.miv_wx,R.id.miv_yl})
    public void OnViewClicked(View view){
        Intent intent = new Intent(this,AccountNumberBindDetailActivity.class);
        switch (view.getId()){
            case R.id.miv_zfb:
                intent.putExtra("type", Constants.BIND_TYPE_ZFB);
                break;
            case R.id.miv_wx:
                intent.putExtra("type", Constants.BIND_TYPE_WX);
                break;
            case R.id.miv_yl:
                intent = new Intent(this,AddBankCardActivity.class);
               break;
        }

        startActivityForResult(intent,11);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 11){
            if (resultCode == RESULT_OK){
                int type = data.getIntExtra("type", 0);
                if (type == Constants.BIND_TYPE_ZFB){
                    zfb.setInfo("");
                }else if(type == Constants.BIND_TYPE_WX){
                    wx.setInfo("");
                }else if (type == Constants.BIND_TYPE_YL){
                    yl.setInfo("");
                }


            }

        }
    }
}
