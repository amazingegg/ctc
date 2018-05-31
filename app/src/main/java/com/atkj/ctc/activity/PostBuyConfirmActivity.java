package com.atkj.ctc.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.text.method.NumberKeyListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atkj.ctc.MainActivity;
import com.atkj.ctc.R;
import com.atkj.ctc.bean.PostJsonBean;
import com.atkj.ctc.bean.UserBean;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Arith;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.views.ItemView;
import com.atkj.ctc.views.MyToolBar;
import com.atkj.ctc.views.PswInputView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by Administrator on 2017/12/19 0019.
 */

public class PostBuyConfirmActivity extends ToobarActivity {

    private static final String TAG = "PostBuyConfirmActivity";
    public static final int BIND_ZFB = 3;
    private boolean isBuy;
    @BindView(R.id.iv_cont)
    ItemView ivCont;//买入数量
    @BindView(R.id.iv_price)
    ItemView ivPrice;//买入价格
    @BindView(R.id.tv_price)
    TextView tvPrice;//价格
    @BindView(R.id.rl_time)
    RelativeLayout ivTime;//付款期限
    @BindView(R.id.et_time)
    EditText etTime;//付款期限
    @BindView(R.id.iv_earnest_money)
    ItemView ivEarnestMoney;//交易保证金
    @BindView(R.id.bt_buy)
    TextView btBuy;
    @BindView(R.id.cb_weixin)
    CheckBox cbWeixin;
    @BindView(R.id.cb_zfb)
    CheckBox cbZfb;
    @BindView(R.id.cb_yinlian)
    CheckBox cbYinlian;
    @BindView(R.id.et_remark)
    EditText etRemark;


    @BindView(R.id.ll_post)
    LinearLayout llPost;
    @BindView(R.id.ll_post_success)
    LinearLayout llPostSuccess;
    @BindView(R.id.iv_success)
    ImageView ivSuccess;
    @BindView(R.id.tv_time)
    TextView tvTime;

