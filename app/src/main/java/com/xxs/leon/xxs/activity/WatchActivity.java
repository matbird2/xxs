package com.xxs.leon.xxs.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.adapter.WatchViewpagerAdapter;
import com.xxs.leon.xxs.fragment.WatchFragment;
import com.xxs.leon.xxs.fragment.WatchFragment_;
import com.xxs.leon.xxs.utils.ACache;
import com.xxs.leon.xxs.utils.L;
import com.xxs.leon.xxs.utils.XXSPref_;
import com.xxs.leon.xxs.wedget.curl.CurlPage;
import com.xxs.leon.xxs.wedget.curl.CurlView;
import com.xxs.leon.xxs.wedget.pinch.PinchImageView;
import com.xxs.leon.xxs.wedget.pinch.PinchImageViewPager;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.api.sharedpreferences.IntPrefField;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by maliang on 15/12/4.
 */
@EActivity(R.layout.activity_watch_pinch)
public class WatchActivity extends AppCompatActivity{

    @ViewById
    protected PinchImageViewPager pager;
    @ViewById
    protected TextView tv_index;
    @ViewById
    protected TextView tv_fittype;
    @Pref
    XXSPref_ xxsPref;

    ACache aCache;

    private ArrayList<String> albumList;
    private String baseurl;
    private String albumId;
    private int currentPosition;

    private LinkedList<PinchImageView> viewCache;

    private IconicsDrawable error_icon;

    @AfterInject
    void init(){
        Bundle bundle = this.getIntent().getBundleExtra("bundle");
        albumList = (ArrayList<String>) bundle.getSerializable("albumList");
        baseurl = bundle.getString("baseurl");
        albumId = bundle.getString("albumId");
        viewCache = new LinkedList<PinchImageView>();

        aCache = ACache.get(this,"WatchHistory");

        error_icon = new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_broken_image)
                .color(Color.GRAY)
                .sizeDp(160);
    }

    @AfterViews
    void initViews(){
        initFitType();

        pager.setOffscreenPageLimit(4);
        pager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return albumList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                PinchImageView piv;
                if (viewCache.size() > 0) {
                    piv = viewCache.remove();
                    piv.reset();
                } else {
                    piv = new PinchImageView(WatchActivity.this);
                    piv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                }
                /*if (xxsPref.fitType().get() == 0) {
                    Glide.with(WatchActivity.this).load(baseurl + albumList.get(position)).error(R.drawable.glide_placeholder_bg).fitCenter().into(piv);
                } else if (xxsPref.fitType().get() == 1) {
                    piv.setScaleType(ImageView.ScaleType.FIT_XY);
                    Glide.with(WatchActivity.this).load(baseurl + albumList.get(position)).error(R.drawable.glide_placeholder_bg).into(piv);
                }*/
//                Picasso.with(WatchActivity.this).load(baseurl + albumList.get(position)).placeholder(R.drawable.glide_placeholder_bg).error(error_icon).fit().into(piv);
                Glide.with(WatchActivity.this).load(baseurl + albumList.get(position)).asBitmap().placeholder(R.drawable.glide_placeholder_bg).error(error_icon).into(new MyBitmapImageViewTarget(piv));
                container.addView(piv);
                return piv;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                PinchImageView piv = (PinchImageView) object;
                container.removeView(piv);
                viewCache.add(piv);
            }

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                pager.setMainPinchImageView((PinchImageView) object);
            }
        });

        pager.setOnPageChangeListener(new PinchImageViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                tv_index.setText((position + 1) + "/" + albumList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if(aCache.getAsString(albumId) != null){
            pager.setCurrentItem(Integer.parseInt(aCache.getAsString(albumId)));
        }
    }

    private void initFitType(){
        IntPrefField fitType = xxsPref.fitType();
        if (fitType.get() == 0) {
            tv_fittype.setText("适中");
        } else if (fitType.get() == 1) {
            tv_fittype.setText("全屏");
        }
    }

    @Click(R.id.tv_fittype)
    void clickFitType(){
        IntPrefField fitType = xxsPref.fitType();
        if (fitType.get() == 0) {
            fitType.put(1);
            tv_fittype.setText("全屏");
        } else if (fitType.get() == 1) {
            fitType.put(0);
            tv_fittype.setText("适中");
        }
    }

    @Click(R.id.go_first)
    void clickGofirstPage(){
        if(currentPosition != 0 && pager != null)
            pager.setCurrentItem(0);
    }

    @Override
    public void onBackPressed() {
        aCache.put(albumId,currentPosition+"");
        aCache.put("lastAlbum",albumId);
        super.onBackPressed();
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

    class MyBitmapImageViewTarget extends BitmapImageViewTarget{
        public MyBitmapImageViewTarget(ImageView view) {
            super(view);
        }

        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
            if (bitmap != null && view.getScaleType() != ImageView.ScaleType.FIT_XY) {
                view.setScaleType(ImageView.ScaleType.FIT_XY);
            }
            super.onResourceReady(bitmap, anim);
        }

        @Override
        protected void setResource(Bitmap resource) {
            super.setResource(resource);
        }

        @Override
        public void onLoadFailed(Exception e, Drawable errorDrawable) {
            if (errorDrawable != null && view != null && view.getScaleType() != ImageView.ScaleType.CENTER_CROP) {
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            super.onLoadFailed(e, errorDrawable);
        }

        @Override
        public void onLoadStarted(Drawable placeholder) {
            if (placeholder != null && placeholder != null && view != null && view.getScaleType() != ImageView.ScaleType.CENTER_CROP) {
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            super.onLoadStarted(placeholder);
        }

        @Override
        public void onLoadCleared(Drawable placeholder) {
            if (placeholder != null && placeholder != null && view != null && view.getScaleType() != ImageView.ScaleType.CENTER_CROP) {
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            super.onLoadCleared(placeholder);
        }
    }
}
