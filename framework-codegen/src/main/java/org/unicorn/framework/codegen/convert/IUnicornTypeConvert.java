package org.unicorn.framework.codegen.convert;

import org.unicorn.framework.codegen.mapper.DbCloumnTypeInfo;

public interface IUnicornTypeConvert {
	/**
     * <p>
     * 执行类型转换
     * </p>
     *
     * @param fieldType
     *            字段类型
     * @return
     */
	DbCloumnTypeInfo processTypeConvert(String fieldType);
}
