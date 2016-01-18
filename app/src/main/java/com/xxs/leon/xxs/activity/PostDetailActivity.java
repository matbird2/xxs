package com.xxs.leon.xxs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.rest.bean.Post;
import com.xxs.leon.xxs.rest.engine.impl.CommenEngineImpl;
import com.xxs.leon.xxs.richeditor.RichEditor;
import com.xxs.leon.xxs.utils.InitView;
import com.xxs.leon.xxs.utils.L;
import com.xxs.leon.xxs.utils.TimeUtil;
import com.xxs.leon.xxs.utils.Tools;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

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
    @Extra
    int enterType;

    @Bean
    CommenEngineImpl engine;

    Post post;

    @AfterInject
    void init(){

    }

    @AfterViews
    void initViews(){
        InitView.instance().initToolbar(toolbar, this, postTitle+"");

        editor.setPadding(10, 10, 10, 10);
        editor.setEnabled(false);
        editor.setFocusable(false);

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
            getPhotoThumbnail(post.getUser().getPhoto(), photo);

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
        Glide.with(this).load(thumbnailUrl).crossFade(500).placeholder(R.drawable.default_head_photo).error(R.drawable.default_head_photo).centerCrop().into(iv);
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

    @Click(R.id.send_comment)
    void clickSendComment(){
        if(postId != null && !TextUtils.isEmpty(postId));
            CommentDialogActivity_.intent(this).commentType(0).postId(postId).start();
    }

    @Override
    public void onBackPressed() {
        L.e(L.TEST, "activity size:" + Tools.getActivityHeapSize(this));
        if(enterType == 0){
            finish();
        }else if(enterType == 1){
            if(Tools.getActivityHeapSize(this) > 1){
                finish();
            }else{
                MainActivity_.intent(this).start();
                finish();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            if(enterType == 0){
                finish();
            }else if(enterType == 1){
                if(Tools.getActivityHeapSize(this) > 1){
                    finish();
                }else{
                    MainActivity_.intent(this).start();
                    finish();
                }
            }
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
