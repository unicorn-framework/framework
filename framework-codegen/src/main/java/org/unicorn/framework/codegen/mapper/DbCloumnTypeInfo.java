package org.unicorn.framework.codegen.mapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
/**
 * @author  xiebin
 */
public class DbCloumnTypeInfo {
	 /**
     * jdbc类型
     */
    private final String jdbcType;

    /**
     * 包路径
     */
    private final String importPkg;
    
    /**
     * java类型
     */
    private final String javaType;
}
