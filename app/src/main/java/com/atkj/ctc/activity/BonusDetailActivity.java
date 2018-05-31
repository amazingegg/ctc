package com.atkj.ctc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.androidkun.xtablayout.XTabLayout;
import com.atkj.ctc.R;
import com.atkj.ctc.adapter.MyViewPagerAdapter;
import com.atkj.ctc.fragment.BonusDetailFragment;
import com.atkj.ctc.fragment.ListFragment;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.views.MyToolBar;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2018/4/8 0008.
 */

public class BonusDetailActivity extends ToobarActivity{


    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    XTabLayout tabLayout;




    private ArrayList<Fragment> mFragments;
    private java.lang.String TAG = "BonusDetailActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_bonus_detail);

        ButterKnife.bind(this);
        setToobarTitle(getString(R.string.a495));
        initEvent();

    }



    public static void actionStart(Context context){
        Intent intent = new Intent(context,BonusDetailActivity.class);
        context.startActivity(intent);
    }

    public void initEvent() {

        mFragments = new ArrayList<>();
        String[] titles = {getString(R.string.a120), getString(R.string.a504),
                getString(R.string.a505), getString(R.string.a506)/*, getString(R.string.a507)*/};
        int[] type = {Constants.BONUS_TYPE_ALL,Constants.BONUS_TYPE_SHARE,Constants.BONUS_TYPE_SERVICE,Constants.BONUS_TYPE_SHARED
        ,Constants.BONUS_TYPE_LUCKY};

        for (int i = 0; i < titles.length; i++) {
            mFragments.add(BonusDetailFragment.newInstance(type[i]));
            tabLayout.addTab(tabLayout.newTab().setText(titles[i]));
        }

        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager(), mFragments, titles);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

    }



















}

