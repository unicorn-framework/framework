/**
 * 
 * Copyright (c) 2015 All Rights Reserved.
 */
package org.unicorn.framework.util.hash;

import java.security.MessageDigest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import lombok.extern.slf4j.Slf4j;

/**
 * hmac算法
 * hash 散列消息鉴别码
 *
 * @author xiebin
 */
@Slf4j
public class HMAC {


    public final static String SHA1 = "HmacSHA1";

    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                                                "a", "b", "c", "d", "e", "f" };

    /**
     * 转换字节数组为16进制字串
     * @param b 字节数组
     * @return 16进制字串
     */
    private static String byteArrayToNumString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            //使用本函数则返回加密结果的10进制数字字串，即全数字形式
            resultSb.append(byteToNumString(b[i]));
        }
        return resultSb.toString();
    }

    /**
     * 转换字节数组为10进制字串
     * @param b 字节数组
     * @return 16进制字串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            //若使用本函数转换则可得到加密结果的16进制表示，即数字字母混合的形式
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /**
     * 转换字节为16进制字符串
     * @param b 字节
     * @return 16进制字符串
     */
    private static String byteToNumString(byte b) {

        int _b = b;
        if (_b < 0) {
            _b = 256 + _b;
        }
        return String.valueOf(_b);
    }

    /**
     * 转换字节为10进制字符串
     * @param b 字节
     * @return 10进制字符串
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * 将字节数组算出16进制HMAC串
     * @param origin 字节数组
     * @param key	密钥
     * @param algo	加密方式：HmacSHA1,
     * @return 16进制HMAC字符串
     */
    public static String Encode16(byte[] origin, String key, String algo) {
        String resultString = null;
        try {
            Mac mac = Mac.getInstance(algo);
            SecretKeySpec spec = new SecretKeySpec(key.getBytes(), algo);
            mac.init(spec);
            resultString = byteArrayToHexString(mac.doFinal(origin));

            MessageDigest md = MessageDigest.getInstance("SHA-1");
            resultString = byteArrayToHexString(md.digest(origin));
        } catch (Exception e) {
            log.error("SHA-1加密失败", e);
        }
        return resultString;
    }

    /**
     * 将字符串算出16进制HMAC串，字符串按系统编码进行编码
     * @param origin 字符串
     * @param key	密钥
     * @param algo	加密方式：HmacSHA1,
     * @return 16进制HMAC字符串
     */
    public static String Encode16(String origin, String key, String algo) {
        String resultString = null;
        try {
            Mac mac = Mac.getInstance(algo);
            SecretKeySpec spec = new SecretKeySpec(key.getBytes(), algo);
            mac.init(spec);
            resultString = byteArrayToHexString(mac.doFinal(origin.getBytes()));
        } catch (Exception e) {
            log.error("加密失败", e);
        }
        return resultString;
    }
}
