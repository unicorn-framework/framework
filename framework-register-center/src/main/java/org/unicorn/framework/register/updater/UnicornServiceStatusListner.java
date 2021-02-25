package org.unicorn.framework.register.updater;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.loadbalancer.ServerListUpdater;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * 用来监听nacos服务上下线通知
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2021-02-25 10:15
 */
@Component
@Slf4j
public class UnicornServiceStatusListner {
    @Autowired
    private UnicornServiceListUpdater unicornServiceListUpdater;
    @Resource
    private NacosDiscoveryProperties discoveryProperties;

    @PostConstruct
    public void init() throws Exception {
        //初始化监听服务上下线
        NamingService naming = discoveryProperties.namingServiceInstance();
        List<String> serviceNames = naming.getServicesOfServer(1, 1000).getData();
        serviceNames.stream().forEach(serviceName -> {
            try {
                naming.subscribe(serviceName, (event -> {
                    //通知ribbon更新服务列表
                    ServerListUpdater.UpdateAction updateAction = unicornServiceListUpdater.getUpdateAction();
                    if (updateAction != null) {
                        updateAction.doUpdate();
                    }
                    NamingEvent namingEvent = (NamingEvent) event;
                    List<Instance> instances = namingEvent.getInstances();
                    String name = namingEvent.getServiceName();
                    if (instances != null && !instances.isEmpty()) {
                        instances.stream().forEach(instance -> {
                            log.info("发现服务" + name + "：" + instance);
                        });
                    } else {
                        log.info("服务" + name + "列表为空");
                    }
                }));
            } catch (NacosException e) {
                e.printStackTrace();
            }
        });
    }
}
