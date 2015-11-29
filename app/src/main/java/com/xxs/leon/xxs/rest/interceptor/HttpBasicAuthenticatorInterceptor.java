package com.xxs.leon.xxs.rest.interceptor;

import android.util.Log;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * Created by leon on 15-11-24.
 */
public class HttpBasicAuthenticatorInterceptor implements ClientHttpRequestInterceptor{
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String methodName = request.getMethod().name();
        String url = request.getURI().toString();
        Log.d("TEST","methodName ==> "+methodName+" url ==> "+url);
        return execution.execute(request,body);
    }
}
