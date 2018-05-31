package com.atkj.ctc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.atkj.ctc.R;

/**
 * Created by Administrator on 2017/12/25 0025.
 */

public class AboutActivity extends ToobarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_about);
        setToobarTitle(getString(R.string.a174));


    }


    public static void actionStart(Context context){
        Intent intent = new Intent(context,AboutActivity.class);
        context.startActivity(intent);

    }





























}
