package com.xxs.leon.xxs.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.kogitune.activity_transition.ActivityTransition;
import com.kogitune.activity_transition.ExitActivityTransition;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.xxs.leon.xxs.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

/**
 * Created by leon on 15-11-21.
 */
@EActivity(R.layout.activity_category)
public class CategoryActivity extends AppCompatActivity{
    @ViewById
    protected ImageView icon;

    @InstanceState
    protected Bundle savedInstanceState;

    protected ExitActivityTransition exitTransition;
    @AfterViews
    void init(){

    }
    @AfterViews
    void initView(){
        IconicsDrawable iconicsDrawable = new IconicsDrawable(this)
                .icon(FontAwesome.Icon.faw_book)
                .color(getResources().getColor(R.color.primary))
                .sizeDp(48);
        icon.setImageDrawable(iconicsDrawable);

        exitTransition = ActivityTransition.with(getIntent()).to(icon).start(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        exitTransition.exit(this);
    }
}
