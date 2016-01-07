package com.xxs.leon.xxs.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.utils.InitView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.update.UpdateStatus;

/**
 * Created by leon on 15-12-28.
 */
@EActivity(R.layout.activity_settings)
public class SettingsActivity extends AppCompatActivity{
    @ViewById
    protected Toolbar toolbar;

    @AfterInject
    void init(){

    }

    @AfterViews
    void initViews(){
        InitView.instance().initToolbar(toolbar, this, "设置");
    }

    @Click(R.id.ll_update)
    void clickUpdate(){
        BmobUpdateAgent.forceUpdate(this);
        BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateResponse) {
                if (updateStatus == UpdateStatus.Yes) {//版本有更新

                } else if (updateStatus == UpdateStatus.No) {
                    Toast.makeText(SettingsActivity.this, "当前是最新版本", Toast.LENGTH_SHORT).show();
                } else if (updateStatus == UpdateStatus.EmptyField) {//此提示只是提醒开发者关注那些必填项，测试成功后，无需对用户提示
//                    Toast.makeText(SettingsActivity.this, "请检查你AppVersion表的必填项，1、target_size（文件大小）是否填写；2、path或者android_url两者必填其中一项。", Toast.LENGTH_SHORT).show();
                } else if (updateStatus == UpdateStatus.IGNORED) {
//                    Toast.makeText(SettingsActivity.this, "该版本已被忽略更新", Toast.LENGTH_SHORT).show();
                } else if (updateStatus == UpdateStatus.ErrorSizeFormat) {
//                    Toast.makeText(SettingsActivity.this, "请检查target_size填写的格式，请使用file.length()方法获取apk大小。", Toast.LENGTH_SHORT).show();
                } else if (updateStatus == UpdateStatus.TimeOut) {
                    Toast.makeText(SettingsActivity.this, "查询出错或查询超时", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
