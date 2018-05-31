package com.atkj.ctc.utils;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public interface Url {
    String test = "http://ctctest.atkj6666.com/api";
    String testStaticUrl = "http://ctctest.atkj6666.com";

    String nativee = "http://192.168.31.199:8070";//阿青本地
    String nativee1 = "http://192.168.31.240:8070";//老陈本地
    String nativee2 = "http://192.168.31.66:8080";
    String nativee1_token = "http://192.168.31.240:8075";

    String release = "https://www.safectc.pro:8071";
    String releaseStaticUrl = "http://ctcadmin.atkj6666.com";

    String baseUrl = test;
    String baseStaticUrl = releaseStaticUrl;


    /*
    http://ctc.safectc.pro:8070
    内网:http://192.168.31.199:8070
    http://ctctest.atkj6666.com
    http://172.105.203.215:8666 静态地址 IP
    正式服务器的接口地址：http://www.safectc.pro:8070/
    测试服务器的web后台：120.77.83.36:8070
    测试服务器的接口：120.77.83.36:8070*/

    String verificationCode = baseUrl + "/user/sendMobileCoded";//发送验证码
    String register = baseUrl + "/user/registered";//注册
    String login = baseUrl + "/user/login";//登录
    String updatePwd = baseUrl + "/user/updatePwd";//修改密码
    String sellList = baseUrl + "/order/sellList";//卖单列表
    String buyList = baseUrl + "/order/buyList";//买单列表
    String sellDetail = baseUrl + "/order/sellDetail";// 买入 卖单详情
    String buyDetail = baseUrl + "/order/buyDetail";//卖出 买单详情
    String myAssets = baseUrl + "/user/myAssets";//我的资产
    String loginOut = baseUrl + "/user/loginOut";//退出登录
    String modifyPwd = baseUrl + "/user/modifyPwd";//修改登录密码
    String updateName = baseUrl + "/user/updateName";//修改用户昵称
    String indexPage = baseUrl + "/order/indexPage";//首页前10条数据


    String sellConfirm = baseUrl + "/order/sellConfirm";//买入 卖单详情确认
    String buyConfirm = baseUrl + "/order/buyConfirm";//卖出 买单详情确认

    String buyOrderConfirm = baseUrl + "/pay/buyOrderConfirm";//卖单-买家确认付款 buyOrderConfirm
    String sellOrderConfirm = baseUrl + "/pay/sellOrderConfirm";//卖单-买家确认付款 buyOrderConfirm

    String availableFreezeCont = baseUrl + "/user/myAccount";//发布 可用,冻结数量
    String postBuyOrder = baseUrl + "/order/buyOrderAdd";//发布 买单
    String postSellOrder = baseUrl + "/order/sellOrderAdd";//发布 卖单
    String myDelete = baseUrl + "/order/myDelete";//我的 删除订单
    String myAppeal = baseUrl + "/order/myAppeal";//我的 申诉
    String myOrder = baseUrl + "/order/myOrder";//我的 我的订单

    String myPublish = baseUrl + "/order/myRelease";//我的 我的发布
    String orderCancel = baseUrl + "/order/orderCancel/";//我的 取消发布

    String paySetPwd = baseUrl +  "/user/paySetPwd";//设置支付密码
    String verificationPwd = baseUrl +  "/user/verificationPwd";//验证支付密码
    String payUpdatePwd = baseUrl +  "/user/payUpdatePwd";// 修改/忘记 支付密码
    String recharge = baseUrl +  "/record/recharge";// 资产 pai充值
    String updateTransferNo = baseUrl + "/record/updateTransferNo/";// 资产 pai充值
    String checkVersion = baseUrl + "/version/getVersion";//版本升级
    String bindSet = baseUrl + "/user/bindSet";//支付绑定
    String getIndexImg = baseUrl + "/img/getIndexImg";//轮播图
    String getRate = baseUrl + "/rate/getRate";//汇率
    String setRealName = baseUrl + "/user/setRealName";//设置实名
    String setBankCard = baseUrl + "/user/setBankCard";//添加银行卡
    String usdExtract = baseUrl + "/record/withdraw";//usd提现
    String buyOrderAdd = baseUrl + "/hall/buyOrderAdd";//大盘交易 买入
    String sellOrderAdd = baseUrl + "/hall/sellOrderAdd";//大盘交易 卖出
    String pendingOrder = baseUrl + "/hall/pendingOrder";//大盘交易 委托前10条记录
    String dealOrder = baseUrl + "/hall/dealOrder";//大盘交易 最新交易
    String myTrust = baseUrl + "/hall/myTrust";//大盘交易 委托
    String myHistory = baseUrl + "/hall/myHistory";//大盘交易 历史
    String cancelTrust = baseUrl + "/hall/cencalTrust";//大盘交易 取消委托
    String memberOpen = baseUrl + "/points/upgrade";//开通会员
    String pointDetails = baseUrl + "/points/pointDetails";//我的积分
    String myBonus = baseUrl + "/user/myBonus";//我的奖金
    String bonusWithdraw = baseUrl + "/record/bonusWithdraw";//我的奖金 提现
    String noticeList = baseUrl + "/announcement/announcementList";//首页 公告
    String getHallFee = baseUrl + "/hall/getHallFee";//大盘交易 手续费
    String getOrderFee = baseUrl + "/order/getOrderFee";//点对点交易 手续费

    String tutorial = baseStaticUrl + "/page/html/novice.html";//新手教程
    String tutorial_en = baseStaticUrl + "/page/html/novice-en.html";//新手教程

    String share = baseStaticUrl + "/page/html/share.html";//分享
    String share_en = baseStaticUrl + "/page/html/share-en.html";//分享

    String shareLogo = baseStaticUrl + "/images/logo.png";//分享 Logo
    String wsMarket = "wss://real.okcoin.com:10440/websocket";//
    String getPrivelege = baseUrl + "/privilege/getPrivelege";//vip特权
    String userCore = baseUrl + "/privilege/userCore";//vip等级

    String kLine = "https://www.okcoin.com/api/v1/kline.do";
    String usdRecharge = baseUrl + "/record/usdRecharge";//usd 充值
    String usdRechargeConfirm = baseUrl + "/record/usdRechargeConfirm";//usd 充值
    String paiWithdraw = baseUrl + "/record/paiWithdraw";//π的提取
    String ctData = baseUrl + "/hall/getCTData";//交易页面 币种 title
    String c2cbuyOrderAdd = baseUrl + "/currency/buyOrderAdd";//币币交易 买单
    String c2csellOrderAdd = baseUrl + "/currency/sellOrderAdd";//币币交易 卖单
    String getWalletAddress = baseUrl + "/wallet/getAddress";//钱包地址
    String transferToAddress = baseUrl + "/wallet/transferToAddress";//提币
    String checkPhone = baseUrl + "/cts/checkPhone";//cts 申购
    String buyCts = baseUrl + "/cts/buyCts";//cts 申购
    String myOrderDetails = baseUrl + "/order/myOrderDetails";
    String myBailmoney = baseUrl + "/order/myBailmoney";
    String myUsdDetails = baseUrl + "/user/myUsdDetails";
    String myBonusDetails = baseUrl + "/user/myBonusDetails";
    String hallTransfer = baseUrl + "/hall/hallTransfer";
    String getCTByCurrencyid = baseUrl + "/hall/getCTByCurrencyid";
    String currencyDetails = baseUrl + "/hall/currencyDetails";
    String getAllCurrency = baseUrl + "/currency/getAllCurrency";
    String myAssetsDetails = baseUrl + "/user/myAssetsDetails";
    String verificationSMS = baseUrl + "/user/verificationSMS";
    String getCurrentCts = baseUrl + "/cts/getCurrentCts";













}
