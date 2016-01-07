package com.xxs.leon.xxs.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gc.materialdesign.views.ProgressBarIndeterminateDeterminate;
import com.gc.materialdesign.widgets.Dialog;
import com.gc.materialdesign.widgets.SnackBar;
import com.umeng.analytics.MobclickAgent;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.constant.Constant;
import com.xxs.leon.xxs.rest.bean.XSUser;
import com.xxs.leon.xxs.rest.bean.request.AddRechargeLogParams;
import com.xxs.leon.xxs.rest.engine.impl.CommenEngineImpl;
import com.xxs.leon.xxs.utils.InitView;
import com.xxs.leon.xxs.utils.L;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * Created by maliang on 15/12/17.
 */
@EActivity(R.layout.activity_webview)
public class PayWebViewActivity extends AppCompatActivity{
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
    private String income;
    private int type; // 0 ：加载url  1：加载html格式的源数据

    @Bean
    CommenEngineImpl engine;

    XSUser currentUser;

    @AfterInject
    void init(){
        Bundle bundle = this.getIntent().getBundleExtra("bundle");
        url = bundle.getString("url","#");
        html = bundle.getString("html", "#");
        type = bundle.getInt("type", 0);
        income = bundle.getString("income", "0");

        currentUser = engine.getCurrentUser();
    }

    @AfterViews
    void initViews(){
        InitView.instance().initToolbar(toolbar,this,"小小书充值");
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
                L.i(L.TEST,"url ==> "+url);
                if(url.contains(Constant.PAY_SUCCESS_CALLBACK)){
                    handlePaySuccess(url);
                    return true;
                }

                if( url.startsWith("http:") || url.startsWith("https:") ) {
                    return false;
                }

                // Otherwise allow the OS to handle things like tel, mailto, etc.
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                SnackBar snackBar = new SnackBar(PayWebViewActivity.this, "网页错误：" + description, "ok", null);
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

    private void handlePaySuccess(String url){
        String paramstring = url.substring(url.lastIndexOf("?") + 1);
        String[] params = paramstring.split("&");
        String trade_status = "";
        String out_trade_no = "";
        String trade_no = "";
        for(String param : params){
            if(param.contains("trade_status")){
                trade_status = param.substring(param.lastIndexOf("=")+1);
            }else if(param.contains("out_trade_no")){
                out_trade_no = param.substring(param.lastIndexOf("=")+1);
            }else if(param.contains("trade_no")){
                trade_no = param.substring(param.lastIndexOf("=")+1);
            }
        }

        if(currentUser != null){
            AddRechargeLogParams addRechargeLogParams = new AddRechargeLogParams();
            addRechargeLogParams.setObjectId(currentUser.getObjectId());
            addRechargeLogParams.setSessionToken(currentUser.getSessionToken());
            addRechargeLogParams.setOut_trade_no(out_trade_no);
            addRechargeLogParams.setTrade_no(trade_no);
            addRechargeLogParams.setIncome(income);
            addRechargeLogParams.setTrade_status(trade_status);
            doAddRechargeLog(addRechargeLogParams);
        }
    }

    @Background
    void doAddRechargeLog(AddRechargeLogParams params){
        String result = engine.handlePaySuccess(params);
        if(!TextUtils.isEmpty(result)){
            afterAddRechargeLog(result);
        }
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void afterAddRechargeLog(String result){
        final Dialog dialog = new Dialog(this,"充值结果",result);
        dialog.show();
        if("success".equals(result)){
            dialog.getButtonAccept().setText("确定");
            dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PayWebViewActivity.this.finish();
                }
            });
        }else{
            dialog.getButtonAccept().setText("反馈");
            dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PayWebViewActivity.this.finish();

                }
            });

            dialog.addCancelButton("离开", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PayWebViewActivity.this.finish();
                }
            });
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
