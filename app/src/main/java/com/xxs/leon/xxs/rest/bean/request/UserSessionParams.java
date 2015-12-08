package com.xxs.leon.xxs.rest.bean.request;

/**
 * Created by maliang on 15/12/8.
 */
public class UserSessionParams {
    private String sessionToken;
    private String objectId;

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
}
