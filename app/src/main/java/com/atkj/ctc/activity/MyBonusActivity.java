package com.atkj.ctc.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.api.ErrorCallback;
import com.atkj.ctc.api.LoadingCallback;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.views.MyToolBar;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2018/1/17 0017.
 */

public class MyBonusActivity extends ToobarActivity {
    private static final java.lang.String TAG = "MyBonusActivity";

    @BindView(R.id.invite_count)
    TextView inviteCount;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.instructions)
    TextView tvInstructions;

    private LoadService loadService;
    private AlertDialog tipsDialog2;
    private AlertDialog extractTips;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_my_bonus);
        ButterKnife.bind(this);

        loadService = LoadSir.getDefault().register(this, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                loadService.showCallback(LoadingCallback.class);
                initData();
            }
        });

        init();
        initData();

    }

    private void initData() {

        getBonus();

    }

    private void getBonus() {
        String userId = AppUtils.getUserId();
        Map<String,String> param = new LinkedHashMap<>();
        param.put("userid",userId);

        NetUtils.get(Url.myBonus, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getBonus onError: " + e.toString());
                showToast(getString(R.string.a537));
                loadService.showCallback(ErrorCallback.class);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getBonus onResponse: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        JSONArray array = obj.getJSONArray("obj");
                        JSONObject object = array.getJSONObject(0);

                        price.setText(object.getString("account"));
                        inviteCount.setText(object.getString("people"));

                        loadService.showSuccess();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    private void init() {
        tvInstructions.getPaint().setFakeBoldText(true);

       /* recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.layout_empty);*/

        getToolBar().setRightText(getString(R.string.a495));
        setToobarTitle(getString(R.string.a110));
        getToolBar().setTextRightVisibility(true);
        getToolBar().setOnRightClickListener(new MyToolBar.OnRightClickListener() {
            @Override
            public void onRightClick() {
                BonusDetailActivity.actionStart(MyBonusActivity.this);
            }
        });


    }


    /*private List<MyBonusBean> mListData;
    private CommonRecycleViewAdapter adapter = new CommonRecycleViewAdapter<MyBonusBean>(R.layout.list_item_my_bonus, mListData) {
        @Override
        protected void convert(BaseViewHolder helper, MyBonusBean item) {

            BigDecimal decimal = new BigDecimal(String.valueOf(item.getAccount()));
            decimal.setScale(4,BigDecimal.ROUND_HALF_UP);
            String bonus = decimal.toPlainString();


            helper.setText(R.id.user, item.getUsername())
                    .setText(R.id.time, AppUtils.timedate(item.getCreatetime(), "yyyy-MM-dd"))
                    .setText(R.id.bonus, bonus);
        }
    };*/


    @OnClick({R.id.extract})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.extract:
               showExtractTips();
                break;


        }


    }

    private void extract() {
        String amount = price.getText().toString();
        int vipstatus = AppUtils.getUser().getVipstatus();
        if (Double.parseDouble(amount) == 0) {
            showToast(getString(R.string.a337));
            return;
        } else if (vipstatus == Constants.MEMBER_ORDINARY) {
            showOpenVipTips();
            return;
        }

        String userId = AppUtils.getUserId();
        Map<String,String> parma = new LinkedHashMap<>();
        parma.put("userid",userId);
        parma.put("account",amount);

        showLoadingDialog();
        NetUtils.put(Url.bonusWithdraw, parma, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "extract onError: " + e.toString());
                dismissDialog();
                showToast(getString(R.string.a537));

            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "extract onResponse: " + response);
                dismissDialog();
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("status") == 200) {
                        showToast(getString(R.string.a338));
                        price.setText("0.00");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void showOpenVipTips() {
        final AlertDialog.Builder customizeDialog = new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_cancel_entrust, null);
        final TextView cancel = dialogView.findViewById(R.id.cancel);
        final TextView enter = dialogView.findViewById(R.id.enter);
        final TextView content = dialogView.findViewById(R.id.content);

        content.setText(getString(R.string.a339));
        enter.setText(getString(R.string.a246));
        enter.setTextColor(getResources().getColor(R.color.textColor2));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipsDialog2.dismiss();
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyBonusActivity.this, MemberActivity.class);
                startActivity(intent);
                tipsDialog2.dismiss();
            }
        });

        TextView title = new TextView(this);
        title.setText("");
        customizeDialog.setCustomTitle(title);
        customizeDialog.setView(dialogView);

        tipsDialog2 = customizeDialog.show();
    }

    private void showExtractTips() {
        final AlertDialog.Builder customizeDialog = new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_cancel_entrust, null);
        final TextView cancel = dialogView.findViewById(R.id.cancel);
        final TextView enter = dialogView.findViewById(R.id.enter);
        final TextView content = dialogView.findViewById(R.id.content);

        content.setText(getString(R.string.a525));
        enter.setText(getString(R.string.a56));
        enter.setTextColor(getResources().getColor(R.color.textColor2));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extractTips.dismiss();
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                extract();

                extractTips.dismiss();
            }
        });

        TextView title = new TextView(this);
        title.setText("");
        customizeDialog.setCustomTitle(title);
        customizeDialog.setView(dialogView);

        extractTips = customizeDialog.show();
    }


}
