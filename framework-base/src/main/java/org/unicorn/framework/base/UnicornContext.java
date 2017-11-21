package org.unicorn.framework.base;

import java.util.HashMap;
import java.util.Map;

public class UnicornContext{
	private static final ThreadLocal<Map<Object, Object>> context = new ThreadLocal<Map<Object, Object>>() {  
        @Override  
        protected Map<Object, Object> initialValue() {  
            return new HashMap<Object, Object>();  
        }  
  
    };  
  
    /** 
     * 根据key获取值 
     * @param key 
     * @return 
     */  
    @SuppressWarnings("unchecked")
	public static <T>  T getValue(Object key) {  
        if(context.get() == null) {  
            return null;  
        }  
        return (T) context.get().get(key);  
    }  
  
    
    
    /** 
     * 存储 
     * @param key 
     * @param value 
     * @return 
     */  
    @SuppressWarnings("unchecked")
	public static <T> T setValue(Object key, T value) {  
        Map<Object, Object> cacheMap = context.get();  
        if(cacheMap == null) {  
            cacheMap = new HashMap<Object, Object>();  
            context.set(cacheMap);  
        }  
        return (T) cacheMap.put(key, value);  
    }  
  
    /** 
     * 根据key移除值 
     * @param key 
     */  
    public static void removeValue(Object key) {  
        Map<Object, Object> cacheMap = context.get();  
        if(cacheMap != null) {  
            cacheMap.remove(key);  
        }  
    }  
  
    /** 
     * 重置 
     */  
    public static void reset() {  
        if(context.get() != null) {  
            context.get().clear();  
        }  
    }  
}
