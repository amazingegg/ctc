package com.atkj.ctc.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.api.ErrorCallback;
import com.atkj.ctc.api.LoadingCallback;
import com.atkj.ctc.bean.PostJsonBean;
import com.atkj.ctc.bean.UserBean;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Arith;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.views.MyToolBar;
import com.atkj.ctc.views.PswInputView;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by Administrator on 2018/1/9 0009.
 */

public class USDExtractActivity extends ToobarActivity {

    private static final int REQUEST_CODE_ADDBANK = 23;
    private static final java.lang.String TAG = "USDExtractActivity";
    private MyToolBar toolBar;


    @BindView(R.id.rl_add_card)
    RelativeLayout rlAddCard;
    @BindView(R.id.rl_bank_card)
    RelativeLayout rlBankCard;
    @BindView(R.id.tv_bank_card)
    TextView tvBankCard;

    @BindView(R.id.extract_count)
    TextView tvExtractCount;//提现数量 汇率越换
    @BindView(R.id.tv_available)
    TextView tvAvailable;
    @BindView(R.id.et_count)
    EditText etCount;//提现数量
    @BindView(R.id.tv_amount)
    TextView tvAmount;//提现金额


    private double rate;//汇率
    private String availableUsd;//可用数
    private AlertDialog checkPayPassWordDialog;
    private int loadDataCount = 0;
    private LoadService loadService;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_usd_extract);
        ButterKnife.bind(this);

        init();

        initEvent();


    }

    private void initEvent() {
        etCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String count = s.toString();
                try {
                    double parseDouble = Double.parseDouble(count);
                    if (rate != 0 && !TextUtils.isEmpty(count)) {
                        String amount = Arith.mul(count, String.valueOf(rate), 2);
                        tvAmount.setText(amount);
                    }
                    if (TextUtils.isEmpty(count)) {
                        tvAmount.setText("0.00");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void init() {
        toolBar = getToolBar();
        setToobarTitle(getString(R.string.a263));
        toolBar.setTextRightVisibility(true);
        toolBar.setRightText(getString(R.string.a391));
        toolBar.setOnRightClickListener(new MyToolBar.OnRightClickListener() {
            @Override
            public void onRightClick() {
                //TODO 提现记录
                showToast(getString(R.string.a248));
            }
        });

        //tvAvailable.setText("可用USD"+);

        List<UserBean.ObjBean.BankcardBean> bankcard = AppUtils.getUser().getBankcard();
        if (bankcard != null && bankcard.size() > 0) {
            rlAddCard.setVisibility(View.GONE);
            rlBankCard.setVisibility(View.VISIBLE);

            UserBean.ObjBean.BankcardBean card = bankcard.get(0);
            String bankcardid = card.getBankcardid();
            String substring = bankcardid.substring(bankcardid.length() - 4, bankcardid.length());
            String str = card.getBankname() + String.format(getString(R.string.a392), substring);
            tvBankCard.setText(str);


        } else {
            rlAddCard.setVisibility(View.VISIBLE);
            rlBankCard.setVisibility(View.GONE);
        }

        loadService = LoadSir.getDefault().register(this, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                loadService.showCallback(LoadingCallback.class);
                initData();

            }
        });


        initData();


    }

    private void initData() {
        loadDataCount = 0;
        getExchangeRate();
        getAvailableUsd();
    }


    @OnClick({R.id.rl_add_card, R.id.bt_commit, R.id.tv_extract_all})

    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_add_card:
                Intent intent = new Intent(this, AddBankCardActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADDBANK);

                break;
            case R.id.bt_commit:
                List<UserBean.ObjBean.BankcardBean> bankcardList = AppUtils.getUser().getBankcard();
                String count = etCount.getText().toString().trim();
                String amount = tvAmount.getText().toString();

                double parseDouble;
                try {
                    parseDouble = Double.parseDouble(count);
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast(getString(R.string.a438));
                    return;
                }

                if (bankcardList.size() == 0 || bankcardList == null) {
                    showToast(getString(R.string.a393));
                    return;
                } else if (TextUtils.isEmpty(count)) {
                    showToast(getString(R.string.a394));
                    return;
                } else if (Double.parseDouble(count) < 10) {
                    showToast(String.format(getString(R.string.a395), 10));
                    return;
                } else if (parseDouble > Double.parseDouble(availableUsd)) {
                    showToast(getString(R.string.a396));
                    return;
                }
                String userId = AppUtils.getUserId();
                String cardid = bankcardList.get(0).getId();
                String rate = String.valueOf(this.rate);

                Map<String, String> param = new LinkedHashMap<>();
                param.put("userid", userId);
                param.put("cardid", cardid);
                param.put("rate", rate);
                param.put("ratetype", "USDCNH");
                param.put("account", count);
                param.put("total", amount);

                showPayPassWordDialog(param);
                break;
            case R.id.tv_extract_all:
                etCount.setText(availableUsd);
                etCount.setSelection(availableUsd.length());
                break;
        }
    }

    private void showPayPassWordDialog(final Map<String, String> param) {
        AlertDialog.Builder customizeDialog = new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_input_pwd, null);
        final PswInputView inputView = dialogView.findViewById(R.id.piv);
        inputView.setInputCallBack(new PswInputView.InputCallBack() {
            @Override
            public void onInputFinish(String result) {
                checkFundsPwd(result, param);
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
                        // 获取EditView中的输入内容
                    }
                });

        checkPayPassWordDialog = customizeDialog.show();

    }


    private void dismissPwdDialog() {
        if (checkPayPassWordDialog != null) {
            if (checkPayPassWordDialog.isShowing()) {

                checkPayPassWordDialog.dismiss();
            }
        }
    }

    private void checkFundsPwd(String pwd, final Map<String, String> param) {

        if (pwd == null || pwd.length() < 6) {
            showToast(getString(R.string.a329));
            return;
        }


        String userid = AppUtils.getUserId();

        Map<String, String> verificationParam = new LinkedHashMap<>();
        verificationParam.put("paypassword", pwd);
        verificationParam.put("id", userid);

        NetUtils.postString(Url.verificationPwd, verificationParam, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(e.toString());
                showToast(getString(R.string.a537));
                dismissPwdDialog();
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {

                        usdExtract(param);


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


    private void getAvailableUsd() {
        String userId = AppUtils.getUserId();

        Map<String, String> param = new LinkedHashMap<>();
        param.put("userId", userId);
        param.put("type", "2");
        param.put("ctid","0");

        NetUtils.get(Url.availableFreezeCont, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getAvailableUsd onError: "+e.toString());
                showToast(getString(R.string.a537));
                loadService.showCallback(ErrorCallback.class);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getAvailableUsd onResponse: "+response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        loadDataCount++;
                        if (loadDataCount == 2) {
                            loadService.showSuccess();
                        }

                        JSONObject obj1 = obj.getJSONObject("obj");
                        availableUsd = obj1.getString("account");
                        tvAvailable.setText(getString(R.string.a448) + availableUsd);

                    } else {
                        showToast(obj.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void usdExtract(Map<String, String> param) {

        showLoadingDialog();
        NetUtils.postString(Url.usdExtract, param, new NetUtils.StringCallBack() {
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
                        showToast(obj.getString("msg"));

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

    private void getExchangeRate() {
        Map<String, String> param = new LinkedHashMap<>();
        param.put("content", "USDCNH");
        param.put("type", "2");

        NetUtils.get(Url.getRate, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getExchangeRate onError: " + e.toString());
                showToast(getString(R.string.a537));
                loadService.showCallback(ErrorCallback.class);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getExchangeRate onResponse: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        loadDataCount++;
                        if (loadDataCount == 2) {
                            loadService.showSuccess();
                        }

                        JSONArray obj1 = obj.getJSONArray("obj");
                        JSONObject jsonObject = obj1.getJSONObject(0);
                        rate = jsonObject.getDouble("price");
                        tvExtractCount.setText(String.format(getString(R.string.a397), String.valueOf(rate)));
                        tvAmount.setText(String.valueOf(Arith.mul(10, rate, 2)));

                    } else {
                        showToast(obj.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADDBANK) {
            if (resultCode == RESULT_OK) {
                rlAddCard.setVisibility(View.GONE);
                rlBankCard.setVisibility(View.VISIBLE);

                String cardNum = data.getStringExtra("cardNum");
                String bank = data.getStringExtra("bank");

                String substring = cardNum.substring(cardNum.length() - 4, cardNum.length());
                String str = bank + String.format(getString(R.string.a392), substring);
                tvBankCard.setText(str);

            }


        }


    }
}
