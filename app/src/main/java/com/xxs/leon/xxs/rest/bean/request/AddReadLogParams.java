package com.xxs.leon.xxs.rest.bean.request;

/**
 * Created by maliang on 15/12/24.
 */
public class AddReadLogParams extends UserSessionParams{
    private String albumId;
    private String insId;

    public String getInsId() {
        return insId;
    }

    public void setInsId(String insId) {
        this.insId = insId;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }
}
