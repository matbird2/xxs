package com.xxs.leon.xxs.utils;

import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by maliang on 15/11/25.
 */
@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface XXSPref {

    @DefaultString("")
    String userInfo();

}
