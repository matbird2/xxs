package com.xxs.leon.xxs.rest.engine;

import android.content.Context;

import com.xxs.leon.xxs.bean.XSBmobChatUser;
import com.xxs.leon.xxs.rest.bean.Album;
import com.xxs.leon.xxs.rest.bean.Comment;
import com.xxs.leon.xxs.rest.bean.Post;
import com.xxs.leon.xxs.rest.bean.UpdateBean;
import com.xxs.leon.xxs.rest.bean.request.AddRechargeLogParams;
import com.xxs.leon.xxs.rest.bean.request.LoginParams;
import com.xxs.leon.xxs.rest.bean.request.PayParams;
import com.xxs.leon.xxs.rest.bean.response.UploadEntity;

import java.util.List;

import cn.bmob.im.bean.BmobChatUser;

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
//    XSUser login(LoginParams loginParams);

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
//    XSUser getCurrentUser();

    /**
     * 注册
     * @param registerParams 参数和登陆使用的参数一致
     * @return
     */
    XSBmobChatUser register(LoginParams registerParams);

    /**
     * 从服务端获取用户信息,不包含sessionToken
     * @param objectId
     * @return
     */
    XSBmobChatUser getUserInfo(String objectId);

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
    UpdateBean updateUserPhoto(BmobChatUser user,String imgUrl);

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
    String sendSignPost(BmobChatUser user);

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
    String sendPost(BmobChatUser user,Post post);

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
    boolean hasUserReadAlbumByUserId(String userId,String albumId);

    /**
     * 判断设备是否阅读过该相册
     * @return
     */
    boolean hasUserReadAlbumByInstallationId(String installationId,String albumId);

    /**
     * 扣除银两
     * @param user
     * @param cost
     * @return
     */
    String costMoney(BmobChatUser user,int cost);

    /**
     * 添加阅读记录
     * @param user
     * @param albumId
     * @return
     */
    String addReadLog(Context context,BmobChatUser user,String albumId);

    /**
     * 获取首页头条公告
     * @return
     */
    Post getTopPost();

    /**
     * 更新用户名
     * @param user
     * @param username
     * @return
     */
    UpdateBean updateUserName(BmobChatUser user,String username);

    /**
     * 更新用户签名
     * @param user
     * @param signword
     * @return
     */
    UpdateBean updateUserSignWord(BmobChatUser user,String signword);

    /**
     * 搜索
     * @param keyword
     * @return
     */
    List<Album> search(String keyword);

    /**
     * 获取推荐相册
     * @param key1
     * @param key2
     * @return
     */
    List<Album> getRecommendAlbumList(String key1,String key2,int type);

    /**
     * 意见反馈
     * @param content
     * @param user
     * @return
     */
    String feedBack(String content,BmobChatUser user);

    /**
     * 求书
     * @param content
     * @param user
     * @return
     */
    String seekBook(String content,BmobChatUser user);

    /**
     * 纠错
     * @param content
     * @param user
     * @return
     */
    String correct(String content,String albumId,BmobChatUser user);

    /**
     * 纠错
     * @return
     */
    String sendComment(String content,String albumId,String postId,String parentId,BmobChatUser user);


    /**
     * 获取评论列表
     * @param skip
     * @param albumId
     * @param postId
     * @return
     */
    List<Comment> getCommentList(int skip,String albumId,String postId);
}
