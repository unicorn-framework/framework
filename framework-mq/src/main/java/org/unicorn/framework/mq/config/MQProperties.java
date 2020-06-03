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
    private Integer sendMsgTimeout = 300000;
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
    private Integer checkThreadPoolMinSize=10;
    /**
     * 检查线程池最大线程数
     */
    private Integer checkThreadPoolMaxSize=10;

    /**
     * 检查线程池线程保持时间
     */
    private Integer checkThreadKeepAlive=100;
    /**
     * 检查线程池，producer本地缓冲请求队列大小
     */
    private Integer checkRequestHoldMax=2000;
    /**
     * 消费线程池：最小线程数
     */
    private Integer consumeThreadMin=20;

    /**
     * 消费线程池：最大线程数
     */
    private Integer consumeThreadMax=64;
    /**
     * 消息发送失败重试次数
     */
    private Integer retryTimesWhenSendFailed=3;
    /**
     * 消息消费失败重试次数
     */
    private Integer maxReconsumeTimes=-1;

    /**
     * 消息幂等保障时间
     */
    private Integer idempontentScences=60;

    private Integer consumeMessageBatchMaxSize=3;

}
