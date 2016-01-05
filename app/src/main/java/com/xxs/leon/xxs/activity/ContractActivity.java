package com.xxs.leon.xxs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.rest.engine.impl.CommenEngineImpl;
import com.xxs.leon.xxs.utils.Tools;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by maliang on 15/12/30.
 */
@EActivity(R.layout.activity_contract)
public class ContractActivity extends AppCompatActivity{
    @ViewById
    protected Toolbar toolbar;
    @ViewById
    protected TextView version;

    @AfterInject
    void init(){

    }

    @AfterViews
    void initViews(){
        version.setText(Tools.getAppVersionName(this));
        toolbar.setTitle("联系");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Click(R.id.web)
    void clickWeb(){
        Bundle bundle = new Bundle();
        bundle.putString("url", "http://xiaoxiaoshu.bmob.cn");
        bundle.putString("html", "#");
        bundle.putInt("type",0);
        Intent intent = new Intent(this,WebViewActivity_.class);
        intent.putExtra("bundle",bundle);
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
