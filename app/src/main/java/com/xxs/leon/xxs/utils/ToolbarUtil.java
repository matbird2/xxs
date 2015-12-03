package com.xxs.leon.xxs.utils;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by maliang on 15/12/3.
 */
public class ToolbarUtil {

    public static void initToolbar(AppCompatActivity context,Toolbar toolbar){
        if (toolbar != null) {
            context.setSupportActionBar(toolbar);
            final ActionBar actionBar = context.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }
}
