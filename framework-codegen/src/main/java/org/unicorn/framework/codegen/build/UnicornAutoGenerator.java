package org.unicorn.framework.codegen.build;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.assertj.core.util.Sets;
import org.unicorn.framework.codegen.bo.ClassBaseContext;
import org.unicorn.framework.codegen.bo.EntityContext;
import org.unicorn.framework.codegen.config.UnicornConstVal;
import org.unicorn.framework.codegen.config.UnicornTemplateConfig;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
			setServiceImpl(context,tableInfo,config);
			setControllerContext(context,tableInfo,config);
			setDtoContext(context,tableInfo,config);
			// api
            setApiServiceImpl(context,tableInfo,config);
            setApiControllerContext(context,tableInfo,config);
		}
		return ctxData;
	}

    public void setApiServiceImpl(VelocityContext context, UnicornTableInfo tableInfo, UnicornConfigBuilder config) {
        ClassBaseContext serviceImplContext=new ClassBaseContext();
        serviceImplContext.setName(tableInfo.getServiceImplName()+"Api");
        String pkg=config.getPackageInfo().get(UnicornConstVal.API_SERVICEIMPL)+"."+tableInfo.getEntityPath().toLowerCase();
        serviceImplContext.setPkg(pkg);
        serviceImplContext.setClassImportPath(pkg+".Api"+tableInfo.getServiceImplName());
        serviceImplContext.setBeanName("api"+serviceImplContext.getName());
        context.put("apiServiceImplContext", serviceImplContext);
    }

    public void setApiControllerContext(VelocityContext context, UnicornTableInfo tableInfo, UnicornConfigBuilder config) {
        ClassBaseContext controllerContext=new ClassBaseContext();
        controllerContext.setName(tableInfo.getControllerName()+"Api");
        String pkg=config.getPackageInfo().get(UnicornConstVal.API_CONTROLLER)+"."+tableInfo.getEntityPath().toLowerCase();
        controllerContext.setPkg(pkg);
        controllerContext.setClassImportPath(pkg+".Api"+tableInfo.getControllerName());
        context.put("apiControllerContext", controllerContext);
    }


    public void setDtoContext(VelocityContext context,UnicornTableInfo tableInfo,UnicornConfigBuilder config){
		ClassBaseContext dtoContext=new ClassBaseContext();
		dtoContext.setName(tableInfo.getPageRequestDto());
		String pkg=config.getPackageInfo().get(UnicornConstVal.DTO)+"."+tableInfo.getEntityPath().toLowerCase();
		dtoContext.setPkg(pkg);
		dtoContext.setClassImportPath(pkg+"."+tableInfo.getPageRequestDto());
		dtoContext.setBeanName(dtoContext.getName().substring(0,1).toLowerCase()+dtoContext.getName().substring(1));
		context.put("dtoContext", dtoContext);
	}

	public void setControllerContext(VelocityContext context,UnicornTableInfo tableInfo,UnicornConfigBuilder config){
		ClassBaseContext controllerContext=new ClassBaseContext();
		controllerContext.setName(tableInfo.getControllerName());
		String pkg=config.getPackageInfo().get(UnicornConstVal.CONTROLLER)+"."+tableInfo.getEntityPath().toLowerCase();
		controllerContext.setPkg(pkg);
		controllerContext.setClassImportPath(pkg+"."+tableInfo.getControllerName());
		context.put("controllerContext", controllerContext);
	}

	public void setServiceImpl(VelocityContext context,UnicornTableInfo tableInfo,UnicornConfigBuilder config){
		ClassBaseContext serviceImplContext=new ClassBaseContext();
		serviceImplContext.setName(tableInfo.getServiceImplName());
		String pkg=config.getPackageInfo().get(UnicornConstVal.SERVICEIMPL)+"."+tableInfo.getEntityPath().toLowerCase();
		serviceImplContext.setPkg(pkg);
		serviceImplContext.setClassImportPath(pkg+"."+tableInfo.getServiceImplName());
		serviceImplContext.setBeanName(serviceImplContext.getName().substring(0,1).toLowerCase()+serviceImplContext.getName().substring(1));
		context.put("serviceImplContext", serviceImplContext);
	}

	public void setMapper(VelocityContext context,UnicornTableInfo tableInfo,UnicornConfigBuilder config){

		ClassBaseContext mapperContext=new ClassBaseContext();
		mapperContext.setName(tableInfo.getMapperName());
		String pkg=config.getPackageInfo().get(UnicornConstVal.MAPPER)+"."+tableInfo.getEntityPath().toLowerCase();
		mapperContext.setPkg(pkg);
		mapperContext.setClassImportPath(pkg+"."+tableInfo.getMapperName());
		mapperContext.setBeanName(mapperContext.getName().substring(0,1).toLowerCase()+mapperContext.getName().substring(1));
		context.put("mapperContext", mapperContext);
	}


	public void setEntity(VelocityContext context,UnicornTableInfo tableInfo,UnicornConfigBuilder config){
		EntityContext entityContext=new EntityContext();
		String pkg=config.getPackageInfo().get(UnicornConstVal.ENTITY)+"."+tableInfo.getEntityPath().toLowerCase();
		entityContext.setName(tableInfo.getEntityName());
		entityContext.setPkg(pkg);
		entityContext.setImportSet(getImportSet(tableInfo));
		entityContext.setClassImportPath(pkg+"."+tableInfo.getEntityName());
		entityContext.setBeanName(entityContext.getName().substring(0,1).toLowerCase()+entityContext.getName().substring(1));
		context.put("entityContext", entityContext);
	}

	public Set<String> getImportSet(UnicornTableInfo tableInfo){
		Set<String> importSet= Sets.newHashSet();
		List<UnicornTableField> fieldList=tableInfo.getFields();
		for(UnicornTableField field:fieldList){
			String pkg=field.getColumnType().getImportPkg();
			if(StringUtils.isNotBlank(pkg)){
				if (pkg.equals(LocalDateTime.class.getName()) || pkg.equals(LocalDate.class.getName())
						|| pkg.equals(LocalTime.class.getName())) {
					importSet.add("com.fasterxml.jackson.annotation.JsonFormat");
					importSet.add("org.springframework.format.annotation.DateTimeFormat");

					field.setDateFlag(pkg.equals(LocalDateTime.class.getName())?"1":pkg.equals(LocalDate.class.getName())?"2":"3");
				}
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
