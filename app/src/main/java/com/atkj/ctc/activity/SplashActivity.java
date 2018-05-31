package com.atkj.ctc.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.atkj.ctc.MainActivity;
import com.atkj.ctc.R;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/12/22 0022.
 */

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeLanguage();
        setChildView(R.layout.activity_splash);


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);


        testM();

    }

    private void testM() {
        LogUtils.d("testM: ");
    }

    private void changeLanguage() {
        SharedPreferences sp = getSharedPreferences(Constants.LANGUAGE, Context.MODE_PRIVATE);
        String country = sp.getString("COUNTRY", "");
        Locale locale;
        switch (country) {
            case "zh":
                locale = Locale.CHINESE;
                break;
            case "en":
                locale = Locale.ENGLISH;
                break;
            default:
                locale = Locale.CHINESE;
        }

        AppUtils.changeAppLanguage(this, locale, false);
    }


}
