package com.xxs.leon.xxs.activity;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.gc.materialdesign.widgets.Dialog;
import com.gc.materialdesign.widgets.SnackBar;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.umeng.analytics.MobclickAgent;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.rest.bean.XSUser;
import com.xxs.leon.xxs.rest.bean.XXSBmobInstallation;
import com.xxs.leon.xxs.rest.bean.request.LoginParams;
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

import java.util.List;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

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
    @ViewById
    protected Toolbar toolbar;
    @Bean
    CommenEngineImpl engine;

    @AfterInject
    void init(){

    }

    @AfterViews
    void initViews(){
        InitView.instance().initToolbar(toolbar, this, "登录");
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
            updateInstallationWithUid();
        }else{
            SnackBar snackBar = new SnackBar(this,user.getError()+"","ok",null);
            snackBar.show();
        }
    }

    private void updateInstallationWithUid(){
        if(engine.getCurrentUser() != null){
            BmobQuery<XXSBmobInstallation> query = new BmobQuery<>();
            query.addWhereEqualTo("installationId", BmobInstallation.getInstallationId(this));
            query.findObjects(this, new FindListener<XXSBmobInstallation>() {
                @Override
                public void onSuccess(List<XXSBmobInstallation> list) {
                    if (list != null && list.size() > 0) {
                        XXSBmobInstallation xxsBmobInstallation = list.get(0);
                        xxsBmobInstallation.setUid(engine.getCurrentUser().getObjectId());
                        xxsBmobInstallation.update(LoginActivity.this, new UpdateListener() {

                            @Override
                            public void onSuccess() {
                               LoginActivity.this.finish();
                            }

                            @Override
                            public void onFailure(int code, String msg) {
                                L.w(L.TEST, "设备信息更新失败:" + msg);
                                LoginActivity.this.finish();
                            }
                        });
                    }else{
                        LoginActivity.this.finish();
                    }
                }

                @Override
                public void onError(int i, String s) {
                    L.w(L.TEST, "失败:" + s);
                    LoginActivity.this.finish();
                }
            });
        }else{
            LoginActivity.this.finish();
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

    @Click(R.id.register)
    void clickRegister(){
        RegisterActivity_.intent(this).start();
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
