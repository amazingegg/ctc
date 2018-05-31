package com.atkj.ctc.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.bean.PostJsonBean;
import com.atkj.ctc.bean.UserBean;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.views.ItemView;
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

public class PersonalInfoActivity extends ToobarActivity {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_uid)
    ItemView ivUid;
    @BindView(R.id.iv_country)
    ItemView ivCountry;
    @BindView(R.id.iv_regist_time)
    ItemView ivRegistTime;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_personal_info);
        ButterKnife.bind(this);
        setToobarTitle(getString(R.string.a173));

        init();


    }

    private void init() {
        if (AppUtils.isLogin) {
            UserBean.ObjBean obj = AppUtils.getUser();
            tvName.setText(obj.getUsername());
            ivUid.setInfo(obj.getUid());
            ivCountry.setInfo(getString(R.string.a36));
            ivRegistTime.setInfo(AppUtils.timedate(obj.getCreatetime()));
        }
    }


    @OnClick({R.id.rl_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_name:
                showDialog();
                break;

        }


    }

    private void showDialog() {

        final AlertDialog.Builder customizeDialog = new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_customize, null);
        TextView title = new TextView(this);
        title.setPadding(30, 30, 30, 30);
        title.setText(getString(R.string.a374));
        title.setTextColor(getResources().getColor(R.color.f000000));
        title.setTextSize(24);
        customizeDialog.setCustomTitle(title);
        customizeDialog.setView(dialogView);
        customizeDialog.setPositiveButton(getString(R.string.a56), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 获取EditView中的输入内容
                EditText edit_text = dialogView.findViewById(R.id.et_name);
                String username = edit_text.getText().toString().trim();

                String pattern = "[\u4e00-\u9fa5\\w]+";

                if (TextUtils.isEmpty(username)) {
                    showToast(getString(R.string.a375));
                    return;
                }else if (username.length() < 4 || username.length() > 10) {
                    showToast(getString(R.string.a309));
                    return;
                }else if (!username.matches(pattern)){
                    showToast(getString(R.string.a538));
                    return;
                }



                upDateName(dialog, username);

            }
        });
        customizeDialog.setNegativeButton(getString(R.string.a199), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        customizeDialog.show();

    }

    private void upDateName(final DialogInterface dialog, final String username) {

        String userid = AppUtils.getUserId();

        Map<String,String> param = new LinkedHashMap<>();
        param.put("id",userid);
        param.put("username",username);

        showLoadingDialog();
        NetUtils.put(Url.updateName, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(e.toString());
                dialog.dismiss();
                showToast(getString(R.string.a537));
                dismissDialog();
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(response);
                dismissDialog();

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {

                        showToast(obj.getString("msg"));
                        tvName.setText(username);
                        AppUtils.userBean.getObj().setUsername(username);

                        AppUtils.saveUserInfo(AppUtils.getGson().toJson(AppUtils.userBean));
                        dialog.dismiss();
                    } else {
                        showToast(obj.getString("msg"));
                        dialog.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


}
