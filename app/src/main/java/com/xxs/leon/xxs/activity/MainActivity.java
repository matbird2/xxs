package com.xxs.leon.xxs.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gc.materialdesign.widgets.Dialog;
import com.gc.materialdesign.widgets.SnackBar;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.kogitune.activity_transition.ActivityTransitionLauncher;
import com.mikepenz.crossfadedrawerlayout.view.CrossfadeDrawerLayout;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.interfaces.ICrossfader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;
import com.umeng.analytics.MobclickAgent;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.fragment.NewFragment_;
import com.xxs.leon.xxs.fragment.PostFragment_;
import com.xxs.leon.xxs.fragment.RecyclerViewFragment;
import com.xxs.leon.xxs.rest.bean.XSUser;
import com.xxs.leon.xxs.rest.bean.request.PayParams;
import com.xxs.leon.xxs.rest.engine.CommenEngine;
import com.xxs.leon.xxs.rest.engine.impl.CommenEngineImpl;
import com.xxs.leon.xxs.test.SecondActivity_;
import com.xxs.leon.xxs.utils.L;
import com.xxs.leon.xxs.utils.TimeUtil;
import com.xxs.leon.xxs.utils.ToolbarUtil;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.update.UpdateStatus;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity{

    @ViewById(R.id.materialViewPager)
    protected MaterialViewPager mViewPager;
    @ViewById
    protected ImageView logo_in;
    @ViewById
    protected FrameLayout logo;
    @ViewById
    protected RelativeLayout content;
    @Extra
    int enterType;

    @Bean
    CommenEngineImpl engine;

    @InstanceState
    Bundle savedInstanceState;

    protected Toolbar toolbar;

    private AccountHeader headerResult = null;
    private Drawer result = null;
    private MiniDrawer miniResult = null;
    private CrossfadeDrawerLayout crossfadeDrawerLayout = null;

    IconicsDrawable signIcon;
    IconicsDrawable penIcon;
    IconicsDrawable peopleDrawable;

    XSUser resultUser;
//    private IProfile profile;

    @AfterInject
    void init(){
        initIconRes();
        loadUserInfo();
    }

    @AfterViews
    void initView(){
        BmobUpdateAgent.setUpdateOnlyWifi(false);
        BmobUpdateAgent.update(MainActivity.this);
        /*BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateResponse) {
                if (updateStatus == UpdateStatus.Yes) {//版本有更新

                } else if (updateStatus == UpdateStatus.No) {
                    Toast.makeText(MainActivity.this, "版本无更新", Toast.LENGTH_SHORT).show();
                } else if (updateStatus == UpdateStatus.EmptyField) {//此提示只是提醒开发者关注那些必填项，测试成功后，无需对用户提示
                    Toast.makeText(MainActivity.this, "请检查你AppVersion表的必填项，1、target_size（文件大小）是否填写；2、path或者android_url两者必填其中一项。", Toast.LENGTH_SHORT).show();
                } else if (updateStatus == UpdateStatus.IGNORED) {
                    Toast.makeText(MainActivity.this, "该版本已被忽略更新", Toast.LENGTH_SHORT).show();
                } else if (updateStatus == UpdateStatus.ErrorSizeFormat) {
                    Toast.makeText(MainActivity.this, "请检查target_size填写的格式，请使用file.length()方法获取apk大小。", Toast.LENGTH_SHORT).show();
                } else if (updateStatus == UpdateStatus.TimeOut) {
                    Toast.makeText(MainActivity.this, "查询出错或查询超时", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        initMaterialViewpager();
        initDrawerView();
    }

    void initIconRes(){
        signIcon = new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_assignment_o)
                .color(Color.WHITE)
                .sizeDp(20);
        penIcon = new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_edit)
                .color(Color.WHITE)
                .sizeDp(20);
        peopleDrawable = new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_account_circle)
                .color(Color.GRAY)
                .sizeDp(24);
    }

    @Background
    void loadUserInfo(){
        if(engine.getCurrentUser() != null){
            resultUser = engine.getUserInfo(engine.getCurrentUser().getObjectId());
            renderUserView(resultUser);
        }
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void renderUserView(XSUser user){
        if(defualtProfile != null)
            headerResult.removeProfile(defualtProfile);
        IProfile userProfile = new ProfileDrawerItem().withName(resultUser.getUsername()).withIcon(resultUser.getPhoto()).withIdentifier(6);
        headerResult.addProfile(userProfile,0);

    }

    IProfile defualtProfile = null;
    private void initDrawerView(){
//        defualtProfile = new ProfileDrawerItem().withName("").withIcon("http://lhh.a8z8.com/data/attachment/forum/day_100921/1009212215583d83df596a2516.jpg").withIdentifier(6);
        defualtProfile = new ProfileDrawerItem().withName("").withIcon("http://avatar.l99.com/200x222/22/MjAxMzA4MjcxMDM3NDhfMTQuMTA3LjIwLjEzN181Mjg1MjA=.jpg").withIdentifier(6);
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(defualtProfile)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        //IMPORTANT! notify the MiniDrawer about the profile click
                        miniResult.onProfileClick();
                        if (profile.getIdentifier() == 6) {
                            if(engine.getCurrentUser() == null){
                                LoginActivity_.intent(MainActivity.this).start();
                            }else{
                                UserActivity_.intent(MainActivity.this).start();
                            }
                        }
                        //false if you have not consumed the event and it should close the drawer
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        //create the CrossfadeDrawerLayout which will be used as alternative DrawerLayout for the Drawer
        crossfadeDrawerLayout = new CrossfadeDrawerLayout(this);

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withDrawerLayout(crossfadeDrawerLayout)
                .withHasStableIds(true)
                .withDrawerWidthDp(72)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("首页").withIcon(FontAwesome.Icon.faw_home).withIdentifier(1),
                        new PrimaryDrawerItem().withName("小书").withIcon(FontAwesome.Icon.faw_book)/*.withBadge("22").withBadgeStyle(new BadgeStyle(Color.RED, Color.RED))*/.withIdentifier(2),
                        new PrimaryDrawerItem().withName("我的").withIcon(GoogleMaterial.Icon.gmd_account_circle).withIdentifier(3),

                        new SectionDrawerItem().withName("其他"),
                        new SecondaryDrawerItem().withName("设置").withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(4),
                        new SecondaryDrawerItem().withName("联系").withIcon(GoogleMaterial.Icon.gmd_format_color_fill).withIdentifier(5)
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if(drawerItem.getIdentifier() == 2){
//                            View v = drawerItem.getViewHolder(content).itemView;
                            final Intent intent = new Intent(MainActivity.this, CategoryActivity_.class);
//                            ActivityTransitionLauncher.with(MainActivity.this).from(v.findViewById(R.id.material_drawer_icon)).launch(intent);
                            MainActivity.this.startActivity(intent);
                        }else if(drawerItem.getIdentifier() == 3){
                            if(engine.getCurrentUser() != null){
                                final Intent intent = new Intent(MainActivity.this, UserActivity_.class);
                                MainActivity.this.startActivity(intent);
                            }else{
                                LoginActivity_.intent(MainActivity.this).start();
                            }
                        }else if(drawerItem.getIdentifier() == 4){
//                            BmobUpdateAgent.initAppVersion(MainActivity.this);
                            SettingsActivity_.intent(MainActivity.this).start();
                        }else if(drawerItem.getIdentifier() == 5){
                            ContractActivity_.intent(MainActivity.this).start();
                        }
                        return miniResult.onItemClick(drawerItem);
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .build();

        //define maxDrawerWidth
        crossfadeDrawerLayout.setMaxWidthPx(DrawerUIUtils.getOptimalDrawerWidth(this));
        //add second view (which is the miniDrawer)
        miniResult = new MiniDrawer().withDrawer(result).withAccountHeader(headerResult);
        //build the view for the MiniDrawer
        View view = miniResult.build(this);
        //set the background of the MiniDrawer as this would be transparent
        view.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(this, com.mikepenz.materialdrawer.R.attr.material_drawer_background, com.mikepenz.materialdrawer.R.color.material_drawer_background));
        //we do not have the MiniDrawer view during CrossfadeDrawerLayout creation so we will add it here
        crossfadeDrawerLayout.getSmallView().addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
        miniResult.withCrossFader(new ICrossfader() {
            @Override
            public void crossfade() {
                boolean isFaded = isCrossfaded();
                crossfadeDrawerLayout.crossfade(400);

                //only close the drawer if we were already faded and want to close it now
                if (isFaded) {
                    result.getDrawerLayout().closeDrawer(GravityCompat.START);
                }
            }

            @Override
            public boolean isCrossfaded() {
                return crossfadeDrawerLayout.isCrossfaded();
            }
        });
    }


    private void initMaterialViewpager(){
        setTitle("");
        toolbar = mViewPager.getToolbar();
        ToolbarUtil.initToolbar(this,toolbar);

        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position % 2) {
                    case 0:
                        return new NewFragment_();
                    case 1:
                        return new PostFragment_();
                    //case 2:
                    //    return WebViewFragment.newInstance();
                    default:
                        return new NewFragment_();
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 2) {
                    case 0:
                        return "最新";
                    case 1:
                        return "Ta说";
//                    case 2:
//                        return "推荐";
                }
                return "";
            }
        });

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        logo.setVisibility(View.VISIBLE);
                        logo_in.setImageDrawable(signIcon);
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.colorPrimary,
                                "http://tupian.qqjay.com/u/2013/1127/19_222949_14.jpg");
                    case 1:
                        logo.setVisibility(View.VISIBLE);
                        logo_in.setImageDrawable(penIcon);
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue,
                                "http://tupian.qqjay.com/u/2013/1127/19_222949_3.jpg");
