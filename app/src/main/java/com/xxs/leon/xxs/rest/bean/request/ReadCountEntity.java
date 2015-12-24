package com.xxs.leon.xxs.rest.bean.request;

import com.xxs.leon.xxs.rest.bean.ReadLog;

import java.util.List;

/**
 * Created by maliang on 15/12/23.
 */
public class ReadCountEntity {
    private int count;

    private List<ReadLog> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ReadLog> getResults() {
        return results;
    }

    public void setResults(List<ReadLog> results) {
        this.results = results;
    }
}
