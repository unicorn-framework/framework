package org.unicorn.framework.enums.common;
/**
 * 消息通讯类型
 * @author xiebin
 *
 */
public enum YesNoStatus {
	
	YES(1,"是"),
	NO(0,"否"),
	;
	
	
	
	
	private int code;
	private String info;
	YesNoStatus(int code,String info){
		this.code=code;
		this.info=info;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	
}
