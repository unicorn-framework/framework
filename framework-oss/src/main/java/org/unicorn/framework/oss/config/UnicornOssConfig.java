package org.unicorn.framework.oss.config;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.unicorn.framework.base.thread.UnicornThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author xiebin
 */
@Configuration
@EnableConfigurationProperties(OssBucketConfig.class)
public class UnicornOssConfig {
    @Autowired
    private OssBucketConfig ossBucketConfig;
    @Autowired
    private OssPartproperties ossPartproperties;
//    @Bean
//    public DefaultAcsClient defaultAcsClient() {
//        DefaultProfile profile = DefaultProfile.getProfile(ossBucketConfig.getRegionId(), ossBucketConfig.getAccessKeyId(), ossBucketConfig.getAccessKeySecret());
//        DefaultAcsClient client = new DefaultAcsClient(profile);
//        return client;
//    }

    @Bean
    public OSSClient ossClient() {
        OSSClient client = new OSSClient(ossBucketConfig.getEndPoint(), ossBucketConfig.getAccessKeyId(), ossBucketConfig.getAccessKeySecret());
        return client;
    }

    @Bean
    public ExecutorService executorService() {
        ExecutorService executorService = new ThreadPoolExecutor(ossPartproperties.getThreadCound(), ossPartproperties.getThreadCound(), 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue(), new UnicornThreadFactory("oss-pool"),
                new ThreadPoolExecutor.CallerRunsPolicy());
        return executorService;
    }

}
