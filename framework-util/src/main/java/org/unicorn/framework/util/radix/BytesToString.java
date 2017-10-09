/**
 * 
 * Copyright (c) 2015 All Rights Reserved.
 */
package org.unicorn.framework.util.radix;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 二进制数组转换成进制字符串工具类
 *
 * @author xiebin
 */
public class BytesToString {

    private final static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
                                              'b', 'c', 'd', 'e', 'f' };

    /**
     * 转换字节数组为10进制字串
     * @param b 字节数组
     * @return 10进制字串
     */
    public static String byteArrayToNumString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToNumString(b[i]));//使用本函数则返回加密结果的10进制数字字串，即全数字形式
        }
        return resultSb.toString();
    }

    /**
     * 转换字节数组为10进制字串
     * @param b			字节数组
     * @param pos		开始位置
     * @param length	长度
     * @return	10进制字串
     */
    public static String byteArrayToNumString(byte[] b, int pos, int length) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = pos; i < pos + length; i++) {
            resultSb.append(byteToNumString(b[i]));//使用本函数则返回加密结果的10进制数字字串，即全数字形式
        }
        return resultSb.toString();
    }

    /**
     * 转换字节数组为16进制字串
     * @param b 字节数组
     * @return 16进制字串
     */
    public static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));//若使用本函数转换则可得到加密结果的16进制表示，即数字字母混合的形式
        }
        return resultSb.toString();
    }

    /**
     * 转换字节数组为16进制字串
     * @param b			字节数组
     * @param pos		开始位置
     * @param length	长度
     * @return	16进制字串
     */
    public static String byteArrayToHexString(byte[] b, int pos, int length) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = pos; i < length + pos; i++) {
            resultSb.append(byteToHexString(b[i]));//若使用本函数转换则可得到加密结果的16进制表示，即数字字母混合的形式
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
        return "" + hexDigits[d1] + hexDigits[d2];
    }

    /**
     * 16进字符串转换成字节数组
     * @param hexString
     * @return
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toLowerCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (Arrays.binarySearch(hexDigits, hexChars[pos]) << 4
                           | Arrays.binarySearch(hexDigits, hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * 转换字符数组内非ASCII编码为%nn的结构字符串
     * @param b
     * @return	非ASCII编码为%nn的结构字符串
     */
    public static String byteArrayToURLString(byte[] b) {
        byte ch = 0x00;
        int i = 0;
        if (b == null || b.length <= 0)
            return null;

        String pseudo[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D",
                            "E", "F" };
        StringBuffer out = new StringBuffer(b.length * 2);

        while (i < b.length) {
            // First check to see if we need ASCII or HEX
            if ((b[i] >= '0' && b[i] <= '9') || (b[i] >= 'a' && b[i] <= 'z')
                || (b[i] >= 'A' && b[i] <= 'Z') || b[i] == '$' || b[i] == '-' || b[i] == '_'
                || b[i] == '.' || b[i] == '!') {
                out.append((char) b[i]);
                i++;
            } else {
                out.append('%');
                ch = (byte) (b[i] & 0xF0); // Strip off high nibble
                ch = (byte) (ch >>> 4); // shift the bits down
                ch = (byte) (ch & 0x0F); // must do this is high order bit is
                // on!
                out.append(pseudo[(int) ch]); // convert the nibble to a
                // String Character
                ch = (byte) (b[i] & 0x0F); // Strip off low nibble
                out.append(pseudo[(int) ch]); // convert the nibble to a
                // String Character
                i++;
            }
        }

        String rslt = new String(out);

        return rslt;
    }

    /**
     * 将hex数据转换成input流
     * @param hexString
     * @return
     */
    public static InputStream hexStringToInputStream(String hexString) {
        return new BufferedInputStream(new ByteArrayInputStream(hexStringToBytes(hexString)));
    }

    /**
     * 读取文件内容
     * @param file 
     * @return 读取文件内容的16进制字符串
     */
    public static String fileArrayToHexString(File file) {
        FileInputStream input = null;
        try {
            input = new FileInputStream(file);
            byte[] buf = new byte[(int) file.length()];
            int off = 0;
            while (off < buf.length) {
                int i = input.read(buf, off, buf.length - off);
                if (i < 0) {
                    break;
                }
                off += i;
            }
            return BytesToString.byteArrayToHexString(buf);
        } catch (Exception e) {
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    /**
     * 字符串转unicode字符串
     * @param string
     * @return	unicode字符串
     */
    public static String stringToUnicode(String string) {

        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }
        return unicode.toString();
    }

    public static String unicode2String(String unicode) {

        StringBuffer string = new StringBuffer();
        String[] hex = unicode.split("\\\\u");

        for (int i = 1; i < hex.length; i++) {
            String s = null;
            // 转换出每一个代码点
            if (hex[i].length() < 4) {
                s = hex[i];
            } else {
                s = hex[i].substring(0, 4).toLowerCase();
            }
            Pattern p = Pattern.compile("^[0-9abcdef]{2,4}$");
            Matcher m = p.matcher(s);
            if (!m.matches()) {
                continue;
            }
            int data = Integer.parseInt(hex[i], 16);
            // 追加成string
            string.append((char) data);
        }

        return string.toString();
    }
}
