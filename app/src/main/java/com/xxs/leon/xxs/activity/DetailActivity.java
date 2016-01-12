package com.xxs.leon.xxs.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.widgets.Dialog;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.adapter.RecommendListAdapter;
import com.xxs.leon.xxs.constant.AlbumType;
import com.xxs.leon.xxs.rest.bean.Album;
import com.xxs.leon.xxs.rest.bean.XSUser;
import com.xxs.leon.xxs.rest.engine.impl.CommenEngineImpl;
import com.xxs.leon.xxs.utils.InitView;
import com.xxs.leon.xxs.utils.L;
import com.xxs.leon.xxs.utils.Tools;

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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobInstallation;

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
    @ViewById
    protected RecyclerView recyclerView;
    @InstanceState
    protected Bundle savedInstanceState;
    @Extra
    String albumId;
    @Extra
    String albumName;
    @Extra
    int enterType;
    @Bean
    CommenEngineImpl engine;

    XSUser currentUser;
    RecommendListAdapter adapter;

    private  Album album;
    private boolean hasUserRead = false;
//    private boolean hasDeviceRead = false;

    @AfterInject
    void init(){
        currentUser = engine.getCurrentUser();
    }

    @AfterViews
    void initViews(){
        InitView.instance().initToolbar(toolbar, this, albumName + "");
        initRecyclerView();
        getAlbumDetail();
        getRecommendAlbumList();
    }

    private void initRecyclerView(){
        adapter = new RecommendListAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        gridLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
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
        L.i(L.TEST, "cover:" + album.getCover());
        toolbar.setTitle(album.getName());
        descriview.setText(album.getDescri());
        pagenum.setText("页数：" + album.getImgs().size() + "页");
        size.setText("大小："+album.getLength()+"M");
        type.setText("类型："+ AlbumType.getType(album.getType()));
        price.setText(album.getPrice() == 0 ? "免费阅读" : "花费：" + album.getPrice() + "银两");
        Glide.with(this).load(album.getCover()).into(backdrop);
    }

    @Background
    void getRecommendAlbumList(){
        if(albumName != null){
            String[] keys = Tools.getRecommendKeywordPair(albumName);
            List<Album> results = engine.getRecommendAlbumList(keys[0], keys[1],album == null ? 0 :album.getType());
            L.i(L.TEST,"size:"+results.size()+" key1 => "+keys[0]+" key2 => "+keys[1]);
            if(results != null && results.size() > 0){
                renderAfterGetRecommendList(results);
            }
        }
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void renderAfterGetRecommendList(List<Album> results){
        adapter.appenList(results);
    }

    @Click(R.id.find_same)
    void clickFind(){
        if(album == null)
            return ;
        Bundle bundle = new Bundle();
        bundle.putInt("type",album.getType());
        Intent intent = new Intent(DetailActivity.this,AlbumListActivity_.class);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    //----------------------------begin ablout read and cost------------------
    @Click(R.id.read)
    void clickRead(){
        if(album == null)
            return ;

        currentUser = engine.getCurrentUser();
        if(album.getPrice() == 0){
            if(currentUser == null){
                L.w(L.TEST,"not login and free.");
                checkDeviceHasRead(BmobInstallation.getInstallationId(this),album.getObjectId());
                gotoWatch();
            }else{
                L.w(L.TEST,"login and free.");
                checkUserHasRead(currentUser.getObjectId(), album.getObjectId());
                gotoWatch();
            }
        }else{
            if(currentUser == null){
                LoginActivity_.intent(this).start();
            } else {
                L.w(L.TEST,"login and not free.");
                checkUserHasReadAndCost(currentUser.getObjectId(),album.getObjectId());
            }
        }
    }

    @Background
    void checkUserHasRead(String userId, String albumId){
        L.w(L.TEST,"checkUserHasRead");
        hasUserRead = engine.hasUserReadAlbumByUserId(userId, albumId);
        if(!hasUserRead){
            engine.addReadLog(this, currentUser, album.getObjectId());
        }
    }

    @Background
    void checkUserHasReadAndCost(String userId, String albumId){
        L.w(L.TEST,"checkUserHasReadAndCost");
        hasUserRead = engine.hasUserReadAlbumByUserId(userId, albumId);
        afterCheckHasRead(hasUserRead);
    }

    @Background
    void checkDeviceHasRead(String installationId,String albumId){
        L.w(L.TEST,"checkDeviceHasRead");
        boolean hasDeviceRead = engine.hasUserReadAlbumByInstallationId(installationId,albumId);
        if(!hasDeviceRead && album != null){
            engine.addReadLog(this, currentUser, album.getObjectId());
        }
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void afterCheckHasRead(boolean hasUserRead){
        L.w(L.TEST,"afterCheckHasRead");
        if(hasUserRead){
            gotoWatch();
        }else{
            getUserInfoAndCheckEnoughMoney();
        }
    }

    @Background
    void getUserInfoAndCheckEnoughMoney(){
        L.w(L.TEST,"getUserInfoAndCheckEnoughMoney");
        if(currentUser == null || album == null)
            return ;
        XSUser userInfo = engine.getUserInfo(currentUser.getObjectId());
        if(userInfo == null)
            return ;
        if(userInfo.getMoney() > album.getPrice()){
            showCostMoneyDialog();
        }else{
            showNoEnoughMoneyDialog(userInfo);
        }
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void showCostMoneyDialog(){
        L.w(L.TEST,"showCostMoneyDialog");
        final Dialog dialog = new Dialog(this,"花费银两","阅读该连环画需要花费 "+album.getPrice()+" 银两");
        dialog.show();
        dialog.getButtonAccept().setText("去阅读");
        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                doCostMoney();
            }
        });
    }

    @Background
    void doCostMoney(){
        L.w(L.TEST,"doCostMoney");
        if(album == null || currentUser == null)
            return ;
        String result = engine.costMoney(currentUser, album.getPrice());
        if (result == null)
            return ;
        if("success".equals(result)){
            checkUserHasRead(currentUser.getObjectId(), album.getObjectId());
            gotoWatch();
        }else{
            showCostBackDialog(result);
        }
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void showCostBackDialog(String result){
        L.w(L.TEST,"showCostBackDialog");
        final Dialog dialog = new Dialog(this,"",result);
        dialog.addCancelButton("知道了");
        dialog.show();
        dialog.getButtonAccept().setText("充值");
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void showNoEnoughMoneyDialog(XSUser userInfo){
        L.w(L.TEST,"showNoEnoughMoneyDialog");
        final Dialog dialog = new Dialog(this,"银两不足","您目前只有"+userInfo.getMoney()+"银两，还不能阅读该连环画哦.");
        dialog.addCancelButton("知道了");
        dialog.show();
        dialog.getButtonAccept().setText("充值");
        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RechargeActivity_.intent(DetailActivity.this).start();
            }
        });
    }


    private void gotoWatch(){
        L.w(L.TEST,"gotoWatch");
        Bundle bundle = new Bundle();
        bundle.putSerializable("albumList", album.getImgs());
        bundle.putString("baseurl", album.getFrom());
        bundle.putString("albumId", album.getObjectId());
        Intent intent = new Intent(this,WatchActivity_.class);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    //----------------------------end ablout read and cost------------------

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
    public void onBackPressed() {
        L.e(L.TEST,"activity size:"+ Tools.getActivityHeapSize(this));
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
