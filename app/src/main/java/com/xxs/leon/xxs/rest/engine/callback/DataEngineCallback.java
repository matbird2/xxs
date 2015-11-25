package com.xxs.leon.xxs.rest.engine.callback;

import com.xxs.leon.xxs.rest.bean.ErrorBean;

/**
 * Created by maliang on 15/11/25.
 */
public interface DataEngineCallback<T> {

    void handleResponse(T t);
    void onSuccess(T t);
    void onError(ErrorBean errorBean);
    void onFailed(Exception e);
}
