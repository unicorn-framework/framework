package org.unicorn.framework.codegen.utils;

import org.unicorn.framework.codegen.build.UnicornAutoGenerator;
import org.unicorn.framework.codegen.config.UnicornDataSourceConfig;
import org.unicorn.framework.codegen.convert.UnicornMysqlTypeConvert;

import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class CodeGeneratorUtil {
	public static void generator(String codePath, String author, UnicornDataSourceConfig dsc, StrategyConfig strategy,
			PackageConfig pc) {
		UnicornAutoGenerator mpg = new UnicornAutoGenerator();
		// 全局配置
		GlobalConfig gc = new GlobalConfig();
		gc.setOutputDir(codePath);
		gc.setFileOverride(true);
		gc.setActiveRecord(true);
		gc.setEnableCache(true);// XML 二级缓存
		gc.setBaseResultMap(true);// XML ResultMap
		gc.setBaseColumnList(true);// XML columList
		gc.setAuthor(author);

		// 自定义文件命名，注意 %s 会自动填充表实体属性！
		gc.setMapperName("%sMapper");
		gc.setXmlName("%sMapper");
		gc.setServiceName("%sService");
		gc.setServiceImplName("%sServiceImpl");
		gc.setControllerName("%sController");
		mpg.setGlobalConfig(gc);
		// 数据源配置
		mpg.setDataSource(dsc);
		// 策略配置
		mpg.setStrategy(strategy);
		// 包配置
		mpg.setPackageInfo(pc);
		// 执行生成
		mpg.execute();
	}

	public static void main(String[] args) throws InterruptedException {
		// 数据源配置
		UnicornDataSourceConfig dsc = new UnicornDataSourceConfig();
		dsc.setDbType(DbType.MYSQL);
		dsc.setTypeConvert(new UnicornMysqlTypeConvert());
		dsc.setDriverName("com.mysql.jdbc.Driver");
		dsc.setUrl(
				"jdbc:mysql://10.181.102.6:3306/internet_of_things?useUnicode=true&amp;characterEncoding=UTF-8&amp;generateSimpleParameterMetadata=true");
		dsc.setUsername("huanuo");
		dsc.setPassword("huanuo@2018");
		// 策略配置
		StrategyConfig strategy = new StrategyConfig();
		strategy.setTablePrefix(new String[] { "t_" });// 此处可以修改为您的表前缀
		strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
		strategy.setInclude(new String[] { "t_role", "t_user_info" }); // 需要生成的表
		// 包配置
		PackageConfig pc = new PackageConfig();
		pc.setParent("com.xb.demo");
		pc.setModuleName("test");
		pc.setMapper("dao");
		pc.setXml("dao");
		pc.setEntity("bo");

		CodeGeneratorUtil.generator("D:\\workspace\\unicorm-framework\\framework-codegen\\src\\main\\java", "xiebin",
				dsc, strategy, pc);
	}
}
