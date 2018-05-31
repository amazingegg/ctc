package com.atkj.ctc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.atkj.ctc.activity.BaseActivity;
import com.atkj.ctc.adapter.CommonRecycleViewAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Panda on 2018/1/4 0004.
 */

public class TestAcitvity extends BaseActivity {

    //TODO 版本更新 更新内容扩展
    //TODO 登录密码缓存加密
    //TODO 自动登录有时会登录失败 需要用户重新登录
    //TODO 自动定时获取列表信息时 根据用户界面更变开启或关闭自动获取 现在会出现不在自动获取页面时也会获取 浪费用户流量


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildView(R.layout.activity_test);


    }


}
