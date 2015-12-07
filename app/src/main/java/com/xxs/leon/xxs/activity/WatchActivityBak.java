package com.xxs.leon.xxs.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.adapter.WatchViewpagerAdapter;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

/**
 * Created by maliang on 15/12/4.
 */
@EActivity(R.layout.activity_watch)
public class WatchActivityBak extends AppCompatActivity{

    @ViewById
    protected ViewPager mViewPager;

    private ArrayList<String> albumList;
    private ArrayList<ImageView> imageViews;
    private WatchViewpagerAdapter adapter;
    private String baseurl;

    @AfterInject
    void init(){
        Bundle bundle = this.getIntent().getBundleExtra("bundle");
        albumList = (ArrayList<String>) bundle.getSerializable("albumList");
        baseurl = bundle.getString("baseurl");
        imageViews = new ArrayList<ImageView>();
    }

    @AfterViews
    void initViews(){
        initViewpager();
        initFragments();
    }

    private void initViewpager(){
        adapter = new WatchViewpagerAdapter(this,albumList,baseurl);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /*if(imageViews != null && imageViews.size() > 0){
                    Glide.with(WatchActivity.this).load(baseurl+albumList.get(position)).into(imageViews.get(position));
                }*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initFragments(){
        if(albumList == null)
            return ;
        imageViews.clear();
        for(int i=0;i<albumList.size();i++){
//            WatchFragment f = new WatchFragment_();
//            Bundle bundle = new Bundle();
//            bundle.putString("url",baseurl+albumList.get(i));
//            f.setArguments(bundle);
//            fragments.add(f);
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageViews.add(imageView);
//            Glide.with(WatchActivity.this).load(baseurl+albumList.get(i)).into(imageView);
        }
        adapter.appendList(imageViews);
    }
}
