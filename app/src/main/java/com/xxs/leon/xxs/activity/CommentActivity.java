package com.xxs.leon.xxs.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.kogitune.activity_transition.ActivityTransition;
import com.kogitune.activity_transition.ExitActivityTransition;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.xxs.leon.xxs.R;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

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

    @Override
    public void onBackPressed() {
        exitTransition.exit(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }
}
