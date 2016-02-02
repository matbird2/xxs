package com.xxs.leon.xxs.rest.engine.impl;

import android.content.Context;
import android.text.TextUtils;

import com.xxs.leon.xxs.bean.XSBmobChatUser;
import com.xxs.leon.xxs.constant.Constant;
import com.xxs.leon.xxs.rest.bean.Album;
import com.xxs.leon.xxs.rest.bean.Comment;
import com.xxs.leon.xxs.rest.bean.Post;
import com.xxs.leon.xxs.rest.bean.UpdateBean;
import com.xxs.leon.xxs.rest.bean.request.AddReadLogParams;
import com.xxs.leon.xxs.rest.bean.request.AddRechargeLogParams;
import com.xxs.leon.xxs.rest.bean.request.CommentParams;
import com.xxs.leon.xxs.rest.bean.request.CostMoneyParams;
import com.xxs.leon.xxs.rest.bean.request.LoginParams;
import com.xxs.leon.xxs.rest.bean.request.PayParams;
import com.xxs.leon.xxs.rest.bean.request.ReadCountEntity;
import com.xxs.leon.xxs.rest.bean.request.SendPostParams;
import com.xxs.leon.xxs.rest.bean.request.ThumbnailParams;
import com.xxs.leon.xxs.rest.bean.request.UpdateUserNameParams;
import com.xxs.leon.xxs.rest.bean.request.UpdateUserPhotoParams;
import com.xxs.leon.xxs.rest.bean.request.UpdateUserSignWordParams;
import com.xxs.leon.xxs.rest.bean.request.UserSessionParams;
import com.xxs.leon.xxs.rest.bean.response.AlbumListEntity;
import com.xxs.leon.xxs.rest.bean.response.CloudRestEntity;
import com.xxs.leon.xxs.rest.bean.response.CommentListEntity;
import com.xxs.leon.xxs.rest.bean.response.HomePostListEntity;
import com.xxs.leon.xxs.rest.bean.response.PayEntity;
import com.xxs.leon.xxs.rest.bean.response.ThumbnailEntity;
import com.xxs.leon.xxs.rest.bean.response.UploadEntity;
import com.xxs.leon.xxs.rest.engine.BaseEngine;
import com.xxs.leon.xxs.rest.engine.CommenEngine;
import com.xxs.leon.xxs.utils.L;
import com.xxs.leon.xxs.utils.Tools;
import com.xxs.leon.xxs.utils.XXSPref_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.BmobInstallation;

/**
 * Created by leon on 15-11-24.
 */
@EBean
public class CommenEngineImpl extends BaseEngine implements CommenEngine{

    @Pref
    XXSPref_ xxsPref;

    public List<Album> getHomeAlbums() {
        String keys = "name,price,status,type,cover,descri";
        String where = "{\"status\":1}";
        int status = 1;
        int limit = 10;
        String order = "-updatedAt";
        AlbumListEntity results = client.getHomeNewAlbums(keys, where, limit, order);
        L.w(L.TEST, "getHomeAlbums :" + (results == null));
        return results != null ? results.getResults() : null;
    }

//    @Override
//    public XSUser login(LoginParams loginParams) {
//        CloudRestEntity entity = client.login(loginParams);
//        String jsonString = entity.getResult();
//        L.i(L.TEST, jsonString);
//        return processUserJsonString(jsonString, false);
//    }


    @Override
    public void logout() {
        xxsPref.userInfo().put("");
        BmobUserManager.getInstance(context).logout();
    }

    /*@Override
    public XSUser getCurrentUser() {
        String jsonString = xxsPref.userInfo().get();
        L.i(L.TEST, "current string:" + jsonString);
        if(TextUtils.isEmpty(jsonString)){
            return null;
        }else{
            jsonString = Tools.decode_decrypt(jsonString);
            L.i(L.TEST, jsonString);
            return processUserJsonString(jsonString,true);
        }
    }*/

    @Override
    public XSBmobChatUser getUserInfo(String objectId) {
        return client.getUserInfo(objectId);
    }

