
package org.unicorn.framework.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
*
*@author xiebin
*
*/
public abstract class AbstractBaseController {
	
	public Gson gson=new Gson();
	
	 private static final Logger log = LoggerFactory.getLogger("controller");
	 
	 public void info(String message){
		 log.info(message); 
	 }
	 
	 public void info(String message,Object ...objs){
		 log.info(message,objs); 
	 }
	 public void warn(String message){
		 log.warn(message); 
	 }
	 
	 public void warn(String message,Object ...objs){
		 log.warn(message,objs); 
	 }
	 
	 public void error(String message){
		 log.error(message); 
	 }
	 public void error(String message,Object ...objs){
		 log.error(message,objs); 
	 }


}
