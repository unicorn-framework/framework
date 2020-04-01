package org.unicorn.framework.util.barcode;

import org.unicorn.framework.util.hash.Base64Util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author zhanghaibo
 * @since 2019/8/8
 */
public abstract class SimpleSlatUitl {
    /** -- key -- **/
    private static final int keys=0x20200323;

    private static final String ENC = "utf-8";


    /**
     * 异或加密
     * @param bytes
     * @return
     */
    public static byte[] encrypt(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        int len = bytes.length;
        for (int i = 0; i < len; i++) {
            bytes[i] ^= keys;
        }
        return bytes;
    }
    /**
     * 字符串加盐
     * @param s
     * @return
     */
    public static String slat(String s) {

        if (s == null) {
            return null;
        }

        try {
           return URLEncoder.encode(Base64Util.encode(encrypt(s.getBytes())),ENC) ;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字符串数字加盐
     * @param s
     * @return
     */
    public static String slatWithNumber(String s) {
        if (s == null) {
            return s;
        }
        try {
            return URLEncoder.encode(Base64Util.encode(encrypt(s.getBytes())),ENC) ;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String deSlat(String s) {
        if (s == null) {
            return null;
        }
        try {

            String urlDecode = URLDecoder.decode(s, ENC);
            byte[] decode = Base64Util.decode(urlDecode);
            return new String(encrypt(decode));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String code = "o6_bmasdasdsad6_2sgVt7hMZOPfL";
        String s = slat(code);
        System.out.println(s);
        System.out.println(deSlat(s));
        System.out.println(deSlat(s).equals(code));
    }
}
