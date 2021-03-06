
package org.unicorn.framework.base.base;

import lombok.extern.slf4j.Slf4j;

/**
*
*@author xiebin
*
*/
@Slf4j
public abstract class AbstractService {
	
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

