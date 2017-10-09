/**
 * 
 * Copyright (c) 2015 All Rights Reserved.
 */
package org.unicorn.framework.util.hash;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.log4j.Logger;
import org.unicorn.framework.util.radix.BytesToString;

/**
 * DES加解密工具类
 *
 * @author xiebin
 */
public class DES {

    private static Logger logger = Logger.getLogger(DES.class);

    /**
     * 解密
     * @param string
     * @param key
     * @return	解密串
     */
    public static String decode16(String string, String key) {
        return BytesToString.byteArrayToHexString(decode(string.getBytes(), key.getBytes()));
    }

    /**
     * 解密
     * @param string
     * @param key
     * @return	解密串
     */
    public static String decode(String string, String key) {
        return new String(decode(string.getBytes(), key.getBytes()));
    }

    /**
     * 解密
     * @param string
     * @param key
     * @return	解密串
     */
    public static String decode16(byte[] string, byte[] key) {
        return BytesToString.byteArrayToHexString(decode(string, key));
    }

    /**
     * 解密
     * @param string
     * @param key
     * @return	解密串
     */
    public static byte[] decode(byte[] string, byte[] key) {
        try {
            SecureRandom random = new SecureRandom();
            // 创建一个DESKeySpec对象  
            DESKeySpec desKey = new DESKeySpec(key);
            // 创建一个密匙工厂  
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // 将DESKeySpec对象转换成SecretKey对象  
            SecretKey securekey = keyFactory.generateSecret(desKey);
            // Cipher对象实际完成解密操作  
            Cipher cipher = Cipher.getInstance("DES");
            // 用密匙初始化Cipher对象  
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            // 真正开始解密操作  
            return cipher.doFinal(string);
        } catch (Throwable e) {
            logger.error(e, e);
        }
        return null;
    }

    /**
     * 加密成16位串
     * @param string
     * @param key
     * @return	解密串
     */
    public static String encode16(String string, String key) {
        return BytesToString.byteArrayToHexString(encode(string.getBytes(), key.getBytes()));
    }

    /**
     * 加密
     * @param string
     * @param key
     * @return	加密串
     */
    public static String encode(String string, String key) {
        return new String(encode(string.getBytes(), key.getBytes()));
    }

    /**
     * 加密
     * @param string
     * @param key
     * @return	加密串
     */
    public static String encodeBytes(byte[] string, byte[] key) {
        return new String(encode(string, key));
    }

    /**
     * 加密16位串
     * @param string
     * @param key
     * @return	加密串
     */
    public static String encode16Bytes(byte[] string, byte[] key) {
        return BytesToString.byteArrayToHexString(encode(string, key));
    }

    /**
     * 加密
     * @param string
     * @param key
     * @return	加密串
     */
    public static byte[] encode(byte[] string, byte[] key) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key);
            //创建一个密匙工厂，然后用它把DESKeySpec转换成  
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            //Cipher对象实际完成加密操作  
            Cipher cipher = Cipher.getInstance("DES");
            //用密匙初始化Cipher对象  
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            //现在，获取数据并加密  
            //正式执行加密操作  
            return cipher.doFinal(string);
        } catch (Throwable e) {
            logger.error(e, e);
        }
        return null;
    }
}