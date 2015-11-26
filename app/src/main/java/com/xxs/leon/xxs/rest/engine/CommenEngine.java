package com.xxs.leon.xxs.rest.engine;

import com.xxs.leon.xxs.rest.bean.Album;
import com.xxs.leon.xxs.rest.bean.XSUser;
import com.xxs.leon.xxs.rest.bean.request.LoginParams;
import com.xxs.leon.xxs.rest.engine.callback.DataEngineCallback;

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

    /**
     * No need background.
     * 注销
     */
    void logout();

    /**
     * No need background.
     * 获取本地user对象
     * @return
     */
    XSUser getCurrentUser();

    /**
     * 注册
     * @param registerParams 参数和登陆使用的参数一致
     * @return
     */
    XSUser register(LoginParams registerParams);
}
