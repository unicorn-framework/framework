package org.unicorn.framework.util.common;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * 随机数工具类
 *
 * @author xiebin
 */
public class RandomUtils {

    /**
     * 获取随机数
     *
     * @param passLength 随机数长度
     * @param type       随机数类型
     * @return
     */
    public static String getCode(int passLength, int type) {
        StringBuffer buffer = null;
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        switch (type) {
            case 0:
                buffer = new StringBuffer("0123456789");
                break;
            case 1:
                buffer = new StringBuffer("abcdefghijklmnopqrstuvwxyz");
                break;
            case 2:
                buffer = new StringBuffer("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
                break;
            case 3:
                buffer = new StringBuffer("123456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ");
                break;
            case 4:
                buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
                break;
            case 5:
                buffer = new StringBuffer("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
                sb.append(buffer.charAt(r.nextInt(buffer.length() - 10)));
                passLength -= 1;
                break;
            case 6:
                String s = UUID.randomUUID().toString();
                sb.append(s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24));
        }

        if (type != 5) {
            int range = buffer.length();
            for (int i = 0; i < passLength; ++i) {
                sb.append(buffer.charAt(r.nextInt(range)));
            }
        }
        return sb.toString();
    }

    public static String getFlowNum(String businessPrefix) {
        return businessPrefix + "_" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + "_" + getCode(5, 0);
    }
}
