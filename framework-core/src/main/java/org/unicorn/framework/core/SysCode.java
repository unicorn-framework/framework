/**
 * Title: SysCode.java<br/>
 * Description: <br/>
 * Copyright: Copyright (c) 2015<br/>
 */
package org.unicorn.framework.core;

/**
 * Title: SysCode<br/>
 * Description: <br/>
 *
 * @author xiebin
 */
public class SysCode {


    private static final String DEFAULT_RES_INFO = "请稍后重试";
    /**
     * 成功
     */
    public static final ResBean SUCCESS = new ResBean("00000", "成功");


    //===================4开头的授权及接口相关异常  start  ==================================
    /**
     * session失效
     */
    public static final ResBean SESSION_ERROR = new ResBean("40001", "session失效");

    /**
     * 凭证无效
     */
    public static final ResBean BAD_CREADENTIAL = new ResBean("40002", "无效凭证");

    /**
     * 无权限
     */
    public static final ResBean UNAUTHOR__ERROR = new ResBean("40003", "无权限");

    /**
     * 接口不存在
     */
    public static final ResBean URL_NOT_EXIST = new ResBean("40004", "接口不存在");
    //===================4开头的授权及接口相关异常  end  ==================================


    //===================6开头的为业务异常  start==================================
    /**
     * 业务异常   60000-69999
     */
    public static final ResBean THROWS_EXCEPTION_MESSSGE = new ResBean("60000", "业务异常");


    //===================6开头的为业务异常  end==================================


    //===================9开头的为系统异常  start==================================
    /**
     * 系统异常  90000-99999
     */
    public static final ResBean BUSINESS_EXCEPTION_MESSSGE = new ResBean("90000", DEFAULT_RES_INFO);

    /**
     * MICRO_SERVICE_ERROR  微服务调用失败
     */
    public static final ResBean MICRO_SERVICE_ERROR = new ResBean("99991", DEFAULT_RES_INFO);


    /**
     * SYS_FAIL,系统未知错误
     */
    public static final ResBean SYS_FAIL = new ResBean("99992", DEFAULT_RES_INFO);

    /**
     * SYS_FAIL,系统未知错误
     */
    public static final ResBean HTTP_RESPONSE_ERROR = new ResBean("99993", DEFAULT_RES_INFO);

    /**
     * 文件超出大小
     */
    public static final ResBean FILE_UPLOAD_TOO_BIG = new ResBean("99994", "上传文件超出限制大小");

    /**
     * 空指针异常
     */
    public static final ResBean SYS_NULL_POINT = new ResBean("99995", DEFAULT_RES_INFO);
    /**
     * 对象转换失败
     */
    public static final ResBean OBJECT_TRANSF_ERROR = new ResBean("99996", DEFAULT_RES_INFO);
    /**
     * 参数错误
     */
    public static final ResBean PARAM_ERROR = new ResBean("99997", DEFAULT_RES_INFO);

    /**
     * 入参为空
     */
    public static final ResBean PARA_NULL = new ResBean("99998", DEFAULT_RES_INFO);
    /**
     * hystrix异常
     */
    public static final ResBean HYSTRIX_EXCEPTION_MESSSGE = new ResBean("99000", DEFAULT_RES_INFO);

    /**
     * hystrix异常
     */
    public static final ResBean HYSTRIX_BAD_REQUEST_EXCEPTION = new ResBean("99001", DEFAULT_RES_INFO);

    /**
     * 类型转换异常
     */
    public static final ResBean CLASS_CAST_EXCEPTION = new ResBean("99002", DEFAULT_RES_INFO);
    /**
     * 外部系统接口调用错误
     **/
    public static final ResBean OUT_SYS_INTERFACE_INVOKE_ERROR = new ResBean("99900", DEFAULT_RES_INFO);

    /**
     * 外部系统接口服务IO异常
     **/
    public static final ResBean OUT_SYS_INTERFACE_IO_ERROR = new ResBean("99901", DEFAULT_RES_INFO);

    /**
     * 加密失败
     **/
    public static final ResBean ENCRYPT_ERROR = new ResBean("99902", DEFAULT_RES_INFO);

    /**
     * 签名验证失败
     **/
    public static final ResBean SIGN_ERROR = new ResBean("99903", DEFAULT_RES_INFO);

    /**
     * REDIS_SYS_FAIL
     */
    public static final ResBean REDIS_SYS_FAIL = new ResBean("99904", DEFAULT_RES_INFO);

    /**
     * 未找到对应的处理器
     */
    public static final ResBean NOT_FOUND_HANDLER = new ResBean("99905", DEFAULT_RES_INFO);

    /**
     * api limit
     */
    public static final ResBean API_LIMIT_ERROR = new ResBean("99906", "接口调用超过次数");


    //===================9开头的为系统异常  end==================================

    //=======================7开头标识数据库异常 start ==============================
    /**
     * 获取数据库连接失败
     */
    public static final ResBean DB_CONNECT_FAIL = new ResBean("70000", DEFAULT_RES_INFO);
    /**
     * 数据库操作失败
     **/
    public static final ResBean DB_ERROR = new ResBean("70001", DEFAULT_RES_INFO);
    //=======================7开头标识数据库异常 start ==============================

}
