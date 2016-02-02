package com.xxs.leon.xxs.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.text.TextUtils;

import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.activity.MainActivity_;
import com.xxs.leon.xxs.activity.MessageListActivity_;
import com.xxs.leon.xxs.bean.XSBmobChatUser;
import com.xxs.leon.xxs.im.inteface.EventListener;
import com.xxs.leon.xxs.rest.engine.impl.CommenEngineImpl;
import com.xxs.leon.xxs.utils.L;
import com.xxs.leon.xxs.utils.NetUtils;

import org.json.JSONObject;

import java.util.ArrayList;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobNotifyManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.config.BmobConstant;
import cn.bmob.im.db.BmobDB;
import cn.bmob.im.inteface.OnReceiveListener;
import cn.bmob.im.util.BmobJsonUtil;
import cn.bmob.im.util.BmobLog;

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
    BmobChatUser currentUser;

    @Override
    public void onReceive(Context context, Intent intent) {
        L.e(L.TEST, "客户端收到推送内容：" + intent.getStringExtra("msg"));

        currentUser = BmobUserManager.getInstance(context).getCurrentUser();

        String json = intent.getStringExtra("msg");
        engine = new CommenEngineImpl();

        boolean isNetConnected = NetUtils.isNetworkAvailable(context);
        if (isNetConnected) {
            parseMessage(context, json);
        } else {
            for (int i = 0; i < ehList.size(); i++)
                ((EventListener) ehList.get(i)).onNetChange(isNetConnected);
        }






        /*if(intent.getStringExtra("msg") == null)
            return ;
         try {
             ObjectMapper objectMapper = new ObjectMapper();
             PushBean bean = objectMapper.readValue(json, PushBean.class);
             if(bean != null){
                 generNotification(context,bean);
             }
         } catch (IOException e) {
             e.printStackTrace();
         }*/
    }

    /*private void generNotification(Context context,PushBean bean){
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
    }*/

    //-------------------about im-------------------------
    public static ArrayList<EventListener> ehList = new ArrayList<>();
    public static final int NOTIFY_ID = 0x000;
    public static int mNewNum = 0;
    CommenEngineImpl engine;


    /**
     * 解析json字符串
     *
     * @param context
     * @param json
     */
    private void parseMessage(final Context context, String json) {
        JSONObject jo;
        try {
            jo = new JSONObject(json);
            String tag = BmobJsonUtil.getString(jo, BmobConstant.PUSH_KEY_TAG);
            if (tag.equals(BmobConfig.TAG_OFFLINE)) {
                if (currentUser != null) {
                    if (ehList.size() > 0) { //有监听的时候，传递下去
                        for (EventListener handler : ehList)
                            handler.onOffline();
                    } else { // 清空数据
                        engine.logout();
                    }
                }
            } else {

                String fromId = BmobJsonUtil.getString(jo, BmobConstant.PUSH_KEY_TARGETID);
                //增加消息接收方的ObjectId--目的是解决多账户登陆同一设备时，无法接收到非当前登陆用户的消息。
                final String toId = BmobJsonUtil.getString(jo, BmobConstant.PUSH_KEY_TOID);
                String msgTime = BmobJsonUtil.getString(jo, BmobConstant.PUSH_READED_MSGTIME);
                if (fromId != null && !BmobDB.create(context, toId).isBlackUser(fromId)) {//该消息发送方不为黑名单用户
                    if (TextUtils.isEmpty(tag)) {//不携带tag标签--此可接收陌生人的消息
                        BmobChatManager.getInstance(context).createReceiveMsg(json, new OnReceiveListener() {

                            @Override
                            public void onSuccess(BmobMsg msg) {
                                // TODO Auto-generated method stub
                                if (ehList.size() > 0) {// 有监听的时候，传递下去
                                    for (int i = 0; i < ehList.size(); i++) {
                                        ((EventListener) ehList.get(i)).onMessage(msg);
                                    }
                                } else {
//                                        boolean isAllow = CustomApplcation.getInstance().getSpUtil().isAllowPushNotify();
                                    if (currentUser != null && currentUser.getObjectId().equals(toId)) {//当前登陆用户存在并且也等于接收方id
                                        mNewNum++;
                                        showMsgNotify(context, msg);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(int code, String arg1) {
                                // TODO Auto-generated method stub
                                BmobLog.i("获取接收的消息失败：" + arg1);
                            }
                        });

                    } else {//带tag标签
                        if (tag.equals(BmobConfig.TAG_ADD_CONTACT)) {
                               /* //保存好友请求道本地，并更新后台的未读字段
                                BmobInvitation message = BmobChatManager.getInstance(context).saveReceiveInvite(json, toId);
                                if(engine.getCurrentUser()!=null){//有登陆用户
                                    if(toId.equals(engine.getCurrentUser().getObjectId())){
                                        if (ehList.size() > 0) {// 有监听的时候，传递下去
                                            for (EventListener handler : ehList)
                                                handler.onAddUser(message);
                                        }else{
                                            showOtherNotify(context, message.getFromname(), toId,  message.getFromname()+"请求添加好友", NewFriendActivity.class);
                                        }
                                    }
                                }*/
                        } else if (tag.equals(BmobConfig.TAG_ADD_AGREE)) {
                                /*String username = BmobJsonUtil.getString(jo, BmobConstant.PUSH_KEY_TARGETUSERNAME);
                                //收到对方的同意请求之后，就得添加对方为好友--已默认添加同意方为好友，并保存到本地好友数据库
                                BmobUserManager.getInstance(context).addContactAfterAgree(username, new FindListener<BmobChatUser>() {

                                    @Override
                                    public void onError(int arg0, final String arg1) {
                                        // TODO Auto-generated method stub

                                    }

                                    @Override
                                    public void onSuccess(List<BmobChatUser> arg0) {
                                        // TODO Auto-generated method stub
                                        //保存到内存中
                                        CustomApplcation.getInstance().setContactList(CollectionUtils.list2map(BmobDB.create(context).getContactList()));
                                    }
                                });
                                //显示通知
                                showOtherNotify(context, username, toId,  username+"同意添加您为好友", MainActivity.class);
                                //创建一个临时验证会话--用于在会话界面形成初始会话
                                BmobMsg.createAndSaveRecentAfterAgree(context, json);*/

                        } else if (tag.equals(BmobConfig.TAG_READED)) {//已读回执
                            String conversionId = BmobJsonUtil.getString(jo, BmobConstant.PUSH_READED_CONVERSIONID);
                            if (currentUser != null) {
                                //更改某条消息的状态
                                BmobChatManager.getInstance(context).updateMsgStatus(conversionId, msgTime);
                                if (toId.equals(currentUser.getObjectId())) {
                                    if (ehList.size() > 0) {// 有监听的时候，传递下去--便于修改界面
                                        for (EventListener handler : ehList)
                                            handler.onReaded(conversionId, msgTime);
                                    }
                                }
                            }
                        }
                    }
                } else {//在黑名单期间所有的消息都应该置为已读，不然等取消黑名单之后又可以查询的到
                    BmobChatManager.getInstance(context).updateMsgReaded(true, fromId, msgTime);
                    BmobLog.i("该消息发送方为黑名单用户");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            //这里截取到的有可能是web后台推送给客户端的消息，也有可能是开发者自定义发送的消息，需要开发者自行解析和处理
            BmobLog.i("parseMessage错误：" + e.getMessage());
        }
    }

    /**
     * 显示与聊天消息的通知
     *
     * @return void
     * @throws
     * @Title: showNotify
     */
    public void showMsgNotify(Context context, BmobMsg msg) {
        // 更新通知栏
        int icon = R.drawable.ic_launcher;
        String trueMsg = "";
        if (msg.getMsgType() == BmobConfig.TYPE_TEXT && msg.getContent().contains("\\ue")) {
            trueMsg = "[表情]";
        } else if (msg.getMsgType() == BmobConfig.TYPE_IMAGE) {
            trueMsg = "[图片]";
        } else if (msg.getMsgType() == BmobConfig.TYPE_VOICE) {
            trueMsg = "[语音]";
        } else if (msg.getMsgType() == BmobConfig.TYPE_LOCATION) {
            trueMsg = "[位置]";
        } else {
            trueMsg = msg.getContent();
        }
        CharSequence tickerText = msg.getBelongUsername() + ":" + trueMsg;
        String contentTitle = msg.getBelongUsername() + " (" + mNewNum + "条新消息)";

        Intent intent = new Intent(context, MessageListActivity_.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

//        boolean isAllowVoice = CustomApplcation.getInstance().getSpUtil().isAllowVoice();
//        boolean isAllowVibrate = CustomApplcation.getInstance().getSpUtil().isAllowVibrate();

        BmobNotifyManager.getInstance(context).showNotifyWithExtras(true, true, icon, tickerText.toString(), contentTitle, tickerText.toString(), intent);
    }
}
