package org.unicorn.framework.util.json;

import com.google.gson.Gson;

/**
 * 
 * @author xiebin
 *
 */
public class GsonUtils {

	private static Gson gson=new Gson();
	
	public static String toJson(Object object){
		return gson.toJson(object);
	}
	
	public static <T> T  fromJson(String json,Class<T> clazz){
		return gson.fromJson(json, clazz);
	}

}
