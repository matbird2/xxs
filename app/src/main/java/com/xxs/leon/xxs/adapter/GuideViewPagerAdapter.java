package com.xxs.leon.xxs.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.activity.MainActivity_;
import com.xxs.leon.xxs.ui.GuideViewItem;
import com.xxs.leon.xxs.utils.XXSPref_;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by leon on 15-12-26.
 */
public class GuideViewPagerAdapter extends PagerAdapter {
    // 界面列表
    // private List<View> views;
    private Queue<GuideViewItem> mLstView;
    // private Context ctx;
    private Activity activity;

    public GuideViewPagerAdapter(Activity activity) {
        this.activity = activity;
        mLstView = new LinkedList<GuideViewItem>();
    }

    public int[] guides = new int[] { R.drawable.guide1,
            R.drawable.guide2, R.drawable.guide3,R.drawable.guide4};


    private void goHome() {
        // 跳转
        MainActivity_.intent(activity).start();
        activity.finish();
    }

    private void setGuided() {
        new XXSPref_(activity).isFirstOpen().put(false);
    }

    @Override
    public int getCount() {
        // return banners.size();
        return guides.length;
    }

    @Override
    public boolean isViewFromObject(View v, Object o) {
        return v == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // VipBanner banner = banners.get(position);

        GuideViewItem itemView = null;
        if (!(mLstView.isEmpty())) {
            itemView = mLstView.remove();
        } else {
            itemView = new GuideViewItem(activity);
        }
        itemView.setData(guides[position]);
        container.addView(itemView);

        if (position == guides.length - 1) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 设置已经引导
                    setGuided();
                    goHome();
                }
            });
        }
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        GuideViewItem itemView = (GuideViewItem) object;
        container.removeView(itemView);
        mLstView.add(itemView);
    }
}
