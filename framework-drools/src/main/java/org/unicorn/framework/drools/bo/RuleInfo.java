package org.unicorn.framework.drools.bo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author  xiebin
 */
@Data
@ToString
public class RuleInfo  implements Serializable {
    /**
     * 规则id，全局唯一
     */
    private Long id;

    /**
     * 场景sceneKey，一个场景对应多个规则，一个场景对应一个业务场景，一个场景对应一个kmodule
     */
    private String sceneKey;

    /**
     * 规则内容，既drl文件内容
     */
    private String content;

}
