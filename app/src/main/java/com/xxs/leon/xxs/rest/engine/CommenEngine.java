package com.xxs.leon.xxs.rest.engine;

import com.xxs.leon.xxs.rest.bean.Album;
import com.xxs.leon.xxs.rest.bean.XSUser;
import com.xxs.leon.xxs.rest.bean.request.LoginParams;

import java.util.List;

/**
 * Created by leon on 15-11-24.
 */
public interface CommenEngine {

    /**
     * 获取首页最新小书，默认十条
     * @return
     */
    List<Album> getHomeAlbums();

    /**
     * 登录
     * @param loginParams  登录参数：包括用户名和密码
     * @return
     */
    XSUser login(LoginParams loginParams);
}
