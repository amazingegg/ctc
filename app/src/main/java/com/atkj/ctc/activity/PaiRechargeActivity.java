package com.atkj.ctc.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.bean.PostJsonBean;
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

public class PaiRechargeActivity extends ToobarActivity{

    @BindView(R.id.ll_recharge)
    LinearLayout llRecharge;
    @BindView(R.id.ll_recharge2)
    LinearLayout llRecharge2;
    @BindView(R.id.ll_transfer_success)
    LinearLayout llTransferSuccess;

    @BindView(R.id.iv_recharge_id)
    ItemView ivRechargeId;//充值单号
    @BindView(R.id.recharge_cont)
    ItemView ivRechargeCont;//充值数量
    @BindView(R.id.iv_discovery)
    ItemView ivDiscovery;//Discovery账号
    @BindView(R.id.iv_conn)
    ItemView ivConn;//联系方式


    @BindView(R.id.et_cont)
    EditText etCont;
    @BindView(R.id.et_discovery)
    EditText etDiscovery;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.remark)
    TextView tvRemark;
    @BindView(R.id.remark2)
    TextView tvRemark2;
    @BindView(R.id.remark3)
    TextView tvRemark3;
    @BindView(R.id.remark5)
    TextView tvRemark5;
    @BindView(R.id.remark6)
    TextView tvRemark6;
    @BindView(R.id.remark7)
    TextView tvRemark7;
    @BindView(R.id.remark8)
    TextView tvRemark8;
    @BindView(R.id.et_transfer_id)
    EditText etTransferId;
    @BindView(R.id.tv_copy_id)
    TextView copy;

    private String orderno;
    private String phoneLast4 = "5871";
    private String discovery = "coolboy";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_pai_recharge);
        ButterKnife.bind(this);
        setToobarTitle(getString(R.string.a353));

        init();

    }

    private void init() {
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.copyClipboard(discovery);
                showToast(getString(R.string.a275));
            }
        });

    }


    @OnClick({R.id.bt_next,R.id.bt_complete})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.bt_next:
                next();
                break;
            case R.id.bt_complete:

                transferComplete();

                break;
        }
    }

    private void transferComplete() {

        Map<String,String> param = new LinkedHashMap<>();
        param.put("orderId",orderno);
        param.put("transferNo","");

        showLoadingDialog();
        NetUtils.put(Url.updateTransferNo, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(e.toString());
                dismissDialog();
                showToast(getString(R.string.a537));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200){
                        dismissDialog();
                        showSuccessPage();
                    }else {
                        dismissDialog();
                        showToast(obj.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showSuccessPage() {
        llRecharge.setVisibility(View.GONE);
        llRecharge2.setVisibility(View.GONE);
        llTransferSuccess.setVisibility(View.VISIBLE);
    }

    private void next() {
        if (TextUtils.isEmpty(etCont.getText().toString())){
            showToast(getString(R.string.a283));
            return;
        }else if (Double.parseDouble(etCont.getText().toString()) < 100){
            showToast(getString(R.string.a355));
            return;
        }else if (TextUtils.isEmpty(etDiscovery.getText().toString())){
            showToast(getString(R.string.a356));
            return;
        }else if(TextUtils.isEmpty(etPhone.getText().toString())){
            showToast(getString(R.string.a310));
            return;
        }else if (etPhone.getText().toString().length() < 11){
            showToast(getString(R.string.a357));
            return;
        }

        String userid =  AppUtils.getUserId();
        String account = etCont.getText().toString().trim();
        String discoveryno = etDiscovery.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        Map<String,String> param = new LinkedHashMap<>();
        param.put("userid",userid);
        param.put("account",account);
        param.put("discoveryno",discoveryno);
        param.put("phone",phone);

        showLoadingDialog();
        NetUtils.postString(Url.recharge, param, new NetUtils.StringCallBack() {
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

                        JSONObject content = obj.getJSONObject("obj");
                        llRecharge.setVisibility(View.GONE);
                        llRecharge2.setVisibility(View.VISIBLE);

                        orderno = content.getString("id");

                        String discoveryno = content.getString("discoveryno");
                        String account = content.getString("account");
                        String orderno = content.getString("orderno");
                        String phone = content.getString("phone");

                        ivRechargeId.setInfo(orderno);
                        ivRechargeCont.setInfo(account);
                        ivDiscovery.setInfo(discoveryno);
                        ivConn.setInfo(phone);
                        String str = String.format(getString(R.string.a359),10);
                        String str2 = getString(R.string.a360);
                        String str3 = getString(R.string.a361);
                        String str5 = String.format(getString(R.string.a362),discoveryno,account);
                        String str6 = getString(R.string.a363);
                        String str7 = String.format(getString(R.string.a364),discovery);
                        String str8 = String.format(getString(R.string.a365),phoneLast4);
                        tvRemark.setText(Html.fromHtml(str));
                        tvRemark2.setText(Html.fromHtml(str2));
                        tvRemark3.setText(Html.fromHtml(str3));
                        tvRemark5.setText(Html.fromHtml(str5));
                        tvRemark6.setText(Html.fromHtml(str6));
                        tvRemark7.setText(Html.fromHtml(str7));
                        tvRemark8.setText(Html.fromHtml(str8));

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
