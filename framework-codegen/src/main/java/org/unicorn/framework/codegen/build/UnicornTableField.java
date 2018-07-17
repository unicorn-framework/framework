package org.unicorn.framework.codegen.build;

import org.unicorn.framework.codegen.mapper.DbCloumnTypeInfo;
import org.unicorn.framework.codegen.mapper.UnicornDbCloumnType;

import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.toolkit.StringUtils;
/**
 * 
 * @author xiebin
 *
 */
public class UnicornTableField {
	    private boolean convert;
	    private boolean keyFlag;
	    /**
	     * 主键是否为自增类型
	     */
	    private boolean keyIdentityFlag;
	    /**
	     * 是否最后一个属性
	     */
	    private boolean lastFlag;
	    private String name;
	    private String type;
	    private String propertyName;
	    private DbCloumnTypeInfo columnType;
	    private String comment;

	    public boolean isConvert() {
	        return convert;
	    }

	    protected void setConvert(StrategyConfig strategyConfig) {
	        if (strategyConfig.isCapitalModeNaming(name)) {
	            this.convert = false;
	        } else {
	            // 转换字段
	            if (StrategyConfig.DB_COLUMN_UNDERLINE) {
	                // 包含大写处理
	                if (StringUtils.containsUpperCase(name)) {
	                    this.convert = true;
	                }
	            } else if (!name.equals(propertyName)) {
	                this.convert = true;
	            }
	        }
	    }

	    public void setConvert(boolean convert) {
	        this.convert = convert;
	    }

	    public boolean isKeyFlag() {
	        return keyFlag;
	    }

	    public void setKeyFlag(boolean keyFlag) {
	        this.keyFlag = keyFlag;
	    }

	    public boolean isKeyIdentityFlag() {
	        return keyIdentityFlag;
	    }

	    public void setKeyIdentityFlag(boolean keyIdentityFlag) {
	        this.keyIdentityFlag = keyIdentityFlag;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getType() {
	        return type;
	    }

	    public void setType(String type) {
	        this.type = type;
	    }

	    public String getPropertyName() {
	        return propertyName;
	    }

	    public void setPropertyName(StrategyConfig strategyConfig, String propertyName) {
	        this.propertyName = propertyName;
	        this.setConvert(strategyConfig);
	    }

	    public DbCloumnTypeInfo getColumnType() {
	        return columnType;
	    }

	    public void setColumnType(DbCloumnTypeInfo columnType) {
	        this.columnType = columnType;
	    }

	    public String getPropertyType() {
	    	String javaType=null;
	        if (null != columnType) {
	        	javaType= columnType.getJavaType();
	        }
	        if(org.apache.commons.lang3.StringUtils.isNotBlank(javaType)){
	        	javaType=javaType.replaceAll(".*\\.", "");
	        }
	        return javaType;
	    }

	    public String getComment() {
	        return comment;
	    }

	    public void setComment(String comment) {
	        this.comment = comment;
	    }

	    public String getCapitalName() {
	        return propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
	    }

		public boolean isLastFlag() {
			return lastFlag;
		}

		public void setLastFlag(boolean lastFlag) {
			this.lastFlag = lastFlag;
		}
	    
}
