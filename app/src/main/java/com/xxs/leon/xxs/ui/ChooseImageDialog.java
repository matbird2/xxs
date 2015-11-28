package com.xxs.leon.xxs.ui;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.xxs.leon.xxs.R;

/**
 * Created by maliang on 15/11/27.
 */
public class ChooseImageDialog extends Dialog{
    Context context;
    View view;
    View backView;
    String title;
    TextView titleTextView;

    ButtonFlat buttonAccept;
    ButtonFlat buttonCancel;
    ButtonFlat buttonCamera;
    ButtonFlat buttonAlbum;

    View.OnClickListener onAcceptButtonClickListener;
    View.OnClickListener onCancelButtonClickListener;
    View.OnClickListener onCameraButtonClickListener;
    View.OnClickListener onAlbumButtonClickListener;

    ProgressBarCircularIndeterminate pb;
    ImageView preview;
    LinearLayout button_container;

    public ChooseImageDialog(Context context,String title) {
        super(context, android.R.style.Theme_Translucent);
        this.context = context;// init Context
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_choose_image_dialog);

        view = (RelativeLayout)findViewById(R.id.contentDialog);
        backView = (RelativeLayout)findViewById(R.id.dialog_rootView);
        backView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getX() < view.getLeft()
                        || event.getX() > view.getRight()
                        || event.getY() > view.getBottom()
                        || event.getY() < view.getTop()) {
                    dismiss();
                }
                return false;
            }
        });

        this.titleTextView = (TextView) findViewById(R.id.title);
        setTitle(title);

        this.buttonAccept = (ButtonFlat) findViewById(R.id.button_accept);
        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStatusWhenSubmit(true);
                if(onAcceptButtonClickListener != null)
                    onAcceptButtonClickListener.onClick(v);
            }
        });

        this.buttonCancel = (ButtonFlat) findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                if (onCancelButtonClickListener != null)
                    onCancelButtonClickListener.onClick(v);
            }
        });

        this.buttonCamera = (ButtonFlat) findViewById(R.id.button_camera);
        buttonCamera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(onCameraButtonClickListener != null)
                    onCameraButtonClickListener.onClick(view);
            }
        });

        this.buttonAlbum = (ButtonFlat) findViewById(R.id.button_album);
        buttonAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onAlbumButtonClickListener != null)
                    onAlbumButtonClickListener.onClick(view);
            }
        });

        pb = (ProgressBarCircularIndeterminate) findViewById(R.id.pb);
        preview = (ImageView) findViewById(R.id.preview);
        button_container = (LinearLayout) findViewById(R.id.button_container);

        changeSecondView(false);
        changeStatusWhenSubmit(false);
    }

    public void changeSecondView(boolean needChange){
        if(needChange){
            button_container.setVisibility(View.VISIBLE);
            preview.setVisibility(View.VISIBLE);
        }else{
            button_container.setVisibility(View.GONE);
            preview.setVisibility(View.GONE);
        }
    }

    public void changeStatusWhenSubmit(boolean isSubmiting){
        if(isSubmiting){
            pb.setVisibility(View.VISIBLE);
        }else{
            pb.setVisibility(View.INVISIBLE);
        }
        buttonCancel.setEnabled(!isSubmiting);
        buttonCamera.setEnabled(!isSubmiting);
        buttonAlbum.setEnabled(!isSubmiting);
        buttonAccept.setEnabled(!isSubmiting);
        backView.setEnabled(!isSubmiting);
    }

    @Override
    public void show() {
        // TODO 自动生成的方法存根
        super.show();
        // set dialog enter animations
        view.startAnimation(AnimationUtils.loadAnimation(context, com.gc.materialdesign.R.anim.dialog_main_show_amination));
        backView.startAnimation(AnimationUtils.loadAnimation(context, com.gc.materialdesign.R.anim.dialog_root_show_amin));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        if(title == null)
            titleTextView.setVisibility(View.GONE);
        else{
            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(title);
        }
    }

    public ImageView getPreview(){
        return preview;
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public void setTitleTextView(TextView titleTextView) {
        this.titleTextView = titleTextView;
    }

    public ButtonFlat getButtonAccept() {
        return buttonAccept;
    }

    public void setButtonAccept(ButtonFlat buttonAccept) {
        this.buttonAccept = buttonAccept;
    }

    public ButtonFlat getButtonCancel() {
        return buttonCancel;
    }

    public void setButtonCancel(ButtonFlat buttonCancel) {
        this.buttonCancel = buttonCancel;
    }

    public void setOnAcceptButtonClickListener(
            View.OnClickListener onAcceptButtonClickListener) {
        this.onAcceptButtonClickListener = onAcceptButtonClickListener;
        if(buttonAccept != null)
            buttonAccept.setOnClickListener(onAcceptButtonClickListener);
    }

    public void setOnCancelButtonClickListener(
            View.OnClickListener onCancelButtonClickListener) {
        this.onCancelButtonClickListener = onCancelButtonClickListener;
        if(buttonCancel != null)
            buttonCancel.setOnClickListener(onCancelButtonClickListener);
    }
    public void setOnCameraButtonClickListener(
            View.OnClickListener onCameraButtonClickListener) {
        this.onCameraButtonClickListener = onCameraButtonClickListener;
        if(buttonCamera != null)
            buttonCamera.setOnClickListener(onCameraButtonClickListener);
    }
    public void setOnAlbumButtonClickListener(
            View.OnClickListener onAlbumButtonClickListener) {
        this.onAlbumButtonClickListener = onAlbumButtonClickListener;
        if(buttonAlbum != null)
            buttonAlbum.setOnClickListener(onAlbumButtonClickListener);
    }

    @Override
    public void dismiss() {
        Animation anim = AnimationUtils.loadAnimation(context, com.gc.materialdesign.R.anim.dialog_main_hide_amination);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        ChooseImageDialog.super.dismiss();
                    }
                });

            }
        });
        Animation backAnim = AnimationUtils.loadAnimation(context, com.gc.materialdesign.R.anim.dialog_root_hide_amin);

        view.startAnimation(anim);
        backView.startAnimation(backAnim);
    }
}
