package org.unicorn.framework.register;

import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;

import java.util.Properties;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws  Exception {
        System.out.println("Hello World!");
        Properties properties = new Properties();
        properties.setProperty("serverAddr","10.10.1.18:8848");

        NamingService naming= NamingFactory.createNamingService(properties);
        naming.registerInstance("gf-backstage-service","10.10.1.1",9002);

    }
}
