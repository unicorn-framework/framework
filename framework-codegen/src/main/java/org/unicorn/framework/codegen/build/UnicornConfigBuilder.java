package org.unicorn.framework.codegen.build;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.unicorn.framework.codegen.config.UnicornConstVal;
import org.unicorn.framework.codegen.config.UnicornDataSourceConfig;
import org.unicorn.framework.codegen.config.UnicornGlobalConfig;
import org.unicorn.framework.codegen.config.UnicornPackageConfig;
import org.unicorn.framework.codegen.config.UnicornStrategyConfig;
import org.unicorn.framework.codegen.config.UnicornTemplateConfig;

import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.config.rules.QuerySQL;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
/**
 * 
 * @author xiebin
 *
 */
public class UnicornConfigBuilder  {
	
	 /**
     * SQL连接
     */
    private Connection connection;
    /**
     * SQL语句类型
     */
    private QuerySQL querySQL;
    private String superEntityClass;
    private String superMapperClass;
    /**
     * service超类定义
     */
    private String superServiceClass;
    private String superServiceImplClass;
    private String superControllerClass;
    /**
     * 数据库表信息
     */
    private List<UnicornTableInfo> tableInfoList;

    /**
     * 包配置详情
     */
    private Map<String, String> packageInfo;
    /**
     * 路径配置信息
     */
    private Map<String, String> pathInfo;

    /**
     * 模板路径配置信息
     */
    private UnicornTemplateConfig template;

    /**
     * 数据库配置
     */
    private UnicornDataSourceConfig dataSourceConfig;

    /**
     * 策略配置
     */
    private UnicornStrategyConfig strategyConfig;

    /**
     * 全局配置信息
     */
    private UnicornGlobalConfig globalConfig;

    /**
     * 在构造器中处理配置
     *
     * @param packageConfig    包配置
     * @param dataSourceConfig 数据源配置
     * @param strategyConfig   表配置
     * @param template         模板配置
     * @param globalConfig     全局配置
     */
    public UnicornConfigBuilder(UnicornPackageConfig packageConfig, UnicornDataSourceConfig dataSourceConfig, UnicornStrategyConfig strategyConfig,
    		UnicornTemplateConfig template, UnicornGlobalConfig globalConfig) {
        // 全局配置
        if (null == globalConfig) {
            this.globalConfig = new UnicornGlobalConfig();
        } else {
            this.globalConfig = globalConfig;
        }
        // 模板配置
        if (null == template) {
            this.template = new UnicornTemplateConfig();
        } else {
            this.template = template;
        }
       
        this.dataSourceConfig = dataSourceConfig;
        handlerDataSource(dataSourceConfig);
        // 策略配置
        if (null == strategyConfig) {
            this.strategyConfig = new UnicornStrategyConfig();
        } else {
            this.strategyConfig = strategyConfig;
        }
        handlerStrategy(this.strategyConfig);
        // 包配置
        if (null == packageConfig) {
            handlerPackage(this.template, this.globalConfig.getOutputDir(), new UnicornPackageConfig());
        } else {
            handlerPackage(this.template, this.globalConfig.getOutputDir(), packageConfig);
        }
    }


