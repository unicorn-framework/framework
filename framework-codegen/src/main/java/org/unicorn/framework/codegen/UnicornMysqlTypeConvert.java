package org.unicorn.framework.codegen;

public class UnicornMysqlTypeConvert implements IUnicornTypeConvert {
	 public UnicornDbCloumnType processTypeConvert(String fieldType) {
	        
	        return UnicornDbCloumnType.getByKey(fieldType);
	    }
}
