package com.atkj.ctc.utils;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.atkj.ctc.MainActivity;
import com.atkj.ctc.MyApplication;
import com.atkj.ctc.R;
import com.atkj.ctc.activity.LoginActivity;
import com.atkj.ctc.bean.AllCurrencyBean;
import com.atkj.ctc.bean.UserBean;
import com.atkj.ctc.bean.VersionBean;
import com.atkj.ctc.scancode.utils.Constant;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import sun.misc.BASE64Encoder;


/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class AppUtils {


    public static UserBean userBean;
    public static boolean isLogin;
    public static boolean isUpdate;
    public static VersionBean updateInfo;
    public static List<Double> lastPrice = new ArrayList<>();
    public static List<AllCurrencyBean.ObjBean> allCurrencyBeanList = new ArrayList<>();
    public static int ctid;
    public static boolean isBuy = true;
    private static boolean isOverdue;


    /**
     * 获取APP更新信息
     * @return
     */
    public static VersionBean getUpdateInfo() {

        return updateInfo;
    }


    /**
     * 获取用户Token
     * @return
     */
    public static String getUserToken() {
        if (userBean != null && isLogin) {
            return userBean.getObj().getToken();
        } else {
            return "";
        }

    }

    /**
     * 获取Gson
     * @return
     */
    public static Gson getGson() {
        return MyApplication.mGson;
    }

    /**
     * 获取Handler
     * @return
     */
    public static Handler getHandler() {
        return MyApplication.mHandler;
    }

    /**
     * 获取全局Context
     * @return
     */
    public static Context getContext() {
        return MyApplication.mContext;
    }


    public static String getUrl(String url, Map<String, String> prams) {
        StringBuilder sb = new StringBuilder();
        sb.append(Url.baseUrl).append(url);
        for (String str : prams.values()) {
            sb.append(str).append("/");
        }

        return sb.toString();
    }

    /**
     * 使用SharedPreferences保存用户登录信息
     *
     * @param context
     * @param username
     * @param password
     */
    public static void saveLoginInfo(Context context, String username, String password) {
        //获取SharedPreferences对象
        SharedPreferences sharedPre = context.getSharedPreferences("config", context.MODE_PRIVATE);
        //获取Editor对象
        SharedPreferences.Editor editor = sharedPre.edit();
        //设置参数
        editor.putString("username", username);
        editor.putString("password", password);
        //提交
        editor.apply();
    }


    /**
     * 获取支付类型
     * @param type 1-微信 2-支付宝 3-银行转账
     * @return
     */
    public static String getPaymentType(String type) {
        if (type == null) {
            type = "";
        }
        String[] split = type.split(",");
        StringBuilder builder = new StringBuilder();
        for (String s : split) {
            switch (s) {
                case "1":
                    builder.append(getContext().getString(R.string.a5));
                    break;
                case "2":
                    if (split[0].equals("2")) {
                        builder.append(getContext().getString(R.string.a3));
                    } else {
                        builder.append("/");
                        builder.append(getContext().getString(R.string.a3));
                    }
                    break;
                case "3":
                    if (split[0].equals("3")) {
                        builder.append(getContext().getString(R.string.a451));
                    } else {
                        builder.append("/");
                        builder.append(getContext().getString(R.string.a451));
                    }
                    break;
            }
        }

        return builder.toString();
    }


    /**
     * 获取用户id
     * @return
     */
    public static String getUserId() {
        if (isLogin && userBean != null) {
            return userBean.getObj().getId();
        } else {
            return "";
        }
    }

    /**
     * 根据手机分辨率从DP转成PX
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 设置透明度
     * @param context
     * @param f
     */
    public static void setWindowAlpha(Activity context, float f) {
        WindowManager.LayoutParams attributes = context.getWindow().getAttributes();
        attributes.alpha = f;
        context.getWindow().setAttributes(attributes);
    }

    /**
     * 获取订单状态
     * @param orderStatus
     * @param isMyOrder
     * @return
     */
    public static String getOrderStatus(int orderStatus, boolean isMyOrder) {
        //我的发布 状态（1-挂单中，2-交易中，3-已完成，4-系统取消，5-未确认订单，6-用户取消）
        switch (orderStatus) {
            case Constants.ORDER_STATUS_PENDING://1
                return getContext().getString(R.string.a340);
            case Constants.ORDER_STATUS_UNPAID://2
                return isMyOrder ? getContext().getString(R.string.a121) : getContext().getString(R.string.a341);

            case Constants.ORDER_STATUS_PAID://3
                return isMyOrder ? getContext().getString(R.string.a122) : getContext().getString(R.string.a123);

            case Constants.ORDER_STATUS_COMLETED://4
                return isMyOrder ? getContext().getString(R.string.a123) : getContext().getString(R.string.a509);

            case Constants.ORDER_STATUS_APPEAR://6
                return isMyOrder ? getContext().getString(R.string.a124) : getContext().getString(R.string.a510);

            case Constants.ORDER_STATUS_CANCEL://5
                return getContext().getString(R.string.a511);
            default:
                return "未识别状态";
        }
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014-06-14  16:09:00"）
     *
     * @param time
     * @return
     */
    public static String timedate(long time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        @SuppressWarnings("unused")
        String times = sdr.format(new Date(time));
        return times;
    }

    /**
     * 时间戳
     * @param time
     * @param pattern
     * @return
     */
    public static String timedate(long time, String pattern) {
        SimpleDateFormat sdr = new SimpleDateFormat(pattern);
        @SuppressWarnings("unused")
        String times = sdr.format(new Date(time));
        return times;
    }

    /**
     * 订单状态
     * @param orderStatus
     * @param type
     * @param userId
     * @param isMyOrder
     * @param sellStatus
     * @param buyStatus
     * @return
     */
    public static String getOrderStatus(int orderStatus, int type, String userId, boolean isMyOrder, String sellStatus, String buyStatus) {
        String myUserId = getUserId();
        switch (orderStatus) {

            case Constants.ORDER_STATUS_PENDING://挂单中
                return isMyOrder ? getContext().getString(R.string.a340) : getContext().getString(R.string.a199);

            case Constants.ORDER_STATUS_UNPAID://未付款
                if (type == 1 && userId.equals(myUserId)) {//卖单-卖家确认收款
                    return getContext().getString(R.string.a452);
                } else if (type == 1 && !userId.equals(myUserId)) {//卖单 -买家确认付款
                    return getContext().getString(R.string.a453);
                } else if (type == 2 && userId.equals(myUserId)) {//买单 - 买家确认付款
                    return getContext().getString(R.string.a453);
                } else if (type == 2 && !userId.equals(myUserId)) {//买单 卖家确认收款
                    return getContext().getString(R.string.a452);
                }
            case Constants.ORDER_STATUS_PAID://已付款

                if (isMyOrder) {//我的订单
                    if (type == 1 && userId.equals(myUserId)) {//卖单-卖家确认收款
                        return sellStatus.equals(Constants.ORDER_COMPLETE) ?
                                getContext().getString(R.string.a454) : getContext().getString(R.string.a452);
                    } else if (type == 1 && !userId.equals(myUserId)) {//卖单 -买家已付款
                        return buyStatus.equals(Constants.ORDER_COMPLETE) ?
                                getContext().getString(R.string.a454) : getContext().getString(R.string.a453);
                    } else if (type == 2 && userId.equals(myUserId)) {//买单 - 买家已付款
                        return buyStatus.equals(Constants.ORDER_COMPLETE) ?
                                getContext().getString(R.string.a454) : getContext().getString(R.string.a453);
                    } else if (type == 2 && !userId.equals(myUserId)) {//买单 卖家确认收款
                        return sellStatus.equals(Constants.ORDER_COMPLETE) ?
                                getContext().getString(R.string.a454) : getContext().getString(R.string.a452);
                    }
                } else {//我的发布
                    /*if (type == 1 && userId.equals(myUserId)) {//卖单-卖家确认收款
                        return getContext().getString(R.string.a452);
                    } else if (type == 1 && !userId.equals(myUserId)) {//卖单 -买家确认付款
                        return getContext().getString(R.string.a453);
                    } else if (type == 2 && userId.equals(myUserId)) {//买单 - 买家确认付款
                        return getContext().getString(R.string.a453);
                    } else if (type == 2 && !userId.equals(myUserId)) {//买单 卖家确认收款
                        return getContext().getString(R.string.a452);
                    }*/
                    return getContext().getString(R.string.a455);
                }

            case Constants.ORDER_STATUS_COMLETED://已完成
                return getContext().getString(R.string.a455);

            case Constants.ORDER_STATUS_CANCEL://未确认订单
                return getContext().getString(R.string.a456);

            case Constants.ORDER_STATUS_APPEAR://申诉中
                return getContext().getString(R.string.a456);

            default:
                return "状态异常";
        }

    }


    /**
     * 获取用户bean
     * @return
     */
    public static UserBean.ObjBean getUser() {
        if (userBean == null) {
            checkLogin(getContext());
            return null;
        } else {
            return userBean.getObj();
        }
    }


    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    public static float getVersionCode(Context mContext) {
        float versionCode = 0;
        try {
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    /**
     * 检查是否登录
     * @param activity
     * @return
     */
    public static boolean checkLogin(Context activity) {
        if (!AppUtils.isLogin) {
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 截取手机中间号码不可见
     * @param phone
     * @return
     */
    public static String splitPhone(String phone) {
        StringBuilder sb = new StringBuilder();
        sb.append(phone.substring(0, 3));
        sb.append("****");
        sb.append(phone.substring(7, phone.length()));

        return sb.toString();
    }

    /**
     * 复制文本到粘贴板
     * @param str
     */
    public static void copyClipboard(String str) {
        // 从API11开始android推荐使用android.content.ClipboardManager
        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
        ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(str);

    }

    /**
     * 关闭输入法
     * @param mEditText
     * @param mContext
     */
    public static void closeSoftKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    /**
     * 显示输入法
     * @param view
     * @param mContext
     */
    public void showSoftKeyboard(View view, Context mContext) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * @param context
     * @return 获取屏幕内容高度不包括虚拟按键
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 根据参数生成签名
     * @param param
     * @return
     */
    public static String getSign(Map<String, String> param) {
        //按字母排序
        Map<String, String> params = new TreeMap<>();
        params.putAll(param);

        StringBuilder stringBuilder = new StringBuilder();
        int size = 1;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            //对每个参数和值进行encode，对字符进行转义
            String key = null;
            String value = null;

            try {
                value = URLEncoder.encode(entry.getValue(), "utf-8");
                key = URLEncoder.encode(entry.getKey(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            stringBuilder.append(key + "=" + value);

            if (size != params.size()) {
                stringBuilder.append("&");
            }
            size++;
        }

        String stringToSign = Constants.app_key + stringBuilder.toString() + Constants.secret + "&";

        MessageDigest digest;// 获取一个实例，并传入加密方式
        BASE64Encoder encoder = new BASE64Encoder();
        String sign = null;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();// 清空一下
            digest.update(stringToSign.getBytes());
            byte[] bytes = digest.digest();
            String encoderSign = encoder.encode(bytes);
            sign = URLEncoder.encode(encoderSign, "utf-8");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return sign;

    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis());
    }


    /**
     * 更改应用语言
     *
     * @param context
     * @param locale      语言地区
     * @param persistence 是否持久化
     */

    public static void changeAppLanguage(Context context, Locale locale, boolean persistence) {

        Resources resources = context.getResources();

        DisplayMetrics metrics = resources.getDisplayMetrics();

        Configuration configuration = resources.getConfiguration();

        configuration.setLocale(locale);

        resources.updateConfiguration(configuration, metrics);

        if (persistence) {
            saveLanguageSetting(context, locale);
        }

    }

    /**
     * 保存语言信息
     * @param context
     * @param locale
     */
    private static void saveLanguageSetting(Context context, Locale locale) {

        SharedPreferences preferences = context.getSharedPreferences(Constants.LANGUAGE, Context.MODE_PRIVATE);

        preferences.edit().putString("COUNTRY", locale.getLanguage()).apply();
    }

    /**
     * 拨打电话
     * @param context
     * @param phoneNum
     */
    public static void diallPhone(Context context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }

    /**
     * 转换数字为非科学计数法
     * @param num
     * @return
     */
    public static String convertCounting(String num) {
        BigDecimal decimal = new BigDecimal(num);
        decimal.setScale(4, BigDecimal.ROUND_HALF_UP);
        return decimal.toPlainString();
    }

    /**
     * 清除币种选择
     */
    public static void clearCurrencySelect() {
        if (allCurrencyBeanList != null) {
            for (int i = 0; i < allCurrencyBeanList.size(); i++) {
                List<AllCurrencyBean.ObjBean.ListBean> list = allCurrencyBeanList.get(i).getList();

                for (int j = 0; j < list.size(); j++) {
                    list.get(j).setSelect(false);
                }
            }

        }


    }

    /**
     * 保存用户信息到sp
     * @param userJson
     */
    public static void saveUserInfo(String userJson) {
        SharedPreferences sp = getContext().getSharedPreferences(Constants.SP_USER_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();

        edit.putString(Constants.SP_USER_JSON, userJson);
        edit.apply();
    }


    public static UserBean getUserBean() {
        SharedPreferences sp = getContext().getSharedPreferences(Constants.SP_USER_INFO, Context.MODE_PRIVATE);

        String user_json = sp.getString(Constants.SP_USER_JSON, null);
        if (user_json != null) {
            return getGson().fromJson(user_json, UserBean.class);
        } else {
            return null;
        }
    }

    public static void login() {


        Context context = AppUtils.getContext();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        Toast.makeText(context, "登录过期,请重新登录", Toast.LENGTH_SHORT).show();

        SharedPreferences sp = context.getSharedPreferences(Constants.SP_USER_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(Constants.IS_LOGIN, false);
        edit.apply();

    }


}
