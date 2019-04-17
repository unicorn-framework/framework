/**
 * 
 * Copyright (c) 2015 All Rights Reserved.
 */
package org.unicorn.framework.util.hash;


import org.apache.commons.codec.binary.Base64;

/**
 * Base64加解密工具类
 * 
 * @author xiebin
 */
public class Base64Util {

    /**
     * Base64解码
     * 
     * @param str
     * @return
     */
    public static byte[] decode(String str) {
        Base64 base64 = new Base64();
        return base64.decode(str.getBytes());
    }

    /**
     * Base64解码
     *
     * @param str
     * @return
     */
    public static String decodeString(String str) {
        Base64 base64 = new Base64();
        return new String(base64.decode(str.getBytes()));
    }

    /**
     * Base64编码
     * 
     * @param b
     * @return
     */
    public static String encode(byte[] b) {
        Base64 base64 = new Base64();
        return new String(base64.encode(b));
    }

    /**
     * Base64编码
     *
     * @param b
     * @return
     */
    public static byte[] encodeByte(byte[] b) {
        Base64 base64 = new Base64();
        return base64.encode(b);
    }
}
