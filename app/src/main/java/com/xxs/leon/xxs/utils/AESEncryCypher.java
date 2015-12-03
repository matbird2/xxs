package com.xxs.leon.xxs.utils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by maliang on 15/11/25.
 */
public class AESEncryCypher {
    /**
     * AES加密
     * @param content
     * @param password
     * @return
     */
    public static byte[] encrypt(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES"); //实例化一个用AES加密算法的密钥生成器
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
            sr.setSeed(password.getBytes());

            kgen.init(128, sr);//使用用户提供的password初始化此密钥生成器，使其具有确定的密钥大小128字节长。
            SecretKey secretKey = kgen.generateKey(); //生成一个密钥。
            byte[] enCodeFormat = secretKey.getEncoded();//返回基本编码格式的密钥，如果此密钥不支持编码，则返回 null。
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");//根据给定的enCodeFormat字节数组构造一个用AES算法加密的密钥。
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");//使用给定的 UTF-8编码将此 String编码到 byte 序列，并将结果存储到byteContent 数组。
            cipher.init(Cipher.ENCRYPT_MODE, key);// 以加密的方式用密钥初始化此 Cipher。
            byte[] result = cipher.doFinal(byteContent);//按byteContent单部分操作加密指定的
            return result; // 加密 返回加密过后的byteContent
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param content
     *            待解密内容
     * @param password
     *            解密密钥
     * @return
     */
    public static byte[] decrypt(byte[] content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");

            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
            sr.setSeed(password.getBytes());

            kgen.init(128, sr);//使用用户提供的password初始化此密钥生成器，使其具有确定的密钥大小128字节长。
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(content);
            return result; // 加密
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
