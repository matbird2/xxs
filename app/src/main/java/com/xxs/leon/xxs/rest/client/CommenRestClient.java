package com.xxs.leon.xxs.rest.client;

import com.xxs.leon.xxs.rest.bean.Album;
import com.xxs.leon.xxs.rest.bean.request.TestParams;
import com.xxs.leon.xxs.rest.bean.response.HomeAlbumEntity;
import com.xxs.leon.xxs.rest.bean.response.TestEntity;

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
@Rest(rootUrl = "https://api.bmob.cn/1",converters = {MappingJackson2HttpMessageConverter.class, StringHttpMessageConverter.class})
@RequiresHeader({"X-Bmob-Application-Id","X-Bmob-REST-API-Key"})
@Accept(MediaType.APPLICATION_JSON)
public interface CommenRestClient extends RestClientHeaders,RestClientErrorHandling,RestClientSupport{

    @Post("/functions/test")
    TestEntity testCloudFunction(TestParams data);

    @Get("/classes/Album/?{keys}&{where}&{limit}&{order}")
    HomeAlbumEntity getHomeNewAlbums(String keys,String where,String limit,String order);

//    @Get("/classes/Album/?{bql}")
//    HomeAlbumEntity getHomeNewAlbums(String bql);
}
