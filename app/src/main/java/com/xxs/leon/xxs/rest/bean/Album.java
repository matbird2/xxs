package com.xxs.leon.xxs.rest.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maliang on 15/11/24.
 */
public class Album extends BaseBean{
    private String name;
    private int price;
    private String remark;
    private Integer status;
    private Integer type;
    private String descri;
    private ArrayList<String> imgs;
    private String cover;
    private Double length;
    private String from;
    private String download;

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescri() {
        return descri;
    }

    public void setDescri(String descri) {
        this.descri = descri;
    }

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "Album{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", remark='" + remark + '\'' +
                ", status=" + status +
                ", type=" + type +
                ", descri='" + descri + '\'' +
                ", imgs=" + imgs +
                ", cover='" + cover + '\'' +
                ", length=" + length +
                '}';
    }
}
