package com.atkj.ctc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atkj.ctc.MainActivity;
import com.atkj.ctc.R;
import com.atkj.ctc.bean.PostJsonBean;
import com.atkj.ctc.bean.UserBean;
import com.atkj.ctc.scancode.utils.Constant;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by Administrator on 2018/1/2 0002.
 */

public class AccountNumberBindDetailActivity extends ToobarActivity {

    @BindView(R.id.ll_bind_detail)
    LinearLayout llBindDetail;
    @BindView(R.id.ll_bind)
    LinearLayout llBind;
    @BindView(R.id.rl_name)
    RelativeLayout rlName;
    @BindView(R.id.rl_id)
    RelativeLayout rlId;


    @BindView(R.id.iv_image)
    ImageView image;
    @BindView(R.id.binded)
    TextView binded;
    @BindView(R.id.tv_tips)
    TextView tips;
    @BindView(R.id.bt_enter)
    TextView enter;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_id)
    EditText etId;


    private boolean isBind;
    private int bindType;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_account_number_bind_detail);
        ButterKnife.bind(this);
        init();


    }

    public static void actionStart(Context context,int type){
        Intent intent = new Intent(context,AccountNumberBindDetailActivity.class);
        intent.putExtra("type",type);
        context.startActivity(intent);


    }

    private void init() {
        int type = getIntent().getIntExtra("type", 0);
        switch (type) {
            case Constants.BIND_TYPE_ZFB:
                setToobarTitle(getString(R.string.a249));
                image.setImageDrawable(getResources().getDrawable(R.drawable.zfb_large));
                LogUtils.d("getAlipayaccount==" + AppUtils.getUser().getAlipayaccount());
                isBind = !TextUtils.isEmpty(AppUtils.getUser().getAlipayaccount());
                if (isBind) {
                    binded.setVisibility(View.VISIBLE);

                    String alipayaccount = AppUtils.getUser().getAlipayaccount();
                    tips.setText(getString(R.string.a250) + alipayaccount);
                    enter.setText(getString(R.string.a251));

                } else {
                    binded.setVisibility(View.GONE);
                    tips.setText(getString(R.string.a8));
                    enter.setText(getString(R.string.a9));
                }


                break;
            case Constants.BIND_TYPE_WX:
                setToobarTitle(getString(R.string.a252));
                image.setImageDrawable(getResources().getDrawable(R.drawable.wxin_large));
                isBind = !TextUtils.isEmpty(AppUtils.getUser().getWechatid());
                if (isBind) {
                    binded.setVisibility(View.VISIBLE);

                    String wechatid = AppUtils.getUser().getWechatid();
                    tips.setText(getString(R.string.a253) + wechatid);
                    enter.setText(getString(R.string.a251));

                } else {
                    binded.setVisibility(View.GONE);
                    tips.setText(getString(R.string.a254));
                    enter.setText(getString(R.string.a9));
                }

                break;
            case Constants.BIND_TYPE_YL:
                //setToobarTitle("银行卡绑定");
                //image.setImageDrawable(getResources().getDrawable(R.drawable.));
                break;

        }

    }

    @OnClick({R.id.bt_enter, R.id.bt_bind_enter})
    public void OnViewClicked(View view) {

        switch (view.getId()) {
            case R.id.bt_enter:
                int type = getIntent().getIntExtra("type", 0);

                switch (type) {
                    case Constants.BIND_TYPE_ZFB:

                        llBindDetail.setVisibility(View.GONE);
                        llBind.setVisibility(View.VISIBLE);
                        etName.setHint(getString(R.string.a547));
                        etId.setHint(getString(R.string.a50));
                        bindType = Constants.BIND_TYPE_ZFB;

                        break;
                    case Constants.BIND_TYPE_WX:

                        llBindDetail.setVisibility(View.GONE);
                        llBind.setVisibility(View.VISIBLE);
                        rlName.setVisibility(View.GONE);
                        etId.setHint(getString(R.string.a58));
                        bindType = Constants.BIND_TYPE_WX;

                        break;
                    case Constants.BIND_TYPE_YL:
                        //setToobarTitle("银行卡绑定");
                        //image.setImageDrawable(getResources().getDrawable(R.drawable.));
                        break;

                }

                break;
            case R.id.bt_bind_enter:
                if (bindType == Constants.BIND_TYPE_ZFB) {
                    if (TextUtils.isEmpty(etName.getText().toString())) {
                        showToast(getString(R.string.a256));
                        return;
                    } else if (TextUtils.isEmpty(etId.getText().toString())) {
                        showToast(getString(R.string.a257));
                        return;
                    }
                } else {
                    if (TextUtils.isEmpty(etId.getText().toString())) {
                        showToast(getString(R.string.a258));
                        return;
                    }
                }

                bind();
                break;


        }


    }

    private void bind() {
        String userId = AppUtils.getUserId();
        Map<String, String> param = new LinkedHashMap<>();
        param.put("userid",userId);

        if (bindType == Constants.BIND_TYPE_ZFB) {
            param.put("alipayname", etName.getText().toString());
            param.put("alipayaccount", etId.getText().toString());
        } else if (bindType == Constants.BIND_TYPE_WX){
            param.put("wechatid", etId.getText().toString());
        }

        showLoadingDialog();
        NetUtils.postString(Url.bindSet, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(e.toString());
                showToast(getString(R.string.a537));
                dismissDialog();
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        showToast(getString(R.string.a260));
                        JSONObject obj1 = obj.getJSONObject("obj");
                        UserBean.ObjBean user = AppUtils.getUser();


                        if (bindType == Constants.BIND_TYPE_ZFB) {

                            user.setAlipayname(obj1.getString("alipayname"));
                            user.setAlipayaccount(obj1.getString("alipayaccount"));
                            Intent intent = new Intent();
                            intent.putExtra("type", Constants.BIND_TYPE_ZFB);
                            setResult(RESULT_OK, intent);

                        } else if (bindType == Constants.BIND_TYPE_WX){
                            user.setWechatid(obj1.getString("wechatid"));
                            Intent intent = new Intent();
                            intent.putExtra("type", Constants.BIND_TYPE_WX);
                            setResult(RESULT_OK, intent);
                        }

                        AppUtils.saveUserInfo(AppUtils.getGson().toJson(AppUtils.userBean));

                        dismissDialog();

                        finish();
                    } else {
                        showToast(obj.getString("msg"));
                        dismissDialog();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


}
