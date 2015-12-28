package com.xxs.leon.xxs.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;

import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.umeng.analytics.MobclickAgent;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.adapter.AlbumListAdapter;
import com.xxs.leon.xxs.adapter.HomeNewAlbumRecyclerViewAdapter;
import com.xxs.leon.xxs.constant.AlbumType;
import com.xxs.leon.xxs.rest.bean.Album;
import com.xxs.leon.xxs.rest.engine.impl.CommenEngineImpl;
import com.xxs.leon.xxs.utils.L;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maliang on 15/12/10.
 */
@EActivity(R.layout.activity_album_list)
public class AlbumListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    @ViewById
    protected SwipeRefreshLayout swipeRefreshLayout;
    @ViewById
    protected RecyclerView recyclerView;
    @ViewById
    protected Toolbar toolbar;

    private GridLayoutManager gridLayoutManager;
    private AlbumListAdapter adapter;
    private int lastVisibleItem = 0;
    private int pageIndex = 0;
    private static int PAGE_SIZE = 10;
    private int type = -1;
    private boolean hasMore = true;

    @Bean
    CommenEngineImpl engine;

    @AfterInject
    void init(){
        Bundle bundle = this.getIntent().getBundleExtra("bundle");
        type = bundle.getInt("type",-1);
    }

    @AfterViews
    void initViews(){
        toolbar.setTitle(AlbumType.getType(type)+"");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initListView();

        loadAlbumList(pageIndex);
    }

    // 初始化跟listview相关的控件
    private void initListView(){
        swipeRefreshLayout.setColorSchemeColors(this.getResources().getColor(R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(this);

        // 这句话是为了，第一次进入页面的时候显示加载进度条
        swipeRefreshLayout.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        adapter = new AlbumListAdapter(this);
        gridLayoutManager = new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
//                return adapter.isFooter(position) ? gridLayoutManager.getSpanCount() : 1;
                switch (adapter.getItemViewType(position)) {
                    case AlbumListAdapter.CELL_TYPE:
                        return 1;
                    case AlbumListAdapter.FOOTER_TYPE:
                        return 2;
                    default:
                        return -1;
                }
            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == adapter.getItemCount()) {
//                    swipeRefreshLayout.setRefreshing(true);
                    // 此处在现实项目中，请换成网络请求数据代码，sendRequest .....
                    if (hasMore) {
                        pageIndex += 1;
                        loadAlbumList(pageIndex);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
            }

        });
    }

    @Background
    void loadAlbumList(int page){
        List<Album> result = engine.getCategoryAlbum(type, page * PAGE_SIZE);
        renderViewAfterLoadData(result);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void renderViewAfterLoadData(List<Album> result){
        if(result != null ){
            swipeRefreshLayout.setRefreshing(false);
            adapter.appenList(result);
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
        adapter.clear();
        loadAlbumList(pageIndex);
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
