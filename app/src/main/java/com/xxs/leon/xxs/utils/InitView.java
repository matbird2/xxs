package com.xxs.leon.xxs.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;

import com.xxs.leon.xxs.R;

/**
 * Created by maliang on 16/1/6.
 */
public class InitView {
    private static InitView initView;

    private InitView(){}

    public static InitView instance(){
        if(initView == null){
            initView = new InitView();
        }
        return initView;
    }

    public void initSwipeRefreshLayout(Context context,SwipeRefreshLayout swipeRefreshLayout,boolean isFirstRefreshing,SwipeRefreshLayout.OnRefreshListener listener){
        swipeRefreshLayout.setColorSchemeColors(R.color.colorAccent);
        // 这句话是为了，第一次进入页面的时候显示加载进度条
        swipeRefreshLayout.setProgressViewOffset(false,0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, context.getResources().getDisplayMetrics()));
        swipeRefreshLayout.setRefreshing(isFirstRefreshing);
        swipeRefreshLayout.setOnRefreshListener(listener);
    }

    public void initToolbar(Toolbar toolbar,AppCompatActivity activity,String title,Drawable logoDrawable,String subTitle,Drawable naviDrawable){
        if(logoDrawable != null)
            toolbar.setLogo(logoDrawable);
        toolbar.setTitle(title);
        if(subTitle != null)
            toolbar.setSubtitle(subTitle);

        activity.setSupportActionBar(toolbar);
        if(naviDrawable != null)
            toolbar.setNavigationIcon(naviDrawable);

        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void initToolbar(Toolbar toolbar,AppCompatActivity activity,String title){
        initToolbar(toolbar,activity,title,null,null,null);
    }
}
