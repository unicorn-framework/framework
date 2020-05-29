package org.unicorn.framework.oss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * oss存储服务分片属性
 *
 * @author xiebin
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "unicorn.oss.part")
public class OssPartproperties implements Serializable {

    private static final long serialVersionUID = -1;


    /**
     * 分片大小 单位（MB) 默认5MB
     */
    private Long partSize = 5L;

    /**
     * 分片数量
     */
    private Long partCount = 10000L;
    /**
     * 线程数量
     */
    private Integer threadCound = 30;

}
