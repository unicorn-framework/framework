package org.unicorn.framework.gateway.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * @author  xiebin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "基础请求头", description = "基础请求头")
public class BaseSecurityDto implements Serializable {
    private static final long serialVersionUID=1L;
    /**
     * 请求token
     */
    private String token;
    /**
     * 请求时间戳
     */
    private String timestamp;
    /**
     * 签名
     */
    private String sign;
    /**
     * 请求报文
     */
    private String body;
    /**
     * 请求参数
     */
    private String queryString;
    @Override
    public String toString(){
        StringBuilder sbuilder=new StringBuilder();
        if(!StringUtils.isBlank(token)){
            sbuilder.append(token);
        }
        if(!StringUtils.isBlank(timestamp)){
            sbuilder.append(timestamp);
        }
        if(!StringUtils.isBlank(body)){
            sbuilder.append(body);
        }
        if(!StringUtils.isBlank(queryString)){
            sbuilder.append(queryString);
        }
        return sbuilder.toString();
    }
}
