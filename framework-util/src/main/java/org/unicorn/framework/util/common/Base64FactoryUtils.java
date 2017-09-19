/**
 * Title: XmlUtils.java<br/>
 * Description: <br/>
 * Copyright: Copyright (c) 2016<br/>
 * 
 *
 */
package org.unicorn.framework.util.common;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

/**
 * 
 * @author xiebin
 *
 */
public class Base64FactoryUtils {
    /**
     * 将流转化为base64编码串
     * @param inputStream
     * @return
     * @throws Exception 
     */
	public static String inputSteamToBase64(InputStream inputStream) throws Exception{
		try{
			Encoder encoder = Base64.getEncoder();
			byte[] bytes=new byte[inputStream.available()];
			inputStream.read(bytes);
			inputStream.close();
	        return encoder.encodeToString(bytes);
		}catch(Exception e){
		}
		return null;
	}
	   /**
     * 将base64编码串转化为流
     * @param base64Str
     * @return
     */
	public static InputStream base64ToInputStream(String base64Str){
		Decoder decoder = Base64.getDecoder();
		// Base64解码
        byte[] bytes = decoder.decode(base64Str);
        return new ByteArrayInputStream(bytes);
	}
}
