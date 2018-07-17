package org.unicorn.framework.codegen;

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
	UnicornDbCloumnType processTypeConvert(String fieldType);
}
