package org.unicorn.framework.codegen.convert;

import org.unicorn.framework.codegen.mapper.UnicornDbCloumnType;

public class UnicornMysqlTypeConvert implements IUnicornTypeConvert {
	 public UnicornDbCloumnType processTypeConvert(String fieldType) {
	        
	        return UnicornDbCloumnType.getByKey(fieldType);
	    }
}
