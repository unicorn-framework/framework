package org.unicorn.framework.elastic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

/**
 * springboot启动器
 * @author xiebin
 */

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class
})
@ImportResource(locations={"classpath:elasticjob.xml"})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
