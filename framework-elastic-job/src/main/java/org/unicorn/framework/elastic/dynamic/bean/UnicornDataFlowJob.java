package org.unicorn.framework.elastic.dynamic.bean;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 数据流job
 * @author xiebin
 */
@Data
@ApiModel(value = "数据流job对象", description = "数据流job对象")
public class UnicornDataFlowJob extends Job {
}
