package com.xxs.leon.xxs.utils;

import com.xxs.leon.xxs.constant.Constant;

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
}
