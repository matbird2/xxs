package com.xxs.leon.xxs.activity;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.gc.materialdesign.widgets.SnackBar;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.umeng.analytics.MobclickAgent;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.bean.XSBmobChatUser;
import com.xxs.leon.xxs.rest.bean.UpdateBean;
import com.xxs.leon.xxs.rest.engine.impl.CommenEngineImpl;
import com.xxs.leon.xxs.utils.InitView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import cn.bmob.im.BmobUserManager;

/**
 * Created by maliang on 15/12/30.
 */
@EActivity(R.layout.activity_update_username)
public class UpdateUsernameActivity extends AppCompatActivity{
    @ViewById
    protected MaterialEditText account;
    @ViewById
    protected ButtonRectangle login;
    @ViewById
    protected ProgressBarCircularIndeterminate pb;
    @ViewById
    protected Toolbar toolbar;
    @Bean
    CommenEngineImpl engine;

    @AfterInject
    void init(){

    }

    @AfterViews
    void initViews(){
        InitView.instance().initToolbar(toolbar, this, "修改用户名");
        changeStatus(true);
    }

    @Click(R.id.login)
    void clickSubmit(){
        String usernameString = account.getText().toString();
        if(TextUtils.isEmpty(usernameString) ){
            SnackBar snackBar = new SnackBar(this,"新的用户名不能为空.","ok",null);
            snackBar.show();
            return ;
        }

        changeStatus(false);
        doUpdateUsername(usernameString);
    }

    @Background
    void doUpdateUsername(String username){
        SystemClock.sleep(1000);

        UpdateBean bean = engine.updateUserName(BmobUserManager.getInstance(this).getCurrentUser(),username);
        afterUpdateUsername(bean);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void afterUpdateUsername(UpdateBean result){
        changeStatus(true);
        if(result.getCode() == 0){
            SnackBar snackBar = new SnackBar(this,"用户名修改成功","ok",null);
            snackBar.show();
            finish();
        }else{
            SnackBar snackBar = new SnackBar(this,result.getError()+"","ok",null);
            snackBar.show();
        }
    }

    void changeStatus(boolean isEnable){
        account.setEnabled(isEnable);
        login.setEnabled(isEnable);
        if(isEnable)
            pb.setVisibility(View.INVISIBLE);
        else
            pb.setVisibility(View.VISIBLE);

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
