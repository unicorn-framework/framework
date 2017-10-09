/**
 * Title: CacheClient.java<br/>
 * Description: <br/>
 * Copyright: Copyright (c) 2015<br/>
 * 
 *
 */
package org.unicorn.framework.cache.cache;

import org.unicorn.framework.core.exception.PendingException;

/**
 * 
 * @author xiebin
 *
 */
public interface CacheClient {

    
    
    //session生命周期的二级缓存，可以用来存储BO对象
    public void set(String key,Object obj) throws PendingException;
    
    //timeout为秒级
    public void set(String key,Object obj,int timeout) throws PendingException;
    
    public Object get(String key) throws PendingException;
    
    public Object get(String key,int timeout) throws PendingException;
    
    public void remove(String key) throws PendingException;
}
