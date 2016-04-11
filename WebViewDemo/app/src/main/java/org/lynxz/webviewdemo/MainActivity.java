package org.lynxz.webviewdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private WebView mWv;
    private static final String mUrl = "http://www.jianshu.com/users/302253a7ed00/latest_articles";
    private static final String TAG = "webView";
    private WebChromeClient.CustomViewCallback myCallback;
    private View myView;
    private ProgressDialog mProgressDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //        requestWindowFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Makes Progress bar Visible
        //        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);

        // 用于显示加载进度
        mProgressDlg = new ProgressDialog(this);
        mProgressDlg.setMessage("loading...");
        mProgressDlg.setCancelable(true);
        mProgressDlg.setCanceledOnTouchOutside(true);

        mWv = (WebView) findViewById(R.id.wv);

        mWv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // super.onProgressChanged(view, newProgress);
                Log.i(TAG, "onProgressChanged " + newProgress);
                MainActivity.this.setProgress(newProgress);
                if (newProgress <= 90) {
                    mProgressDlg.setProgress(newProgress);
                } else {
                    mProgressDlg.dismiss();
                }

            }


            @Override
            public void onReceivedTitle(WebView view, String title) {
                // title 是获取到的网页title,可以将之设置为webView所在页面的标题
                MainActivity.this.setTitle(title);
            }
        });


        mWv.setWebViewClient(new WebViewClient() {
            /**
             * returns
             *      false : tells the platform not to override the URL, but to load it in the WebView.
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 这个方法我没有重写的话也还是使用webView来加载链接
                // 而且我这里测试返回的true/false貌似没什么影响

                if (Uri.parse(url).getHost().endsWith("jianshu.com")) {
                    //指定服务器链接在当前webView中跳转
                    view.loadUrl(url);
                    return false;
                } else if (Uri.parse(url).getHost().length() == 0) {
                    // 本地连接的话直接在webView中跳转
                    return false;
                }

                // 使用系统浏览器打开网址
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                // 加载某些网站的时候会报:ERR_CONNECTION_REFUSED
                Log.i(TAG, "error ..... " + error.toString() + " \n " + error.toString());
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                if (mProgressDlg.isShowing()) {
                    mProgressDlg.dismiss();
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i(TAG, "onPageStarted");
                if (!mProgressDlg.isShowing()) {
                    mProgressDlg.show();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.i(TAG, "onPageFinished");
                if (mProgressDlg.isShowing()) {
                    mProgressDlg.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(MainActivity.this, "error occur " + description, Toast.LENGTH_SHORT).show();
            }
        });


        // 也可通过重写Activity的onBackPressed方法来支持回退
        mWv.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && mWv.canGoBack()) {
                    mWv.goBack();
                    return true;
                }
                return false;
            }
        });


        // webView设置
        WebSettings wvSettings = mWv.getSettings();
        //启用js代码支持
        wvSettings.setJavaScriptEnabled(true);
        //使用内置的缩放控制功能按钮,有些网页可能会禁用掉,所以不一定看的到效果
        //另外,官网建议启用缩放控制的时候,webView的宽/高不要设置成wrap_content
        wvSettings.setBuiltInZoomControls(true);
        //指定页面默认编码,不过即使这么设置了,loadData()仍有可能出现中文乱码的请款
        wvSettings.setDefaultTextEncodingName("utf-8");

        //强制手机使用 desktop-size viewport
        wvSettings.setUseWideViewPort(true);
        wvSettings.setLoadWithOverviewMode(true);

        mWv.addJavascriptInterface(new BasicJsAppInterface(this), "AndroidApp");

        // 允许通过chrome进行调试
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 官网: https://developer.chrome.com/devtools/docs/remote-debugging#debugging-webviews
            // 官网说WebView不受manifest的debuggable标签的影响,若需要在该标签启用时才允许调试,则添加如下条件判断
            int debuggable = getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE;
            if (0 != debuggable) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }

        loadPage();

        //用户点击可下载的链接时,会自动调用downloadListener的onDownloadStart方法
        mWv.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                // your download thread
            }
        });

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWv.loadUrl("javascript:setLabel('label','通过android调用js代码')");

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mWv.canGoBack()) {
            mWv.goBack();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 加载网页数据
     */
    private void loadPage() {
        // 1. 从本机asset目录中加载网页资源
        mWv.loadUrl("file:///android_asset/index.html");

        //      // 2. 加载HTML String
        //        String summary = "<!DOCTYPE html>\n" +
        //                "<html lang=\"zh_CN\">\n" +
        //                "<head>\n" +
        //                "    <meta charset=\"UTF-8\">\n" +
        //                "    <title>webViewDemoFromAsset</title>\n" +
        //                "    <script src=\"js/basic.js\"></script>\n" +
        //                "</head>\n" +
        //                "<body>\n" +
        //                "<div>\n" +
        //                "    <button id=\"btn\" onclick='showToast()'>调用android toast</button>\n" +
        //                "</div>\n" +
        //                "\n" +
        //                "<label id='label'>js android代码互调测试</label>\n" +
        //                "\n" +
        //                "<br>\n" +
        //                "<a href=\"http://www.jianshu.com/users/302253a7ed00/latest_articles/\">个人主页</a>\n" +
        //                "</body>\n" +
        //                "</html>";
        //
        //        // 官网例子给的下面的写法,但是会出现中文乱码,
        //        // 原因:http://blog.csdn.net/top_code/article/details/9163597
        //        // mWv.loadData(summary, "text/html", "utf-8");
        //
        //        mWv.loadData(summary, "text/html;charset=UTF-8", null);

        //        // 3. 加载线上资源
        //        mWv.loadUrl(mUrl);
    }
}
