package com.xxs.leon.xxs.activity;

import android.app.Activity;
import android.os.SystemClock;
import android.view.KeyEvent;

import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.utils.XXSPref_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * Created by leon on 15-12-26.
 */
@EActivity(R.layout.activity_splash)
public class SplashActivity extends Activity{

    @Pref
    XXSPref_ xxsPref;
    private long startTime;

    @AfterInject
    void init(){
    }

    @AfterViews
    void initViews(){
        startTime = System.currentTimeMillis();
        switchOpenView();
    }

    @Override
    public void onPause() {
        super.onPause();
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
}
