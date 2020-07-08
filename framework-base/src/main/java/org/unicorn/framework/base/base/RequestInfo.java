/**
 * Title: RequestInfo.java<br/>
 * Description: <br/>
 * Copyright: Copyright (c) 2015<br/>
 */
package org.unicorn.framework.base.base;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * Title: RequestInfo<br/>
 * Description: 请求信息<br/>
 *
 *
 * @author xiebin
 */
@Builder
@Data
public class RequestInfo implements Serializable {

    // 系统信息

    /**
     *  应用名称
     */

    private String appName;
    /**
     * 实例名称
     */
    private String insId;
    private String serverName;
    private String sysCnl;

    /**
     * 消息信息
     */
    private String msgId;
    private long requestTime;
    private long responseTime;
    private String remoteIp;

    /**
     * 方便定位会话信息所以将requestTokenID放入
     */
    private String requestTokenId;
    /**
     * 方便定位会话信息所以将responseTokenID放入
     */
    private String responseTokenID;

    private String code;
    private String desc;
    private String urlWithOutContext;
    private String userAgent;
    private byte[] requestData;
    private byte[] responseData;
    private String msgCd;
    private String msgMark;

    /**
     * 终端信息
     */
    private String termType;
    private String termVersion;
    private String termId;

    private String appVersion;
    private String appCnl;
    private String msgInf;

    /**
     * 用户信息
     */
    private String userNo;
    /**
     * 电话号码
     */
    private String phoneNum;

    public static final String getReqCode() {
        return null;
    }

}
