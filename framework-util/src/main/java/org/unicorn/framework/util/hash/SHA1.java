/**
 * 
 * Copyright (c) 2015 All Rights Reserved.
 */
package org.unicorn.framework.util.hash;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.unicorn.framework.util.radix.BytesToString;

import lombok.extern.slf4j.Slf4j;

/**
 * SHA1的算法
 * 安全散列算法
 *
 * @author xiebin
 */
@Slf4j
public class SHA1 {


    /**
     * 将字节数组算出16进制SHA串
     * @param origin 字节数组
     * @return 16进制SHA字符串
     */
    public static String Encode16(byte[] origin) {
        byte[] b = Encode(origin);
        if (b == null) {
            return null;
        }
        return BytesToString.byteArrayToHexString(b);
    }

    /**
     * 返回字节数组算出二进制SHA串
     * @param origin	字节数组
     * @return	二进制SHA串
     */
    public static byte[] Encode(byte[] origin) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            return md.digest(origin);
        } catch (NoSuchAlgorithmException e) {
            log.error("SHA-1", e);
        }
        return null;
    }

    /**
     * 将字符串算出16进制SHA-1串，字符串按系统编码进行编码
     * @param origin 字符串
     * @return 16进制SHA-1字符串
     */
    public static String Encode16(String origin) {
        return Encode16(origin.getBytes());
    }

    /**
     * 将文件内容算出16进制SHA-1串
     * @param FileName 文件完整路径
     * @return 16进制SHA-1字符串
     */
    public static String EncodeFile16(String FileName) {
        File Files = new File(FileName);
        return Encode16(Files);
    }

    /**
     * 将文件内容算出16进制SHA-1串
     * @param Files 文件对象
     * @return 16进制SHA-1字符串
     */
    public static String Encode16(File Files) {
        String resultString = null;
        FileInputStream fr = null;
        DigestInputStream mdfile = null;
        try {
            fr = new FileInputStream(Files);
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            mdfile = new DigestInputStream(fr, md);
            int len = 64 * 1024;
            byte buf[] = new byte[len];
            while (mdfile.read(buf, 0, len) != -1) {
            }
            resultString = BytesToString.byteArrayToHexString(md.digest());
        } catch (Exception e) {
        	  log.error("SHA-1", e);
        } finally {
            if (mdfile != null) {
                try {
                    mdfile.close();
                } catch (IOException e) {
                }
                mdfile = null;
            }
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                }
                fr = null;
            }
        }
        return resultString;
    }
}
