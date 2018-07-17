package org.unicorn.framework.codegen.build;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.velocity.VelocityContext;
import org.assertj.core.util.Sets;
import org.unicorn.framework.codegen.bo.ControllerContext;
import org.unicorn.framework.codegen.bo.EntityContext;
import org.unicorn.framework.codegen.bo.MapperContext;
import org.unicorn.framework.codegen.bo.ServiceContext;
import org.unicorn.framework.codegen.bo.ServiceImplContext;
import org.unicorn.framework.codegen.config.UnicornConstVal;
import org.unicorn.framework.codegen.config.UnicornTemplateConfig;

/**
 * 
 * @author xiebin
 *
 */
public class UnicornAutoGenerator extends UnicornAbstractGenerator {
	
	/**
	 * 设置上下文
	 */
	@Override
	public Map<String, VelocityContext> setContextData(UnicornConfigBuilder config,Map<String, VelocityContext> ctxData){
		Set<String> keySet=ctxData.keySet();
		for(String key:keySet){
			VelocityContext context=ctxData.get(key);
			UnicornTableInfo tableInfo=(UnicornTableInfo)context.get("table");
			setEntity(context,tableInfo,config);
			setMapper(context,tableInfo,config);
			setService(context,tableInfo,config);
			setServiceImpl(context,tableInfo,config);
			setControllerContext(context,tableInfo,config);
		}
		 return ctxData;
	}
	
	public void setControllerContext(VelocityContext context,UnicornTableInfo tableInfo,UnicornConfigBuilder config){
		ControllerContext controllerContext=new ControllerContext();
		controllerContext.setName(tableInfo.getControllerName());
		String pkg=config.getPackageInfo().get(UnicornConstVal.CONTROLLER)+"."+tableInfo.getEntityPath();
		controllerContext.setPkg(pkg);
		controllerContext.setClassImportPath(pkg+"."+tableInfo.getControllerName());
		context.put("controllerContext", controllerContext);
	}
	
	public void setServiceImpl(VelocityContext context,UnicornTableInfo tableInfo,UnicornConfigBuilder config){
		ServiceImplContext serviceImplContext=new ServiceImplContext();
		serviceImplContext.setName(tableInfo.getServiceImplName());
		String pkg=config.getPackageInfo().get(UnicornConstVal.SERVICEIMPL)+"."+tableInfo.getEntityPath();
		serviceImplContext.setPkg(pkg);
		serviceImplContext.setClassImportPath(pkg+"."+tableInfo.getServiceImplName());
		context.put("serviceImplContext", serviceImplContext);
	}
	public void setService(VelocityContext context,UnicornTableInfo tableInfo,UnicornConfigBuilder config){
		ServiceContext serviceContext=new ServiceContext();
		serviceContext.setName(tableInfo.getServiceName());
		String pkg=config.getPackageInfo().get(UnicornConstVal.SERIVCE)+"."+tableInfo.getEntityPath();
		serviceContext.setPkg(pkg);
		serviceContext.setClassImportPath(pkg+"."+tableInfo.getServiceName());
		serviceContext.setBeanName(serviceContext.getName().substring(0,1).toLowerCase()+serviceContext.getName().substring(1));
		context.put("serviceContext", serviceContext);
	}
	
	
	public void setMapper(VelocityContext context,UnicornTableInfo tableInfo,UnicornConfigBuilder config){
		
		MapperContext mapperContext=new MapperContext();
		mapperContext.setName(tableInfo.getMapperName());
		String pkg=config.getPackageInfo().get(UnicornConstVal.MAPPER)+"."+tableInfo.getEntityPath();
		mapperContext.setPkg(pkg);
		mapperContext.setClassImportPath(pkg+"."+tableInfo.getMapperName());
		mapperContext.setBeanName(mapperContext.getName().substring(0,1).toLowerCase()+mapperContext.getName().substring(1));
		context.put("mapperContext", mapperContext);
	}
	
	
	public void setEntity(VelocityContext context,UnicornTableInfo tableInfo,UnicornConfigBuilder config){
		EntityContext entityContext=new EntityContext();
		String pkg=config.getPackageInfo().get(UnicornConstVal.ENTITY)+"."+tableInfo.getEntityPath();
		entityContext.setName(tableInfo.getEntityName());
		entityContext.setPkg(pkg);
		entityContext.setImportSet(getImportSet(tableInfo));
		entityContext.setClassImportPath(pkg+"."+tableInfo.getEntityName());
		context.put("entityContext", entityContext);
	}
	
	public Set<String> getImportSet(UnicornTableInfo tableInfo){
		Set<String> importSet=Sets.newHashSet();
		List<UnicornTableField> fieldList=tableInfo.getFields();
		for(UnicornTableField field:fieldList){
			String pkg=field.getColumnType().getImportPkg();
			if(org.apache.commons.lang3.StringUtils.isNotBlank(pkg)){
				importSet.add(pkg);
			}
		}
		return importSet;
	}
	@Override
	public UnicornTemplateConfig getInitTemplate() {
		return new UnicornTemplateConfig();
	}

}
