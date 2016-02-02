package com.xxs.leon.xxs.rest.bean;

import com.xxs.leon.xxs.bean.XSBmobChatUser;

/**
 * Created by maliang on 15/12/24.
 */
public class ReadLog extends BaseBean{
    private String albumId;
    private XSBmobChatUser user;

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public XSBmobChatUser getUser() {
        return user;
    }

    public void setUser(XSBmobChatUser user) {
        this.user = user;
    }
}
