package org.unicorn.framework.codegen.build;

import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import org.unicorn.framework.codegen.config.UnicornStrategyConfig;

import java.util.*;
/**
 * 
 * @author xiebin
 *
 */
public class UnicornTableInfo {
	
	 private boolean convert;
	    private String name;
	    private String comment;

	    private String entityName;
	    private String mapperName;
	    private String xmlName;
	    private String serviceName;
	    private String serviceImplName;
	    private String controllerName;
	    private String pageRequestDto;

	    private List<UnicornTableField> fields;
	    private List<String> importPackages = new ArrayList<>();
	    private String fieldNames;

	    public boolean isConvert() {
	        return convert;
	    }

	    protected void setConvert(UnicornStrategyConfig strategyConfig) {
	        if (strategyConfig.containsTablePrefix(name)) {
	            // 包含前缀
	            this.convert = true;
	        } else if (strategyConfig.isCapitalModeNaming(name)) {
	            // 包含
	            this.convert = false;
	        } else {
	            // 转换字段
	            if (UnicornStrategyConfig.DB_COLUMN_UNDERLINE) {
	                // 包含大写处理
	                if (StringUtils.containsUpperCase(name)) {
	                    this.convert = true;
	                }
	            } else if (!entityName.equalsIgnoreCase(name)) {
	                this.convert = true;
	            }
	        }
	    }

	    public void setConvert(boolean convert) {
	        this.convert = convert;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getComment() {
	        return comment;
	    }

	    public void setComment(String comment) {
	        this.comment = comment;
	    }

	    public String getEntityPath() {
	        StringBuilder ep = new StringBuilder();
	        ep.append(entityName.substring(0, 1).toLowerCase());
	        ep.append(entityName.substring(1));
	        return ep.toString().toLowerCase();
	    }

	    public String getEntityName() {
	        return entityName;
	    }

	    public void setEntityName(UnicornStrategyConfig strategyConfig, String entityName) {
	        this.entityName = entityName;
	        this.setConvert(strategyConfig);
	    }

	    public String getMapperName() {
	        return mapperName;
	    }

	    public void setMapperName(String mapperName) {
	        this.mapperName = mapperName;
	    }

	    public String getXmlName() {
	        return xmlName;
	    }

	    public void setXmlName(String xmlName) {
	        this.xmlName = xmlName;
	    }

	    public String getServiceName() {
	        return serviceName;
	    }

	    public void setServiceName(String serviceName) {
	        this.serviceName = serviceName;
	    }

	    public String getServiceImplName() {
	        return serviceImplName;
	    }

	    public void setServiceImplName(String serviceImplName) {
	        this.serviceImplName = serviceImplName;
	    }

	    public String getControllerName() {
	        return controllerName;
	    }

	    public void setControllerName(String controllerName) {
	        this.controllerName = controllerName;
	    }

	    public List<UnicornTableField> getFields() {
	        return fields;
	    }

	    public void setFields(List<UnicornTableField> fields) {
	        if (CollectionUtils.isNotEmpty(fields)) {
	            this.fields = fields;
	            // 收集导入包信息
	            Set<String> pkgSet = new HashSet<>();
	            for (UnicornTableField field : fields) {
	                if (null != field.getColumnType() && null != field.getColumnType().getImportPkg()) {
	                    pkgSet.add(field.getColumnType().getImportPkg());
	                }
	                if (field.isKeyFlag()) {
	                    // 主键
	                    if (field.isConvert() || field.isKeyIdentityFlag()) {
	                        pkgSet.add("com.baomidou.mybatisplus.annotations.TableId");
	                    }
	                    // 自增
	                    if (field.isKeyIdentityFlag()) {
	                        pkgSet.add("com.baomidou.mybatisplus.enums.IdType");
	                    }
	                } else if (field.isConvert()) {
	                    // 普通字段
	                    pkgSet.add("com.baomidou.mybatisplus.annotations.TableField");
	                }
	            }
	            if (!pkgSet.isEmpty()) {
	                this.importPackages = new ArrayList<>(Arrays.asList(pkgSet.toArray(new String[]{})));
	            }
	        }
	    }

	    public List<String> getImportPackages() {
	        return importPackages;
	    }

	    public void setImportPackages(String pkg) {
	        importPackages.add(pkg);
	    }

	    /**
	     * 转换filed实体为xmlmapper中的basecolumn字符串信息
	     *
	     * @return
	     */
	    public String getFieldNames() {
	        if (StringUtils.isEmpty(fieldNames)) {
	            StringBuilder names = new StringBuilder();
	            for (int i = 0; i < fields.size(); i++) {
	            	UnicornTableField fd = fields.get(i);
	                if (i == fields.size() - 1) {
	                    names.append(cov2col(fd));
	                } else {
	                    names.append(cov2col(fd)).append(", ");
	                }
	            }
	            fieldNames = names.toString();
	        }
	        return fieldNames;
	    }

	    /**
	     * mapper xml中的字字段添加as
	     *
	     * @param field 字段实体
	     * @return 转换后的信息
	     */
	    private String cov2col(UnicornTableField field) {
	        if (null != field) {
//	            return field.isConvert() ? field.getName() + " AS " + field.getPropertyName() : field.getName();
				return field.getName();
	        }
	        return StringUtils.EMPTY;
	    }

	
	
	/**
	 * 主键信息
	 */
	private UnicornTableField primaryTableField;

	public UnicornTableField getPrimaryTableField() {
		return primaryTableField;
	}
 
	public void setPrimaryTableField(UnicornTableField primaryTableField) {
		this.primaryTableField = primaryTableField;
	}

	public String getPageRequestDto() {
		return pageRequestDto;
	}

	public void setPageRequestDto(String pageRequestDto) {
		this.pageRequestDto = pageRequestDto;
	}
	
	
	
}
