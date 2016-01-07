package com.xxs.leon.xxs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.umeng.analytics.MobclickAgent;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.constant.Constant;
import com.xxs.leon.xxs.rest.bean.XSUser;
import com.xxs.leon.xxs.rest.bean.request.PayParams;
import com.xxs.leon.xxs.rest.engine.impl.CommenEngineImpl;
import com.xxs.leon.xxs.utils.InitView;
import com.xxs.leon.xxs.utils.L;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * Created by maliang on 15/12/22.
 */
@EActivity(R.layout.activity_recharge)
public class RechargeActivity extends AppCompatActivity{
    @ViewById
    protected Toolbar toolbar;
    @Bean
    CommenEngineImpl engine;

    XSUser currentUser;

    @AfterInject
    void init(){
        currentUser = engine.getCurrentUser();
    }

    @AfterViews
    void initViews(){
        InitView.instance().initToolbar(toolbar, this, "充值");
    }

    @Click(R.id.ch1)
    void recharge1(){
        pay(1,10);
    }
    @Click(R.id.ch5)
    void recharge5(){
        pay(5, 55);
    }
    @Click(R.id.ch10)
    void recharge10(){
        pay(10,120);
    }
    @Click(R.id.ch20)
    void recharge20(){
        pay(20,250);
    }
    @Click(R.id.ch50)
    void recharge50(){
        pay(50,650);
    }
    @Click(R.id.ch100)
    void recharge100(){
        pay(100,1500);
    }

    @Background
    void pay(double price,int income){
        if(currentUser == null)
            return ;
        PayParams params = new PayParams();
        params.setBody(currentUser.getObjectId()+"通过渠道-"+ Constant.CHANNEL+"-充值"+price+"元");
        params.setOrder_price(price);
        params.setProduct_name("小小书应用充值");
        String result = engine.pay(params);

        gotoPayWebActivity(result, income);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void gotoPayWebActivity(String result,int income){
        L.i(L.TEST,"pay result ==> "+result);
        Bundle bundle = new Bundle();
        bundle.putString("url", "#");
        bundle.putString("html", result);
        bundle.putString("income", income+"");
        bundle.putInt("type", 1);
        Intent intent = new Intent(this,PayWebViewActivity_.class);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
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
