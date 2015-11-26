package com.xxs.leon.xxs.rest.bean.response;

/**
 * Created by maliang on 15/11/26.
 */
public class UploadEntity {
    private String filename;
    private String group;
    private String url;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
