package com.xxs.leon.xxs.activity;

import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.wedget.pinch.PinchImageView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by maliang on 15/12/23.
 */
@EActivity(R.layout.activity_show_webimage)
public class ShowWebImageActivity extends AppCompatActivity{

    @ViewById
    protected PinchImageView web_image;

    private String imagePath;

    @AfterInject
    void init(){
        this.imagePath = getIntent().getStringExtra("image");
    }

    @AfterViews
    void initViews(){
        Glide.with(ShowWebImageActivity.this).load(imagePath).fitCenter().into(web_image);
    }

    @Click(R.id.web_image)
    void clickImage(){
        this.finish();
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
