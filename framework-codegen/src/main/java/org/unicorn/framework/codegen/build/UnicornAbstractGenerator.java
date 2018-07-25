package org.unicorn.framework.codegen.build;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.unicorn.framework.codegen.config.UnicornConstVal;
import org.unicorn.framework.codegen.config.UnicornDataSourceConfig;
import org.unicorn.framework.codegen.config.UnicornGlobalConfig;
import org.unicorn.framework.codegen.config.UnicornPackageConfig;
import org.unicorn.framework.codegen.config.UnicornStrategyConfig;
import org.unicorn.framework.codegen.config.UnicornTemplateConfig;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
/**
 * 
 * @author xiebin
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public abstract class UnicornAbstractGenerator {
	private static final Log logger = LogFactory.getLog(UnicornAbstractGenerator.class);
	
	private UnicornConfigBuilder config;
	
	/**
     * velocity引擎
     */
    private VelocityEngine engine;
	/**
	 * 数据源配置
	 */
	private UnicornDataSourceConfig dataSource;
	/**
	 * 数据库表配置
	 */
	private UnicornStrategyConfig strategy;
	/**
	 * 包 相关配置
	 */
	private UnicornPackageConfig packageInfo;
	/**
	 * 模板 相关配置
	 */
	private UnicornTemplateConfig template;
	/**
	 * 全局 相关配置
	 */
	private UnicornGlobalConfig globalConfig;

	/**
	 * 初始化配置
	 */
	protected void initConfig() {
		if (null == config) {
			config = new UnicornConfigBuilder(packageInfo, dataSource, strategy, getInitTemplate(), globalConfig);
		}
	}
	
	/**
	 * 分析数据
	 * @param config
	 * @return
	 */
	public  Map<String, VelocityContext> analyzeData(UnicornConfigBuilder config) {
		Map<String, VelocityContext> ctxData = new HashMap<>();
		//获取表信息
		List<UnicornTableInfo> tableList = config.getTableInfoList();
		//获取包信息
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		VelocityContext ctx;
		for (UnicornTableInfo tableInfo : tableList) {
			ctx = new VelocityContext();
			//设置作者
			ctx.put("author", config.getGlobalConfig().getAuthor());
			//设置生成日期
			ctx.put("date", date);
			//设置表信息
			ctx.put("table", tableInfo);
			ctx.put("entity", tableInfo.getEntityName());
			ctxData.put(tableInfo.getEntityName(), ctx);
		}
		setContextData(config,ctxData);
		return ctxData;
	}
	
	/**
	 * 获取初始化模板
	 * @return
	 */
	public abstract UnicornTemplateConfig getInitTemplate() ;
	/**
	 * 设置上下文信息
	 * @param config
	 * @param ctxData
	 * @return
	 */
	public abstract Map<String, VelocityContext> setContextData(UnicornConfigBuilder config,Map<String, VelocityContext> ctxData);
	/**
	 * 生成代码
	 */
	public void execute() {
		// 初始化配置
		initConfig();
		// 创建输出文件路径
		mkdirs(config.getPathInfo());
		// 获取上下文
		Map<String, VelocityContext> ctxData = analyzeData(config);
		// 循环生成文件
		for (Map.Entry<String, VelocityContext> ctx : ctxData.entrySet()) {
			batchOutput(ctx.getKey(), ctx.getValue());
		}
		// 打开输出目录
		open();
		logger.debug("==========================文件生成完成！！！==========================");
	}
	
	
	
	/**
	 * 打开输出目录
	 */
	private void open(){
		if (config.getGlobalConfig().isOpen()) {
			try {
				String osName = System.getProperty("os.name");
				if (osName != null) {
					if (osName.contains("Mac")) {
						Runtime.getRuntime().exec("open " + config.getGlobalConfig().getOutputDir());
					} else if (osName.contains("Windows")) {
						Runtime.getRuntime().exec("cmd /c start " + config.getGlobalConfig().getOutputDir());
					} else {
						logger.debug("文件输出目录:" + config.getGlobalConfig().getOutputDir());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 处理输出目录
	 *
	 * @param pathInfo
	 *            路径信息
	 */
	private void mkdirs(Map<String, String> pathInfo) {
		for (Map.Entry<String, String> entry : pathInfo.entrySet()) {
			File dir = new File(entry.getValue());
			if (!dir.exists()) {
				boolean result = dir.mkdirs();
				if (result) {
					logger.debug("创建目录： [" + entry.getValue() + "]");
				}
			}
		}
	}
	
	
	/**
	 * 合成上下文与模板 留给定制开发的钩子
	 *
	 * @param context
	 *            vm上下文
	 */
	public void batchOutput(String entityName, VelocityContext context) {
		try {
			UnicornTableInfo tableInfo = (UnicornTableInfo) context.get("table");
			Map<String, String> pathInfo = config.getPathInfo();
			String entityFile=getFilePath(tableInfo,pathInfo.get(UnicornConstVal.ENTITY_PATH),tableInfo.getEntityName(),UnicornConstVal.JAVA_SUFFIX);
			String mapperFile=getFilePath(tableInfo,pathInfo.get(UnicornConstVal.MAPPER_PATH),tableInfo.getMapperName(),UnicornConstVal.JAVA_SUFFIX);
			String xmlFile=getFilePath(tableInfo,pathInfo.get(UnicornConstVal.XML_PATH),tableInfo.getXmlName(),UnicornConstVal.XML_SUFFIX);
			String implFile=getFilePath(tableInfo,pathInfo.get(UnicornConstVal.SERVICEIMPL_PATH),tableInfo.getServiceImplName(),UnicornConstVal.JAVA_SUFFIX);
			String controllerFile=getFilePath(tableInfo,pathInfo.get(UnicornConstVal.CONTROLLER_PATH),tableInfo.getControllerName(),UnicornConstVal.JAVA_SUFFIX);
			String dtoFile=getFilePath(tableInfo,pathInfo.get(UnicornConstVal.DTO_PATH),tableInfo.getPageRequestDto(),UnicornConstVal.JAVA_SUFFIX);
			UnicornTemplateConfig template = config.getTemplate();
            //创建entity文件
			createFile(context,template.getEntity(),entityFile);
			//创建mapper接口文件
			createFile(context,template.getMapper(),mapperFile);
			//创建mapper映射文件
			createFile(context,template.getXml(),xmlFile);
			//创建接口实现文件
			createFile(context,template.getServiceImpl(),implFile);
			//创建controller文件
			createFile(context,template.getController(),controllerFile);
			//创建dto文件
			createFile(context,template.getDto(),dtoFile);
		} catch (IOException e) {
			logger.error("无法创建文件，请检查配置信息！", e);
		}
	}
    /**
     * 创建文件
     * @param context
     * @param templatePath
     * @param filePath
     * @throws IOException
     */
	public void createFile(VelocityContext context,String templatePath,String filePath) throws IOException{
		// 根据override标识来判断是否需要创建文件
		if (isCreate(filePath)) {
			vmToFile(context, templatePath, filePath);
		}
	}
	
	
	
	
	/**
	 * 获取文件路径
	 * @param tableInfo
	 * @param pathInfo
	 * @param fileName
	 * @param fileSuffix
	 * @return
	 */
	private  String getFilePath(UnicornTableInfo tableInfo,String pathInfo,  String fileName,String fileSuffix){
		return pathInfo +File.separator+tableInfo.getEntityPath()+File.separator+fileName+fileSuffix;
		
	}
	/**
	 * 检测文件是否存在
	 *
	 * @return 是否
	 */
	private boolean isCreate(String filePath) {
		File file = new File(filePath);
		return !file.exists() || config.getGlobalConfig().isFileOverride();
	}
	
	/**
	 * 将模板转化成为文件
	 *
	 * @param context
	 *            内容对象
	 * @param templatePath
	 *            模板文件
	 * @param outputFile
	 *            文件生成的目录
	 */
	private void vmToFile(VelocityContext context, String templatePath, String outputFile) throws IOException {
		if (StringUtils.isEmpty(templatePath)) {
			return;
		}
		VelocityEngine velocity = getVelocityEngine();
		Template template = velocity.getTemplate(templatePath, UnicornConstVal.UTF8);
		File file = new File(outputFile);
		if (!file.getParentFile().exists()) {
			// 如果文件所在的目录不存在，则创建目录
			if (!file.getParentFile().mkdirs()) {
				logger.debug("创建文件所在的目录失败!");
				return;
			}
		}
		FileOutputStream fos = new FileOutputStream(outputFile);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, UnicornConstVal.UTF8));
		template.merge(context, writer);
		writer.close();
		logger.debug("模板:" + templatePath + ";  文件:" + outputFile);
	}
	
	/**
	 * 设置模版引擎，主要指向获取模版路径
	 */
	private VelocityEngine getVelocityEngine() {
		if (engine == null) {
			Properties p = new Properties();
			p.setProperty(UnicornConstVal.VM_LOADPATH_KEY, UnicornConstVal.VM_LOADPATH_VALUE);
			p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, "");
			p.setProperty(Velocity.ENCODING_DEFAULT, UnicornConstVal.UTF8);
			p.setProperty(Velocity.INPUT_ENCODING, UnicornConstVal.UTF8);
			p.setProperty(Velocity.OUTPUT_ENCODING, UnicornConstVal.UTF8);
			p.setProperty("file.resource.loader.unicode", "true");
			engine = new VelocityEngine(p);
		}
		return engine;
	}

}
