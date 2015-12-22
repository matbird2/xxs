package com.xxs.leon.xxs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.constant.AlbumType;
import com.xxs.leon.xxs.rest.bean.Album;
import com.xxs.leon.xxs.rest.bean.XSUser;
import com.xxs.leon.xxs.rest.engine.impl.CommenEngineImpl;
import com.xxs.leon.xxs.utils.L;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maliang on 15/12/3.
 */
@EActivity(R.layout.activity_detail)
public class DetailActivity extends AppCompatActivity{

    @ViewById
    protected ImageView backdrop;
    @ViewById
    protected CoordinatorLayout root_container;
    @ViewById
    protected Toolbar toolbar;
    @ViewById
    protected TextView descriview;
    @ViewById
    protected TextView pagenum;
    @ViewById
    protected TextView size;
    @ViewById
    protected TextView type;
    @ViewById
    protected TextView price;
    @InstanceState
    protected Bundle savedInstanceState;
    @Extra
    String albumId;
    @Extra
    String albumName;
    @Bean
    CommenEngineImpl engine;

    XSUser currentUser;

    private  Album album;

    @AfterInject
    void init(){
        currentUser = engine.getCurrentUser();
    }

    @AfterViews
    void initViews(){
        toolbar.setTitle(albumName+"");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getAlbumDetail();
    }

    @Background
    void getAlbumDetail(){
        if(albumId != null){
            album = engine.getAlbumById(albumId);
            if(album != null){
                renderViewAfterRequest(album);
            }
        }
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void renderViewAfterRequest(Album album){
        L.i(L.TEST,"cover:"+album.getCover());
        toolbar.setTitle(album.getName());
        descriview.setText(album.getDescri());
        pagenum.setText("页数：" + album.getImgs().size()+"页");
        size.setText("大小："+album.getLength()+"M");
        type.setText("类型："+ AlbumType.getType(album.getType()));
        price.setText(album.getPrice() == 0 ? "免费阅读" : "花费："+album.getPrice()+"银两");
        Glide.with(this).load(album.getCover()).into(backdrop);
    }

    @Click(R.id.read)
    void clickRead(){
        if(album == null)
            return ;
        if(album.getPrice() == 0){
            gotoWatch();
        }else{
            currentUser = engine.getCurrentUser();
            if(currentUser == null){
                LoginActivity_.intent(this).start();
            }else{

            }
        }
    }

    private void gotoWatch(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("albumList", album.getImgs());
        bundle.putString("baseurl", album.getFrom());
        Intent intent = new Intent(this,WatchActivity_.class);
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
