package org.unicorn.framework.util.barcode;

import org.unicorn.framework.util.common.RandomUtils;

/**
 * @author zhanghaibo
 * @since 2019/8/8
 */
public abstract class SimpleSlatUitl {

    /**
     * 字符串加盐
     * @param s
     * @return
     */
    public static String slat(String s) {
        if (s == null || s.length() < 13) {
            return s;
        }
        StringBuffer stringBuffer = new StringBuffer(s);
        stringBuffer.insert(1, RandomUtils.getCode(2, 4));
        stringBuffer.insert(4, RandomUtils.getCode(2, 4));
        stringBuffer.insert(7, RandomUtils.getCode(2, 4));
        stringBuffer.insert(10, RandomUtils.getCode(2, 4));
        stringBuffer.insert(13, RandomUtils.getCode(2, 4));
        return stringBuffer.toString();
    }

    public static String deSlat(String s) {
        if (s == null || s.length() < 13) {
            return s;
        }
        StringBuffer stringBuffer = new StringBuffer(s);
        stringBuffer.replace(13, 15,"");
        stringBuffer.replace(10, 12, "");
        stringBuffer.replace(7, 9, "");
        stringBuffer.replace(4, 6, "");
        stringBuffer.replace(1, 3, "");
        return stringBuffer.toString();
    }

    public static void main(String[] args) {
        String s = slat("o6_bmasdasdsad6_2sgVt7hMZOPfL");
        System.out.println(s);
        System.out.println(deSlat(s));
    }
}
