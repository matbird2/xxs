package com.xxs.leon.xxs.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Vibrator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.activity.DetailActivity_;
import com.xxs.leon.xxs.activity.MainActivity_;
import com.xxs.leon.xxs.activity.PostDetailActivity_;
import com.xxs.leon.xxs.utils.L;

import java.io.IOException;

/**
 * Created by maliang on 15/12/30.
 */
public class XXSPushMessageReceiver extends BroadcastReceiver {

    private MediaPlayer mMediaPlayer;
    private final static int VIBRATOR_TIME = 500; // 震动时长
    private final static int NOTIFICATION_ID = 1; // notification的ID
    private static NotificationManager mNotificationManager;
    private static Notification mNotification;
    private Notification.Builder builder;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
         L.e(L.TEST, "客户端收到推送内容：" + intent.getStringExtra("msg"));
         try {
             ObjectMapper objectMapper = new ObjectMapper();
             PushBean bean = objectMapper.readValue(intent.getStringExtra("msg"), PushBean.class);
             if(bean != null){
                 generNotification(context,bean);
             }
         } catch (IOException e) {
             e.printStackTrace();
         }
    }

    private void generNotification(Context context,PushBean bean){
        if (bean == null) {
            return;
        }
        sendNotification(context, bean);

        makeNotifySound(context);
        Vibrator vibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(VIBRATOR_TIME);
    }

    private void sendNotification(Context context,PushBean bean){
        if (builder == null) {
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            builder = new Notification.Builder(context).setTicker(bean.getTitle()).setSmallIcon(R.drawable.ic_launcher);
        }

        Intent notificationIntent = null;
        switch (bean.getType()) {
            case 0: {
                notificationIntent = new Intent(context, MainActivity_.class);
                notificationIntent.putExtra("enterType",1);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                break;
            }
            case 1: {
                notificationIntent = new Intent(context, DetailActivity_.class);
                notificationIntent.putExtra("albumId",bean.getObjectId());
                notificationIntent.putExtra("albumName",bean.getTitle());
                notificationIntent.putExtra("enterType",1);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                break;
            }
            case 2: {
                notificationIntent = new Intent(context, PostDetailActivity_.class);
                notificationIntent.putExtra("postId",bean.getObjectId());
                notificationIntent.putExtra("postTitle",bean.getTitle());
                notificationIntent.putExtra("enterType",1);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                break;
            }
        }
        PendingIntent contentIntent = PendingIntent.getActivity(context,100, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mNotification = builder.setContentIntent(contentIntent).setContentTitle(bean.getTitle()).setContentText(bean.getContent()).build();
            mNotification.flags = Notification.FLAG_AUTO_CANCEL;
        }
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);

    }

    private void makeNotifySound(final Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mMediaPlayer == null) {
                    mMediaPlayer = new MediaPlayer();
                }
                if (mMediaPlayer.isPlaying()) {
                    return;
                }
                AssetFileDescriptor afd = null;
                try {
                    afd = context.getAssets().openFd("notify.mp3");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                if (afd != null) {
                    try {
                        mMediaPlayer.reset();
                        mMediaPlayer.setDataSource(afd.getFileDescriptor(),
                                afd.getStartOffset(), afd.getLength());
                        mMediaPlayer.prepare();
                        mMediaPlayer.start();
                        afd.close();
                    } catch (IllegalArgumentException e) {
                    } catch (IllegalStateException e) {
                    } catch (IOException e) {
                    }
                }
            }
        }).start();
    }
}
