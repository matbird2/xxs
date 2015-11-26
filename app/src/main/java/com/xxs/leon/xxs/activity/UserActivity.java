package com.xxs.leon.xxs.activity;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gc.materialdesign.widgets.SnackBar;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.rest.bean.XSUser;
import com.xxs.leon.xxs.rest.engine.impl.CommenEngineImpl;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by maliang on 15/11/26.
 */
@EActivity(R.layout.activity_user)
public class UserActivity extends AppCompatActivity{

    @ViewById
    protected ImageView backdrop;
    @ViewById
    protected ImageView photo;
    @ViewById
    protected TextView name;
    @ViewById
    protected TextView point;
    @ViewById
    protected TextView money;
    @ViewById
    protected TextView sign;

    @Bean
    CommenEngineImpl engine;

    IconicsDrawable account_icon,right_icon,head_icon;
    XSUser user;

    @AfterInject
    void init(){
        initIconRes();
        user = engine.getCurrentUser();
    }

    @AfterViews
    void initViews(){
        Glide.with(this).load("http://tupian.qqjay.com/u/2013/1127/19_222949_4.jpg").into(backdrop);
        if(user != null){
            doGetUserInfo(user.getObjectId());
        }
    }

    @Background
    void doGetUserInfo(String objectId){
        user = engine.getUserInfo(objectId);
        renderView(user);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void renderView(XSUser resultUser){
        name.setText(user.getUsername());
        point.setText(user.getPoint()+"");
        money.setText(user.getMoney()+"");
        sign.setText(user.getSignword());
        Glide.with(this).load(user.getPhoto()+"").error(account_icon).bitmapTransform(new CropCircleTransformation(this)).into(photo);
    }

    void initIconRes(){
        account_icon = new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_account_circle)
                .color(Color.GRAY)
                .sizeDp(60);

        right_icon = new IconicsDrawable(this)
                .icon(FontAwesome.Icon.faw_angle_right)
                .color(Color.BLACK)
                .sizeDp(20);
        head_icon = new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_cat)
                .color(Color.BLACK)
                .sizeDp(20);
    }

    @Click(R.id.ll_nick)
    void click1(){
        SnackBar snackBar = new SnackBar(this,"click","ok",null);
        snackBar.show();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }
}
