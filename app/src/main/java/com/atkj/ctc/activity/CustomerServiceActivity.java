package com.atkj.ctc.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.bean.MarketListBean;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.Url;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/26 0026.
 */

public class CustomerServiceActivity extends ToobarActivity {


    String url = "ws://ctctest.atkj6666.com:8071/websocket";
    private WebSocketClient webSocketClient;


    @BindView(R.id.hot_line)
    RelativeLayout hotLine;
    @BindView(R.id.tv_wechat2)
    TextView wechat;
    @BindView(R.id.tv_wechat1)
    TextView wechat1;
    @BindView(R.id.tv_hot_line)
    TextView tel1;
    @BindView(R.id.tv_qq2)
    TextView qq;

    private java.lang.String TAG = "CustomerServiceActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_customer_service);
        ButterKnife.bind(this);
        setToobarTitle(getString(R.string.a218));
        initEvent();

    }

    private void initEvent() {


    }

    private String URL = "wss://api.huobipro.com/ws";
    public void initWSCline() {
        webSocketClient = null;
        if (webSocketClient == null) {
            webSocketClient = new WebSocketClient(URI.create(URL)) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    //LogUtils.d( "test==onOpen", handshakedata.getHttpStatus() + "");
                    //LogUtils.d( "test==onOpen", handshakedata.getHttpStatusMessage());
                    LogUtils.d(TAG, "onOpen: ");
                    webSocketClient.send("{ \"sub\": \"market.btcusdt.trade.detail\",\"id\": \"20258995\"}");

                }

                @Override
                public void onMessage(String message) {
                    //Json的解析类对象
                    JsonParser parser = new JsonParser();
                    //将JSON的String 转成一个JsonArray对象
                    JsonArray jsonArray = parser.parse(message).getAsJsonArray();
                    JsonElement jsonElement = jsonArray.get(0);

                    LogUtils.d(TAG, "onMessage: "+message);


                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    //LogUtils.d( "test====Connection closed by " + (remote ? "remote peer" : "us") + ", info=" + reason);
                    //closeConn();
                    LogUtils.d(TAG, "onClose: "+reason);
                }

                @Override
                public void onError(Exception ex) {
                    LogUtils.d(TAG, "onError: "+ex.toString());

                }
            };
        }

        webSocketClient.connect();
    }


    @OnClick({R.id.bt_commit,R.id.hot_line,R.id.wechat,R.id.qq,R.id.wechat1})
    public void onViewClicked(View view){


        switch (view.getId()){
            case R.id.bt_commit:
                showToast(getString(R.string.a248));//TODO 客服留言
                //initWSCline();
                break;
            case R.id.hot_line:
                AppUtils.diallPhone(this,tel1.getText().toString());
                break;
            case R.id.wechat:
                AppUtils.copyClipboard(wechat.getText().toString());
                showToast(getString(R.string.a275));
                break;
            case R.id.wechat1:
                AppUtils.copyClipboard(wechat1.getText().toString());
                showToast(getString(R.string.a275));
                break;
            case R.id.qq:
                AppUtils.copyClipboard(qq.getText().toString());
                showToast(getString(R.string.a275));
                break;
        }









    }














}
