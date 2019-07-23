package org.unicorn.framework.codegen.mapper;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author xiebin
 *
 */
public class MysqlDbCloumnTypeMapper {
	public static Map<String,DbCloumnTypeInfo>  _MAP=new HashMap<>();
	static {
		_MAP.put("VARCHAR", new DbCloumnTypeInfo("VARCHAR", null,"java.lang.String"));
		_MAP.put("CHAR", new DbCloumnTypeInfo("CHAR", null,"java.lang.String"));
		_MAP.put("LONGVARCHAR", new DbCloumnTypeInfo("LONGVARCHAR", null,"java.lang.String"));
		_MAP.put("TEXT", new DbCloumnTypeInfo("VARCHAR", null,"java.lang.String"));
		_MAP.put("LONGTEXT", new DbCloumnTypeInfo("VARCHAR", null,"java.lang.String"));
		_MAP.put("TINYINT", new DbCloumnTypeInfo("INTEGER", null,"java.lang.Integer"));
		_MAP.put("SMALLINT", new DbCloumnTypeInfo("INTEGER", null,"java.lang.Integer"));
		_MAP.put("INT", new DbCloumnTypeInfo("INTEGER", null,"java.lang.Integer"));
		_MAP.put("INTEGER", new DbCloumnTypeInfo("INTEGER", null,"java.lang.Integer"));
		_MAP.put("BIGINT", new DbCloumnTypeInfo("BIGINT", null,"java.lang.Long"));
		_MAP.put("REAL", new DbCloumnTypeInfo("REAL", null,"java.lang.Float"));
		_MAP.put("FLOAT", new DbCloumnTypeInfo("FLOAT", null,"java.lang.Float"));
		_MAP.put("DOUBLE", new DbCloumnTypeInfo("DOUBLE", null,"java.lang.Double"));
		_MAP.put("BIT", new DbCloumnTypeInfo("BIT", null,"java.lang.Boolean"));
		_MAP.put("DATE", new DbCloumnTypeInfo("DATE", "java.time.LocalDate","java.util.LocalDate"));
		_MAP.put("TIME", new DbCloumnTypeInfo("TIME", "java.time.LocalTime","java.sql.LocalTime"));
		_MAP.put("TIMESTAMP", new DbCloumnTypeInfo("TIMESTAMP", "java.time.LocalDateTime","java.util.LocalDateTime"));
		_MAP.put("DATETIME", new DbCloumnTypeInfo("DATETIME", "java.time.LocalDateTime","java.util.LocalDateTime"));
		_MAP.put("BLOB", new DbCloumnTypeInfo("BLOB", "java.sql.Blob", "java.sql.Blob"));
		_MAP.put("CLOB", new DbCloumnTypeInfo("CLOB", "java.sql.Clob", "java.sql.Clob"));
		_MAP.put("NUMERIC", new DbCloumnTypeInfo("NUMERIC", "java.math.BigInteger", "java.math.BigInteger"));
		_MAP.put("DECIMAL", new DbCloumnTypeInfo("DECIMAL", "java.math.BigInteger", "java.math.BigInteger"));
	}
	
}
