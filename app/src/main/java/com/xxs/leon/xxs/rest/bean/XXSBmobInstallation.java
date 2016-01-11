package com.xxs.leon.xxs.rest.bean;

import android.content.Context;

import cn.bmob.v3.BmobInstallation;

/**
 * Created by maliang on 16/1/11.
 */
public class XXSBmobInstallation extends BmobInstallation{

    private String uid;

    public XXSBmobInstallation(Context context) {
        super(context);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
