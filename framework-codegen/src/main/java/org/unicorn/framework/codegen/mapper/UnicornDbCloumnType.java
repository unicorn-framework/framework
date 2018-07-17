package org.unicorn.framework.codegen.mapper;

import java.util.HashMap;
import java.util.Map;

public enum UnicornDbCloumnType {
	VARCHAR("VARCHAR", null,"java.lang.String"),
	CHAR("CHAR", null,"java.lang.String"),
	LONGVARCHAR("LONGVARCHAR", null,"java.lang.String"),
	TEXT("TEXT", null,"java.lang.String"),
	LONGTEXT("LONGTEXT", null,"java.lang.String"),
	
	
	TINYINT("TINYINT", null,"java.lang.Integer"),
	SMALLINT("SMALLINT", null,"java.lang.Integer"),
	INT("INTEGER", null,"java.lang.Integer"),
	INTEGER("INTEGER", null,"java.lang.Integer"),
	BIGINT("BIGINT", null,"java.lang.Long"),
	
	REAL("REAL", null,"java.lang.Float"),
    FLOAT("FLOAT", null,"java.lang.Float"),
    
    DOUBLE("DOUBLE", null,"java.lang.Double"),
    BIT("BIT", null,"java.lang.Boolean"),
    BOOLEAN("BOOLEAN", null,"java.lang.Boolean"),
    
    DATE("DATE", "java.util.Date","java.util.Date"),
    TIME("TIME", "java.sql.Time","java.sql.Time"),
    TIMESTAMP("TIMESTAMP", "java.util.Date","java.util.Date"),
    DATETIME("DATETIME", "java.util.Date","java.util.Date"),
    
    BLOB("BLOB", "java.sql.Blob", "java.sql.Blob"),
    CLOB("CLOB", "java.sql.Clob","java.sql.Clob"),
    
    NUMERIC("NUMERIC", "java.math.BigInteger", "java.math.BigInteger"),
    DECIMAL("DECIMAL", "java.math.BigInteger", "java.math.BigInteger"),
    ;

	
	private  static Map<String,UnicornDbCloumnType> _MAP=new HashMap<>();
	static{
		UnicornDbCloumnType unicornDbCloumnTypes[]=UnicornDbCloumnType.values();
		for(UnicornDbCloumnType unicornDbCloumnType :unicornDbCloumnTypes){
			_MAP.put(unicornDbCloumnType.toString(), unicornDbCloumnType);
			
		}
	}
	public  static UnicornDbCloumnType getByKey(String key){
		return _MAP.get(key);
	}
    /**
     * jdbc类型
     */
    private final String jdbcType;

    /**
     * 包路径
     */
    private final String pkg;
    
    /**
     * java类型
     */
    private final String javaType;

    UnicornDbCloumnType(final String type, final String pkg,String javaType) {
        this.jdbcType = type;
        this.pkg = pkg;
        this. javaType=javaType;
    }

    public String getJdbcType() {
//    	if("INT".equals(this.jdbcType)){
//    		return "INTEGER";
//    	}
        return this.jdbcType;
    }

    public String getPkg() {
        return this.pkg;
    }
    public String getJavaType() {
        return this.javaType;
    }
}
