package com.xxs.leon.xxs.rest.bean;

import com.xxs.leon.xxs.bean.XSBmobChatUser;

import java.util.List;

/**
 * Created by maliang on 15/12/10.
 */
public class Post extends BaseBean{
    private String content;
    private String title;
    private int status;
    private int comment_count;
    private int classic;
    private String linked_url;
    private List<String> imgs;
    private String excerpt;
    private XSBmobChatUser user;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public XSBmobChatUser getUser() {
        return user;
    }

    public void setUser(XSBmobChatUser user) {
        this.user = user;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public String getLinked_url() {
        return linked_url;
    }

    public void setLinked_url(String linked_url) {
        this.linked_url = linked_url;
    }

    public int getClassic() {
        return classic;
    }

    public void setClassic(int classic) {
        this.classic = classic;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
