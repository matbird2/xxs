package com.xxs.leon.xxs.rest.bean.response;

import com.xxs.leon.xxs.rest.bean.Comment;

import java.util.List;

/**
 * Created by maliang on 16/1/19.
 */
public class CommentListEntity {
    private List<Comment> results;

    public List<Comment> getResults() {
        return results;
    }

    public void setResults(List<Comment> results) {
        this.results = results;
    }
}
