package org.unicorn.framework.mybatis.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
/**
 * 
 * @author xiebin
 *
 */
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		 return DynamicRoutingContextHolder.getRouteStrategy();
	}
	

}
