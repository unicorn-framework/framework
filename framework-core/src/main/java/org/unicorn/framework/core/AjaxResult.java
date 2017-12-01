package org.unicorn.framework.core;

import java.io.Serializable;

public class AjaxResult<T>  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int retcode = 1;
	private String retmsg = "操作成功";
	private T data;
	
	public AjaxResult(int retcode, String retmsg, T data){
		this.retcode = retcode;
		this.retmsg = retmsg;
		this.data = data;
	}
	
	public AjaxResult(int retcode, String retmsg){
		this.retcode = retcode;
		this.retmsg = retmsg;
	}
	
	public AjaxResult(T data){
		this.retmsg = "查询成功";
		this.data = data;
	}
	
	public AjaxResult(int retcode){
		this.retcode = retcode;
		this.retmsg = "操作失败";
	}
	
	public AjaxResult(String retmsg){
		this.retcode = 0;
		this.retmsg = retmsg;
	}
	
	public AjaxResult(){
		
	}

	public int getRetcode() {
		return retcode;
	}
	public void setRetcode(int retcode) {
		this.retcode = retcode;
	}
	public String getRetmsg() {
		return retmsg;
	}
	public void setRetmsg(String retmsg) {
		this.retmsg = retmsg;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "AjaxResult [retcode=" + retcode + ", retmsg=" + retmsg + ", data=" + data + "]";
	}

}
