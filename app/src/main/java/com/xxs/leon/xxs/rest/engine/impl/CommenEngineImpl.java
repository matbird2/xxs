package com.xxs.leon.xxs.rest.engine.impl;

import com.xxs.leon.xxs.rest.bean.Album;
import com.xxs.leon.xxs.rest.bean.response.HomeAlbumEntity;
import com.xxs.leon.xxs.rest.engine.BaseEngine;
import com.xxs.leon.xxs.rest.engine.CommenEngine;

import org.androidannotations.annotations.EBean;

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
}
