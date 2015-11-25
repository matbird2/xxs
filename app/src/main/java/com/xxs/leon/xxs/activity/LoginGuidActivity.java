package com.xxs.leon.xxs.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.xxs.leon.xxs.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

/**
 * Created by maliang on 15/11/25.
 */
@EActivity(R.layout.activity_login_guid)
public class LoginGuidActivity extends AppCompatActivity{


    @Click(R.id.login)
    void clickLogin(){
        startActivity(new Intent(this,LoginActivity_.class));
    }
}
