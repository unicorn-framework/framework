package org.unicorn.framework.codegen.util;

import org.unicorn.framework.codegen.build.UnicornAutoGenerator;
import org.unicorn.framework.codegen.config.UnicornDataSourceConfig;
import org.unicorn.framework.codegen.config.UnicornGlobalConfig;
import org.unicorn.framework.codegen.config.UnicornPackageConfig;
import org.unicorn.framework.codegen.config.UnicornStrategyConfig;

/**
 * @author xiebin
 */
public class CodeGeneratorUtil {
    public static void generator(UnicornGlobalConfig gc, UnicornDataSourceConfig dsc, UnicornStrategyConfig strategy,
                                 UnicornPackageConfig pc) {
        UnicornAutoGenerator mpg = new UnicornAutoGenerator();
        // 全局配置
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
}
