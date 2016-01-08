package com.xxs.leon.xxs.activity;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.umeng.analytics.MobclickAgent;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.adapter.AlbumListAdapter;
import com.xxs.leon.xxs.adapter.SearchListAdapter;
import com.xxs.leon.xxs.rest.bean.Album;
import com.xxs.leon.xxs.rest.engine.impl.CommenEngineImpl;
import com.xxs.leon.xxs.utils.InitView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maliang on 16/1/8.
 */
@EActivity(R.layout.activity_search)
public class SearchActivity extends AppCompatActivity{
    @ViewById
    protected Toolbar toolbar;
    @ViewById
    protected RecyclerView recyclerView;
    @Bean
    CommenEngineImpl engine;

    private LinearLayoutManager mLayoutManager;
    private SearchListAdapter adapter;
    private List<Album> contents = new ArrayList<>();

    @AfterInject
    void init(){

    }

    @AfterViews
    void initViews(){
        InitView.instance().initToolbar(toolbar,this,"搜索");

        initListView();
    }

    // 初始化跟listview相关的控件
    private void initListView(){
        adapter = new SearchListAdapter(this);
        mLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            menu.findItem(R.id.search).setVisible(true);
            final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
            searchView.setSubmitButtonEnabled(true);
//            searchView.setIconifiedByDefault(false);
//            searchView.clearFocus();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String s) {
                    if(!TextUtils.isEmpty(s)){
                        doSearch(s);
                    }
                    return true;
                }


                @Override
                public boolean onQueryTextChange(String s) {
                    if(!TextUtils.isEmpty(s)){
                        doSearch(s);
                    }
                    return true;
                }
            });
        } else {
            menu.findItem(R.id.search).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Background
    void doSearch(String keyword){
        List<Album> albums = engine.search(keyword);
        if(albums != null && albums.size() > 0){
            renderAfterSearch(albums);
        }
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void renderAfterSearch(List<Album> albums){
        adapter.clear();
        adapter.appenList(albums);
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
