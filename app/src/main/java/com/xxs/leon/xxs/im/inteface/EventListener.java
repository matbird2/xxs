package com.xxs.leon.xxs.im.inteface;

/**
 * Created by maliang on 16/1/29.
 */

import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.bean.BmobMsg;

/**
 * 事件监听
 */
public abstract interface EventListener {
    /**
     * 接收到消息
     * @param message
     */
    public abstract void onMessage(BmobMsg message);

    /**
     * 已读回执
     * @param conversionId
     * @param msgTime
     */
    public abstract void onReaded(String conversionId,String msgTime);

    /**
     * 网络改变
     * @param isNetConnected
     */
    public abstract void onNetChange(boolean isNetConnected);

    /**
     * 好友请求
     * @param message
     */
    public abstract void onAddUser(BmobInvitation message);

    /**
     * 下线通知
     */
    public abstract void onOffline();
}
