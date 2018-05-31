package com.atkj.ctc.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.activity.CTSPurchaseActivity;
import com.atkj.ctc.activity.LanguageActivity;
import com.atkj.ctc.activity.LoginActivity;
import com.atkj.ctc.activity.MemberActivity;
import com.atkj.ctc.activity.MyBonusActivity;
import com.atkj.ctc.activity.MyIntegralActivity;
import com.atkj.ctc.activity.MyOrderActivity;
import com.atkj.ctc.activity.PersonalInfoActivity;
import com.atkj.ctc.activity.SettingActivity;
import com.atkj.ctc.activity.ShareActivity;
import com.atkj.ctc.activity.WebViewActivity;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.Url;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public class MyFragment extends BaseFragment {
    private static final java.lang.String TAG = MyFragment.class.getSimpleName();
    // 这里的参数只是一个举例可以根据需求更改
    private String name;
    private Unbinder unbinder;

    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_uid)
    TextView tvUid;
    @BindView(R.id.switch_language)
    TextView tvLanguage;
    @BindView(R.id.iv_country)
    ImageView ivCountry;



    /**
     * 通过工厂方法来创建Fragment实例
     * 同时给Fragment来提供参数来使用
     *
     * @param param1 参数1.
     * @return Fragment的实例.
     */
    public static MyFragment newInstance(String param1) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putString("name", param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString("name");
        }
    }

    @Override
    public void initData() {

        SharedPreferences sp = getContext().getSharedPreferences(Constants.LANGUAGE, Context.MODE_PRIVATE);
        String language = sp.getString("COUNTRY","");

        setLanguage(language);


    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    private void setLanguage(String language) {
        switch (language){
            case "zh":
                tvLanguage.setText("简体中文");
                ivCountry.setImageDrawable(getResources().getDrawable(R.drawable.china));
                break;
            case "en":
                tvLanguage.setText("English");
                ivCountry.setImageDrawable(getResources().getDrawable(R.drawable.english));
                break;
        }
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_my, container, false);
        unbinder = ButterKnife.bind(this, view);
        //TODO 横屏布局错乱


        initEvent();
        return view;
    }

    private void initEvent() {

        tvName.getPaint().setFakeBoldText(true);


    }

    /**
     * onAttach中连接监听接口 确保Activity支持该接口
     */
    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

    }

    @Override
    public void onResume() {
        super.onResume();

        LogUtils.d(TAG, " onResume");

        if (AppUtils.isLogin) {
            tvId.setText(AppUtils.getUser().getUid());
            tvName.setText(AppUtils.getUser().getUsername());
            tvUid.setText("UID:");
        } else {
            tvId.setText(getString(R.string.a426));
            tvName.setText(getString(R.string.a427));
            tvUid.setText(getString(R.string.a428));
        }
    }


    @OnClick({R.id.miv_setting, R.id.ll_myorder, R.id.ll_mypost, R.id.tv_name, R.id.tv_uid,
            R.id.miv_integral, R.id.miv_invite, R.id.miv_help, R.id.iv_member, R.id.miv_bonus
            ,R.id.switch_language,R.id.ll_cts,R.id.miv_customer})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.switch_language://语言选择
                intent = new Intent(getActivity(), LanguageActivity.class);
                startActivity(intent);

                break;

            case R.id.ll_myorder://我的订单
                if (AppUtils.checkLogin(getActivity())) return;

                intent = new Intent(getActivity(), MyOrderActivity.class);
                intent.putExtra("isMyOrder", true);
                startActivity(intent);
                break;
            case R.id.ll_mypost://我的发布
                if (AppUtils.checkLogin(getActivity())) return;

                intent = new Intent(getActivity(), MyOrderActivity.class);
                intent.putExtra("isMyOrder", false);
                startActivity(intent);
                break;

            case R.id.iv_member://会员
                if (AppUtils.checkLogin(getActivity())) return;

                intent = new Intent(getActivity(), MemberActivity.class);
                startActivity(intent);

                break;
            case R.id.miv_integral://积分
                if (AppUtils.checkLogin(getActivity())) return;

                intent = new Intent(getActivity(), MyIntegralActivity.class);
                startActivity(intent);
                break;
            case R.id.miv_bonus://奖金
                if (AppUtils.checkLogin(getActivity())) return;

                intent = new Intent(getActivity(), MyBonusActivity.class);
                startActivity(intent);

                break;
            case R.id.miv_invite://分享
                if (AppUtils.checkLogin(getActivity())) return;
                intent = new Intent(getActivity(), ShareActivity.class);
                startActivity(intent);
                break;
            case R.id.miv_help://帮助中心
                intent = new Intent(getActivity(), WebViewActivity.class);
                String url;
                SharedPreferences sp = getContext().getSharedPreferences(Constants.LANGUAGE, Context.MODE_PRIVATE);
                String country = sp.getString("COUNTRY", "");
                switch (country) {
                    case "zh":
                        url = Url.tutorial;
                        break;
                    case "en":
                        url = Url.tutorial_en;
                        break;
                    default:
                        url = Url.tutorial;
                }
                intent.putExtra("url", url);
                startActivity(intent);
                break;
            case R.id.miv_customer://在线客服
                if (AppUtils.checkLogin(getActivity())) return;

                showToast(getString(R.string.a248));
                break;
            case R.id.miv_setting://设置
                intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_name://登录 昵称
                if (AppUtils.checkLogin(getActivity())) return;

                intent = new Intent(getActivity(), PersonalInfoActivity.class);
                startActivity(intent);

                break;
            case R.id.tv_uid://登录 点击登录
                if (!AppUtils.isLogin) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_cts://CTS 申购
                if (AppUtils.checkLogin(getActivity())) return;
                CTSPurchaseActivity.actionStart(getContext());

                break;
        }


    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2){
            if (resultCode == Activity.RESULT_OK){
                String language = data.getStringExtra("language");
                setLanguage(language);
            }

        }




    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * onDetach中注销接口
     */
    @Override
    public void onDetach() {
        super.onDetach();
    }


}
