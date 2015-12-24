package com.xxs.leon.xxs.rest.bean.request;

/**
 * Created by maliang on 15/12/24.
 */
public class AddReadLogParams extends UserSessionParams{
    private String albumId;

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }
}
