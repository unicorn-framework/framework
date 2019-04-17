package org.unicorn.framework.codegen.config;

import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import lombok.Data;

/**
 * @author xiebin
 */
@Data
public class UnicornTemplateConfig extends TemplateConfig{

	private String entity = "/mytemplates/entity.java.vm";

	private String serviceImpl = "/mytemplates/serviceImpl.java.vm";

    private String mapper = "/mytemplates/mapper.java.vm";

    private String xml = "/mytemplates/mapper.xml.vm";

    private String controller ="/mytemplates/controller.java.vm";
    
    private String dto ="/mytemplates/dto.java.vm";
    
    private String entityExample ="/mytemplates/entityExample.java.vm";

}
