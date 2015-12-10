package com.xxs.leon.xxs.rest.bean.response;

import com.xxs.leon.xxs.rest.bean.Album;

import java.util.List;

/**
 * Created by maliang on 15/11/24.
 */
public class AlbumListEntity {
    private List<Album> results;

    public List<Album> getResults() {
        return results;
    }

    public void setResults(List<Album> results) {
        this.results = results;
    }
}