//                    case 2:
//                        logo.setVisibility(View.INVISIBLE);
//                        return HeaderDesign.fromColorResAndUrl(
//                                R.color.cyan,
//                                "http://tupian.qqjay.com/u/2013/1127/19_222949_4.jpg");
                }

                //execute others actions if needed (ex : modify your header logo)
                return null;
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
    }

    @Click(R.id.logo)
    void onClickLogo(){
        mViewPager.notifyHeaderChanged();
        if(engine.getCurrentUser() == null){
            LoginActivity_.intent(this).start();
        }else{
            int currentItem = mViewPager.getViewPager().getCurrentItem();
            if(currentItem == 0){
                logo.setEnabled(false);
                doSendSignPost();
            }else if(currentItem == 1){
//                ActivityTransitionLauncher.with(MainActivity.this).from(logo).launch(intent);
                final Intent intent = new Intent(MainActivity.this, CommentActivity_.class);
                startActivity(intent);
            }
        }

//        L.i(L.TEST,"生成时间标签:"+ TimeUtil.generTimeShowWord("2015-12-16 15:23:03"));
    }

    @Background
    void doSendSignPost(){
        String result = engine.sendSignPost(engine.getCurrentUser());
        renderAfterSendSign(result);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void renderAfterSendSign(String result){
        logo.setEnabled(true);
        Dialog dialog = new Dialog(this,"签到",result);
        dialog.show();
        dialog.getButtonAccept().setText("确定");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            if (enterType == 0){
                SnackBar snackBar = new SnackBar(this, "确定退出[小小书]?", "退出", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity.this.finish();
                        System.exit(0);
                    }
                });
                snackBar.show();
            }else if(enterType == 1){
                finish();
            }
        }
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
