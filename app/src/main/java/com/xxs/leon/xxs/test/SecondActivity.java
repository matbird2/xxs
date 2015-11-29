package com.xxs.leon.xxs.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.kogitune.activity_transition.ActivityTransition;
import com.kogitune.activity_transition.ExitActivityTransition;
import com.xxs.leon.xxs.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

/**
 * Created by leon on 15-11-21.
 */
@EActivity(R.layout.activity_second)
public class SecondActivity extends AppCompatActivity{

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
        /*IconicsDrawable iconicsDrawable = new IconicsDrawable(this)
                .icon(FontAwesome.Icon.faw_book)
                .color(Color.GRAY)
                .sizeDp(24);*/
        icon.setImageResource(R.drawable.logo_white);

        exitTransition = ActivityTransition.with(getIntent()).to(icon).start(savedInstanceState);
    }

   /* @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }*/

    @Override
    public void onBackPressed() {
        exitTransition.exit(this);
    }
}
