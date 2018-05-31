package com.atkj.ctc.utils;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Administrator on 2018/1/8 0008.
 */

public abstract class MarketWSCline {

    //WebSocketClient 和 address
    private WebSocketClient mWebSocketClient;
    private boolean isOpen;


    //初始化WebSocketClient

    /**
     * @throws URISyntaxException
     */
    public void initSocketClient() throws URISyntaxException {
        if (mWebSocketClient == null) {
            mWebSocketClient = new WebSocketClient(URI.create(Url.wsMarket)) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    //连接成功
                    LogUtils.d("opened connection");
                    MarketWSCline.this.onOpen(serverHandshake);
                }


                @Override
                public void onMessage(String s) {
                    //服务端消息
                    LogUtils.d("received:" + s);
                    MarketWSCline.this.onMessage(s);
                }


                @Override
                public void onClose(int i, String s, boolean remote) {
                    //连接断开，remote判定是客户端断开还是服务端断开
                    LogUtils.d("Connection closed by " + (remote ? "remote peer" : "us") + ", info=" + s);
                    //
                    MarketWSCline.this.onClose(i,s,remote);
                }


                @Override
                public void onError(Exception e) {
                    LogUtils.d("error:" + e);
                    MarketWSCline.this.onError(e);
                }
            };


        }
    }

    abstract void onOpen(ServerHandshake serverHandshake);
    abstract void onMessage(String string);
    abstract void onClose(int i, String s, boolean remote);
    abstract void onError(Exception e);


    //连接
    public void connect() {
        mWebSocketClient.connect();
    }


    //断开连接
    private void closeConnect() {
        try {
            mWebSocketClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mWebSocketClient = null;
        }
    }


    //发送消息

    /**
     *
     */
    public void send(String msg) {
        if (mWebSocketClient.isOpen()){
            mWebSocketClient.send(msg);
        }

    }


}
