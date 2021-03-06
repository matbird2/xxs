package com.xxs.leon.xxs.utils;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by maliang on 15/11/25.
 */
@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface XXSPref {

    @DefaultString("")
    String userInfo();

    @DefaultBoolean(true)
    boolean isFirstOpen();

    @DefaultInt(0)
    int fitType();
}
