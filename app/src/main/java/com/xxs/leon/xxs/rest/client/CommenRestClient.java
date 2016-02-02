package com.xxs.leon.xxs.rest.client;

import com.xxs.leon.xxs.bean.XSBmobChatUser;
import com.xxs.leon.xxs.rest.bean.Album;
import com.xxs.leon.xxs.rest.bean.BaseBean;
import com.xxs.leon.xxs.rest.bean.request.AddReadLogParams;
import com.xxs.leon.xxs.rest.bean.request.AddRechargeLogParams;
import com.xxs.leon.xxs.rest.bean.request.CollectInstallationParams;
import com.xxs.leon.xxs.rest.bean.request.CommentParams;
import com.xxs.leon.xxs.rest.bean.request.CostMoneyParams;
import com.xxs.leon.xxs.rest.bean.request.LoginParams;
import com.xxs.leon.xxs.rest.bean.request.PayParams;
import com.xxs.leon.xxs.rest.bean.request.ReadCountEntity;
import com.xxs.leon.xxs.rest.bean.request.SendPostParams;
import com.xxs.leon.xxs.rest.bean.request.TestParams;
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
import com.xxs.leon.xxs.rest.bean.response.TestEntity;
import com.xxs.leon.xxs.rest.bean.response.ThumbnailEntity;
import com.xxs.leon.xxs.rest.bean.response.UploadEntity;
import com.xxs.leon.xxs.rest.interceptor.HttpBasicAuthenticatorInterceptor;
import com.xxs.leon.xxs.rest.requestfactory.MyRequestFactory;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Put;
import org.androidannotations.annotations.rest.RequiresHeader;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.MediaType;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.androidannotations.api.rest.RestClientHeaders;
import org.androidannotations.api.rest.RestClientSupport;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

/**
 * Created by maliang on 15/11/24.
 */
@Rest(rootUrl = "https://api.bmob.cn/1", converters = {MappingJackson2HttpMessageConverter.class, StringHttpMessageConverter.class, ByteArrayHttpMessageConverter.class},interceptors = HttpBasicAuthenticatorInterceptor.class)
@RequiresHeader({"X-Bmob-Application-Id", "X-Bmob-REST-API-Key"})
@Accept(MediaType.APPLICATION_JSON)
public interface CommenRestClient extends RestClientHeaders, RestClientErrorHandling, RestClientSupport {

    @Post("/functions/test")
    TestEntity testCloudFunction(TestParams data);

    @Get("/classes/Album/?keys={keys}&where={where}&limit={limit}&order={order}")
    AlbumListEntity getHomeNewAlbums(String keys,String where,int limit,String order);

    @Post("/functions/doLogin")
    CloudRestEntity login(LoginParams data);

    /**
     *
     * @param data
     * @return "result": "{\n  \"createdAt\": \"2015-11-26 14:13:49\",\n  \"objectId\": \"a7cd7105a3\",\n  \"sessionToken\": \"08f0e2ce40a8503e803e968e796f271f\"\n}"
     * 返回值里只包含了objectId 和sessionToken 两个可用的数据，获取用户信息需要再次请求
     */
    @Post("/functions/doSignUp")
    CloudRestEntity register(LoginParams data);

    /**
     * 获取用户信息,不包含sessionToken
     * @param objectId
     * @return
     */
    @Get("/users/{objectId}")
    XSBmobChatUser getUserInfo(String objectId);

    @Post("/files/{remotefilename}")
    @RequiresHeader({"X-Bmob-Application-Id", "X-Bmob-REST-API-Key","Content-Type"})
    UploadEntity uploadFile(byte[] fileBytes,String remotefilename);

    @Post("/functions/doUpdateUserPhoto")
    CloudRestEntity updateUserPhoto(UpdateUserPhotoParams params);

    @Get("/classes/Album/{objectId}")
    Album getAlbumById(String objectId);

    @Post("/functions/doSendSignPost")
    CloudRestEntity sendSignPost(UserSessionParams data);

    @Get("/classes/Album/?keys={keys}&where={where}&limit={limit}&skip={skip}&order={order}")
    AlbumListEntity getAlbumsByType(String keys,String where,int limit,int skip,String order);

    @Get("/classes/Post/?keys={keys}&where={where}&limit={limit}&skip={skip}&order={order}&include={include}")
    HomePostListEntity getHomePostList(String keys,String where,int limit,int skip,String order,String include);

    @Post("/images/thumbnail")
    ThumbnailEntity getThumbnail(ThumbnailParams params);

    @Post("/functions/doSendPost")
    CloudRestEntity sendPost(SendPostParams data);

    @Get("/classes/Post/{objectId}/?keys={keys}&include={include}")
    com.xxs.leon.xxs.rest.bean.Post getPostDetailById(String objectId,String keys,String include);

    @Post("/webpay")
    PayEntity webPay(PayParams params);

    @Post("/functions/doAddMoneyLog")
    CloudRestEntity addRechargeLog(AddRechargeLogParams params);

    @Get("/classes/Read_Log/?where={where}&limit={limit}&count={count}")
    ReadCountEntity getReadLogCount(String where,int limit,int count);

    @Post("/functions/doCostMoney")
    CloudRestEntity costMoney(CostMoneyParams params);

    @Post("/functions/doAddReadLog")
    CloudRestEntity addReadLog(AddReadLogParams params);

    @Post("/functions/doUpdateUserName")
    CloudRestEntity updateUserName(UpdateUserNameParams params);

    @Post("/functions/doUpdateUserSignword")
    CloudRestEntity updateUserSignWord(UpdateUserSignWordParams params);

    @Post("/functions/doCollectInstallation")
    CloudRestEntity collectInstallation(CollectInstallationParams params);

    @Get("/classes/Album/?keys={keys}&where={where}&limit={limit}&order={order}")
    AlbumListEntity search(String keys,String where,int limit,String order);

    @Get("/classes/Album/?keys={keys}&where={where}&limit={limit}&order={order}")
    AlbumListEntity getRecommendAlbumList(String keys,String where,int limit,String order);

    @Post("/functions/doFeedBack")
    CloudRestEntity sendFeedBack(CommentParams params);

    @Post("/functions/doSeekBook")
    CloudRestEntity sendSeekBook(CommentParams params);

    @Post("/functions/doCorrect")
    CloudRestEntity sendCorrect(CommentParams params);

    @Post("/functions/doComment")
    CloudRestEntity sendComment(CommentParams params);

    @Get("/classes/Comment/?keys={keys}&where={where}&limit={limit}&skip={skip}&order={order}&include={include}")
    CommentListEntity getCommentList(String keys,String where,int limit,int skip,String order,String include);
}
