package com.xxs.leon.xxs.activity;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.widgets.SnackBar;
import com.umeng.analytics.MobclickAgent;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.bean.XSBmobChatUser;
import com.xxs.leon.xxs.rest.engine.impl.CommenEngineImpl;
import com.xxs.leon.xxs.utils.L;
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

import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;

/**
 * Created by maliang on 16/1/18.
 */
@EActivity(R.layout.ui_comment_dialog)
public class CommentDialogActivity extends Activity{

    @ViewById
    EditText et_content;
    @ViewById
    TextView tv_title;
    @Extra
    int commentType;
    @Extra
    int correctPosition;
    @Extra
    String postId;
    @Extra
    String parentId;
    @Extra
    String albumId;
    @Bean
    CommenEngineImpl engine;
    BmobChatUser currentUser;

    @AfterInject
    void init(){
        L.i(L.TEST, "CommentDialogActivity postId => " + postId);
        currentUser = BmobUserManager.getInstance(this).getCurrentUser();
    }

    @AfterViews
    void initViews(){
        switch (commentType){
            case 3:
                tv_title.setText("意见反馈");
                et_content.setHint("请输入反馈内容");
                break;
            case 2:
                tv_title.setText("纠错");
                et_content.setHint("请输入错误详情，例如：图片加载失败，图片残缺等。谢谢~");
                break;
            case 1:
                tv_title.setText("求书");
                et_content.setHint("请输入连环画的描述，越详细越好");
                break;
            case 0:
                tv_title.setText("评论");
                et_content.setHint("请输入评论内容");
                break;
        }
    }

    @Click(R.id.tv_cancel)
    void clickCancel(){
        this.finish();
    }

    @Click(R.id.tv_send)
    void clickSend(){
        String content = et_content.getText().toString();
        if(content == null || TextUtils.isEmpty(content.trim())){
            SnackBar snackBar = new SnackBar(this,"内容不能为空.","ok",null);
            snackBar.show();
            return ;
        }

        switch (commentType){
            case 3:
                String info = Tools.getDeviceInfo(this);
                content = info+"内容："+content;
                doFeedBack(content);
                break;
            case 2:
                content = "错误位置:"+(correctPosition+1)+"\n"+content;
                doCorrect(content);
                break;
            case 1:
                if(currentUser == null){
                    Toast.makeText(this,"登录之后才能求书哦",Toast.LENGTH_SHORT).show();
                    LoginActivity_.intent(this).start();
                }else{
                    doSeekBook(content);
                }

            case 0:
                if(currentUser == null){
                    Toast.makeText(this,"登录之后才能评论哦",Toast.LENGTH_SHORT).show();
                    LoginActivity_.intent(this).start();
                }else{
                    doComment(content);
                }
                break;
        }
    }

    @Background
    void doComment(String content){
        String result = engine.sendComment(content, albumId, postId, parentId, currentUser);
        afterComment(result);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void afterComment(String result){
        if("success".equals(result)){
            Toast.makeText(this,"评论成功~",Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(this,"出现错误~",Toast.LENGTH_SHORT).show();
        }
    }

    @Background
    void doCorrect(String content){
        String result = engine.correct(content, albumId, currentUser);
        afterCorrect(result);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void afterCorrect(String result){
        if("success".equals(result)){
            Toast.makeText(this,"谢谢您提供的纠错信息~",Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(this,"出现错误~",Toast.LENGTH_SHORT).show();
        }
    }

    @Background
    void doSeekBook(String content){
        String result = engine.seekBook(content, currentUser);
        afterSeekBook(result);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void afterSeekBook(String result){
        if("success".equals(result)){
            Toast.makeText(this,"求书成功,小小书将会尽快为你找到连环画~",Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(this,"出现错误~",Toast.LENGTH_SHORT).show();
        }
    }

    @Background
    void doFeedBack(String content){
        String result = engine.feedBack(content, currentUser);
        afterFeedBack(result);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void afterFeedBack(String result){
        if("success".equals(result)){
            Toast.makeText(this,"反馈成功，感谢您的建议",Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(this,"反馈失败",Toast.LENGTH_SHORT).show();
        }
    }

    @Click(R.id.rl_content)
    void clickContainer(){
        finish();
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
