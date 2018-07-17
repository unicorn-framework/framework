package org.unicorn.framework.codegen.convert;

import org.unicorn.framework.codegen.mapper.DbCloumnTypeInfo;
import org.unicorn.framework.codegen.mapper.MysqlDbCloumnTypeMapper;

public class UnicornMysqlTypeConvert implements IUnicornTypeConvert {
	 public DbCloumnTypeInfo processTypeConvert(String fieldType) {
	        
	        return MysqlDbCloumnTypeMapper._MAP.get(fieldType);
	    }
}
