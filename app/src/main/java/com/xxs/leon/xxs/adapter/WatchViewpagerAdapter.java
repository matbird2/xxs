package com.xxs.leon.xxs.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by maliang on 15/12/4.
 */
public class WatchViewpagerAdapter extends PagerAdapter {
    private ArrayList<ImageView> imageViews = new ArrayList<>();
    private ArrayList<String> albumList;
    private Context context;
    private String baseurl;

    public WatchViewpagerAdapter(Context context,ArrayList<String> albumList,String baseurl){
        this.context = context;
        this.albumList = albumList;
        this.baseurl = baseurl;
    }


    @Override
    public int getCount() {
        return imageViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(imageViews.get(position),position);
        Glide.with(context).load(baseurl+albumList.get(position)).into(imageViews.get(position));
        return imageViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imageViews.get(position));
    }

    public void appendList(ArrayList<ImageView> imageViews) {
        imageViews.clear();
        if (!imageViews.containsAll(imageViews) && imageViews.size() > 0) {
            imageViews.addAll(imageViews);
        }
        notifyDataSetChanged();
    }

    /*private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    ;
    private final FragmentManager fm;

    public WatchViewpagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    public WatchViewpagerAdapter(FragmentManager fm,ArrayList<Fragment> fragments) {
        super(fm);
        this.fm = fm;
        this.fragments = fragments;
    }

    public void appendList(ArrayList<Fragment> fragment) {
        fragments.clear();
        if (!fragments.containsAll(fragment) && fragment.size() > 0) {
            fragments.addAll(fragment);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void setFragments(ArrayList<Fragment> fragments) {
        if (this.fragments != null) {
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : this.fragments) {
                ft.remove(f);
            }
            ft.commit();
            ft = null;
            fm.executePendingTransactions();
        }
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 这里Destroy的是Fragment的视图层次，并不是Destroy Fragment对象
        super.destroyItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (fragments.size() <= position) {
            position = position % fragments.size();
        }
        Object obj = super.instantiateItem(container, position);
        return obj;
    }*/
}
