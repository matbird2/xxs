package com.xxs.leon.xxs.rest.engine;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxs.leon.xxs.constant.Constant;
import com.xxs.leon.xxs.rest.client.CommenRestClient;
import com.xxs.leon.xxs.rest.client.CommenRestClient_;
import com.xxs.leon.xxs.rest.handler.CommenRestErrorHandler;
import com.xxs.leon.xxs.utils.XXSPref_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

/**
 * Created by leon on 15-11-24.
 */
@EBean
public class BaseEngine {

    @RootContext
    protected Context context;
    protected CommenRestClient client = new CommenRestClient_(context);
    protected  ObjectMapper objectMapper = new ObjectMapper();
    protected XXSPref_ xxsPref = new XXSPref_(context);

    {
        CommenRestErrorHandler errorHandler = new CommenRestErrorHandler();
        client.setRestErrorHandler(errorHandler);
        client.setHeader("X-Bmob-Application-Id", Constant.X_BMOB_APPLICATION_ID);
        client.setHeader("X-Bmob-REST-API-Key",Constant.X_BMOB_REST_API_KEY);
    }
}
