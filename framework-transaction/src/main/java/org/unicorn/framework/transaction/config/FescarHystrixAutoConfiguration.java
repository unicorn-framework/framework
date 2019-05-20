package org.unicorn.framework.transaction.config;

import com.netflix.hystrix.HystrixCommand;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author  xiebin
 */
@Configuration
@ConditionalOnClass(HystrixCommand.class)
public class FescarHystrixAutoConfiguration {
    @Bean
    FescarHystrixConcurrencyStrategy fescarHystrixConcurrencyStrategy() {
        return new FescarHystrixConcurrencyStrategy();
    }

}
