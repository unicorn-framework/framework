package org.unicorn.framework.enums.jms;
/**
 * 消息通讯类型
 * @author xiebin
 *
 */
public enum JmsACKStatus {
	
	SENDING(0,"待发送"),
	SENDED(1,"已发送"),
	FINISH(2,"完成")
	;
	
	
	
	
	private int code;
	private String info;
	JmsACKStatus(int code,String info){
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
