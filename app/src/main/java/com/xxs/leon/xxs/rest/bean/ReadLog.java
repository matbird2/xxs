package com.xxs.leon.xxs.rest.bean;

/**
 * Created by maliang on 15/12/24.
 */
public class ReadLog extends BaseBean{
    private String albumId;
    private XSUser user;

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public XSUser getUser() {
        return user;
    }

    public void setUser(XSUser user) {
        this.user = user;
    }
}
