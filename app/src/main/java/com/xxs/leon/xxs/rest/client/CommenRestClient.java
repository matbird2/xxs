package com.xxs.leon.xxs.rest.client;

import com.xxs.leon.xxs.rest.bean.Album;
import com.xxs.leon.xxs.rest.bean.XSUser;
import com.xxs.leon.xxs.rest.bean.request.LoginParams;
import com.xxs.leon.xxs.rest.bean.request.TestParams;
import com.xxs.leon.xxs.rest.bean.response.CloudRestEntity;
import com.xxs.leon.xxs.rest.bean.response.HomeAlbumEntity;
import com.xxs.leon.xxs.rest.bean.response.TestEntity;
import com.xxs.leon.xxs.rest.interceptor.HttpBasicAuthenticatorInterceptor;
import com.xxs.leon.xxs.rest.requestfactory.MyRequestFactory;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.RequiresHeader;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.MediaType;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.androidannotations.api.rest.RestClientHeaders;
import org.androidannotations.api.rest.RestClientSupport;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

/**
 * Created by maliang on 15/11/24.
 */
@Rest(rootUrl = "https://api.bmob.cn/1", converters = {MappingJackson2HttpMessageConverter.class, StringHttpMessageConverter.class},interceptors = HttpBasicAuthenticatorInterceptor.class)
@RequiresHeader({"X-Bmob-Application-Id", "X-Bmob-REST-API-Key"})
@Accept(MediaType.APPLICATION_JSON)
public interface CommenRestClient extends RestClientHeaders, RestClientErrorHandling, RestClientSupport {

    @Post("/functions/test")
    TestEntity testCloudFunction(TestParams data);

    @Get("/classes/Album/?keys={keys}&where={where}&limit={limit}&order={order}")
    HomeAlbumEntity getHomeNewAlbums(String keys,String where,int limit,String order);

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
    XSUser getUserInfo(String objectId);
}
