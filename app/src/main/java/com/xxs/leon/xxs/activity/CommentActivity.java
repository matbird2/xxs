package com.xxs.leon.xxs.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gc.materialdesign.widgets.SnackBar;
import com.kogitune.activity_transition.ActivityTransition;
import com.kogitune.activity_transition.ExitActivityTransition;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.constant.Constant;
import com.xxs.leon.xxs.rest.bean.UpdateBean;
import com.xxs.leon.xxs.rest.bean.XSUser;
import com.xxs.leon.xxs.rest.bean.request.TestParams;
import com.xxs.leon.xxs.rest.bean.response.AlbumListEntity;
import com.xxs.leon.xxs.rest.bean.response.TestEntity;
import com.xxs.leon.xxs.rest.bean.response.UploadEntity;
import com.xxs.leon.xxs.rest.client.CommenRestClient;
import com.xxs.leon.xxs.rest.engine.impl.CommenEngineImpl;
import com.xxs.leon.xxs.rest.handler.CommenRestErrorHandler;
import com.xxs.leon.xxs.ui.ChooseImageDialog;
import com.xxs.leon.xxs.utils.L;
import com.xxs.leon.xxs.utils.MatCacheUtils;
import com.xxs.leon.xxs.utils.Tools;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.richeditor.RichEditor;

/**
 * Created by leon on 15-11-22.
 */
@EActivity(R.layout.activity_comment)
public class CommentActivity extends AppCompatActivity{
    private static final int REQUEST_CODE_ALBUM = 1;
    private static final int REQUEST_CODE_CAMERA = 2;

    @InstanceState
    protected Bundle savedInstanceState;
    @ViewById
    protected FloatingActionButton fab;
    @ViewById
    protected Toolbar toolbar;
    @ViewById
    protected RichEditor editor;

    protected ExitActivityTransition exitTransition;

    ChooseImageDialog dialog;
    String dateTime;
    String targeturl = null;
    XSUser currentUser;

    @Bean
    CommenEngineImpl engine;

    @AfterInject
    void init(){
        currentUser = engine.getCurrentUser();
    }

    @AfterViews
    void initViews(){
        initToolbar();
        exitTransition = ActivityTransition.with(getIntent()).to(fab).start(savedInstanceState);
    }

    private void initToolbar(){
        toolbar.setTitle("发表文章");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initRicheditor();
    }

    private void initRicheditor(){
        editor.setEditorHeight(300);
        editor.setEditorFontSize(14);
        editor.setEditorFontColor(getResources().getColor(R.color.text_color_87_black));
        editor.setPadding(10, 10, 10, 10);
        editor.setPlaceholder("请输入正文内容...");
    }

    @Click(R.id.action_undo)
    void clickUndo(){
        editor.undo();
    }

    @Click(R.id.action_redo)
    void clickRedo(){
        editor.redo();
    }

    boolean isBold = false;
    @Click(R.id.action_bold)
    void clickBold(View v){
        editor.setBold();
        isBold = !isBold;
        if(isBold){
            v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }else{
            v.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
    }

    boolean isItalic = false;
    @Click(R.id.action_italic)
    void clickItalic(View v){
        editor.setItalic();
        isItalic = !isItalic;
        if(isItalic){
            v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }else{
            v.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
    }

    boolean isStrike = false;
    @Click(R.id.action_strikethrough)
    void clickStrike(View v){
        editor.setStrikeThrough();
        isStrike = !isStrike;
        if(isStrike){
            v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }else{
            v.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
    }

    boolean isUnderline = false;
    @Click(R.id.action_underline)
    void clickUnderLine(View v){
        editor.setUnderline();
        isUnderline = !isUnderline;
        if(isUnderline){
            v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }else{
            v.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
    }

    @Click(R.id.action_heading1)
    void clickHeading1(){
        editor.setHeading(1);
    }

    @Click(R.id.action_heading2)
    void clickHeading2(){
        editor.setHeading(2);
    }

    @Click(R.id.action_heading3)
    void clickHeading3(){
        editor.setHeading(3);
    }

    @Click(R.id.action_heading4)
    void clickHeading4(){
        editor.setHeading(4);
    }

    @Click(R.id.action_heading5)
    void clickHeading5(){
        editor.setHeading(5);
    }

    @Click(R.id.action_heading6)
    void clickHeading6(){
        editor.setHeading(6);
    }

    boolean isChangeTextColor = false;
    @Click(R.id.action_txt_color)
    void clickTextColor(View v){
        editor.setTextColor(isChangeTextColor ? getResources().getColor(R.color.colorPrimaryDark) : getResources().getColor(R.color.text_color_87_black));
        isChangeTextColor = !isChangeTextColor;
    }

    boolean isChangeBackgrounColor = false;
    @Click(R.id.action_bg_color)
    void clickBgColor(View v){
        editor.setTextBackgroundColor(isChangeBackgrounColor ? getResources().getColor(R.color.colorAccent) : Color.TRANSPARENT);
        isChangeBackgrounColor = !isChangeBackgrounColor;
    }

    @Click(R.id.action_insert_image)
    void clickInsertImage(View v){
        dialog = new ChooseImageDialog(this,"插入图片");
        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doUploadImage();
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
                File f = new File(MatCacheUtils.getCacheDirectory(CommentActivity.this, true, "pic") + dateTime);
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
    void doUploadImage(){
        if (targeturl != null && currentUser != null) {
            UploadEntity uploadEntity = engine.uploadFile("post_photo_" + currentUser.getObjectId() + "_" + dateTime + ".jpg", targeturl);
            if (uploadEntity != null && uploadEntity.getCode() == 0 && currentUser != null) {
                String imageUrl = engine.getThumbnail(Constant.BASE_IMAGE_FILE_URL+uploadEntity.getUrl(),200);
                L.i(L.TEST,"imageUrl :"+imageUrl);
                afterUploadImage(imageUrl);
            }else {
                SnackBar snackBar = new SnackBar(this,uploadEntity.getError()+"","ok",null);
                snackBar.show();
            }
        }
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void afterUploadImage(String imageUrl){
        if(dialog != null)
            dialog.dismiss();
        editor.insertImage(imageUrl,"xxs");
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
                targeturl = Tools.saveToSdCard(bitmap,genPhotoName());
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
                targeturl = Tools.saveToSdCard(bitmap, genPhotoName());
                renderDialog(targeturl);
            }
        }
    }

    private void renderDialog(String targeturl){
        if(targeturl != null && dialog!= null){
            dialog.changeSecondView(true);
            ImageView preview = dialog.getPreview();
            Glide.with(this).load(targeturl).into(preview);
        }
    }

    private String genPhotoName(){
        String filePath = MatCacheUtils.getCacheDirectory(this, true, "pic")
                + dateTime + "_photo.jpg";
        return filePath;
    }

    @Override
    public void onBackPressed() {
        exitTransition.exit(this);
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
}
