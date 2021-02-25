package org.unicorn.framework.register.offline;

import com.alibaba.nacos.api.naming.NamingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2021-02-25 14:41
 */
@Slf4j
public class UnicornNacosOffline {

    @Resource
    private NacosDiscoveryProperties discoveryProperties;

    @PreDestroy
    public void destory() {
        try {
            NamingService naming = discoveryProperties.namingServiceInstance();
            naming.deregisterInstance(discoveryProperties.getService(), discoveryProperties.getIp(), discoveryProperties.getPort());
            log.info("{}服务下线成功",discoveryProperties.getService());
        } catch (Exception e) {
            log.error("{}服务下线失败", discoveryProperties.getService(),e);
        }

    }
}
