package com.xxs.leon.xxs.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.kogitune.activity_transition.ActivityTransition;
import com.kogitune.activity_transition.ExitActivityTransition;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.rest.bean.request.TestParams;
import com.xxs.leon.xxs.rest.bean.response.TestEntity;
import com.xxs.leon.xxs.rest.client.CommenRestClient;
import com.xxs.leon.xxs.rest.handler.CommenErrorHandler;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

/**
 * Created by leon on 15-11-22.
 */
@EActivity(R.layout.activity_comment)
public class CommentActivity extends AppCompatActivity{

    @InstanceState
    protected Bundle savedInstanceState;
    @ViewById
    protected FloatingActionButton fab;
    @ViewById
    protected Toolbar toolbar;

    IconicsDrawable okIcon;

    protected ExitActivityTransition exitTransition;

    @AfterInject
    void init(){
        initIconRes();
    }

    @AfterViews
    void initViews(){
        initToolbar();
        exitTransition = ActivityTransition.with(getIntent()).to(fab).start(savedInstanceState);
    }

    void initIconRes(){
        okIcon = new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_check)
                .color(Color.WHITE)
                .sizeDp(20);
    }

    private void initToolbar(){
        if (toolbar != null) {
            setSupportActionBar(toolbar);

            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }

    @RestService
    CommenRestClient client;
    @Bean
    CommenErrorHandler errorHandler;

    @Click(R.id.fab)
    void testRest(){
        client.setRestErrorHandler(errorHandler);
        
        testCloud();
    }

    @Background
    void testCloud(){
        TestParams params = new TestParams();
        params.setTest("能不能成功啊？");
        TestEntity entity = client.testCloudFunction(params);
        Log.d("TEST","result:"+entity.getResult());
    }

    @Override
    public void onBackPressed() {
        exitTransition.exit(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }
}
