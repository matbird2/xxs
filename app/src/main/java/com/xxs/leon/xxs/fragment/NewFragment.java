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
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.adapter.HomeNewAlbumRecyclerViewAdapter;
import com.xxs.leon.xxs.adapter.TestRecyclerViewAdapter;
import com.xxs.leon.xxs.rest.bean.Album;
import com.xxs.leon.xxs.rest.engine.impl.CommenEngineImpl;
import com.xxs.leon.xxs.utils.L;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
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
    RecyclerViewMaterialAdapter adapter;
    private int lastVisibleItem;
    private List<Album> mContentItems = new ArrayList<>();

    @Bean
    CommenEngineImpl engine;

    @AfterInject
    void init() {

    }

    @AfterViews
    void initViews() {
        swipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(this);

        // 这句话是为了，第一次进入页面的时候显示加载进度条
        swipeRefreshLayout.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

//        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView,
//                                             int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE
//                        && lastVisibleItem + 1 == adapter.getItemCount()) {
//                    swipeRefreshLayout.setRefreshing(true);
//                    L.i(L.TEST,"last");
//                    // 此处在现实项目中，请换成网络请求数据代码，sendRequest .....
//                    handler.sendEmptyMessageDelayed(0, 3000);
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
//            }
//
//        });

        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new RecyclerViewMaterialAdapter(new HomeNewAlbumRecyclerViewAdapter(getActivity(),mContentItems));
        recyclerView.setAdapter(adapter);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), recyclerView, null);

        loadData();
    }

    @Background
    void loadData(){
        List<Album> results = engine.getHomeAlbums();
        if(results != null){
            updateView(results);
        }
    }

    @UiThread
    void updateView(List<Album> results){
        swipeRefreshLayout.setRefreshing(false);
        mContentItems.addAll(results);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        mContentItems.clear();
        loadData();
    }
}
