package com.xxs.leon.xxs.rest.engine.impl;

import android.text.TextUtils;

import com.xxs.leon.xxs.rest.bean.Album;
import com.xxs.leon.xxs.rest.bean.UpdateBean;
import com.xxs.leon.xxs.rest.bean.XSUser;
import com.xxs.leon.xxs.rest.bean.request.LoginParams;
import com.xxs.leon.xxs.rest.bean.request.UpdateUserPhotoParams;
import com.xxs.leon.xxs.rest.bean.response.CloudRestEntity;
import com.xxs.leon.xxs.rest.bean.response.HomeAlbumEntity;
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

/**
 * Created by leon on 15-11-24.
 */
@EBean
public class CommenEngineImpl extends BaseEngine implements CommenEngine{

    @Pref
    XXSPref_ xxsPref;

    public List<Album> getHomeAlbums() {
        String keys = "name,price,status,type,cover";
        String where = "{\"status\":1}";
        int status = 1;
        int limit = 10;
        String order = "-updatedAt";
        HomeAlbumEntity results = client.getHomeNewAlbums(keys, where, limit, order);
        return results != null ? results.getResults() : null;
    }

    @Override
    public XSUser login(LoginParams loginParams) {
        CloudRestEntity entity = client.login(loginParams);
        String jsonString = entity.getResult();
        L.i(L.TEST, jsonString);
        return processUserJsonString(jsonString, false);
    }


    @Override
    public void logout() {
        xxsPref.userInfo().put("");
    }

    @Override
    public XSUser getCurrentUser() {
        String jsonString = xxsPref.userInfo().get();
        L.i(L.TEST, "current string:" + jsonString);
        if(TextUtils.isEmpty(jsonString)){
            return null;
        }else{
            jsonString = Tools.decode_decrypt(jsonString);
            L.i(L.TEST,jsonString);
            return processUserJsonString(jsonString,true);
        }
    }

    @Override
    public XSUser register(LoginParams registerParams) {
        CloudRestEntity entity = client.register(registerParams);
        String jsonString = entity.getResult();
        return processUserJsonString(jsonString, false);
    }

    @Override
    public XSUser getUserInfo(String objectId) {
        return client.getUserInfo(objectId);
    }

    @Override
    public UploadEntity uploadFile(String remoteFileName, String filePath) {
        client.setHeader("Content-Type","image/jpeg");
        File file = new File(filePath);
        byte[] fileBytes = Tools.file2BetyArray(file);
        L.i(L.TEST,"fileBytes length = "+fileBytes.length+"   file length = "+file.length());
        if(fileBytes.length > 0 && fileBytes.length == file.length()){
            UploadEntity entity = client.uploadFile(fileBytes,remoteFileName);
            L.i(L.TEST,"url:"+entity.getUrl());
            return entity;
        }
        return null;
    }

    @Override
    public UpdateBean updateUserPhoto(XSUser user,String imgUrl) {
        UpdateUserPhotoParams params = new UpdateUserPhotoParams();
        params.setSessionToken(user.getSessionToken());
        params.setObjectId(user.getObjectId());
        params.setImgUrl(imgUrl);
        CloudRestEntity entity = client.updateUserPhoto(params);
        UpdateBean bean = null;
        try {
            L.i(L.TEST,"updateUserPhoto:"+entity.getResult());
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

    /**
     * 处理用户数据，以字符串的形式保存本地，返回XSUser
     * @param jsonString
     * @param isLocal 是否是本地数据，如果是，就不需要再次保存到本地
     * @return
     */
    private XSUser processUserJsonString(String jsonString,boolean isLocal){
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
    }

}
