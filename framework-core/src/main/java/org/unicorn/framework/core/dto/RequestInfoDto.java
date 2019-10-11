package org.unicorn.framework.core.dto;

import lombok.*;
import org.assertj.core.util.Lists;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
/**
 * @author xiebin
 *
 */
public class RequestInfoDto  implements Serializable {
    /**
     * 请求ID
     */
    private String requestId;
    /**
     * 请求url
     */
    private String  requestUrl;
    /**
     * http请求方法
     */
    private String httpMethod;
    /**
     * 请求IP
     */
    private String remoteIp;
    /**
     * 请求控制器的方法
     */
    private String requestMethod;
    /**
     * 请求报文
     */
    private List<String> requestBodys= Lists.newArrayList();
}