    private String price;
    private String cont;
    private AlertDialog bindPaymentDialog;
    private String ctid;
    private String currency;
    private PopupWindow pop;
    private AlertDialog payPassWordDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_post_buy_confirm);
        ButterKnife.bind(this);

        init();
        initEvent();

        initData();

    }

    private void initData() {

        getBailmoney();
    }

    private void getBailmoney() {

        Map<String,String> param = new LinkedHashMap<>();
        param.put("userid",AppUtils.getUserId());
        param.put("account",cont);

        NetUtils.get(Url.myBailmoney, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                showToast(getString(R.string.a537));
                LogUtils.d(TAG, "getBailmoney onError: "+e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getBailmoney onResponse: "+response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("status") == 200){

                        String money = object.getString("obj");
                        ivEarnestMoney.setInfo(money+currency);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void initEvent(){
        getToolBar().setOnBackClickListener(new MyToolBar.OnBackClickListener() {
            @Override
            public void onBackClick() {
                Intent intent = new Intent(PostBuyConfirmActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });

        etTime.setKeyListener(new NumberKeyListener() {
            @NonNull
            @Override
            protected char[] getAcceptedChars() {

                return "0123456789".toCharArray();
            }

            @Override
            public int getInputType() {
                //3 数字键盘
                return 3;
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(PostBuyConfirmActivity.this,MainActivity.class);
            startActivity(intent);
        }


        return super.onKeyDown(keyCode, event);
    }

    private void init() {
        isBuy = getIntent().getBooleanExtra("isBuy", true);
        setToobarTitle(getString(R.string.a297));

       /* Drawable drawableGreen = getResources().getDrawable(R.drawable.selecter_checkbox_bg);
        Drawable drawableRed = getResources().getDrawable(R.drawable.selecter_checkbox_bg_red);
        cbZfb.setBackground(isBuy ? drawableRed : drawableGreen);
        cbYinlian.setBackground(isBuy ? drawableRed : drawableGreen);*/

        cbZfb.setChecked(true);


        price = getIntent().getStringExtra("price");
        cont = getIntent().getStringExtra("cont");
        ctid = getIntent().getStringExtra("ctid");
        currency = getIntent().getStringExtra("currency");

        int colorSell = getResources().getColor(R.color.textColor_green);
        int colorBuy = getResources().getColor(R.color.textColor2);
        tvPrice.setTextColor(isBuy ? colorBuy : colorSell);

        tvPrice.setText(Arith.mul(price,cont,2));

        Drawable drawableSell = getResources().getDrawable(R.drawable.selecter_login_bg_sell);
        Drawable drawableBuy = getResources().getDrawable(R.drawable.selecter_login_bg);
        btBuy.setBackground(isBuy ? drawableBuy : drawableSell);

        Drawable buy = getResources().getDrawable(R.drawable.chenggong);
        Drawable sell = getResources().getDrawable(R.drawable.chenggong1);
        ivSuccess.setImageDrawable(isBuy ? buy : sell);

        ivCont.setVisibility(isBuy ? View.VISIBLE : View.GONE);
        ivPrice.setVisibility(isBuy ? View.VISIBLE : View.GONE);
        ivCont.setInfo(cont);
        ivPrice.setInfo(price + " CNY");

        //ivTime.setVisibility(isBuy ? View.GONE : View.VISIBLE);
        ivEarnestMoney.setVisibility(isBuy ? View.GONE : View.VISIBLE);


    }


    @OnClick({R.id.bt_buy})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.bt_buy:
                String durationtime = etTime.getText().toString().trim();
                if (TextUtils.isEmpty(durationtime)){
                    showToast(getString(R.string.a383));
                    return;
                }

                if (!cbWeixin.isChecked() && !cbYinlian.isChecked() && !cbZfb.isChecked() ){
                    showToast(getString(R.string.a161));
                    return;
                }

                //检查是否绑定支付宝
                String alipayaccount = AppUtils.getUser().getAlipayaccount();
                List<UserBean.ObjBean.BankcardBean> bankcard = AppUtils.getUser().getBankcard();


                String paytype;
                StringBuilder builder = new StringBuilder();
                if(cbWeixin.isChecked()){
                    builder.append("1");
                }
                if (cbZfb.isChecked()){

                    if (alipayaccount == null || TextUtils.isEmpty(alipayaccount)){
                        showBindPaymentTips(Constants.BIND_TYPE_ZFB);
                        return;
                    }

                    if (builder.length() > 0){
                        builder.append(",2");
                    }else {
                        builder.append("2");
                    }
                }
                if (cbYinlian.isChecked()){

                    //检查是否绑定银联账号
                    if (bankcard == null || bankcard.size() == 0){
                        showBindPaymentTips(Constants.BIND_TYPE_YL);
                        return;
                    }


                    if (builder.length() > 0){
                        builder.append(",3");
                    }else {
                        builder.append("3");
                    }
                }
                paytype = builder.toString();

                String userid =  AppUtils.getUserId();
                final String remark = etRemark.getText().toString().trim();

                Map<String,String> param = new LinkedHashMap<>();
                param.put("userid",userid);
                param.put("account",cont);
                param.put("ctid",ctid);
                param.put("price",price);
                param.put("durationtime",durationtime);
                param.put("paytype",paytype);
                param.put("remake",remark);


                if (isBuy){
                    postOrder(param);
                }else {
                    String paypassword = AppUtils.getUser().getPaypassword();
                    if ("".equals(paypassword) || paypassword == null) {
                        Intent payPwdIntent = new Intent(this, PayPassWordActivity.class);
                        startActivity(payPwdIntent);
                        return;
                    }

                    getFee(cont,param);
                }


                break;
        }



    }

    //交易时的弹窗
    private void showPopu(final String fee, final Map<String, String> param1) {
        final View content = LayoutInflater.from(this).inflate(R.layout.pop_transaction, null);
        pop = new PopupWindow(content, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);

        ImageView close = content.findViewById(R.id.iv_close);
        TextView openMember = content.findViewById(R.id.tv_open_member);
        TextView vip = content.findViewById(R.id.tv_vip);
        final TextView tvContent = content.findViewById(R.id.tv_content);
        final TextView tvNext = content.findViewById(R.id.tv_next);
        int vipstatus = AppUtils.getUser().getVipstatus();
        if (vipstatus == Constants.MEMBER_VIP) {
            String str = getString(R.string.a379);
            vip.setText(str);
            openMember.setText("");
        } else if (vipstatus == Constants.MEMBER_ORDINARY) {
            vip.setText(getString(R.string.a380));
            openMember.setText(getString(R.string.a246));
        } else if (vipstatus == Constants.MEMBER_SPECIAL_VIP) {
            vip.setText(getString(R.string.a381));
        }

        tvContent.setText(getString(R.string.a244, String.valueOf(fee), currency));
        final TextPaint paint = tvContent.getPaint();
        paint.setFakeBoldText(true);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
        openMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostBuyConfirmActivity.this, MemberActivity.class);
                startActivity(intent);
                pop.dismiss();
            }
        });
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPayPassWordDialog(param1);
                pop.dismiss();
            }
        });

        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams attributes = getWindow().getAttributes();
                attributes.alpha = 1f;
                getWindow().setAttributes(attributes);
            }
        });
        pop.showAtLocation(btBuy, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.alpha = 0.5f;
        getWindow().setAttributes(attributes);

    }

    private void showPayPassWordDialog(final Map<String, String> param1) {
        AlertDialog.Builder customizeDialog = new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_input_pwd, null);
        final PswInputView inputView = dialogView.findViewById(R.id.piv);
        inputView.setInputCallBack(new PswInputView.InputCallBack() {
            @Override
            public void onInputFinish(String result) {
                checkFundsPwd(result,param1);
                inputView.hideSoftInput();
            }

            @Override
            public void onInputChange(String result) {
            }
        });
        TextView title = new TextView(this);
        title.setGravity(Gravity.CENTER_HORIZONTAL);
        title.setPadding(30, 30, 30, 30);
        title.setText(getString(R.string.a328));
        title.setTextColor(getResources().getColor(R.color.f000000));
        title.setTextSize(24);
        customizeDialog.setCustomTitle(title);
        customizeDialog.setView(dialogView);

        customizeDialog.setPositiveButton("",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        payPassWordDialog = customizeDialog.show();

    }

    private void checkFundsPwd(String pwd, final Map<String, String> param1) {

        if (pwd == null || pwd.length() < 6) {
            showToast(getString(R.string.a329));
            return;
        }
        String userid = AppUtils.getUserId();
        String timestamp = AppUtils.getTimeStamp();

        Map<String, String> param = new LinkedHashMap<>();
        param.put("timestamp", timestamp);
        param.put("paypassword", pwd);
        param.put("id", userid);

        String url = Url.verificationPwd;
        NetUtils.postString(url, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "checkFundsPwd onError: "+e.toString());
                dismissPwdDialog();
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "checkFundsPwd onResponse: "+response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {

                        postOrder(param1);

                        dismissPwdDialog();
                    } else {
                        showToast(obj.getString("msg"));
                        dismissPwdDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void dismissPwdDialog() {
        if (payPassWordDialog != null) {
            if (payPassWordDialog.isShowing()) {

                payPassWordDialog.dismiss();
            }
        }
    }

    private void getFee(String count, final Map<String, String> param1) {
        String userId = AppUtils.getUserId();

        Map<String, String> param = new LinkedHashMap<>();
        param.put("userid", userId);
        param.put("account", count);

        NetUtils.get(Url.getOrderFee, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getFee onError: " + e.toString());
                showToast(getString(R.string.a537));
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getFee onResponse: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        JSONObject obj1 = obj.getJSONObject("obj");
                        String fee = AppUtils.convertCounting(obj1.getString("fee"));
                        showPopu(fee,param1);
                    } else {
                        showToast(obj.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showBindPaymentTips(final int type) {
        final android.app.AlertDialog.Builder customizeDialog = new android.app.AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_cancel_entrust, null);
        final TextView cancel = dialogView.findViewById(R.id.cancel);
        final TextView content = dialogView.findViewById(R.id.content);
        content.setGravity(Gravity.CENTER);
        content.setText(type == Constants.BIND_TYPE_ZFB ? getString(R.string.a286) : getString(R.string.a550));
        TextView enter = dialogView.findViewById(R.id.enter);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindPaymentDialog.dismiss();
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type == Constants.BIND_TYPE_ZFB){
                    Intent intent = new Intent(PostBuyConfirmActivity.this,AccountNumberBindDetailActivity.class);
                    intent.putExtra("type", Constants.BIND_TYPE_ZFB);
                    startActivity(intent);
                }else if (type == Constants.BIND_TYPE_YL){
                    Intent intent = new Intent(PostBuyConfirmActivity.this,AddBankCardActivity.class);
                    startActivity(intent);
                }


                bindPaymentDialog.dismiss();
            }
        });

        TextView title = new TextView(this);
        title.setText("");
        customizeDialog.setCustomTitle(title);
        customizeDialog.setView(dialogView);

        customizeDialog.setPositiveButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        bindPaymentDialog = customizeDialog.show();

    }

    private void postOrder( Map<String,String> param) {

        showLoadingDialog();
        NetUtils.postString(isBuy ? Url.postBuyOrder : Url.postSellOrder, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "postOrder onError: "+e.toString());
                showToast(getString(R.string.a537));
                dismissDialog();
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "postOrder onResponse: "+response);
                dismissDialog();
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200){
                        llPost.setVisibility(View.GONE);
                        llPostSuccess.setVisibility(View.VISIBLE);
                        Timer timer = new Timer();
                        timer.schedule(task,0,1000);

                    }else {
                        showToast(obj.getString("msg"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private TimerTask task = new TimerTask() {

        int num = 4;
        @Override
        public void run() {
            num--;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (num == 0){
                        if (!isFinishing()){
                            Intent intent = new Intent(PostBuyConfirmActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                        cancel();
                    }
                    tvTime.setText(String.format(getString(R.string.a384),num));
                }
            });



        }
    };

}
