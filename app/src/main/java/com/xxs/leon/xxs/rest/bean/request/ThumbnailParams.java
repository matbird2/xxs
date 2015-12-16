package com.xxs.leon.xxs.rest.bean.request;

/**
 * Created by maliang on 15/12/16.
 */
public class ThumbnailParams {
    private String image;
    private int mode;
    private int quality;
    private int height;
    private int width;
    private int outType;
    private int longEdge;
    private int shortEdge;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getShortEdge() {
        return shortEdge;
    }

    public void setShortEdge(int shortEdge) {
        this.shortEdge = shortEdge;
    }

    public int getLongEdge() {
        return longEdge;
    }

    public void setLongEdge(int longEdge) {
        this.longEdge = longEdge;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getOutType() {
        return outType;
    }

    public void setOutType(int outType) {
        this.outType = outType;
    }
}
