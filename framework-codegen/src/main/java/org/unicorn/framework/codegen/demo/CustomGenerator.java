package org.unicorn.framework.codegen.demo;

import org.unicorn.framework.codegen.UnicornAutoGenerator;
import org.unicorn.framework.codegen.config.UnicornDataSourceConfig;
import org.unicorn.framework.codegen.convert.UnicornMysqlTypeConvert;

import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class CustomGenerator {
	public static void main(String[] args) throws InterruptedException {
		UnicornAutoGenerator mpg = new UnicornAutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir("D:\\workspace\\unicorm-framework\\framework-codegen\\src\\main\\java");
        gc.setFileOverride(true);
        gc.setActiveRecord(true);
        gc.setEnableCache(true);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(true);// XML columList
        gc.setAuthor("xiebin");

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
         gc.setMapperName("%sMapper");
         gc.setXmlName("%sMapper");
         gc.setServiceName("%sService");
         gc.setServiceImplName("%sServiceImpl");
         gc.setControllerName("%sController");
         mpg.setGlobalConfig(gc);

        // 数据源配置
        UnicornDataSourceConfig dsc = new UnicornDataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setTypeConvert(new UnicornMysqlTypeConvert());

        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUrl("jdbc:mysql://10.181.102.6:3306/internet_of_things?useUnicode=true&amp;characterEncoding=UTF-8&amp;generateSimpleParameterMetadata=true");
        dsc.setUsername("huanuo");
        dsc.setPassword("huanuo@2018");
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
         strategy.setTablePrefix(new String[] { "t_"});// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
         strategy.setInclude(new String[] { "t_role" }); // 需要生成的表
         strategy.setInclude(new String[] { "t_user_info" }); // 需要生成的表
        // strategy.setExclude(new String[]{"test"}); // 排除生成的表
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.xb.demo");
        pc.setModuleName("test");
        pc.setMapper("dao");
        pc.setXml("dao");
        pc.setEntity("bo");
        mpg.setPackageInfo(pc);

        // 执行生成
        mpg.execute();
    }
}
