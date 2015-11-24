package com.xxs.leon.xxs.rest.requestfactory;

import android.util.Log;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.io.IOException;
import java.net.URI;

/**
 * Created by leon on 15-11-24.
 */
public class MyRequestFactory implements ClientHttpRequestFactory{
    @Override
    public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
        Log.d("TEST","url ==> "+uri);
//        SimpleClientHttpRequestFactory
        return null;
    }
}
