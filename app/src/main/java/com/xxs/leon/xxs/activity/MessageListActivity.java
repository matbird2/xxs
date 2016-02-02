package com.xxs.leon.xxs.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.umeng.analytics.MobclickAgent;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.adapter.MessageListAdapter;
import com.xxs.leon.xxs.utils.InitView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.bmob.im.db.BmobDB;

/**
 * Created by maliang on 16/2/2.
 */
@EActivity(R.layout.activity_message_list)
public class MessageListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    @ViewById
    protected SwipeRefreshLayout swipeRefreshLayout;
    @ViewById
    protected RecyclerView recyclerView;
    @ViewById
    protected Toolbar toolbar;

    MessageListAdapter adapter;
    private LinearLayoutManager mLayoutManager;

    @AfterInject
    void init(){
    }

    @AfterViews
    void initViews(){
        InitView.instance().initToolbar(toolbar, this, "消息");

        initListView();
    }

    // 初始化跟listview相关的控件
    private void initListView(){
        InitView.instance().initSwipeRefreshLayout(this, swipeRefreshLayout, false, this);

        adapter = new MessageListAdapter(this);
        mLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        adapter.appenList(BmobDB.create(this).queryRecents());
    }

    @Override
    public void onRefresh() {
        adapter.clear();
        adapter.appenList(BmobDB.create(this).queryRecents());
        swipeRefreshLayout.setRefreshing(false);
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
