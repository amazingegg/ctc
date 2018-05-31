package com.atkj.ctc.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.Constants;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.Url;
import com.atkj.ctc.views.MyToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Administrator on 2018/1/17 0017.
 */

public class ShareActivity extends ToobarActivity {


    private static final String TAG = "ShareActivity";
    @BindView(R.id.webView)
    WebView mWebView;


    private WebSettings mWebSettings;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildLayout(R.layout.activity_share);
        ButterKnife.bind(this);


        init();

        initWeb();


    }

    private void init() {

        getToolBar().setRightText(getString(R.string.a110));
        getToolBar().setTextRightVisibility(true);
        getToolBar().setOnRightClickListener(new MyToolBar.OnRightClickListener() {
            @Override
            public void onRightClick() {
                if (AppUtils.checkLogin(ShareActivity.this)) return;

                Intent intent = new Intent(ShareActivity.this, MyBonusActivity.class);
                startActivity(intent);
            }
        });


        getToolBar().setOnBackClickListener(new MyToolBar.OnBackClickListener() {
            @Override
            public void onBackClick() {
                finish();
            }
        });


    }


    private void initWeb() {
        mWebSettings = mWebView.getSettings();

        //设置可用JS脚本
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);

        mWebSettings.setSupportZoom(true);
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setUseWideViewPort(true);

        //设置WebChromeClient类
        mWebView.setWebChromeClient(new WebChromeClient() {
            //获取网站标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                System.out.println("标题在这里 ==" + view.getTitle());
                getToolBar().setTitle(title);

            }


            //获取加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    String progress = newProgress + "%";
                    //loading.setText(progress);
                } else if (newProgress == 100) {
                    String progress = newProgress + "%";
                    //loading.setText(progress);
                }
            }
        });

        //设置WebViewClient类
        mWebView.setWebViewClient(new WebViewClient() {
            //设置加载前的函数
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                System.out.println("开始加载了");
                //beginLoading.setText("开始加载了");

            }

            //设置结束加载函数
            @Override
            public void onPageFinished(WebView view, String url) {
                //endLoading.setText("结束加载了");

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                // 步骤2：根据协议的参数，判断是否是所需要的url
                // 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
                //假定传入进来的 url = "js://webview?arg1=111&arg2=222"（同时也是约定好的需要拦截的）

                Uri uri = Uri.parse(url);
                // 如果url的协议 = 预先约定的 js 协议
                // 就解析往下解析参数
                if (uri.getScheme().equals("js")) {

                    // 如果 authority  = 预先约定协议里的 webview，即代表都符合约定的协议
                    // 所以拦截url,下面JS开始调用Android需要的方法
                    if (uri.getAuthority().equals("share")) {
                       /* //  步骤3：
                        // 执行JS所需要调用的逻辑
                        System.out.println("js调用了Android的方法");
                        // 可以在协议上带有参数并传递到Android上
                        HashMap<String, String> params = new HashMap<>();
                        Set<String> collection = uri.getQueryParameterNames();
                        LogUtils.d(TAG, "shouldOverrideUrlLoading: =="+collection.toString());*/
                        showShare();
                    }

                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

        });

        String url;
        SharedPreferences sp = getSharedPreferences(Constants.LANGUAGE, Context.MODE_PRIVATE);
        String country = sp.getString("COUNTRY", "");
        switch (country) {
            case "zh":
                url = Url.share;
                break;
            case "en":
                url = Url.share_en;
                break;
            default:
                url = Url.share;
        }
        mWebView.loadUrl(url);

    }

    //@JavascriptInterface
    public void showShare() {
        if (AppUtils.checkLogin(this)) return;

        String url = Url.baseStaticUrl + "/page/html/register_index.html?userid=" + AppUtils.getUserId();
        LogUtils.d(TAG, "showShare: " + url);

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle(getString(R.string.share_title));

        // text是分享文本，所有平台都需要这个字段
        oks.setText("CTC交易平台上线啦！交易无费用，注册邀请送不停！还有更多福利好礼等你来哦！");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImageUrl(Url.shareLogo);
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl(url);
        // 启动分享GUI
        oks.show(this);
    }


}
