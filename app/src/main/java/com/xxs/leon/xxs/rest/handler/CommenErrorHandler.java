package com.xxs.leon.xxs.rest.handler;

import android.util.Log;

import org.androidannotations.annotations.EBean;
import org.androidannotations.api.rest.RestErrorHandler;
import org.springframework.core.NestedRuntimeException;

/**
 * Created by maliang on 15/11/24.
 */
@EBean
public class CommenErrorHandler implements RestErrorHandler{

    @Override
    public void onRestClientExceptionThrown(NestedRuntimeException e) {
        Log.e("TEST","rest error:"+e.getMessage());
    }
}
