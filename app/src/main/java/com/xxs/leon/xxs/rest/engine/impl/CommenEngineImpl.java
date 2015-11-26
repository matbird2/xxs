package com.xxs.leon.xxs.rest.engine.impl;

import android.text.TextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxs.leon.xxs.constant.Constant;
import com.xxs.leon.xxs.rest.bean.Album;
import com.xxs.leon.xxs.rest.bean.XSUser;
import com.xxs.leon.xxs.rest.bean.request.LoginParams;
import com.xxs.leon.xxs.rest.bean.response.CloudRestEntity;
import com.xxs.leon.xxs.rest.bean.response.HomeAlbumEntity;
import com.xxs.leon.xxs.rest.bean.response.UploadEntity;
import com.xxs.leon.xxs.rest.engine.BaseEngine;
import com.xxs.leon.xxs.rest.engine.CommenEngine;
import com.xxs.leon.xxs.utils.AESEncryCypher;
import com.xxs.leon.xxs.utils.L;
import com.xxs.leon.xxs.utils.Tools;
import com.xxs.leon.xxs.utils.XXSPref_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by leon on 15-11-24.
 */
@EBean
public class CommenEngineImpl extends BaseEngine implements CommenEngine{

    @Pref
    XXSPref_ xxsPref;

    @Override
    public List<Album> getHomeAlbums() {
        String keys = "name,price,status,type,cover";
        String where = "{\"status\":1}";
        int status = 1;
        int limit = 10;
        String order = "-updatedAt";
        HomeAlbumEntity results = client.getHomeNewAlbums(keys, where, limit, order);
        return results.getResults();
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
    public void uploadFile(String remoteFileName, String filePath) {
        client.setHeader("Content-Type","image/jpeg");
        File file = new File(filePath);
        byte[] fileBytes = file2BetyArray(file);
        if(fileBytes != null){
            UploadEntity entity = client.uploadFile(fileBytes);
            L.i(L.TEST,"url:"+entity.getUrl());
        }
    }

    /**
     * 处理用户数据，以字符串的形式保存本地，返回XSUser
     * @param jsonString
     * @param isLocal 是否是本地数据，如果是，就不需要再次保存到本地
     * @return
     */
    private XSUser processUserJsonString(String jsonString,boolean isLocal){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
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

    public static byte[] file2BetyArray(File file)
    {
        /*if (!file.exists()) {
            return null;
        }
        FileInputStream stream = null;
        ByteArrayOutputStream out = null;
        try {
            stream = new FileInputStream(file);
            out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1) {
                out.write(b, 0, n);
            }
            return out.toByteArray();// 此方法大文件OutOfMemory
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            try {
                stream.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;*/

        if (!file.exists()) {
            return null;
        }
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
}
