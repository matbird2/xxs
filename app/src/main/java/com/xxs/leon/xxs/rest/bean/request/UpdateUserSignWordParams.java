package com.xxs.leon.xxs.rest.bean.request;

/**
 * Created by maliang on 15/11/27.
 */
public class UpdateUserSignWordParams extends UserSessionParams{
    private String signword;

    public String getSignword() {
        return signword;
    }

    public void setSignword(String signword) {
        this.signword = signword;
    }
}
