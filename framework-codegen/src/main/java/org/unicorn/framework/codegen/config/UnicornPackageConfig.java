package org.unicorn.framework.codegen.config;

import com.baomidou.mybatisplus.toolkit.StringUtils;

/**
 * @author  xiebin
 */
public class UnicornPackageConfig {
	 /**
     * 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
     */
    private String parent = "org.unicorn.frameworker";

    /**
     * 父包模块名。
     */
    private String moduleName = null;

    /**
     * Entity包名
     */
    private String entity = "bo";

    /**
     * Service包名
     */
    private String service = "service";

    /**
     * Service Impl包名
     */
    private String serviceImpl = "service.base";
    /**
     * Mapper包名
     */
    private String mapper = "dao";

    /**
     * Mapper XML包名
     */
    private String xml = "dao";

    /**
     * Controller包名
     */
    private String controller = "controller.base";
    /**
     * dto包名
     */
    private String dto = "dto";
    

    public String getParent() {
        if (StringUtils.isNotEmpty(moduleName)) {
            return parent + "." + moduleName;
        }
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getServiceImpl() {
        return serviceImpl;
    }

    public void setServiceImpl(String serviceImpl) {
        this.serviceImpl = serviceImpl;
    }

    public String getMapper() {
        return mapper;
    }

    public void setMapper(String mapper) {
        this.mapper = mapper;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getController() {
        if (StringUtils.isEmpty(controller)) {
            return "web";
        }
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

	public String getDto() {
		return dto;
	}

	public void setDto(String dto) {
		this.dto = dto;
	}
    
}
