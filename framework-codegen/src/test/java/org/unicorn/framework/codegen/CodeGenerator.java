package org.unicorn.framework.codegen;

import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.unicorn.framework.codegen.config.UnicornDataSourceConfig;
import org.unicorn.framework.codegen.config.UnicornGlobalConfig;
import org.unicorn.framework.codegen.config.UnicornPackageConfig;
import org.unicorn.framework.codegen.config.UnicornStrategyConfig;
import org.unicorn.framework.codegen.util.CodeGeneratorUtil;

/**
 * @author zhanghaibo
 * @since 2019/6/28
 */
public class CodeGenerator {

    public static void main(String[] args) throws InterruptedException {
        // 数据源配置
        UnicornDataSourceConfig dsc = new UnicornDataSourceConfig();
        dsc.setUrl("jdbc:mysql://192.168.1.135:3306/l_order?useUnicode=true&characterEncoding=UTF-8&generateSimpleParameterMetadata=true");
        dsc.setUsername("tianrangf");
        dsc.setPassword("tianrangf@2019");
        // 策略配置
        UnicornStrategyConfig strategy = new UnicornStrategyConfig();
        strategy.setTablePrefix(new String[] { "t_" });// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        strategy.setInclude(new String[] {"t_order_details"}); // 需要生成的表
        // 包配置
        UnicornPackageConfig pc = new UnicornPackageConfig();
        pc.setParent("com.gf.order");
        pc.setModuleName("test");
        pc.setApiPackage(true);
        //全局配置
        UnicornGlobalConfig gc = new UnicornGlobalConfig();
        gc.setOutputDir("D:\\tmp");
        gc.setAuthor("zz");
        gc.setFileOverride(true);
        gc.setOpen(false);

        CodeGeneratorUtil.generator(gc,dsc, strategy, pc);
    }

}