    @Override
    public XSBmobChatUser register(LoginParams registerParams) {
        CloudRestEntity entity = client.register(registerParams);
        String jsonString = entity.getResult();
        try {
            XSBmobChatUser user = objectMapper.readValue(jsonString, XSBmobChatUser.class);
            return user;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UploadEntity uploadFile(String remoteFileName, String filePath) {
        client.setHeader("Content-Type", "image/jpeg");
        File file = new File(filePath);
        byte[] fileBytes = Tools.file2BetyArray(file);
//        L.i(L.TEST,"fileBytes length = "+fileBytes.length+"   file length = "+file.length());
        if(fileBytes.length > 0 && fileBytes.length == file.length()){
            UploadEntity entity = client.uploadFile(fileBytes,remoteFileName);
//            L.i(L.TEST,"url:"+entity.getUrl());
            L.w(L.TEST, "uploadFile :" + (entity == null));
            return entity;
        }
        return null;
    }

    @Override
    public UpdateBean updateUserPhoto(BmobChatUser user,String imgUrl) {
        UpdateUserPhotoParams params = new UpdateUserPhotoParams();
        params.setSessionToken(user.getSessionToken());
        params.setObjectId(user.getObjectId());
        params.setImgUrl(imgUrl);
        CloudRestEntity entity = client.updateUserPhoto(params);
        L.w(L.TEST, "updateUserPhoto :" + (entity == null));
        UpdateBean bean = null;
        try {
            L.i(L.TEST, "updateUserPhoto:" + entity.getResult());
            bean = objectMapper.readValue(entity.getResult(),UpdateBean.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bean;
    }

    @Override
    public Album getAlbumById(String objectId) {
        return client.getAlbumById(objectId);
    }

    @Override
    public String sendSignPost(BmobChatUser user) {
        UserSessionParams params = new UserSessionParams();
        params.setObjectId(user.getObjectId());
        params.setSessionToken(user.getSessionToken());
        CloudRestEntity entity = client.sendSignPost(params);
        L.w(L.TEST, "sendSignPost :" + (entity == null));
        if(entity != null){
            return entity.getResult();
        }else{
            return "";
        }
    }

    @Override
    public List<Album> getCategoryAlbum(int type, int skip) {
        String keys = "name,price,status,type,cover";
        int status = 1;
        String where = "{\"status\":"+status+",\"type\":"+type+"}";
        String order = "-updatedAt";
        int limit = 10;
        AlbumListEntity entity = client.getAlbumsByType(keys, where, limit, skip, order);
        L.w(L.TEST, "getCategoryAlbum :" + (entity == null));
        return entity != null ? entity.getResults() : null;
    }

    @Override
    public List<Post> getHomePostList(int skip) {
        String keys = "excerpt,title,imgs,user,comment_count";
        int status = 0;
        String where = "{\"status\":"+status+" }";
        String order = "-createdAt";
        int limit = 10;
        String include = "user[username|photo]";
        HomePostListEntity entity = client.getHomePostList(keys, where, limit, skip, order, include);
        L.w(L.TEST, "getHomePostList :" + (entity == null));
        return entity != null ? entity.getResults() : null;
    }

    @Override
    public String getThumbnail(String image,int width,int quality) {
        ThumbnailParams params = new ThumbnailParams();
        params.setImage(image);
        params.setQuality(quality);
        params.setWidth(width);
        params.setMode(0);
        ThumbnailEntity entity = client.getThumbnail(params);
        L.w(L.TEST, "getThumbnail :" + (entity == null));
        return entity != null ? Constant.BASE_IMAGE_FILE_URL+entity.getUrl() : "";
    }

    @Override
    public String sendPost(BmobChatUser user, Post post) {
        SendPostParams params = new SendPostParams();
        params.setObjectId(user.getObjectId());
        params.setSessionToken(user.getSessionToken());
        params.setContent(post.getContent());
        params.setTitle(post.getTitle());
        params.setExcerpt(post.getExcerpt());
        CloudRestEntity entity = client.sendPost(params);
        L.w(L.TEST, "sendPost :" + (entity == null));
        if(entity != null){
            return entity.getResult();
        }else{
            return "";
        }
    }

    @Override
    public Post getPostDetial(String objectId) {
        String keys = "excerpt,title,imgs,linked_url,content,status,classic,user,comment_count";
        String include = "user[username|photo]";
        Post post = client.getPostDetailById(objectId, keys, include);
        L.w(L.TEST, "getPostDetial :" + (post == null));
        return post != null ? post : null;
    }

    @Override
    public String pay(PayParams params) {
        PayEntity entity = client.webPay(params);
        L.w(L.TEST, "pay :" + (entity == null));
        String result = null;
        if(entity != null && entity.getCode() == 0){
            return entity.getHtml();
        }else{
            return "#";
        }
    }

    @Override
    public String handlePaySuccess(AddRechargeLogParams params) {
        CloudRestEntity entity = client.addRechargeLog(params);
        L.w(L.TEST, "pay :" + (entity == null));
        return entity != null ? entity.getResult() : "充值出现问题，请提交问题反馈";
    }

    @Override
    public boolean hasUserReadAlbumByUserId(String userId, String albumId) {
        String where = "{\"user\":{\"__type\":\"Pointer\",\"className\":\"_User\",\"objectId\":\""+userId+"\"},\"albumId\":\""+albumId+"\"}";
        int count = 1;
        int limit = 0;
        ReadCountEntity entity = client.getReadLogCount(where, limit, count);
        L.w(L.TEST,"hasUserReadAlbumByUserId :"+(entity == null));
        if(entity == null)
            return false;
        return entity.getCount() > 0 ? true : false;
    }

    @Override
    public boolean hasUserReadAlbumByInstallationId(String installationId, String albumId) {
        String where = "{\"installationId\":\""+installationId+"\",\"albumId\":\""+albumId+"\"}";
        int count = 1;
        int limit = 0;
        ReadCountEntity entity = client.getReadLogCount(where, limit, count);
        L.w(L.TEST,"hasUserReadAlbumByInstallationId :"+(entity == null));
        if(entity == null)
            return false;
        return entity.getCount() > 0 ? true : false;
    }

    @Override
    public String costMoney(BmobChatUser user, int cost) {
        CostMoneyParams params = new CostMoneyParams();
        params.setObjectId(user.getObjectId());
        params.setSessionToken(user.getSessionToken());
        params.setCost(cost);
        CloudRestEntity entity = client.costMoney(params);
        L.w(L.TEST, "costMoney :" + (entity == null));
        return entity != null ? entity.getResult() : null;
    }

    @Override
    public String addReadLog(Context context,BmobChatUser user, String albumId) {
        AddReadLogParams params = new AddReadLogParams();
        if(user != null){
            params.setObjectId(user.getObjectId());
        }
        params.setAlbumId(albumId);
        if(BmobInstallation.getCurrentInstallation(context) != null){
            params.setInsId(BmobInstallation.getInstallationId(context));
        }
        CloudRestEntity entity = client.addReadLog(params);
        L.w(L.TEST, "addReadLog :" + (entity == null));
        return entity != null ? entity.getResult() : null;
    }

    @Override
    public Post getTopPost() {
        String keys = "excerpt,title,linked_url,user";
        String where = "{\"status\":0,\"classic\":2}";
        String order = "-createdAt";
        int limit = 1;
        int skip = 0;
        String include = "user[username|photo]";
        HomePostListEntity entity = client.getHomePostList(keys, where, limit, skip, order, include);
        L.w(L.TEST, "getTopPost :" + (entity == null));
        return entity != null && entity.getResults().size()>0 ? entity.getResults().get(0) : null;
    }

    @Override
    public UpdateBean updateUserName(BmobChatUser user, String username) {
        UpdateUserNameParams params = new UpdateUserNameParams();
        params.setSessionToken(user.getSessionToken());
        params.setObjectId(user.getObjectId());
        params.setUsername(username);
        CloudRestEntity entity = client.updateUserName(params);
        L.w(L.TEST, "updateUserName :" + (entity == null));
        UpdateBean bean = null;
        try {
            L.i(L.TEST, "updateUserName:" + entity.getResult());
            bean = objectMapper.readValue(entity.getResult(),UpdateBean.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bean;
    }

    @Override
    public UpdateBean updateUserSignWord(BmobChatUser user, String signword) {
        UpdateUserSignWordParams params = new UpdateUserSignWordParams();
        params.setSessionToken(user.getSessionToken());
        params.setObjectId(user.getObjectId());
        params.setSignword(signword);
        CloudRestEntity entity = client.updateUserSignWord(params);
        L.w(L.TEST, "updateUserSignWord :" + (entity == null));
        UpdateBean bean = null;
        try {
            L.i(L.TEST, "updateUserSignWord:" + entity.getResult());
            bean = objectMapper.readValue(entity.getResult(),UpdateBean.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bean;
    }

    @Override
    public List<Album> search(String keyword) {
        String keys = "name";
        String where = "{\"status\":1,\"name\":{\"$regex\":\".*"+keyword+".*\"}}";
        int limit = 10;
        String order = "-updatedAt";
        AlbumListEntity results = client.search(keys, where, limit, order);
        L.w(L.TEST,"search :"+(results == null));
        return results != null ? results.getResults() : null;
    }

    @Override
    public List<Album> getRecommendAlbumList(String key1, String key2,int type) {
        String keys = "name,cover";
        String where = "{\"status\":1,\"$or\":[{\"name\":{\"$regex\":\".*"+key1+".*\"}},{\"name\":{\"$regex\":\".*"+key2+".*\"}},{\"type\":"+type+"}]}";
        int limit = 10;
        String order = "-updatedAt";
        AlbumListEntity results = client.search(keys, where, limit, order);
        L.w(L.TEST,"getRecommendAlbumList :"+(results == null));
        return results != null ? results.getResults() : null;
    }

    @Override
    public String feedBack(String content, BmobChatUser user) {
        CommentParams params = new CommentParams();
        params.setContent(content);
        if(user != null)
            params.setUserId(user.getObjectId());
        CloudRestEntity entity = client.sendFeedBack(params);
        L.w(L.TEST, "feedBack :" + (entity == null));
        return entity != null  ? entity.getResult() : "error";
    }

    @Override
    public String seekBook(String content, BmobChatUser user) {
        CommentParams params = new CommentParams();
        params.setContent(content);
        if(user != null)
            params.setUserId(user.getObjectId());
        CloudRestEntity entity = client.sendSeekBook(params);
        L.w(L.TEST, "seekBook :" + (entity == null));
        return entity != null  ? entity.getResult() : "error";
    }

    @Override
    public String correct(String content, String albumId, BmobChatUser user) {
        CommentParams params = new CommentParams();
        params.setContent(content);
        if(user != null)
            params.setUserId(user.getObjectId());
        params.setAlbumId(albumId);
        CloudRestEntity entity = client.sendCorrect(params);
        L.w(L.TEST, "correct :" + (entity == null));
        return entity != null  ? entity.getResult() : "error";
    }

    @Override
    public String sendComment(String content,String albumId,String postId,String parentId,BmobChatUser user) {
        CommentParams params = new CommentParams();
        params.setContent(content);
        if(user != null)
            params.setUserId(user.getObjectId());
        if(albumId != null)
            params.setAlbumId(albumId);
        if(parentId != null)
            params.setParentId(parentId);
        if(postId != null)
            params.setPostId(postId);
        CloudRestEntity entity = client.sendComment(params);
        L.w(L.TEST, "sendComment :" + (entity == null));
        return entity != null  ? entity.getResult() : "error";
    }

    @Override
    public List<Comment> getCommentList(int skip, String albumId, String postId) {
        String where ;
        if(albumId == null && postId != null){
            where = "{\"status\":1,\"type\":0,\"post\":{\"__type\":\"Pointer\",\"className\":\"Post\",\"objectId\":\""+postId+"\"}}";
        }else{
            where = "{\"status\":1,\"type\":0,\"album\":{\"__type\":\"Pointer\",\"className\":\"Album\",\"objectId\":\""+albumId+"\"}}";
        }
        String keys = "content,user,parent";
        String order = "-createdAt";
        int limit = 10;
        String include = "user[username|photo],parent[content].user[username]";
        CommentListEntity entity = client.getCommentList(keys, where, limit, skip, order, include);
        L.w(L.TEST, "getCommentList :" + (entity == null));
        return entity != null ? entity.getResults() : null;
    }

    /**
     * 处理用户数据，以字符串的形式保存本地，返回XSUser
     * @param jsonString
     * @param isLocal 是否是本地数据，如果是，就不需要再次保存到本地
     * @return
     */
    /*private XSUser processUserJsonString(String jsonString,boolean isLocal){
        try {
            XSUser user = objectMapper.readValue(jsonString, XSUser.class);
            if (!isLocal && user.getCode() == 0){
                jsonString = Tools.encrypt_encode(jsonString);
                xxsPref.userInfo().put(jsonString);
            }
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

}
