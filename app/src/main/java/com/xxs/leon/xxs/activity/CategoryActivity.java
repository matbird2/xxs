package com.xxs.leon.xxs.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kogitune.activity_transition.ActivityTransition;
import com.kogitune.activity_transition.ExitActivityTransition;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.ChangeImageTransform;
import com.transitionseverywhere.Scene;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.xxs.leon.xxs.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

/**
 * Created by leon on 15-11-21.
 */
@EActivity(R.layout.activity_category)
public class CategoryActivity extends AppCompatActivity{
    @ViewById
    protected ViewGroup scene_root;

    @InstanceState
    protected Bundle savedInstanceState;

    private Scene mScene1;
    private Scene mScene2;

    //    protected ExitActivityTransition exitTransition;
    @AfterViews
    void init(){

    }
    @AfterViews
    void initView(){
        /*IconicsDrawable iconicsDrawable = new IconicsDrawable(this)
                .icon(FontAwesome.Icon.faw_book)
                .color(getResources().getColor(R.color.primary))
                .sizeDp(48);
        icon.setImageDrawable(iconicsDrawable);

        exitTransition = ActivityTransition.with(getIntent()).to(icon).start(savedInstanceState);*/

        mScene1 = new Scene(scene_root,scene_root.findViewById(R.id.container));
        mScene2 = Scene.getSceneForLayout(scene_root,R.layout.category_scene2,this);


    }

    /*@Override
    public void onBackPressed() {
        exitTransition.exit(this);
    }*/

    @Click(R.id.go)
    void goTransition(){
        TransitionSet set = new TransitionSet();
        Slide slide = new Slide(Gravity.LEFT);
        slide.addTarget(R.id.go);
        set.addTransition(slide);
        set.addTransition(new ChangeBounds());
        set.addTransition(new ChangeImageTransform());
        set.setOrdering(TransitionSet.ORDERING_TOGETHER);
        set.setDuration(500);
        TransitionManager.go(mScene2,set);
    }
}
