package com.xxs.leon.xxs.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.umeng.analytics.MobclickAgent;
import com.xxs.leon.xxs.R;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.bmob.v3.update.BmobUpdateAgent;

/**
 * Created by leon on 15-12-28.
 */
@EActivity(R.layout.activity_settings)
public class SettingsActivity extends AppCompatActivity{
    @ViewById
    protected Toolbar toolbar;

    @AfterInject
    void init(){

    }

    @AfterViews
    void initViews(){
        toolbar.setTitle("设置");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Click(R.id.ll_update)
    void clickUpdate(){
        BmobUpdateAgent.forceUpdate(this);
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
