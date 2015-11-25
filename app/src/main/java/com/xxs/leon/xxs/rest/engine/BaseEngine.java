package com.xxs.leon.xxs.rest.engine;

import com.xxs.leon.xxs.constant.Constant;
import com.xxs.leon.xxs.rest.client.CommenRestClient;
import com.xxs.leon.xxs.rest.handler.CommenErrorHandler;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;

/**
 * Created by leon on 15-11-24.
 */
@EBean
public class BaseEngine {
    @RestService
    protected CommenRestClient client;
    @Bean
    CommenErrorHandler errorHandler;

    public BaseEngine(){
        client.setRestErrorHandler(errorHandler);
        client.setHeader("X-Bmob-Application-Id", Constant.X_BMOB_APPLICATION_ID);
        client.setHeader("X-Bmob-REST-API-Key",Constant.X_BMOB_REST_API_KEY);
    }
}
