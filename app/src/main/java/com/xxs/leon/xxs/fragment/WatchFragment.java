package com.xxs.leon.xxs.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xxs.leon.xxs.R;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by maliang on 15/12/4.
 */
@EFragment(R.layout.fragment_watch)
public class WatchFragment extends Fragment{

    @ViewById
    protected ImageView image;

    private Bundle bundle;
    @AfterInject
    void init(){
        bundle = getArguments();
    }

    @AfterViews
    void initViews(){
        if(bundle != null){
            Glide.with(this).load(bundle.getString("url","")).into(image);
        }
    }
}
