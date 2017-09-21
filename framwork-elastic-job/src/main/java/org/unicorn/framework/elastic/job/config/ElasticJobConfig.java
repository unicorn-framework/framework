
package org.unicorn.framework.elastic.job.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
*
*@author xiebin
*
*/
@Configuration
@ImportResource(locations={"classpath*:*elasticjob.xml"})
public class ElasticJobConfig {
    @Bean
    public String hello(){
    	System.out.println("hehehhe");
    	return "hello elastic job";
    }
}

