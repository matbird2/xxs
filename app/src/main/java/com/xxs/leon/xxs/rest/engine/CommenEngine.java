package com.xxs.leon.xxs.rest.engine;

import com.xxs.leon.xxs.rest.bean.Album;
import com.xxs.leon.xxs.rest.bean.Post;
import com.xxs.leon.xxs.rest.bean.UpdateBean;
import com.xxs.leon.xxs.rest.bean.XSUser;
import com.xxs.leon.xxs.rest.bean.request.AddRechargeLogParams;
import com.xxs.leon.xxs.rest.bean.request.LoginParams;
import com.xxs.leon.xxs.rest.bean.request.PayParams;
import com.xxs.leon.xxs.rest.bean.request.ThumbnailParams;
import com.xxs.leon.xxs.rest.bean.response.CloudRestEntity;
import com.xxs.leon.xxs.rest.bean.response.UploadEntity;
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

    /**
     * 从服务端获取用户信息,不包含sessionToken
     * @param objectId
     * @return
     */
    XSUser getUserInfo(String objectId);

    /**
     * 上传文件
     * @param remoteFileName 远程的文件名，带后缀
     * @param filePath 本地文件的具体路劲
     * @return
     */
    UploadEntity uploadFile(String remoteFileName,String filePath);

    /**
     * 更新用户头像信息
     * @param imgUrl
     * @param user  必须是 getLocalUser 的user对象
     * @return
     */
    UpdateBean updateUserPhoto(XSUser user,String imgUrl);

    /**
     * 查询相册详情信息
     * @param objectId
     * @return
     */
    Album getAlbumById(String objectId);

    /**
     * 用户签到
     * @param user
     * @return
     */
    String sendSignPost(XSUser user);

    /**
     * 根据类别获取相册列表
     * @param type
     * @param skip
     * @return
     */
    List<Album> getCategoryAlbum(int type,int skip);

    /**
     * 获取首页Post列表
     * @param skip
     * @return
     */
    List<Post> getHomePostList(int skip);

    /**
     * 获取某个图片的缩略图，节省流量
     * @return 返回缩略图地址
     */
    String getThumbnail(String image,int width,int quality);

    /**
     * 发布文章
     * @param user
     * @param post
     * @return
     */
    String sendPost(XSUser user,Post post);

    /**
     * 获取文章详情
     * @param objectId
     * @return
     */
    Post getPostDetial(String objectId);

    /**
     * 支付
     * @param params
     * @return
     */
    String pay(PayParams params);

    /**
     * 支付成功后添加充值记录
     * @param params
     * @return
     */
    String handlePaySuccess(AddRechargeLogParams params);

    /**
     * 判断用户是否阅读过该相册
     * @return
     */
    boolean hasUserReadAlbumById(String userId,String albumId);

    /**
     * 扣除银两
     * @param user
     * @param cost
     * @return
     */
    String costMoney(XSUser user,int cost);

    /**
     * 添加阅读记录
     * @param user
     * @param albumId
     * @return
     */
    String addReadLog(XSUser user,String albumId);
}
