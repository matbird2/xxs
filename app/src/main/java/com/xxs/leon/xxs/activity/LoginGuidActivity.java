package com.xxs.leon.xxs.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.gc.materialdesign.widgets.SnackBar;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.rest.bean.XSUser;
import com.xxs.leon.xxs.rest.engine.impl.CommenEngineImpl;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

/**
 * Created by maliang on 15/11/25.
 */
@EActivity(R.layout.activity_login_guid)
public class LoginGuidActivity extends AppCompatActivity{

    @Bean
    CommenEngineImpl engine;

    @Click(R.id.login)
    void clickLogin(){
        startActivity(new Intent(this,LoginActivity_.class));
    }

    @Click(R.id.register)
    void clickRegister(){
//        startActivity(new Intent(this,RegisterActivity_.class));
        testUpload();
    }

    @Background
    void testUpload(){
        engine.uploadFile("haha.jpg","/sdcard/test1.jpg");
    }
}
