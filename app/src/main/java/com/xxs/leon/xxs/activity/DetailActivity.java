package com.xxs.leon.xxs.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kogitune.activity_transition.ActivityTransition;
import com.kogitune.activity_transition.ExitActivityTransition;
import com.xxs.leon.xxs.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

/**
 * Created by maliang on 15/12/3.
 */
@EActivity(R.layout.activity_detail)
public class DetailActivity extends AppCompatActivity{

    @ViewById
    protected ImageView backdrop;
    @ViewById
    protected CoordinatorLayout root_container;
    @InstanceState
    protected Bundle savedInstanceState;

    protected ExitActivityTransition exitTransition;

    @AfterViews
    void initViews(){
        Glide.with(this).load("http://tupian.qqjay.com/u/2013/1127/19_222949_4.jpg").into(backdrop);
        exitTransition = ActivityTransition.with(getIntent()).to(root_container).start(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        exitTransition.exit(this);
    }

}
