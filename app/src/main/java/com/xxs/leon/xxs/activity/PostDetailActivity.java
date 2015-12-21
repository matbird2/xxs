package com.xxs.leon.xxs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.rest.bean.Post;
import com.xxs.leon.xxs.rest.engine.impl.CommenEngineImpl;
import com.xxs.leon.xxs.utils.TimeUtil;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import jp.wasabeef.richeditor.RichEditor;

/**
 * Created by maliang on 15/12/21.
 */
@EActivity(R.layout.activity_post_detail)
public class PostDetailActivity extends AppCompatActivity{

    @ViewById
    protected RichEditor editor;
    @ViewById
    protected TextView title;
    @ViewById
    protected TextView source;
    @ViewById
    protected Toolbar toolbar;
    @ViewById
    protected ImageView photo;
    @ViewById
    protected TextView username;
    @ViewById
    protected TextView timetag;

    @Extra
    String postId;
    @Extra
    String postTitle;

    @Bean
    CommenEngineImpl engine;

    Post post;

    @AfterInject
    void init(){

    }

    @AfterViews
    void initViews(){
        toolbar.setTitle(postTitle == null ? "" : postTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editor.setPadding(10, 10, 10, 10);

        doGetPostDetail();
    }


    @Background
    void doGetPostDetail(){
        if(postId != null){
            post = engine.getPostDetial(postId);
            afterGetPostDetail(post);
        }
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void afterGetPostDetail(Post post){
        if(post != null && post.getCode() == 0){
            title.setText(post.getTitle());
            editor.setHtml(post.getContent());
            username.setText(post.getUser().getUsername());
            timetag.setText(TimeUtil.generTimeShowWord(post.getCreatedAt()));
            getPhotoThumbnail(post.getUser().getPhoto(),photo);

            if(post.getLinked_url() == null || "#".equals(post.getLinked_url())){
                source.setVisibility(View.GONE);
            }else {
                source.setVisibility(View.VISIBLE);
            }
        }
    }

    @Background
    void getPhotoThumbnail(String image,ImageView iv){
        String thumbnailUrl = engine.getThumbnail(image,100,75);
        renderPhoto(thumbnailUrl, iv);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void renderPhoto(String thumbnailUrl,ImageView iv){
        Glide.with(this).load(thumbnailUrl).crossFade(500).centerCrop().into(iv);
    }

    @Click(R.id.source)
    void clickSource(){
        if(post == null)
            return ;
        Bundle bundle = new Bundle();
        bundle.putString("url", post.getLinked_url());
        bundle.putString("html", "#");
        bundle.putInt("type",0);
        Intent intent = new Intent(this,WebViewActivity_.class);
        intent.putExtra("bundle",bundle);
        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
