package org.unicorn.framework.mq.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xiebin
 * RocketMQ的配置参数
 */
@Data
@ConfigurationProperties(prefix = "unicorn.rocketmq")
public class MQProperties {
    /**
     * config name server address
     */
    private String nameServerAddress;
    /**
     * config producer group , default to DPG+RANDOM UUID like DPG-fads-3143-123d-1111
     */
    private String producerGroup;
    /**
     * config transaction group
     */
    private String producerTransactionGroup;
    /**
     * config send message timeout
     */
    private Integer sendMsgTimeout = 3000;
    /**
     * switch of trace message consumer: send message consumer info to topic: MQ_TRACE_DATA
     */
    private Boolean traceEnabled = Boolean.FALSE;
    /**
     * config whether to open transaction
     */
    private Boolean transactionEnable = Boolean.TRUE;
    /**
     * 检查线程池最小线程数
     */
    private Integer checkThreadPoolMinSize=1;
    /**
     * 检查线程池最大线程数
     */
    private Integer checkThreadPoolMaxSize=1;
    /**
     * 检查线程池，producer本地缓冲请求队列大小
     */
    private Integer checkRequestHoldMax=2000;

}
