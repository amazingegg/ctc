package com.atkj.ctc.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.atkj.ctc.MainActivity;
import com.atkj.ctc.R;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Constants;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/5 0005.
 */

public class LanguageActivity extends ToobarActivity{


    @BindView(R.id.iv_chinese)
    ImageView ivChinese;
    @BindView(R.id.iv_english)
    ImageView ivEnglish;
    @BindView(R.id.iv_korea)
    ImageView ivKorea;
    @BindView(R.id.iv_japan)
    ImageView ivJapan;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_language);
        setToobarTitle(getString(R.string.a168));
        ButterKnife.bind(this);



        init();



    }

    private void init() {
        SharedPreferences sp = getSharedPreferences(Constants.LANGUAGE, Context.MODE_PRIVATE);
        String language = sp.getString("COUNTRY","");
        setLanguage(language);
    }

    private void setLanguage(String language) {
        switch (language){
            case "zh":
                ivEnglish.setVisibility(View.GONE);
                ivChinese.setVisibility(View.VISIBLE);
                ivKorea.setVisibility(View.GONE);
                ivJapan.setVisibility(View.GONE);
                break;
            case "en":
                ivEnglish.setVisibility(View.VISIBLE);
                ivChinese.setVisibility(View.GONE);
                ivKorea.setVisibility(View.GONE);
                ivJapan.setVisibility(View.GONE);
                break;
        }
    }

    @OnClick({R.id.rl_en,R.id.rl_zh,R.id.rl_korea,R.id.rl_japan})
    public void onViewClicked(View view){
        Locale locale = null;
        switch (view.getId()){
            case R.id.rl_en:
                locale = Locale.ENGLISH;
                ivEnglish.setVisibility(View.VISIBLE);
                ivChinese.setVisibility(View.GONE);
                ivKorea.setVisibility(View.GONE);
                ivJapan.setVisibility(View.GONE);


                break;
            case R.id.rl_zh:
                locale = Locale.CHINESE;
                ivEnglish.setVisibility(View.GONE);
                ivChinese.setVisibility(View.VISIBLE);
                ivKorea.setVisibility(View.GONE);
                ivJapan.setVisibility(View.GONE);
                break;
            case R.id.rl_korea:
                showToast(getString(R.string.a248));

                return;
            case R.id.rl_japan:
                showToast(getString(R.string.a248));

                return;

        }

        AppUtils.changeAppLanguage(this,locale,true);

        Intent intent = new Intent(this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }


}
