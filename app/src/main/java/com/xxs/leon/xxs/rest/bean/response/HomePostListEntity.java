package com.xxs.leon.xxs.rest.bean.response;

import com.xxs.leon.xxs.rest.bean.Post;

import java.util.List;

/**
 * Created by maliang on 15/12/16.
 */
public class HomePostListEntity {
    private List<Post> results;

    public List<Post> getResults() {
        return results;
    }

    public void setResults(List<Post> results) {
        this.results = results;
    }
}
