/**
 * 
 * Copyright (c) 2015 All Rights Reserved.
 */
package org.unicorn.framework.util.hash;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

import org.unicorn.framework.util.radix.BytesToString;

import lombok.extern.slf4j.Slf4j;

/**
 * md5算法
 *
 * @author xiebin
 */
@Slf4j
public class MD5 {


    /**
     * 将字节数组算出16进制MD5串
     * @param origin 字节数组
     * @return 16进制MD5字符串
     */
    public static String Encode16(byte[] origin) {
        String resultString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = BytesToString.byteArrayToHexString(md.digest(origin));
        } catch (Exception e) {
            log.error("MD5加密失败", e);
        }
        return resultString;
    }

    /**
     * 将字符串算出16进制MD5串，字符串按系统编码进行编码
     * @param origin 字符串
     * @return 16进制MD5字符串
     */
    public static String Encode16(String origin) {
        return Encode16(origin.getBytes());
    }

    /**
     * 将文件内容算出16进制MD5串
     * @param FileName 文件完整路径
     * @return 16进制MD5字符串
     */
    public static String EncodeFile16(String FileName) {
        File Files = new File(FileName);
        return Encode16(Files);
    }

    /**
     * 将文件内容算出16进制MD5串
     * @param Files 文件对象
     * @return 16进制MD5字符串
     */
    public static String Encode16(File Files) {
        String resultString = null;
        FileInputStream fr = null;
        try {
            fr = new FileInputStream(Files);
            resultString = Encode16(fr);
        } catch (Exception e) {
        	 log.error("MD5加密失败", e);
        } finally {
            try {
                fr.close();
            } catch (IOException e) {
            }
            fr = null;
        }
        return resultString;
    }

    /**
     * 将流的内容算出16进制MD5串
     * @param inputStream
     * @return	16进制MD5串
     */
    public static String Encode16(InputStream inputStream) {
        String resultString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            DigestInputStream mdfile = new DigestInputStream(inputStream, md);
            int len = 64 * 1024;
            byte buf[] = new byte[len];
            while (mdfile.read(buf, 0, len) != -1) {
            }
            resultString = BytesToString.byteArrayToHexString(md.digest());
        } catch (Exception e) {
        	 log.error("MD5加密失败", e);
        }
        return resultString;
    }
}
