package com.atkj.ctc.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.atkj.ctc.R;
import com.atkj.ctc.bean.PostJsonBean;
import com.atkj.ctc.scancode.utils.Constant;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.views.EditView;
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
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/12/25 0025.
 */

public class ModifyPwdActivity extends ToobarActivity {

    @BindView(R.id.ev_old_pwd)
    EditView evOldPwd;
    @BindView(R.id.ev_new_pwd)
    EditView evNewPwd;
    @BindView(R.id.ev_new_pwd2)
    EditView evNewPwd2;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_modify_pwd);
        ButterKnife.bind(this);
        setToobarTitle(getString(R.string.a172));
    }

    @OnClick({R.id.bt_enter})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.bt_enter:
                String oldPwd = evOldPwd.getContentText();
                String newPwd = evNewPwd.getContentText();
                String newPwd2 = evNewPwd2.getContentText();

                if (TextUtils.isEmpty(oldPwd)){
                    showToast(getString(R.string.a333));
                    return;
                }else if (TextUtils.isEmpty(newPwd)){
                    showToast(getString(R.string.a334));
                    return;
                }else if (TextUtils.isEmpty(newPwd2)){
                    showToast(getString(R.string.a335));
                    return;
                }else if (!newPwd.equals(newPwd2)){
                    showToast(getString(R.string.a336));
                    return;
                }

                modifyPwd(oldPwd,newPwd2);
                break;



        }
    }

    private void modifyPwd(String oldPwd,String newPwd) {
        Map<String,String> param = new LinkedHashMap<>();
        String userid =  AppUtils.getUserId();

        param.put("userId", userid);
        param.put("oldPassWord",oldPwd);
        param.put("newPassWord",newPwd);

        showLoadingDialog();
        NetUtils.put(Url.modifyPwd, param, new NetUtils.StringCallBack() {
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
                    if (obj.getInt("status") == 200){
                        dismissDialog();

                        showToast(obj.getString("msg"));

                        finish();
                    }else {
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
