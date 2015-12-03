package com.xxs.leon.xxs.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.fragment.NewFragment_;
import com.xxs.leon.xxs.fragment.RecyclerViewFragment;
import com.xxs.leon.xxs.rest.bean.XSUser;
import com.xxs.leon.xxs.rest.engine.CommenEngine;
import com.xxs.leon.xxs.test.SecondActivity_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

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

    @Bean
    CommenEngine engine;

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

//    XSUser currentUser;
//    XSUser resultUser;

    @AfterInject
    void init(){
        initIconRes();
    }

    @AfterViews
    void initView(){
//        currentUser = engine.getCurrentUser();

        initMaterialViewpager();
        initDrawerView();
//        loadUserInfo();
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

    /*@Background
    void loadUserInfo(){
        if(currentUser != null){
            resultUser = engine.getUserInfo(currentUser.getObjectId());
            renderUserView(resultUser);
        }
    }*/

    /*@UiThread(propagation = UiThread.Propagation.REUSE)
    void renderUserView(XSUser user){
        if(profile != null){
            ((ProfileDrawerItem)profile).withName(user.getUsername()).withIcon(user.getPhoto());
        }
    }*/

    private IProfile profile;
    private void initDrawerView(){
        profile = new ProfileDrawerItem().withName("").withIcon(peopleDrawable).withIdentifier(1);

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(profile)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        //IMPORTANT! notify the MiniDrawer about the profile click
                        miniResult.onProfileClick();
                        if(profile.getIdentifier() == 1){
                            startActivity(new Intent(MainActivity.this,LoginGuidActivity_.class));
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
                        new PrimaryDrawerItem().withName("小书").withIcon(FontAwesome.Icon.faw_book).withBadge("22").withBadgeStyle(new BadgeStyle(Color.RED, Color.RED)).withIdentifier(2),
                        new PrimaryDrawerItem().withName("我的").withIcon(GoogleMaterial.Icon.gmd_account_circle).withIdentifier(3),

                        new SectionDrawerItem().withName("其他"),
                        new SecondaryDrawerItem().withName("设置").withIcon(GoogleMaterial.Icon.gmd_settings),
                        new SecondaryDrawerItem().withName("联系").withIcon(GoogleMaterial.Icon.gmd_format_color_fill).withTag("Bullhorn")
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
                            final Intent intent = new Intent(MainActivity.this, UserActivity_.class);
                            MainActivity.this.startActivity(intent);
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
        if (toolbar != null) {
            setSupportActionBar(toolbar);

            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setHomeButtonEnabled(true);
            }
        }

        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position % 3) {
                    case 0:
                        return new NewFragment_();
                    //case 1:
                    //    return RecyclerViewFragment.newInstance();
                    //case 2:
                    //    return WebViewFragment.newInstance();
                    default:
                        return RecyclerViewFragment.newInstance();
                }
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 3) {
                    case 0:
                        return "最新";
                    case 1:
                        return "Ta说";
                    case 2:
                        return "推荐";
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
                    case 2:
                        logo.setVisibility(View.INVISIBLE);
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.cyan,
                                "http://tupian.qqjay.com/u/2013/1127/19_222949_4.jpg");
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
        final Intent intent = new Intent(MainActivity.this, CommentActivity_.class);
        ActivityTransitionLauncher.with(MainActivity.this).from(logo).launch(intent);
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
            super.onBackPressed();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }
}