    /**
     * 处理包配置
     *
     * @param template  TemplateConfig
     * @param outputDir
     * @param config    PackageConfig
     */
    private void handlerPackage(UnicornTemplateConfig template, String outputDir, UnicornPackageConfig config) {
        packageInfo = new HashMap<>();
        packageInfo.put(UnicornConstVal.MODULENAME, config.getModuleName());
        packageInfo.put(UnicornConstVal.ENTITY, joinPackage(config.getParent(), config.getEntity()));
        packageInfo.put(UnicornConstVal.MAPPER, joinPackage(config.getParent(), config.getMapper()));
        packageInfo.put(UnicornConstVal.XML, joinPackage(config.getParent(), config.getXml()));
        packageInfo.put(UnicornConstVal.SERVICEIMPL, joinPackage(config.getParent(), config.getServiceImpl()));
        packageInfo.put(UnicornConstVal.CONTROLLER, joinPackage(config.getParent(), config.getController()));
        packageInfo.put(UnicornConstVal.DTO, joinPackage(config.getParent(), config.getDto()));

        // 生成路径信息
        pathInfo = new HashMap<>();
        if (StringUtils.isNotEmpty(template.getEntity())) {
            pathInfo.put(UnicornConstVal.ENTITY_PATH, joinPath(outputDir, packageInfo.get(UnicornConstVal.ENTITY)));
        }
        if (StringUtils.isNotEmpty(template.getMapper())) {
            pathInfo.put(UnicornConstVal.MAPPER_PATH, joinPath(outputDir, packageInfo.get(UnicornConstVal.MAPPER)));
        }
        if (StringUtils.isNotEmpty(template.getXml())) {
            pathInfo.put(UnicornConstVal.XML_PATH, joinPath(outputDir, packageInfo.get(UnicornConstVal.XML)));
        }
        if (StringUtils.isNotEmpty(template.getServiceImpl())) {
            pathInfo.put(UnicornConstVal.SERVICEIMPL_PATH, joinPath(outputDir, packageInfo.get(UnicornConstVal.SERVICEIMPL)));
        }
        if (StringUtils.isNotEmpty(template.getController())) {
            pathInfo.put(UnicornConstVal.CONTROLLER_PATH, joinPath(outputDir, packageInfo.get(UnicornConstVal.CONTROLLER)));
        }
        if (StringUtils.isNotEmpty(template.getDto())) {
            pathInfo.put(UnicornConstVal.DTO_PATH, joinPath(outputDir, packageInfo.get(UnicornConstVal.DTO)));
        }
    }

    /**
     * 处理数据源配置
     *
     * @param config DataSourceConfig
     */
    private void handlerDataSource(UnicornDataSourceConfig config) {
        connection = config.getConn();
        querySQL = getQuerySQL(config.getDbType());
    }

    /**
     * 处理数据库表 加载数据库表、列、注释相关数据集
     *
     * @param config StrategyConfig
     */
    private void handlerStrategy(UnicornStrategyConfig config) {
        processTypes(config);
        tableInfoList = getTablesInfo(config);
    }

    /**
     * 处理superClassName,IdClassType,IdStrategy配置
     *
     * @param config 策略配置
     */
    private void processTypes(UnicornStrategyConfig config) {
        if (StringUtils.isEmpty(config.getSuperServiceClass())) {
            superServiceClass = UnicornConstVal.SUPERD_SERVICE_CLASS;
        } else {
            superServiceClass = config.getSuperServiceClass();
        }
        if (StringUtils.isEmpty(config.getSuperServiceImplClass())) {
            superServiceImplClass = UnicornConstVal.SUPERD_SERVICEIMPL_CLASS;
        } else {
            superServiceImplClass = config.getSuperServiceImplClass();
        }
        if (StringUtils.isEmpty(config.getSuperMapperClass())) {
            superMapperClass = UnicornConstVal.SUPERD_MAPPER_CLASS;
        } else {
            superMapperClass = config.getSuperMapperClass();
        }
        superEntityClass = config.getSuperEntityClass();
        superControllerClass = config.getSuperControllerClass();
    }

    /**
     * 处理表对应的类名称
     *
     * @param tableList   表名称
     * @param strategy    命名策略
     * @param tablePrefix
     * @return 补充完整信息后的表
     */
    private List<UnicornTableInfo> processTable(List<UnicornTableInfo> tableList, NamingStrategy strategy, String[] tablePrefix) {
        for (UnicornTableInfo tableInfo : tableList) {
            tableInfo.setEntityName(strategyConfig, NamingStrategy.capitalFirst(processName(tableInfo.getName(), strategy, tablePrefix)));
           
            if (StringUtils.isNotEmpty(globalConfig.getMapperName())) {
                tableInfo.setMapperName(String.format(globalConfig.getMapperName(), tableInfo.getEntityName()));
            } else {
                tableInfo.setMapperName(tableInfo.getEntityName() + UnicornConstVal.MAPPER);
            }
            if (StringUtils.isNotEmpty(globalConfig.getXmlName())) {
                tableInfo.setXmlName(String.format(globalConfig.getXmlName(), tableInfo.getEntityName()));
            } else {
                tableInfo.setXmlName(tableInfo.getEntityName() + UnicornConstVal.MAPPER);
            }
            if (StringUtils.isNotEmpty(globalConfig.getServiceImplName())) {
                tableInfo.setServiceImplName(String.format(globalConfig.getServiceImplName(), tableInfo.getEntityName()));
            } else {
                tableInfo.setServiceImplName(tableInfo.getEntityName() + UnicornConstVal.SERVICEIMPL);
            }
            if (StringUtils.isNotEmpty(globalConfig.getControllerName())) {
                tableInfo.setControllerName(String.format(globalConfig.getControllerName(), tableInfo.getEntityName()));
            } else {
                tableInfo.setControllerName(tableInfo.getEntityName() + UnicornConstVal.CONTROLLER);
            }
            if (StringUtils.isNotEmpty(globalConfig.getPageDtoName())) {
                tableInfo.setPageRequestDto(String.format(globalConfig.getPageDtoName(), tableInfo.getEntityName()));
            } else {
                tableInfo.setPageRequestDto(tableInfo.getEntityName() + UnicornConstVal.DTO);
            }
        }
        return tableList;
    }

