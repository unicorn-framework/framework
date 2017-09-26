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
    /** SYS_FAIL,系统未知错误 */
	public static final ResBean SYS_FAIL = new ResBean("90000", "系统未知错误");
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
    /** 数据库操作失败**/
    public static final ResBean DB_ERROR = new ResBean("80001", "数据库操作失败");
    
    
    /**
     * 数据库操作入参为空
     */
    public static final ResBean PARA_NULL = new ResBean("80000", "数据库操作入参为空");

    /**
     * rpc调用异常，服务不可用
     */
    public static final ResBean RPC_FAIL = new ResBean("70001", "rpc调用异常，服务不可用");


}
