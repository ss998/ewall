package com.tcl.ewall;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Map;

public class MainActivity extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText ip = (EditText) findViewById(R.id.serverIP);
        final Button button = (Button) findViewById(R.id.roomOK);

        Context context = MainActivity.this;
        final SharedPreferences sharedPreferences = context.getSharedPreferences("data", Activity.MODE_PRIVATE);
        ip.setText(sharedPreferences.getString("ip", null));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("ip", ip.getText().toString());
                editor.commit();

                setContentView(R.layout.activity_web);

                webView = (WebView) findViewById(R.id.web);

                WebSettings webSettings = webView.getSettings();
                /**
                 * setAllowFileAccess 启用或禁止WebView访问文件数据 setBlockNetworkImage 是否显示网络图像
                 * setBuiltInZoomControls 设置是否支持缩放 setCacheMode 设置缓冲的模式
                 * setDefaultFontSize 设置默认的字体大小 setDefaultTextEncodingName 设置在解码时使用的默认编码
                 * setFixedFontFamily 设置固定使用的字体 setJavaSciptEnabled 设置是否支持Javascript
                 * setLayoutAlgorithm 设置布局方式 setLightTouchEnabled 设置用鼠标激活被选项
                 * setSupportZoom 设置是否支持变焦
                 * */
                webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
                webSettings.setPluginState(WebSettings.PluginState.ON);
                webSettings.setAllowFileAccess(true);
                webSettings.setLoadWithOverviewMode(true);
                webSettings.setBuiltInZoomControls(true);// 隐藏缩放按钮
                webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕
                webSettings.setUseWideViewPort(true);// 可任意比例缩放
                webSettings.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
                webSettings.setSavePassword(true);
                webSettings.setSaveFormData(true);// 保存表单数据
                webSettings.setJavaScriptEnabled(true);
                webSettings.setDomStorageEnabled(true);
                webView.setSaveEnabled(false);
                webSettings.setSaveFormData(false);
                // 下面的一句话是必须的，必须要打开javaScript不然所做一切都是徒劳的
                webSettings.setJavaScriptEnabled(true);
                webSettings.setSupportZoom(false);
                //setWebChromeClient主要处理解析，渲染网页等浏览器做的事情
                //这个方法必须有，就算类中没有函数也可以，不然视频播放不了
                webView.setWebChromeClient(new mWebChromeClient());

                String WebUrl = "http://v.qq.com/iframe/player.html?vid=o0318tp1ddw&tiny=0&auto=0";// + ip.getText().toString();
                webView.addJavascriptInterface(new WebInterface(MainActivity.this), "ton");
                webView.loadUrl(WebUrl);
            }
        });
    }

    public class mWebChromeClient extends WebChromeClient {
        @Override
        //播放网络视频时全屏会被调用的方法
        public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
            webView.addView(view);
        }
    }

    public class WebInterface {
        Context ctx;

        WebInterface(Context c) {
            ctx = c;
        }

        public String getJSRoomNumber(String room) {
            return room;
        }

        @JavascriptInterface
        public void transMessage(String str) {
            Toast.makeText(ctx, str,Toast.LENGTH_LONG).show();
        }
    }
}
