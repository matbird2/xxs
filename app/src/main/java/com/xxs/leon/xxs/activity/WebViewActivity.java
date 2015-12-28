package com.xxs.leon.xxs.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gc.materialdesign.views.ProgressBarIndeterminateDeterminate;
import com.gc.materialdesign.widgets.SnackBar;
import com.umeng.analytics.MobclickAgent;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.utils.L;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by maliang on 15/12/17.
 */
@EActivity(R.layout.activity_webview)
public class WebViewActivity extends AppCompatActivity{
    @ViewById
    protected Toolbar toolbar;
    @ViewById
    protected WebView webview;
    @ViewById
    protected ProgressBarIndeterminateDeterminate pb;

    /* 网页是否加载完成 */
    private boolean isLoadComplete = false;
    private String url;
    private String html;
    private int type; // 0 ：加载url  1：加载html格式的源数据

    @AfterInject
    void init(){
        Bundle bundle = this.getIntent().getBundleExtra("bundle");
        url = bundle.getString("url","#");
        html = bundle.getString("html","#");
        type = bundle.getInt("type", 0);
    }

    @AfterViews
    void initViews(){
        toolbar.setTitle("小小书");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        configWebview();
    }

    private void configWebview(){
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsConfirm(WebView view, String url,
                                       String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    isLoadComplete = true;
                    pb.setVisibility(View.GONE);
                    return;
                }

                isLoadComplete = false;
                pb.setVisibility(View.VISIBLE);
                pb.setMax(100);
                pb.setProgress(progress);
                super.onProgressChanged(view, progress);
            }
        });

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view,final String url) {
                if( url.startsWith("http:") || url.startsWith("https:") ) {
                    return false;
                }

                // Otherwise allow the OS to handle things like tel, mailto, etc.
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                SnackBar snackBar = new SnackBar(WebViewActivity.this, "网页错误：" + description, "ok", null);
                snackBar.show();
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }
        });

        webview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {  //表示按返回键时的操作
                        webview.goBack();   //后退
                        return true;    //已处理
                    }
                }
                return false;
            }
        });

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setTextSize(WebSettings.TextSize.NORMAL);

        if(type == 0){
            webview.loadUrl(url);
        }else if(type == 1){
            webview.loadData(html,"text/html","utf-8");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}
