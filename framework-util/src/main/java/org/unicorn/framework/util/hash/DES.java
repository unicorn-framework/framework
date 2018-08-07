/**
 * 
 * Copyright (c) 2015 All Rights Reserved.
 */
package org.unicorn.framework.util.hash;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.unicorn.framework.util.radix.BytesToString;

import lombok.extern.slf4j.Slf4j;

/**
 * DES加解密工具类
 *
 * @author xiebin
 */
@Slf4j
public class DES {


    public static void main(String[] args) {  
          
        String content="你好你好你好";  
        String key="01234567";  
      
        System.out.println("加密前："+content);  
        byte[] encrypted=encode(content.getBytes(), key.getBytes());  
        System.out.println("加密后："+encode16(content,key));  
        byte[] decrypted=decode(encrypted, key.getBytes());  
        System.out.println("解密后："+new String(decrypted));  
        System.out.println(decode16("488eb117913186cfc7fbe9d128b6f61676be7a5459f8e71d",key));
    }  
  
    /**
     * 加密16位串
     * @param string
     * @param key
     * @return	加密串
     */
    public static String encode16(String connent,String key){        
       return BytesToString.byteArrayToHexString(encode(connent.getBytes(),key.getBytes()));
    }  
    
    /**
     * 加密
     * @param contentBytes
     * @param keyBytes
     * @return	加密串
     */
    public static byte[] encode(byte[] contentBytes, byte[] keyBytes){        
        try {  
            DESKeySpec keySpec=new DESKeySpec(keyBytes);  
            SecretKeyFactory keyFactory=SecretKeyFactory.getInstance("DES");              
            SecretKey key=keyFactory.generateSecret(keySpec);         
            Cipher cipher=Cipher.getInstance("DES/CBC/PKCS5Padding");  
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(keySpec.getKey()));             
            byte[] result=cipher.doFinal(contentBytes);  
            return result;  
        } catch (Exception e) {  
        	 log.error("加密错误", e);
        }  
        return null;  
    }  
    
    
    /**
     * 解密
     * @param string
     * @param key
     * @return	解密串
     */
    public static String decode16(String content,String key) {
    	
        return new String(decode(BytesToString.hexStringToBytes(content),key.getBytes()) );
    }

    
    /**
     * 解密
     * @param contentBytes
     * @param keyBytes
     * @return	解密串
     */
    public static byte[] decode(byte[] contentBytes, byte[] keyBytes){        
        try {  
            DESKeySpec keySpec=new DESKeySpec(keyBytes);  
            SecretKeyFactory keyFactory=SecretKeyFactory.getInstance("DES");  
            SecretKey key=keyFactory.generateSecret(keySpec);  
            Cipher cipher=Cipher.getInstance("DES/CBC/PKCS5Padding");  
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(keyBytes));  
            byte[] result=cipher.doFinal(contentBytes);  
            return result;  
        } catch (Exception e) {  
        	 log.error("解密错误", e);
        }  
        return null;  
    }  
}