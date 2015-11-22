package com.xxs.leon.xxs.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.kogitune.activity_transition.ActivityTransition;
import com.kogitune.activity_transition.ExitActivityTransition;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
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
    protected FrameLayout logo;
    @ViewById
    protected ImageView logo_in;

    IconicsDrawable okIcon;

    protected ExitActivityTransition exitTransition;

    @AfterInject
    void init(){
        initIconRes();
    }

    @AfterViews
    void initViews(){
        logo_in.setImageDrawable(okIcon);
        exitTransition = ActivityTransition.with(getIntent()).to(logo).start(savedInstanceState);
    }

    void initIconRes(){
        okIcon = new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_check)
                .color(Color.WHITE)
                .sizeDp(20);
    }

    @Override
    public void onBackPressed() {
        exitTransition.exit(this);
    }
}
