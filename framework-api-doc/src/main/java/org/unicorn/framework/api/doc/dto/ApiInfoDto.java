package org.unicorn.framework.api.doc.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2020-07-29 16:48
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ApiInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private String groupName;
    private String keyword;
}
