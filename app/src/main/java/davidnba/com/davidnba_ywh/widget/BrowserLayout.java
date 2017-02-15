package davidnba.com.davidnba_ywh.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import davidnba.com.davidnba_ywh.R;

import static android.R.attr.description;

/**
 * Created by 仁昌居士 on 2017/2/14.
 */

public class BrowserLayout extends LinearLayout {
    private Context mContext = null;
    private WebView mWebView = null;
    private View mBrowserControllerView = null;
    private ImageButton mGoBackBtn = null;
    private ImageButton mGoForwardBtn = null;
    private ImageButton mGoBrowserBtn = null;
    private ImageButton mRefreshBtn = null;

    private int mBarHeight = 5;
    private ProgressBar mProgressBar = null;

    private String mLoadUrl;
    private OnReceiveTitleListener listener;


    public BrowserLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        setOrientation(VERTICAL);
        mProgressBar = (ProgressBar) LayoutInflater.from(context).inflate(R.layout.progress_horizontal, null);
        mProgressBar.setMax(100);
        mProgressBar.setProgress(0);
        addView(mProgressBar, WindowManager.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, mBarHeight, getResources().getDisplayMetrics()));
        mWebView = new WebView(context);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setBuiltInZoomControls(false);//设置是否支持缩放
        mWebView.getSettings().setSupportMultipleWindows(true);

        /*setAllowFileAccess( boolean allow);　　　　　　//设置启用或禁止访问文件数据
        　setBuiltInZoomControls( boolean enabled);　　//设置是否支持缩放
        　setDefaultFontSize( int size);　　　　　　　　  //设置默认的字体大小
        setJavaScriptEnabled( boolean flag);　　　　　 //设置是否支持JavaScript
        setSupportZoom( boolean support);　　　　　　//设置是否支持变焦*/


        LayoutParams lps = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        addView(mWebView, lps);

        //WebChromeClient主要用来辅助WebView处理Javascript的对话框、网站图标、网站标题以及网页加载进度等。
        mWebView.setWebChromeClient(new AppCacheWebChromeClient());
        //WebViewClient主要用来辅助WebView处理各种通知、请求等事件。
        mWebView.setWebViewClient(new MonitorWebClient());

        mBrowserControllerView = LayoutInflater.from(context).inflate(R.layout.browser_controller, null);
        mGoBackBtn = (ImageButton) mBrowserControllerView.findViewById(R.id.browser_controller_back);
        mGoForwardBtn = (ImageButton) mBrowserControllerView.findViewById(R.id.browser_controller_forward);
        mGoBrowserBtn = (ImageButton) mBrowserControllerView.findViewById(R.id.browser_controller_go);
        mRefreshBtn = (ImageButton) mBrowserControllerView.findViewById(R.id.browser_controller_refresh);

        mGoBackBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canGoBack()) {
                    goBack();
                }
            }
        });

        mGoForwardBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (canGoForward()) {
                    goForward();
                }
            }
        });

        mRefreshBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                refresh(mLoadUrl);
            }
        });

        mGoBrowserBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mLoadUrl)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(mLoadUrl));
                    mContext.startActivity(intent);
                }
            }
        });

        addView(mBrowserControllerView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    private void refresh(String mLoadUrl) {
        mWebView.reload();
    }

    private void goForward() {
        if (null != mWebView) {
            mWebView.goForward();//浏览器前进到下一页面
        }
    }

    private boolean canGoForward() {
        return null != mWebView ? mWebView.canGoForward() :false;
    }

    private void goBack() {
        if (null != mWebView) {
            mWebView.goBack();//浏览器前进到下一页面
        }
    }

    private boolean canGoBack() {
        return null != mWebView ? mWebView.canGoBack() :false;
    }

    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    public WebView getWebView(){
        return mWebView != null ? mWebView : null;
    }

    public void hideBrowserController(){
        mBrowserControllerView.setVisibility(View.GONE);
    }

    public void showBrowserController(){
        mBrowserControllerView.setVisibility(View.VISIBLE);
    }

    private class MonitorWebClient extends WebViewClient {

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            //错误提示
            Toast toast = Toast.makeText(mContext, "Oh no! " + description, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
            toast.show();
            //错误处理
            try {
                mWebView.stopLoading();
            } catch (Exception e) {
            }
            try {
                mWebView.clearView();
            } catch (Exception e) {
            }
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            }
        }

        //当load有ssl层的https页面时，如果这个网站的安全证书在Android无法得到认证，WebView就会变成一个空白页，而并不会像PC浏览器中那样跳出一个风险提示框
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            //忽略证书的错误继续Load页面内容
            handler.proceed();
            //handler.cancel(); // Android默认的处理方式
            //handleMessage(Message msg); // 进行其他处理
            //  super.onReceivedSslError(view, handler, error);
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mLoadUrl = url;
            if (listener != null) {
                listener.onPageFinished();
            }
        }
    }

    private class AppCacheWebChromeClient extends WebChromeClient {

        /*onReachedMaxAppCacheSize
        添加自 API level 7
        该方法在 API level 19 被弃用
        该方法将不再被调用; WebView现在使用Html5 / JavaScript Quota Management API.
        通知应用程序内核已经到达最大的appcache。appcache是HTML5针对offline的一个数据处理标准。

        参数
        requiredStorage	通过应用程序缓存操作触发通知所需的存储量，以字节为单位。
        quota	当前的最大应用程序缓存大小,以字节为单位。
        quotaUpdater	一个通知WebView使用新配额的WebStorage.QuotaUpdater实例。*/
        @Override
        public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater quotaUpdater) {
            //    Log.e(APP_CACHE, "onReachedMaxAppCacheSize reached, increasing space: " + spaceNeeded);
            quotaUpdater.updateQuota(requiredStorage * 2);
        }

       /* 在WebChromeClient中，
        当网页的加载进度发生变化时，onProgressChanged(WebView view, int newProgress)方法会被调用；
        当网页的图标发生改变时，onReceivedIcon(WebView view, Bitmap icon)方法会被调用；
        当网页的标题发生改变时，onReceivedTitle(WebView view, String title)方法会被调用。
        利用这些方法，我们便可以很容易的获得网页的加载进度、网页的标题和图标等信息了*/

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(newProgress);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (listener != null)
                listener.onReceive(title);
        }
    }

    public interface OnReceiveTitleListener {
        void onReceive(String title);

        void onPageFinished();
    }

    public void setOnReceiveTitleListener(OnReceiveTitleListener listener) {
        this.listener = listener;
    }
}
