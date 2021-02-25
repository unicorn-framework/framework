package org.unicorn.framework.register.updater;

import com.netflix.loadbalancer.PollingServerListUpdater;
import org.springframework.stereotype.Component;

/**
 *通知调用ribbon刷新服务列表
 *
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2021-02-25 10:13
 */
@Component("ribbonServerListUpdater")
public class UnicornServiceListUpdater extends PollingServerListUpdater {
    private UpdateAction updateAction;

    @Override
    public synchronized void start(UpdateAction updateAction) {
        this.updateAction = updateAction;
        super.start(updateAction);
    }

    public UpdateAction getUpdateAction() {
        return updateAction;
    }
}
