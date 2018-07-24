package org.unicorn.framework.codegen.config;

import com.baomidou.mybatisplus.generator.config.TemplateConfig;

public class UnicornTemplateConfig extends TemplateConfig{
	
	
	 
	private String entity = "/mytemplates/entity.java.vm";;

	private String serviceImpl = "/mytemplates/serviceImpl.java.vm";

    private String mapper = "/mytemplates/mapper.java.vm";

    private String xml = "/mytemplates/mapper.xml.vm";

    private String controller ="/mytemplates/controller.java.vm";
    
    private String dto ="/mytemplates/dto.java.vm";

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
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
