package com.xxs.leon.xxs.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.adapter.HomeNewAlbumRecyclerViewAdapter;
import com.xxs.leon.xxs.adapter.TestRecyclerViewAdapter;
import com.xxs.leon.xxs.rest.bean.Album;
import com.xxs.leon.xxs.rest.bean.Post;
import com.xxs.leon.xxs.rest.engine.impl.CommenEngineImpl;
import com.xxs.leon.xxs.utils.ACache;
import com.xxs.leon.xxs.utils.InitView;
import com.xxs.leon.xxs.utils.L;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;

import java.io.IOException;
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
    private List<Object> mContentItems = new ArrayList<>();

    @Bean
    CommenEngineImpl engine;

    ACache aCache;
    ObjectMapper objectMapper;

    @AfterInject
    void init() {
        aCache = ACache.get(getActivity());
        objectMapper = new ObjectMapper();
    }

    @AfterViews
    void initViews() {
        InitView.instance().initSwipeRefreshLayout(getActivity(),swipeRefreshLayout,true,this);

        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new RecyclerViewMaterialAdapter(new HomeNewAlbumRecyclerViewAdapter(getActivity(),mContentItems));
        recyclerView.setAdapter(adapter);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), recyclerView, null);

        loadLocalAlbumData();
        loadData();
    }

    private void loadData(){
        mContentItems.clear();
        loadAlbumData();
        loadTopPostData();
    }

    void loadLocalAlbumData(){
        String homeAlbumJsonString = aCache.getAsString("HomeAlbumList");
        if(homeAlbumJsonString != null){
            try {
                List<Album> list = objectMapper.readValue(homeAlbumJsonString, new TypeReference<List<Album>>(){});
                L.i(L.TEST,"list size ==> "+list.size()+"  first item ==> "+list.get(0));
                if(list != null && list.size() > 0){
                    mContentItems.addAll(list);
                    mContentItems.add(0,null);
                    adapter.mvp_notifyDataSetChanged();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Background
    void loadAlbumData(){
        List<Album> results = engine.getHomeAlbums();
        String jsonString = null;
        if(results != null){
            try {
                jsonString = objectMapper.writeValueAsString(results);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            if(jsonString != null)
                aCache.put("HomeAlbumList",jsonString);
            updateAlbumView(results);
        }
    }

    @Background
    void loadTopPostData(){
        Post topPost = engine.getTopPost();
        L.e(L.TEST, "post is null? " + (topPost == null));
        if(topPost != null){
            updateTopPostView(topPost);
        }
    }

    @UiThread
    void updateAlbumView(List<Album> results){
        swipeRefreshLayout.setRefreshing(false);
        mContentItems.addAll(results);
        adapter.mvp_notifyDataSetChanged();
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void updateTopPostView(Post post){
        mContentItems.add(0,post);
        adapter.mvp_notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        loadData();
    }
}
