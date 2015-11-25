package com.xxs.leon.xxs.rest.engine.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxs.leon.xxs.rest.bean.Album;
import com.xxs.leon.xxs.rest.bean.XSUser;
import com.xxs.leon.xxs.rest.bean.request.LoginParams;
import com.xxs.leon.xxs.rest.bean.response.CloudRestEntity;
import com.xxs.leon.xxs.rest.bean.response.HomeAlbumEntity;
import com.xxs.leon.xxs.rest.engine.BaseEngine;
import com.xxs.leon.xxs.rest.engine.CommenEngine;
import com.xxs.leon.xxs.utils.L;

import org.androidannotations.annotations.EBean;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by leon on 15-11-24.
 */
@EBean
public class CommenEngineImpl extends BaseEngine implements CommenEngine{

    @Override
    public List<Album> getHomeAlbums() {
        String keys = "name,price,status,type,cover";
        String where = "{\"status\":1}";
        int status = 1;
        int limit = 10;
        String order = "-updatedAt";
        HomeAlbumEntity results = client.getHomeNewAlbums(keys,where,limit,order);
        return results.getResults();
    }

    @Override
    public XSUser login(LoginParams loginParams) {
        CloudRestEntity entity = client.login(loginParams);
        String jsonString = entity.getResult();
        L.i(L.TEST,jsonString);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            XSUser user = objectMapper.readValue(jsonString, XSUser.class);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
