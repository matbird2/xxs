package com.xxs.leon.xxs.activity;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.gc.materialdesign.widgets.SnackBar;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.rest.bean.XSUser;
import com.xxs.leon.xxs.rest.bean.request.LoginParams;
import com.xxs.leon.xxs.rest.engine.impl.CommenEngineImpl;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * Created by maliang on 15/11/26.
 */
@EActivity(R.layout.activity_register)
public class RegisterActivity extends AppCompatActivity{

    @ViewById
    protected MaterialEditText account;
    @ViewById
    protected MaterialEditText password;
    @ViewById
    protected MaterialEditText confirm;
    @ViewById
    protected ButtonRectangle login;
    @ViewById
    protected ProgressBarCircularIndeterminate pb;
    @Bean
    CommenEngineImpl engine;

    @AfterInject
    void init(){

    }

    @AfterViews
    void initViews(){
        changeStatus(true);
    }

    @Click(R.id.login)
    void clickLogin(){
        String usernameString = account.getText().toString();
        String passwordString = password.getText().toString();
        String confirmString = confirm.getText().toString();
        if(TextUtils.isEmpty(usernameString) || TextUtils.isEmpty(passwordString) || TextUtils.isEmpty(confirmString)){
            SnackBar snackBar = new SnackBar(this,"请正确填写账号密码","ok",null);
            snackBar.show();
            return ;
        }

        if(passwordString.length() < 6){
            SnackBar snackBar = new SnackBar(this,"请输入至少六位密码","ok",null);
            snackBar.show();
            return ;
        }

        if(!passwordString.equals(confirmString)){
            SnackBar snackBar = new SnackBar(this,"两次输入的密码不一致","ok",null);
            snackBar.show();
            return ;
        }

        changeStatus(false);
        LoginParams params = new LoginParams();
        params.setUsername(usernameString);
        params.setPassword(passwordString);
        doRegister(params);
    }

    @Background
    void doRegister(LoginParams params){
        SystemClock.sleep(2000);
        XSUser user = engine.register(params);
        afterRegister(user);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void afterRegister(XSUser user){
        changeStatus(true);
        // do something
        SnackBar snackBar = new SnackBar(this,user.getCode()+"","ok",null);
        snackBar.show();
    }

    void changeStatus(boolean isEnable){
        account.setEnabled(isEnable);
        password.setEnabled(isEnable);
        login.setEnabled(isEnable);
        confirm.setEnabled(isEnable);
        if(isEnable)
            pb.setVisibility(View.INVISIBLE);
        else
            pb.setVisibility(View.VISIBLE);

    }
}

