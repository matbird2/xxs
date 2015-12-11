package com.xxs.leon.xxs.rest.bean.request;

/**
 * Created by maliang on 15/11/27.
 */
public class UpdateUserPhotoParams extends UserSessionParams{
    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
