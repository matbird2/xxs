package com.xxs.leon.xxs.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.umeng.analytics.MobclickAgent;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.adapter.AlbumListAdapter;
import com.xxs.leon.xxs.adapter.CommentListAdapter;
import com.xxs.leon.xxs.rest.bean.Comment;
import com.xxs.leon.xxs.rest.bean.Post;
import com.xxs.leon.xxs.rest.engine.impl.CommenEngineImpl;
import com.xxs.leon.xxs.utils.InitView;
import com.xxs.leon.xxs.utils.L;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maliang on 16/1/19.
 */
@EActivity(R.layout.activity_show_all_comment)
public class ShowAllCommentActivity extends Activity{

    public static final int REQUEST_REPLY_COMMENT = 1;

    @ViewById
    protected RecyclerView recyclerView;
    @Bean
    CommenEngineImpl engine;
    @Extra
    String postId;

    Post post;

    private LinearLayoutManager linearLayoutManager;
    private List<Comment> mContents = new ArrayList<>();
    private CommentListAdapter adapter;
    private int lastVisibleItem = 0;
    private int pageIndex = 0;
    private static int PAGE_SIZE = 10;
    private boolean hasMore = true;

    @AfterInject
    void init(){

    }

    @AfterViews
    void initViews(){
        initRecyclerView();
        loadCommentList(pageIndex);
    }

    //--------------------begine about comment list---------------
    private void initRecyclerView(){
        adapter = new CommentListAdapter(this,mContents,postId,null);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == adapter.getItemCount()) {
                    if (hasMore) {
                        pageIndex += 1;
                        loadCommentList(pageIndex);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }

        });

        adapter.setOnGetThumbnailAndDisplayImageViewListener(new CommentListAdapter.OnGetThumbnailAndDisplayImageViewListener() {
            @Override
            public void getAndDisplay(String image, ImageView iv) {
                getPhotoThumbnail(image, iv);
            }
        });

    }

    @Background
    void loadCommentList(int page){
        if(postId == null || TextUtils.isEmpty(postId))
            return;
        List<Comment> result = engine.getCommentList(page * PAGE_SIZE, null, postId);
        L.w(L.TEST, "comment size:" + result.size());
        renderViewAfterLoadData(result);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void renderViewAfterLoadData(List<Comment> result){
        if(result != null ){
            mContents.addAll(result);
            adapter.notifyDataSetChanged();
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

    @Background
    void getPhotoThumbnail(String image,ImageView iv){
        String thumbnailUrl = engine.getThumbnail(image, 100, 75);
        renderPhoto(thumbnailUrl, iv);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void renderPhoto(String thumbnailUrl,ImageView iv){
        Glide.with(this).load(thumbnailUrl).crossFade(500).placeholder(R.drawable.default_head_photo).error(R.drawable.default_head_photo).centerCrop().into(iv);
    }

    @Click(R.id.iv_close)
    void clickClose(){
        finish();
    }

    @Click(R.id.rl_content)
    void clickContainer(){
        finish();
    }

    @OnActivityResult(REQUEST_REPLY_COMMENT)
    void onResultFromReply(int resultCode,Intent data){
        pageIndex = 0;
        mContents.clear();
        loadCommentList(pageIndex);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
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
