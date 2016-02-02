package com.xxs.leon.xxs.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gc.materialdesign.widgets.Dialog;
import com.gc.materialdesign.widgets.SnackBar;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.umeng.analytics.MobclickAgent;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.bean.XSBmobChatUser;
import com.xxs.leon.xxs.constant.Constant;
import com.xxs.leon.xxs.rest.bean.UpdateBean;
import com.xxs.leon.xxs.rest.bean.response.UploadEntity;
import com.xxs.leon.xxs.rest.engine.impl.CommenEngineImpl;
import com.xxs.leon.xxs.ui.ChooseImageDialog;
import com.xxs.leon.xxs.utils.InitView;
import com.xxs.leon.xxs.utils.L;
import com.xxs.leon.xxs.utils.MatCacheUtils;
import com.xxs.leon.xxs.utils.Tools;

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

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.listener.FindListener;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by maliang on 15/11/26.
 */
@EActivity(R.layout.activity_user)
public class UserActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private static final int REQUEST_CODE_ALBUM = 1;
    private static final int REQUEST_CODE_CAMERA = 2;

    @ViewById
    protected SwipeRefreshLayout swipeRefreshLayout;
    @ViewById
    protected ImageView backdrop;
    @ViewById
    protected ImageView photo;
    @ViewById
    protected TextView name;
    @ViewById
    protected TextView point;
    @ViewById
    protected TextView money;
    @ViewById
    protected TextView sign;
    @ViewById
    protected Toolbar toolbar;
    @ViewById
    protected LinearLayout ll_loginout;
    @ViewById
    protected LinearLayout ll_im;
    @Extra
    String userId;

    @Bean
    CommenEngineImpl engine;

    IconicsDrawable account_icon,right_icon,head_icon;
    ChooseImageDialog dialog;
    XSBmobChatUser resultUser;
    BmobChatUser currentUser;
    String dateTime;
    String targeturl = null;
    boolean isMyCenter = false;

    @AfterInject
    void init(){
        initIconRes();
        currentUser = BmobUserManager.getInstance(this).getCurrentUser();
    }

    @AfterViews
    void initViews(){
        InitView.instance().initToolbar(toolbar,this,"");
        InitView.instance().initSwipeRefreshLayout(this, swipeRefreshLayout, true,this);

        if(!TextUtils.isEmpty(userId) && userId.equals(currentUser.getObjectId())){
            isMyCenter = true;
            ll_loginout.setVisibility(View.VISIBLE);
            ll_im.setVisibility(View.GONE);
        }else{
            isMyCenter = false;
            ll_loginout.setVisibility(View.INVISIBLE);
            ll_im.setVisibility(View.VISIBLE);
        }

        if(!TextUtils.isEmpty(userId)){
            doGetUserInfo(userId);
        }

        Glide.with(this).load(Constant.DEFAULT_USER_CENTER_BACKGROUND_URL).placeholder(R.drawable.default_image_loading).error(R.drawable.default_loading_error).into(backdrop);

    }

    @Background
    void doGetUserInfo(String objectId){
        resultUser = engine.getUserInfo(objectId);
        renderView(resultUser);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void renderView(XSBmobChatUser resultUser){
        swipeRefreshLayout.setRefreshing(false);

        if(dialog != null)
            dialog.dismiss();
        name.setText(resultUser.getUsername());
        point.setText(resultUser.getPoint() + "");
        money.setText(resultUser.getMoney() + "");
        sign.setText(resultUser.getSignword());
        Glide.with(this).load(resultUser.getPhoto()+"").error(account_icon).bitmapTransform(new CropCircleTransformation(this)).into(photo);
    }

    void initIconRes(){
        account_icon = new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_account_circle)
                .color(Color.GRAY)
                .sizeDp(60);

        right_icon = new IconicsDrawable(this)
                .icon(FontAwesome.Icon.faw_angle_right)
                .color(Color.BLACK)
                .sizeDp(20);
        head_icon = new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_cat)
                .color(Color.BLACK)
                .sizeDp(20);
    }

    @Click(R.id.tv_im_contract)
    void clickContract(){
        BmobUserManager.getInstance(this).queryUser(resultUser.getUsername(), new FindListener<XSBmobChatUser>() {
            @Override
            public void onSuccess(List<XSBmobChatUser> list) {
                if (list != null && list.size() > 0) {
                    XSBmobChatUser user = list.get(0);
                    ChatActivity_.intent(UserActivity.this).targetUser(user).start();
                } else {
//                    ShowLog("onSuccess 查无此人");
                    Toast.makeText(UserActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onStart() {
                super.onStart();
                Toast.makeText(UserActivity.this, "请稍候", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Click(R.id.ll_nick)
    void click1(){
        if(isMyCenter)
            UpdateUsernameActivity_.intent(this).start();
    }

    @Click(R.id.ll_sign)
    void clickSignword(){
        if(isMyCenter)
            UpdateUserSignWordActivity_.intent(this).start();
    }

    @Click(R.id.ll_point)
    void clickPoint(){
        if(isMyCenter){
            final Dialog dialog = new Dialog(this,"关于积分","积分越多等级越高哦~");
            dialog.show();
            dialog.getButtonAccept().setText("知道了");
            dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
    }

    @Click(R.id.photo)
    void clickPhoto(){
        if(!isMyCenter)
            return ;
        dialog = new ChooseImageDialog(this,"选择头像");
        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doUploadAndUpdateUserPhoto();
            }
        });
        dialog.setOnCancelButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        dialog.setOnCameraButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date(System.currentTimeMillis());
                dateTime = date.getTime() + "";
                File f = new File(MatCacheUtils.getCacheDirectory(UserActivity.this, true, "pic") + dateTime);
                if (f.exists()) {
                    f.delete();
                }
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri uri = Uri.fromFile(f);

                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(camera, REQUEST_CODE_CAMERA);
            }
        });
        dialog.setOnAlbumButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date(System.currentTimeMillis());
                dateTime = date.getTime() + "";
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                        "image/*");
                startActivityForResult(intent, REQUEST_CODE_ALBUM);
            }
        });
        dialog.show();
    }

    @Background
    void doUploadAndUpdateUserPhoto(){
        if (targeturl != null && userId != null) {
            UploadEntity uploadEntity = engine.uploadFile("head_photo_" + userId + "_" + dateTime + ".jpg", targeturl);
            if (uploadEntity != null && uploadEntity.getCode() == 0 && userId != null) {
                UpdateBean updateBean = engine.updateUserPhoto(currentUser, Constant.BASE_IMAGE_FILE_URL + uploadEntity.getUrl());
                if(updateBean.getCode() == 0){
                    doGetUserInfo(userId);
                }else{
                    SnackBar snackBar = new SnackBar(this,updateBean.getError()+"","ok",null);
                    snackBar.show();
                }
            }else {
                SnackBar snackBar = new SnackBar(this,"更新头像失败","ok",null);
                snackBar.show();
            }
        }
    }

    @Click(R.id.ll_money)
    void clickMoney(){
        if(!isMyCenter)
            return ;
        final Dialog dialog = new Dialog(this,"关于银两","银两可用于应用内消费");
        dialog.addCancelButton("知道了");
        dialog.show();
        dialog.getButtonAccept().setText("充值");
        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RechargeActivity_.intent(UserActivity.this).start();
            }
        });
    }

    @Click(R.id.ll_loginout)
    void clickLoginout(){
        final Dialog dialog = new Dialog(this,"注销账号","确定要退出当前账号吗？");
        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                engine.logout();
                UserActivity.this.finish();
            }
        });
        dialog.setOnCancelButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getButtonAccept().setText("注销");
    }

    @OnActivityResult(REQUEST_CODE_ALBUM)
    void onResultFromAlbum(int resultCode,Intent data){
        if(resultCode == RESULT_OK){
            String fileName = null;
            if (data != null) {
                Uri originalUri = data.getData();
                ContentResolver cr = getContentResolver();
                Cursor cursor = cr.query(originalUri, null, null, null,
                        null);
                if (cursor.moveToFirst()) {
                    do {
                        fileName = cursor.getString(cursor.getColumnIndex("_data"));
                    } while (cursor.moveToNext());
                }
                Bitmap bitmap = Tools.compressImageFromFile(fileName);
                targeturl = Tools.saveToSdCard(bitmap,genPhotoName(fileName));
                renderDialog(targeturl);
            }
        }
    }

    @OnActivityResult(REQUEST_CODE_CAMERA)
    void onResultFromCamera(int resultCode,Intent data){
        if(resultCode == RESULT_OK){
            String filePath = MatCacheUtils.getCacheDirectory(this, true,
                    "pic") + dateTime;
            File file = new File(filePath);
            if (file.exists()) {
                Bitmap bitmap = Tools.compressImageFromFile(filePath);
                targeturl = Tools.saveToSdCard(bitmap, genPhotoName(filePath));
                renderDialog(targeturl);
            }
        }
    }

    private void renderDialog(String targeturl){
        if(targeturl != null && dialog!= null){
            dialog.changeSecondView(true);
            ImageView preview = dialog.getPreview();
            L.e(L.TEST,"targeturl : "+targeturl);
            Glide.with(this).load(targeturl).error(account_icon).bitmapTransform(new CropCircleTransformation(this)).into(preview);
        }
    }

    private String genPhotoName(String fileName){
        String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
        String filePath = MatCacheUtils.getCacheDirectory(this, true, "pic")
                + dateTime + "_photo."+suffix;
        return filePath;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
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

    @Override
    public void onRefresh() {
        if(!TextUtils.isEmpty(userId)){
            doGetUserInfo(userId);
        }
    }
}
