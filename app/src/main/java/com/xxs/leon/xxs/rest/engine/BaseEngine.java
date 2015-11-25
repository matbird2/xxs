package com.xxs.leon.xxs.rest.engine;

import android.content.Context;

import com.xxs.leon.xxs.constant.Constant;
import com.xxs.leon.xxs.rest.client.CommenRestClient;
import com.xxs.leon.xxs.rest.client.CommenRestClient_;
import com.xxs.leon.xxs.rest.handler.CommenRestErrorHandler;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.rest.RestService;

/**
 * Created by leon on 15-11-24.
 */
@EBean
public class BaseEngine {

    @RootContext
    protected Context context;
    protected CommenRestClient client = new CommenRestClient_(context);

    {
        CommenRestErrorHandler errorHandler = new CommenRestErrorHandler();
        client.setRestErrorHandler(errorHandler);
        client.setHeader("X-Bmob-Application-Id", Constant.X_BMOB_APPLICATION_ID);
        client.setHeader("X-Bmob-REST-API-Key",Constant.X_BMOB_REST_API_KEY);
    }
}
