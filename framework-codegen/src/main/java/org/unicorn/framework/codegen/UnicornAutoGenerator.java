package org.unicorn.framework.codegen;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.unicorn.framework.codegen.config.UnicornTemplateConfig;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;

/**
 * 代码生成 注意自动生成的 dao和xml文件需要修改下名称 XXXDao。java XXXDao。xml
 * 
 * @author xiebin
 *
 */
public class UnicornAutoGenerator extends UnicornAbstractGenerator {

	private static final Log logger = LogFactory.getLog(AutoGenerator.class);

	public TemplateConfig getTemplate() {
		return new UnicornTemplateConfig();
	}

}
