package org.unicron.framework.mybatis.generator;

import org.mybatis.generator.api.ShellRunner;
/**
 * 代码生成 注意自动生成的 dao和xml文件需要修改下名称  XXXDao。java  XXXDao。xml
 * @author xiebin
 *
 */
public class CodeGenerator {
	public static void main(String[] args) {
		args = new String[] { "-configfile", "src\\main\\resources\\mybatis-generator.xml", "-overwrite" };
		ShellRunner.main(args);
	}
}
