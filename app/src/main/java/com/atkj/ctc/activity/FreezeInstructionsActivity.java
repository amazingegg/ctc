package com.atkj.ctc.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.atkj.ctc.R;

/**
 * Created by Administrator on 2018/4/4 0004.
 */

public class FreezeInstructionsActivity extends ToobarActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_freeze_instructions);
        setToobarTitle(getString(R.string.a515));




    }
}
