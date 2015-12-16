package com.xxs.leon.xxs.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.adapter.AlbumListAdapter;
import com.xxs.leon.xxs.adapter.HomePostListAdapter;
import com.xxs.leon.xxs.rest.bean.Post;
import com.xxs.leon.xxs.rest.engine.impl.CommenEngineImpl;

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
 * Created by maliang on 15/12/16.
 */
@EFragment(R.layout.fragment_recyclerview)
public class PostFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    @ViewById
    protected SwipeRefreshLayout swipeRefreshLayout;
    @ViewById
    protected RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;
    private List<Post> mContents = new ArrayList<>();
    private HomePostListAdapter adapter;
    private RecyclerViewMaterialAdapter rAdapter;
    private int lastVisibleItem = 0;
    private int pageIndex = 0;
    private static int PAGE_SIZE = 10;
    private int type = -1;
    private boolean hasMore = true;

    @Bean
    CommenEngineImpl engine;

    @AfterInject
    void init(){
    }

    @AfterViews
    void initViews(){
        initListView();
        loadPostList(pageIndex);
    }

    // 初始化跟listview相关的控件
    private void initListView(){
        swipeRefreshLayout.setColorSchemeColors(this.getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(this);

        // 这句话是为了，第一次进入页面的时候显示加载进度条
        swipeRefreshLayout.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        adapter = new HomePostListAdapter(getActivity(),mContents);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        rAdapter = new RecyclerViewMaterialAdapter(adapter);
        recyclerView.setAdapter(rAdapter);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == rAdapter.getItemCount()) {
                    if (hasMore) {
                        pageIndex += 1;
                        loadPostList(pageIndex);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }

        });

        adapter.setOnGetThumbnailAndDisplayImageViewListener(new HomePostListAdapter.OnGetThumbnailAndDisplayImageViewListener() {
            @Override
            public void getAndDisplay(String image, ImageView iv) {
                getPhotoThumbnail(image,iv);
            }
        });

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), recyclerView, null);
    }

    @Background
    void getPhotoThumbnail(String image,ImageView iv){
        String thumbnailUrl = engine.getThumbnail(image,100);
        renderPhoto(thumbnailUrl,iv);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void renderPhoto(String thumbnailUrl,ImageView iv){
        Glide.with(getActivity()).load(thumbnailUrl).crossFade(500).centerCrop().into(iv);
    }

    @Background
    void loadPostList(int page){
        List<Post> result = engine.getHomePostList(page * PAGE_SIZE);
        renderViewAfterLoadData(result);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void renderViewAfterLoadData(List<Post> result){
        if(result != null ){
            swipeRefreshLayout.setRefreshing(false);
            mContents.addAll(result);
            rAdapter.mvp_notifyDataSetChanged();
            if(result.size() < PAGE_SIZE){
                hasMore = false;
                adapter.setFooterViewState(AlbumListAdapter.NO_MORE_DATA);
            }else{
                adapter.setFooterViewState(AlbumListAdapter.LOADING);
            }
        }else{
            adapter.setFooterViewState(AlbumListAdapter.LOAD_FAILED);
        }
    }

    @Override
    public void onRefresh() {
        hasMore = true;
        pageIndex = 0;
        mContents.clear();
        loadPostList(pageIndex);
    }
}
