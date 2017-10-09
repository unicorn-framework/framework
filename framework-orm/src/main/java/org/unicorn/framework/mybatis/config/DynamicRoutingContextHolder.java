package org.unicorn.framework.mybatis.config;

import org.springframework.util.Assert;
/**
 * 
 * @author xiebin
 *
 */
public class DynamicRoutingContextHolder {
	private static final ThreadLocal<RoutingStrategy> contextHolder =
            new ThreadLocal<>();

    public static void setRouteStrategy(RoutingStrategy customerType) {
        Assert.notNull(customerType, "customerType cannot be null");
        contextHolder.set(customerType);
    }

    public static RoutingStrategy getRouteStrategy() {
        return (RoutingStrategy) contextHolder.get();
    }

    public static void clearRouteStrategy() {
        contextHolder.remove();
    }
}
