package com.xxs.leon.xxs.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by maliang on 15/12/16.
 */
public class TimeUtil {

    /**
     * 生成 x分钟以前的字样
     * @param time 形如 2015-12-11 15:25:33 的时间
     * @return
     */
    public static String generTimeShowWord(String time){
        try {
            Calendar now = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar save = Calendar.getInstance();
            save.setTime(format.parse(time));
            int delYear = now.get(Calendar.YEAR) - save.get(Calendar.YEAR);
            int delMonth = now.get(Calendar.MONTH) - save.get(Calendar.MONTH);
            int delDay = now.get(Calendar.DAY_OF_MONTH) - save.get(Calendar.DAY_OF_MONTH);
            int delMinu = now.get(Calendar.MINUTE) - save.get(Calendar.MINUTE);
            int delSecond = now.get(Calendar.SECOND) - save.get(Calendar.SECOND);
            if(delYear > 0){
                return delYear+"年以前";
            }else if(delMonth > 0){
                return delMonth+"个月以前";
            }else if(delDay > 0){
                return delDay+"天以前";
            }else if(delMinu > 0){
                return delMinu+"分钟以前";
            }else if(delSecond > 0){
                return delSecond+"秒钟以前";
            }else{
                return "--";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "-";
    }
}
