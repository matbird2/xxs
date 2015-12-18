package com.xxs.leon.xxs.rest.bean.request;

/**
 * Created by maliang on 15/12/18.
 */
public class SendPostParams extends UserSessionParams{
    private String content;
    private String title;
    private String excerpt;

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

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }
}
