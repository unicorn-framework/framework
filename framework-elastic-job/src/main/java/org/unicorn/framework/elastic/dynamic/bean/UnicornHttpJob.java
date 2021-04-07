package org.unicorn.framework.elastic.dynamic.bean;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Http job
 *
 * @author xiebin
 */
@Data
@ApiModel(value = "Http job对象", description = "Http job对象")
public class UnicornHttpJob extends Job {
    private String url;
    private String method;
    private String data="";
    private String connectTimeout="10000";
    private String readTimeout="5000";
    private String contentType="application/json";
//    private String shardingContext;
}
