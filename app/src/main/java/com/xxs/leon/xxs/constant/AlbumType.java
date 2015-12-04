package com.xxs.leon.xxs.constant;

/**
 * Created by maliang on 15/12/4.
 */
public enum AlbumType {
    JD("经典",0),PS("评书",1),SH("神话",2),MJ("民间",3),GM("革命",4),WX("武侠",5),WG("外国",6),ZT("侦探",7),RW("人物",8),ET("儿童",9),XD("现代",10),QT("其他",11);

    private String type;
    private int index;

    private AlbumType(String type,int index){
        this.type = type;
        this.index = index;
    }

    public static String getType(int index){
        for(AlbumType item : AlbumType.values()){
            if(item.getIndex() == index){
                return item.getType();
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
