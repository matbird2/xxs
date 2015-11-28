package com.xxs.leon.xxs.rest.bean.request;

/**
 * Created by maliang on 15/11/27.
 */
public class UpdateUserPhotoParams {
    private String sessionToken;
    private String objectId;
    private String imgUrl;

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
