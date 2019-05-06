package org.unicorn.framework.elastic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ImportResource;
import org.unicorn.framework.elastic.annotation.EnableElasticJob;

/**
 * springboot启动器
 * @author xiebin
 */

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class
})
@EnableElasticJob
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
