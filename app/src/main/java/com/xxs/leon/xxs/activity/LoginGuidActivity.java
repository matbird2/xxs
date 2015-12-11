package com.xxs.leon.xxs.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.gc.materialdesign.widgets.SnackBar;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.rest.bean.XSUser;
import com.xxs.leon.xxs.rest.engine.impl.CommenEngineImpl;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by maliang on 15/11/25.
 */
@EActivity(R.layout.activity_login_guid)
public class LoginGuidActivity extends AppCompatActivity{

    @ViewById
    protected Toolbar toolbar;
    @Bean
    CommenEngineImpl engine;

    @AfterViews
    void initViews(){
        toolbar.setTitle("登录注册");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Click(R.id.login)
    void clickLogin(){
        startActivity(new Intent(this, LoginActivity_.class));
    }

    @Click(R.id.register)
    void clickRegister(){
//        startActivity(new Intent(this,RegisterActivity_.class));
//        testUpload();
        testUpdateUserPhoto();
    }

    @Background
    void testUpload(){
        engine.uploadFile("haha.jpg", "/sdcard/test1.jpg");
    }

    @Background
    void testUpdateUserPhoto(){
        XSUser user = engine.getCurrentUser();
        if(user == null)
            return ;
        String imgUrl = "xixixi";
        engine.updateUserPhoto(user, imgUrl);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
