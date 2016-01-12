package com.xxs.leon.xxs.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.umeng.analytics.MobclickAgent;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.utils.InitView;
import com.xxs.leon.xxs.utils.L;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * Created by leon on 15-11-21.
 */
@EActivity(R.layout.activity_category)
public class CategoryActivity extends AppCompatActivity{
    @ViewById
    protected ViewGroup scene_root;
    @ViewById
    protected Toolbar toolbar;
    @ViewById
    protected TextView tv1;
    @ViewById
    protected TextView tv2;
    @ViewById
    protected TextView tv3;
    @ViewById
    protected TextView tv4;
    @ViewById
    protected TextView tv5;
    @ViewById
    protected TextView tv6;
    @ViewById
    protected TextView tv7;
    @ViewById
    protected TextView tv8;
    @ViewById
    protected TextView tv9;
    @ViewById
    protected TextView tv10;
    @ViewById
    protected TextView tv11;
    @ViewById
    protected TextView tv12;


    @InstanceState
    protected Bundle savedInstanceState;

    private Scene mScene1;
    private Scene mScene2;

    @AfterViews
    void init(){

    }
    @AfterViews
    void initView(){
        InitView.instance().initToolbar(toolbar, this, "分类");

        mScene1 = new Scene(scene_root,scene_root.findViewById(R.id.container));
        mScene2 = Scene.getSceneForLayout(scene_root,R.layout.category_scene2,this);

        route();
        doAnim();
    }

    private void route(){
        mScene1.getSceneRoot().findViewById(R.id.tv1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAlbumListActivity(0);
            }
        });
        mScene2.getSceneRoot().findViewById(R.id.tv2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAlbumListActivity(1);
            }
        });
        mScene2.getSceneRoot().findViewById(R.id.tv3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAlbumListActivity(2);
            }
        });
        mScene2.getSceneRoot().findViewById(R.id.tv4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAlbumListActivity(3);
            }
        });
        mScene2.getSceneRoot().findViewById(R.id.tv5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAlbumListActivity(4);
            }
        });
        mScene2.getSceneRoot().findViewById(R.id.tv6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAlbumListActivity(5);
            }
        });
        mScene2.getSceneRoot().findViewById(R.id.tv7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAlbumListActivity(6);
            }
        });
        mScene2.getSceneRoot().findViewById(R.id.tv8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAlbumListActivity(7);
            }
        });
        mScene2.getSceneRoot().findViewById(R.id.tv9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAlbumListActivity(8);
            }
        });
        mScene2.getSceneRoot().findViewById(R.id.tv10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAlbumListActivity(9);
            }
        });
        mScene2.getSceneRoot().findViewById(R.id.tv11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAlbumListActivity(10);
            }
        });
        mScene2.getSceneRoot().findViewById(R.id.tv12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAlbumListActivity(11);
            }
        });
    }

    private void goToAlbumListActivity(int type){
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        Intent intent = new Intent(CategoryActivity.this,AlbumListActivity_.class);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    @UiThread(delay = 300)
    void doAnim(){
        TransitionSet set = new TransitionSet();
        Slide slide = new Slide(Gravity.LEFT);
        set.addTransition(slide);
        set.addTransition(new ChangeBounds());
        set.addTransition(new ChangeImageTransform());
        set.setOrdering(TransitionSet.ORDERING_TOGETHER);
        set.setDuration(500);
        TransitionManager.go(mScene2, set);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
