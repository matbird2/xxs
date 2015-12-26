package com.xxs.leon.xxs.activity;

import android.app.Activity;
import android.support.v4.view.ViewPager;

import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.adapter.GuideViewPagerAdapter;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by leon on 15-12-26.
 */
@EActivity(R.layout.activity_guide)
public class GuideActivity extends Activity{

    @ViewById
    protected ViewPager viewpager;

    protected GuideViewPagerAdapter adapter;

    @AfterInject
    void init(){

    }

    @AfterViews
    void initViews(){
        adapter = new GuideViewPagerAdapter(this);
        viewpager.setAdapter(adapter);
    }


}
