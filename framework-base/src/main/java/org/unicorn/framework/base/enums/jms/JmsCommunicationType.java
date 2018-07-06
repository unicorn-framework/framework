package org.unicorn.framework.base.enums.jms;
/**
 * 消息通讯类型
 * @author xiebin
 *
 */
public enum JmsCommunicationType {
	
	P2P("1","点对点模型"),
	PUBLISH_SUBSCRIBE("2","发布订阅模型"),
	;
	
	
	
	
	private String code;
	private String info;
	JmsCommunicationType(String code,String info){
		this.code=code;
		this.info=info;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	
}
