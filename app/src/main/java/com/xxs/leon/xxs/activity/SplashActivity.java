package com.xxs.leon.xxs.activity;

import android.app.Activity;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.constant.Constant;
import com.xxs.leon.xxs.utils.Tools;
import com.xxs.leon.xxs.utils.XXSPref_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;

/**
 * Created by leon on 15-12-26.
 */
@EActivity(R.layout.activity_splash)
public class SplashActivity extends Activity{

    @ViewById
    protected TextView version;
    @Pref
    XXSPref_ xxsPref;
    private long startTime;

    @AfterInject
    void init(){
        Bmob.initialize(this, Constant.X_BMOB_APPLICATION_ID);
        BmobInstallation.getCurrentInstallation(this).save();
        BmobPush.startWork(this,Constant.X_BMOB_APPLICATION_ID);
    }

    @AfterViews
    void initViews(){
        version.setText("版本："+Tools.getAppVersionName(this));
        startTime = System.currentTimeMillis();
        switchOpenView();
    }


    void switchOpenView(){
        final long endTime = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if((endTime - startTime) > 3000){
                }else{
                    SystemClock.sleep(3000 - (endTime - startTime));
                }

                boolean isFirstOpen = xxsPref.isFirstOpen().get();
                if(!isFirstOpen){
                    goHome();
                }else{
                    goGuide();
                }
            }
        }).start();
    }

    private void goGuide() {
        GuideActivity_.intent(this).start();
        this.finish();
    }

    private void goHome(){
        MainActivity_.intent(this).start();
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
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
