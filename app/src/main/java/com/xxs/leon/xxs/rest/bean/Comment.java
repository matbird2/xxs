package com.xxs.leon.xxs.rest.bean;

import com.xxs.leon.xxs.bean.XSBmobChatUser;

/**
 * Created by maliang on 16/1/18.
 */
public class Comment extends BaseBean{

    private int status;
    /*0、普通评论  ：一级评论需要user，album或者post ，二级评论需要user ，album或者post，parent
    1、求书      ：需要user就行
    2、相册纠错       ：album(如果有user就加)
    3、反馈      ：不需要Pointer(如果有user就加)*/
    private int type;
    private String content;
    private XSBmobChatUser user;
    private Post post;
    private Album album;
    private Comment parent;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public XSBmobChatUser getUser() {
        return user;
    }

    public void setUser(XSBmobChatUser user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Comment getParent() {
        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }
}
