package com.atkj.ctc.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atkj.ctc.MainActivity;
import com.atkj.ctc.R;
import com.atkj.ctc.adapter.NoviceTutorialAdapter;
import com.atkj.ctc.bean.NoviceBean;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.DownloadService;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/27 0027.
 */

public class NoviceTutorialActivity extends ToobarActivity implements BaseQuickAdapter.OnItemClickListener {


    @BindView(R.id.rv1)
    RecyclerView rv1;

    @BindView(R.id.rv2)
    RecyclerView rv2;

    private List<NoviceBean> mFDataList;
    private List<String> mQDataList = new ArrayList<>();
    private NoviceTutorialAdapter adapter;
    private AlertDialog tutorialDialog;


    private String[] f = {"打开登录界面，点击右边注册按钮，输入手机号、验证码、密码即可注册成功",
    "可以在登录界面点击忘记密码，通过输入手机号获取验证码进行验证，并修改密码",
    "点击我的发布，点击交易中，点击订单右下角的删除即可删除该订单，如果订单处于交易中，则无法取消订单。",
            "点击“我的订单”，点击“未付款”选项，在已付款的订单右下角点击“确认付款”，卖方将收到您已付款的通" +
                    "知，当其确认付款后，会将数字资产放行给您，届时您就可以在账户中查看您的数字资产。",
            "一般情况下，卖家通常都会在收到付款后，在约定好的时间内尽快放行数字资产。如果您已经付款，但仍在等待，则无需担心，因为所以的在线交易均收到托管保护，卖家无法取走您的数字资产。" +
                    "如果交易出现任何问题，卖家不放行您的数字资产，您可以对交易发起申诉，点击“我的订单”中查看想要完成的订单，点击“申诉”，并在申诉界面提交您的交易问题，平台官方将会协助您尽快解决交易问题。",
            "账户中没有币只可以发布买单，但是不可以发布卖单。",
            "买方拍下订单后会限时30分钟内完成付款并点击确认，超时订单将重新挂出。" +
                    "如果买方30分钟内付款但是没有点击确认，导致订单重新挂出，可提交转账记录进行申诉处理。",
            "点击“交易界面”，在右下角的“+”点击选择要发布买单还是卖单",
            "点击“我的”界面，选择“修改密码”选项，可通过手机号验证的方式进行密码修改。",
            "点击“我的”界面，选择“实名认证”选项，可以进行身份证实名认证和进行账号绑定"
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_novice_tutorial);
        ButterKnife.bind(this);
        setToobarTitle("新手教程");


        initData();
        initEvent();

    }

    private void initData() {
        String[] q = {"如何注册CTC平台账号？",
                "CTC点对点交易平台登录密码忘记，怎么办？",
                "点对点交易下单后，不想继续交易了，怎么办？",
                "我已经付款，如何获得数字资产？",
                "我已经完成付款并点击了“确认付款”，但是过了很久卖家依旧未放行数字货币，该怎么办呢？",
                "账户中没有币，是否可以发布订单？",
                "在点对点交易中，买方拍下后一直未付款，如何处理？",
                "如何发布买单或卖单",
                "如何修改账户密码？",
                "如何进行实名认证和账号绑定？"};
        mQDataList.addAll(Arrays.asList(q));
    }

    private void initEvent() {

        rv1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new NoviceTutorialAdapter(R.layout.list_item_novice_tutorial, mQDataList);
        rv1.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv1.setAdapter(adapter);

        adapter.setOnItemClickListener(this);



        //rv1.setAdapter(adapter);


    }

    private void showTutorialDialog(String str) {
        AlertDialog.Builder customizeDialog = new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_tutorial, null);
        TextView content = dialogView.findViewById(R.id.text);
        content.setText(str);

        TextView title = new TextView(this);
        title.setText("");
        customizeDialog.setCustomTitle(title);
        customizeDialog.setView(dialogView);
        customizeDialog.setPositiveButton("",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        tutorialDialog = customizeDialog.show();

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        showTutorialDialog(f[position]);

    }


}
