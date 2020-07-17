package org.unicorn.framework.codegen.config;

import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import lombok.Data;

/**
 * @author xiebin
 */
@Data
public class UnicornTemplateConfig extends TemplateConfig{

	private String entity = "/generate_templates/backstage/entity.java.vm";

	private String serviceImpl = "/generate_templates/backstage/serviceImpl.java.vm";

    private String mapper = "/generate_templates/backstage/mapper.java.vm";

    private String xml = "/generate_templates/backstage/mapper.xml.vm";

    private String controller = "/generate_templates/backstage/controller.java.vm";
    
    private String dto = "/generate_templates/backstage/dto.java.vm";
    
//    private String entityExample = "/generate_templates/backstage/entityExample.java.vm";

    private String apiServiceImpl = "/generate_templates/api/serviceImpl.java.vm";

    private String apiController = "/generate_templates/api/controller.java.vm";


}
