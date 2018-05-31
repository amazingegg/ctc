package com.atkj.ctc.utils;

/**
 * Created by Administrator on 2017/12/19 0019.
 */

public interface Constants {
    int DEBUGLEVEL = LogUtils.LEVEL_ALL;

    String app_key = "awi9Of";
    String secret = "ctc_secret";

    int ORDER_TYPE_ALL = 0;//全部
    int ORDER_TYPE_UNPAID = 1;//未付款
    int ORDER_TYPE_PAID = 2;//已付款
    int ORDER_TYPE_COMPLETED = 3;//已完成
    int ORDER_TYPE_APPEAR = 4;//申诉中

    int PUBLISH_TYPE_ALL = 0;//全部
    int PUBLISH_TYPE_PENDING = 1;//挂单中
    int PUBLISH_TYPE_TRANSACTION = 2;//交易中
    int PUBLISH_TYPE_COMPLETED = 3;//已完成

    //状态 我的订单  //1-挂单，  2-未付款，3-已付款，4-已完成，  5-取消订单   6-申诉中，
    //状态 我的发布    1-挂单中，2-交易中，3-已完成，4-系统取消，5-未确认订单，6-用户取消）

    int ORDER_STATUS_PENDING = 1;//挂单中
    int ORDER_STATUS_UNPAID = 2;//未付款
    int ORDER_STATUS_PAID = 3;//已付款
    int ORDER_STATUS_COMLETED = 4;//已完成
    int ORDER_STATUS_CANCEL = 5;//取消订单
    int ORDER_STATUS_APPEAR = 6;//申诉中



    String ORDER_PENDING = "PENDING";
    String ORDER_TRANSATION = "TRANSATION";
    String ORDER_COMPLETE = "COMPLETE";
    String ORDER_CANCEL = "CANCEL";
    String ORDER_UNCONFIRMED = "UNCONFIRMED";



    int PAYMENT_WEICHAT = 1;//微信
    int PAYMENT_ALIPAY = 2;//支付宝
    int PAYMENT_YINLIAN = 3;//银联

    String USER_PHONE = "phone";
    String USER_NAME = "username";
    String PASS_WORD = "password";
    String USER = "user";
    String IS_LOGIN = "islogin";

    String SP_USER_INFO = "userInfo";

    String CLIENT_TYPE_ANDROID = "2";


    int ORDER_SELL = 1;
    int ORDER_BUY = 2;
    int ACTIVITY_PAPERWORK_INFO = 1;
    int BIND_TYPE_ZFB = 1;
    int BIND_TYPE_WX = 2;
    int BIND_TYPE_YL = 3;
    String MEDIATYPE = "application/json;charset=utf-8";

    int MEMBER_ORDINARY = 1;//普通用户
    int MEMBER_VIP = 2;//VIP用户
    int MEMBER_SPECIAL_VIP = 3;//特殊VIP用户




    /**
     * 扫描类型
     * 条形码或者二维码：REQUEST_SCAN_MODE_ALL_MODE
     * 条形码： REQUEST_SCAN_MODE_BARCODE_MODE
     * 二维码：REQUEST_SCAN_MODE_QRCODE_MODE
     *
     */
    String REQUEST_SCAN_MODE="ScanMode";
    /**
     * 条形码： REQUEST_SCAN_MODE_BARCODE_MODE
     */
    int REQUEST_SCAN_MODE_BARCODE_MODE = 0X100;
    /**
     * 二维码：REQUEST_SCAN_MODE_ALL_MODE
     */
    int REQUEST_SCAN_MODE_QRCODE_MODE = 0X200;
    /**
     * 条形码或者二维码：REQUEST_SCAN_MODE_ALL_MODE
     */
    int REQUEST_SCAN_MODE_ALL_MODE = 0X300;


    double PRICE_MAX = 10000000;//价格最大值
    String MEMBER_PRICE = "199";
    String LANGUAGE = "language";
    int CURRENCY_MARKET = 1;//大盘 币种关系
    int CURRENCY_C2C = 2;//币币 币种关系
    int CURRENCY_P2P = 3;//点对点 币种关系


    //类型,1-会员升级,2-静态奖,3-直推奖,4-共享奖,5-幸运奖,6-提取USD,7-会员返利,8-服务费
    int BONUS_TYPE_ALL = 0;//全部
    int BONUS_TYPE_MEMBER_UPGRADE = 1;//会员升级
    int BONUS_TYPE_STATIC = 2;//静态奖
    int BONUS_TYPE_SHARE = 3;//分享奖
    int BONUS_TYPE_SHARED = 4;//共享奖
    int BONUS_TYPE_LUCKY = 5;//幸运奖
    int BONUS_TYPE_EXTRACT = 6;//提取USD
    int BONUS_TYPE_REBATE = 7;//会员返利
    int BONUS_TYPE_SERVICE= 8;//服务费


    int CURRENCY_TYPE_C2C = 4;
    int CURRENCY_TYPE_P2P = 3;
    String MINI_EXTRACT = "0.001";//最小提币数量;
    String SP_USER_JSON = "user_json";
}
