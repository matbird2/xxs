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
 * Created by maliang on 15/11/25.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity{

    @ViewById
    protected MaterialEditText account;
    @ViewById
    protected MaterialEditText password;
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
        if(TextUtils.isEmpty(usernameString) || TextUtils.isEmpty(passwordString)){
            SnackBar snackBar = new SnackBar(this,"username or password is null.","ok",null);
            snackBar.show();
            return ;
        }

        changeStatus(false);
        LoginParams params = new LoginParams();
        params.setUsername(usernameString);
        params.setPassword(passwordString);
        doLogin(params);
    }

    @Background
    void doLogin(LoginParams params){
        SystemClock.sleep(2000);
        XSUser user = engine.login(params);
        afterLogin(user);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void afterLogin(XSUser user){
        changeStatus(true);
        // do something
//        SnackBar snackBar = new SnackBar(this,user.getCode()+"","ok",null);
//        snackBar.show();
        if(user.getCode() == 0){
            finish();
        }else{
            SnackBar snackBar = new SnackBar(this,user.getError()+"","ok",null);
            snackBar.show();
        }
    }

    void changeStatus(boolean isEnable){
        account.setEnabled(isEnable);
        password.setEnabled(isEnable);
        login.setEnabled(isEnable);
        if(isEnable)
            pb.setVisibility(View.INVISIBLE);
        else
            pb.setVisibility(View.VISIBLE);

    }
}
