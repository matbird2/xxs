package com.xxs.leon.xxs.rest.bean.request;

import java.util.List;

/**
 * Created by maliang on 15/12/23.
 */
public class ReadCountEntity {
    private int count;
    private List results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List getResults() {
        return results;
    }

    public void setResults(List results) {
        this.results = results;
    }
}