    /**
     * 获取所有的数据库表信息
     *
     * @return 表信息
     */
    private List<UnicornTableInfo> getTablesInfo(UnicornStrategyConfig config) {
        boolean isInclude = (null != config.getInclude() && config.getInclude().length > 0);
        boolean isExclude = (null != config.getExclude() && config.getExclude().length > 0);
        if (isInclude && isExclude) {
            throw new RuntimeException("<strategy> 标签中 <include> 与 <exclude> 只能配置一项！");
        }
        //所有的表信息
        List<UnicornTableInfo> tableList = new ArrayList<>();

        //需要反向生成或排除的表信息
        List<UnicornTableInfo> includeTableList = new ArrayList<>();
        List<UnicornTableInfo> excludeTableList = new ArrayList<>();

        //不存在的表名
        Set<String> notExistTables = new HashSet<>();

        NamingStrategy strategy = config.getNaming();
        PreparedStatement pstate = null;
        try {
            pstate = connection.prepareStatement(querySQL.getTableCommentsSql());
            ResultSet results = pstate.executeQuery();
            UnicornTableInfo tableInfo;
            while (results.next()) {
                String tableName = results.getString(querySQL.getTableName());
                if (StringUtils.isNotEmpty(tableName)) {
                    String tableComment = results.getString(querySQL.getTableComment());
                    tableInfo = new UnicornTableInfo();
                    tableInfo.setName(tableName);
                    tableInfo.setComment(tableComment);
                    if (isInclude) {
                        for (String includeTab : config.getInclude()) {
                            if (includeTab.equalsIgnoreCase(tableName)) {
                                includeTableList.add(tableInfo);
                            } else {
                                notExistTables.add(includeTab);
                            }
                        }
                    } else if (isExclude) {
                        for (String excludeTab : config.getExclude()) {
                            if (excludeTab.equalsIgnoreCase(tableName)) {
                                excludeTableList.add(tableInfo);
                            } else {
                                notExistTables.add(excludeTab);
                            }
                        }
                    }
                    List<UnicornTableField> fieldList = getListFields(tableInfo, strategy);
                    tableInfo.setFields(fieldList);
                    tableList.add(tableInfo);
                } else {
                    System.err.println("当前数据库为空！！！");
                }
            }
            // 将已经存在的表移除，获取配置中数据库不存在的表
            for (UnicornTableInfo tabInfo : tableList) {
                notExistTables.remove(tabInfo.getName());
            }

            if (notExistTables.size() > 0) {
                System.err.println("表 " + notExistTables + " 在数据库中不存在！！！");
            }

            // 需要反向生成的表信息
            if (isExclude) {
                tableList.removeAll(excludeTableList);
                includeTableList = tableList;
            }
            if (!isInclude && !isExclude){
                includeTableList = tableList;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            try {
                if (pstate != null) {
                    pstate.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return processTable(includeTableList, strategy, config.getTablePrefix());
    }


    /**
     * 判断主键是否为identity，目前仅对mysql进行检查
     *
     * @param results ResultSet
     * @return 主键是否为identity
     * @throws SQLException
     */
    private boolean isKeyIdentity(ResultSet results) throws SQLException {
        if (QuerySQL.MYSQL == this.querySQL) {
            String extra = results.getString("Extra");
            if ("auto_increment".equals(extra)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将字段信息与表信息关联
     *
     * @param tableName 表名称
     * @param strategy  命名策略
     * @return 表信息
     */
    private List<UnicornTableField> getListFields(UnicornTableInfo tableInfo,  NamingStrategy strategy){
       String tableName=tableInfo.getName();
    	boolean haveId = false;
        List<UnicornTableField> fieldList = new ArrayList<>();
        try (PreparedStatement pstate = connection.prepareStatement(String.format(querySQL.getTableFieldsSql(), tableName));
              ResultSet results = pstate.executeQuery()){
            while (results.next()) {
				UnicornTableField field = new UnicornTableField();
				String key = results.getString(querySQL.getFieldKey());
				// 避免多重主键设置，目前只取第一个找到ID，并放到list中的索引为0的位置
				boolean isId = StringUtils.isNotEmpty(key) && key.toUpperCase().equals("PRI");
                // 处理ID
                if (isId && !haveId) {
                    field.setKeyFlag(true);
                    if (isKeyIdentity(results)) {
                        field.setKeyIdentityFlag(true);
                    }
                    tableInfo.setPrimaryTableField(field);
                    haveId = true;
                } else {
                    field.setKeyFlag(false);
                }
                // 处理其它信息
                field.setName(results.getString(querySQL.getFieldName()));
                if (strategyConfig.includeSuperEntityColumns(field.getName())) {
                    // 跳过公共字段
                    continue;
                }
                field.setType(results.getString(querySQL.getFieldType()));
                field.setPropertyName(strategyConfig, processName(field.getName(), strategy));
                field.setColumnType(dataSourceConfig.getTypeConvert().processTypeConvert(processJdbcType(field.getType())));
                field.setComment(results.getString(querySQL.getFieldComment()));
                fieldList.add(field);
            }
        }catch (SQLException e){
            System.err.println("SQL Exception："+e.getMessage());
        }
        if(fieldList.size()>0){
        	fieldList.get(fieldList.size()-1).setLastFlag(true);
        }
        return fieldList;
    }

    
    private String processJdbcType(String orignalType){
    	return orignalType=orignalType.replaceAll("\\(.*\\)", "").toUpperCase();
    	
    }
    /**
     * 连接路径字符串
     *
     * @param parentDir   路径常量字符串
     * @param packageName 包名
     * @return 连接后的路径
     */
    private String joinPath(String parentDir, String packageName) {
        if (StringUtils.isEmpty(parentDir)) {
            parentDir = System.getProperty(UnicornConstVal.JAVA_TMPDIR);
        }
        if (!StringUtils.endsWith(parentDir, File.separator)) {
            parentDir += File.separator;
        }
        packageName = packageName.replaceAll("\\.", "\\" + File.separator);
        return parentDir + packageName;
    }

    /**
     * 连接父子包名
     *
     * @param parent     父包名
     * @param subPackage 子包名
     * @return 连接后的包名
     */
    private String joinPackage(String parent, String subPackage) {
        if (StringUtils.isEmpty(parent)) {
            return subPackage;
        }
        return parent + "." + subPackage;
    }

    /**
     * 处理字段名称
     *
     * @return 根据策略返回处理后的名称
     */
    private String processName(String name, NamingStrategy strategy) {
        return processName(name, strategy, null);
    }

    /**
     * 处理字段名称
     *
     * @param name
     * @param strategy
     * @param tablePrefix
     * @return 根据策略返回处理后的名称
     */
    private String processName(String name, NamingStrategy strategy, String[] tablePrefix) {
        boolean removePrefix = false;
        if (tablePrefix != null && tablePrefix.length >= 1) {
            removePrefix = true;
        }
        String propertyName;
        if (removePrefix) {
            if (strategy == NamingStrategy.underline_to_camel) {
                // 删除前缀、下划线转驼峰
                propertyName = NamingStrategy.removePrefixAndCamel(name, tablePrefix);
            } else {
                // 删除前缀
                propertyName = NamingStrategy.removePrefix(name, tablePrefix);
            }
        } else if (strategy == NamingStrategy.underline_to_camel) {
            // 下划线转驼峰
            propertyName = NamingStrategy.underlineToCamel(name);
        } else {
            // 不处理
            propertyName = name;
        }
        return propertyName;
    }

    /**
     * 获取当前的SQL类型
     *
     * @return DB类型
     */
    private QuerySQL getQuerySQL(DbType dbType) {
        for (QuerySQL qs : QuerySQL.values()) {
            if (qs.getDbType().equals(dbType.getValue())) {
                return qs;
            }
        }
        return QuerySQL.MYSQL;
    }
	
}
