package com.xxs.leon.xxs.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.xxs.leon.xxs.constant.Constant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.List;

import cn.bmob.v3.BmobInstallation;

/**
 * Created by maliang on 15/11/26.
 */
public class Tools {

    private static final long COMPRESS_SIZE_LEVEL1 = 400 * 1024;
    private static final long COMPRESS_SIZE_LEVEL2 = 800 * 1024;
    private static final long COMPRESS_SIZE_LEVEL3 = 1000 * 1024;

    /**
     * 1.通过AES加密字符串
     * 2.使用Base64对byte数组编码
     * @param source
     * @return
     */
    public static String encrypt_encode(String source){
        String result = null;
        byte[] aesBytes = AESEncryCypher.encrypt(source, Constant.AES_ENCRYPT_PASSWORD);
        result = Base64.encodeToString(aesBytes);
        return result;
    }

    public static String decode_decrypt(String source){
        String result = null;
        byte[] decodeBytes = Base64.decode(source);
        byte[] decryptBytes = AESEncryCypher.decrypt(decodeBytes, Constant.AES_ENCRYPT_PASSWORD);
        result = new String(decryptBytes);
        return result;
    }

    /**
     * 编码文件到bitmap
     * @param srcPath
     * @return
     */
    public static Bitmap compressImageFromFile(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;// 只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 800f;//
        float ww = 480f;//
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置采样率

        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;// 该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        // return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
        // 其实是无效的,大家尽管尝试
        return bitmap;
    }

    /**
     * 保存图片到SD卡,并返回图片地址
     * @param bitmap
     * @param filePath
     * @return
     */
    public static String saveToSdCard(Bitmap bitmap,String filePath) {
        File file = new File(filePath);
        int quality ;
        long bitmapSize = bitmap.getByteCount();
        if(bitmapSize > COMPRESS_SIZE_LEVEL3 ){
            quality = 85;
        }else if(bitmapSize > COMPRESS_SIZE_LEVEL2){
            quality = 90;
        }else if(bitmapSize > COMPRESS_SIZE_LEVEL1){
            quality = 95;
        }else{
            quality = 100;
        }

        Bitmap.CompressFormat format = null;
        String suffix = filePath.substring(filePath.lastIndexOf(".")+1);
        if("png".equals(suffix)){
            format = Bitmap.CompressFormat.PNG;
        }else if("webp".equals(suffix)){
            format = Bitmap.CompressFormat.WEBP;
        }else{
            format = Bitmap.CompressFormat.JPEG;
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(format, quality, out)) {
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        L.e(L.TEST,"file length :"+file.length()+" filePath:"+filePath+" quality:"+quality+" format:"+format+" bitmapSize:"+bitmapSize+" suffix:"+suffix);
        return file.getAbsolutePath();
    }

    /**
     * file转字节数组
     * @param file
     * @return
     */
    public static byte[] file2BetyArray(File file)
    {
        if (!file.exists()) {
            return null;
        }
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            L.e("VersionInfo", "Exception:" + e.getMessage());
        }
        return versionName;
    }

    /**
     * 分解字符串，用于获取推荐的字符串
     * @param source
     * @return
     */
    public static String[] getRecommendKeywordPair(String source){
        String[] array = new String[2];
        if (source.trim().length() < 2){
            array[0] = "";
            array[1] = "";
            return array;
        }

        if(source.contains("（") ){
            array[0] = source.substring(source.lastIndexOf("（")+1).substring(0, 2);
            array[1] = source.substring(0,2);
        }else if(source.contains("(")){
            array[0] = source.substring(source.lastIndexOf("(")+1).substring(0, 2);
            array[1] = source.substring(0,2);
        }else{
            array[0] = source.substring(0,2);
            array[1] = source.substring(source.length()-2,source.length());
        }
        return array;
    }

    /**
     * 获取activity栈长度
     * @param context
     * @return
     */
    public static int getActivityHeapSize(Context context){
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        return runningTaskInfos != null ? runningTaskInfos.get(0).numActivities : 0;
    }

    public static String getDeviceInfo(Context context){
        StringBuilder sb = new StringBuilder();
        Build buid = new Build();
        sb.append("设备型号："+buid.MODEL);
        sb.append("\n");
        sb.append("系统版本："+Build.VERSION.SDK_INT);
        sb.append("\n");
        try {
            String version = context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName;
            sb.append("应用版本："+version);
            sb.append("\n");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String installationId = BmobInstallation.getInstallationId(context);
        sb.append("设备uid："+installationId);
        sb.append("\n");
        return sb.toString();
    }
}
