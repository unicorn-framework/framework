/**
 * Title: SysCode.java<br/>
 * Description: <br/>
 * Copyright: Copyright (c) 2015<br/>
 * 
 *
 */
package org.unicorn.framework.core;

/**
 * Title: SysCode<br/>
 * Description: <br/>
 * 
 * 
 * @author xiebin
 *
 */
public class SysCode {

	/** 成功 */
	public static final ResBean SUCCESS = new ResBean("00000", "成功");
	/**
	 * 
	 */
	public static final ResBean SESSION_ERROR = new ResBean("40001", "session失效");
	
	
	/** SYS_FAIL,系统未知错误 */
	public static final ResBean SYS_FAIL = new ResBean("90000", "系统未知错误");

	/** SYS_FAIL,系统未知错误 */
	public static final ResBean HTTP_RESPONSE_ERROR = new ResBean("90001", "http响应错误");
	/** MICRO_SERVICE_ERROR  微服务调用失败*/
	public static final ResBean MICRO_SERVICE_ERROR = new ResBean("90009", "微服务调用超时");
	
	/** DROOS_VALUE_ZERO_ERROR  规则计算值为0*/
	public static final ResBean DROOS_VALUE_ZERO_ERROR = new ResBean("90010", "微服务调用超时");
	
	
	
	/** SYS_FAIL,系统未知错误 */
	public static final ResBean CODEUNDEFINED = new ResBean("99999", "系统未知错误");
	/** 空指针异常 **/
	public static final ResBean SYS_NULL_POINT = new ResBean("99991", "空指针异常");
	/** 对象转换失败 **/
	public static final ResBean OBJECT_TRANSF_ERROR = new ResBean("99992", "对象转换失败");
	/** 参数不能为空 **/
	public static final ResBean PARAM_ERROR = new ResBean("99993", "参数不能为空");
	/** 未找到对应的处理器 **/
	public static final ResBean NOT_FOUND_HANDLER = new ResBean("99994", "未找到对应的处理器");

	/** 未找到对应的处理器 **/
	public static final ResBean OUT_SYS_INTERFACE_INVOKE_ERROR = new ResBean("99995", "外部系统接口调用错误");

	/** 未找到对应的处理器 **/
	public static final ResBean OUT_SYS_INTERFACE_IO_ERROR = new ResBean("99996", "外部系统接口服务IO异常");
	
	/**加密失败 **/
	public static final ResBean ENCRYPT_ERROR = new ResBean("99997", "加密失败");
	
	/**签名验证失败 **/
	public static final ResBean SIGN_ERROR = new ResBean("99997", "签名验证失败");
	/**
	 * 6开头的是缓存服务器异常
	 */

	// 和MemCache服务器链接失败
	public static final ResBean CACHE_CONNECT_FAIL = new ResBean("60001", "缓存服务器链接失败");

	// 和MemCache服务器链接返回不为OK
	public static final ResBean CACHE_RSP_NOTOK = new ResBean("60002", "服务器链接返回不为OK");

	/**
	 * 获取数据库连接失败
	 */
	public static final ResBean DB_CONNECT_FAIL = new ResBean("80010", "获取数据库连接失败");
	/** 数据库操作失败 **/
	public static final ResBean DB_ERROR = new ResBean("80001", "数据库操作失败");

	/**
	 * 数据库操作入参为空
	 */
	public static final ResBean PARA_NULL = new ResBean("80000", "数据库操作入参为空");

	/**
	 * jms调用异常，服务不可用
	 */
	public static final ResBean JMS_FAIL = new ResBean("70001", "JMS调用异常，服务不可用");

}
