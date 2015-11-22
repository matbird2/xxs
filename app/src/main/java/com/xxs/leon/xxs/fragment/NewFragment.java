package com.xxs.leon.xxs.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.adapter.TestRecyclerViewAdapter;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leon on 15-11-22.
 */
@EFragment(R.layout.fragment_recyclerview)
public class NewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @ViewById
    protected SwipeRefreshLayout swipeRefreshLayout;
    @ViewById
    protected RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    TestRecyclerViewAdapter adapter;
    private int lastVisibleItem;
    private List<Object> mContentItems = new ArrayList<>();

    @AfterInject
    void init() {

    }

    @AfterViews
    void initViews() {
        swipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(this);

        // 这句话是为了，第一次进入页面的时候显示加载进度条
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == adapter.getItemCount()) {
                    swipeRefreshLayout.setRefreshing(true);
                    // 此处在现实项目中，请换成网络请求数据代码，sendRequest .....
                    handler.sendEmptyMessageDelayed(0, 3000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }

        });

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new TestRecyclerViewAdapter(mContentItems);
        recyclerView.setAdapter(adapter);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), recyclerView, null);

        // 此处在现实项目中，请换成网络请求数据代码，sendRequest .....
        handler.sendEmptyMessageDelayed(0, 3000);
    }

    @Override
    public void onRefresh() {
        mContentItems.add(new Object());
        adapter.notifyDataSetChanged();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            for(int i=0;i<ITEM_COUNT;i++)
                mContentItems.add(new Object());
            adapter.notifyDataSetChanged();
        }
    };
    private static final int ITEM_COUNT = 100;
}
