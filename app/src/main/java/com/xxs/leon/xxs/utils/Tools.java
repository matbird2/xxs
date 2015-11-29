package com.xxs.leon.xxs.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xxs.leon.xxs.constant.Constant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;

/**
 * Created by maliang on 15/11/26.
 */
public class Tools {

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
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)) {
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
