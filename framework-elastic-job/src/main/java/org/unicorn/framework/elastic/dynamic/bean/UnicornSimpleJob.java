package org.unicorn.framework.elastic.dynamic.bean;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author xiebin
 */
@Data
@ApiModel(value = "简单job对象", description = "简单job对象")
public class UnicornSimpleJob extends Job {
}
