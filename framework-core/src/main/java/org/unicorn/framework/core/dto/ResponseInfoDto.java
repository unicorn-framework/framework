package org.unicorn.framework.core.dto;

import lombok.*;
import org.unicorn.framework.core.ResponseDto;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
/**
 * @author xiebin
 *
 */
public class ResponseInfoDto implements Serializable {
    /**
     * 响应ID
     */
    private String responseId;
    /**
     * 响应报文
     */
    private Object responseBody;
    /**
     * 请求开始时间
     */
    private Date requestBeginTime;
    /**
     * 请求结束时间
     */
    private Date requestEndTime;
    /**
     * 请求响应时间
     */
    private String responseTime;
}
