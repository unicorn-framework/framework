package org.unicorn.framework.codegen.convert;

import org.unicorn.framework.codegen.mapper.DbCloumnTypeInfo;
import org.unicorn.framework.codegen.mapper.MysqlDbCloumnTypeMapper;
/**
 * @author  xiebin
 */
public class UnicornMysqlTypeConvert implements IUnicornTypeConvert {
	@Override
	 public DbCloumnTypeInfo processTypeConvert(String fieldType) {
	        
	        return MysqlDbCloumnTypeMapper._MAP.get(fieldType);
	    }
}
