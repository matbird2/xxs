package com.xxs.leon.xxs.rest.bean.request;

/**
 * Created by maliang on 15/11/27.
 */
public class UpdateUserNameParams extends UserSessionParams{
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
