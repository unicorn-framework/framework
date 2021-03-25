package org.unicorn.framework.codegen.config;

import lombok.Data;
import org.assertj.core.util.Lists;
import org.unicorn.framework.codegen.enums.UnicornFileType;

import java.util.List;

/**
 * @author xiebin
 */
@Data
public class UnicornGlobalConfig {
    /**
     * 生成文件的输出目录【默认 D 盘根目录】
     */
    private String outputDir = "D://";

    /**
     * 是否覆盖已有文件
     */
    private boolean fileOverride = false;

    /**
     * 是否打开输出目录 默认不打开
     */
    private boolean open = false;

    /**
     * 是否在xml中添加二级缓存配置
     */
    private boolean enableCache = false;



    private List<UnicornFileType> fileTypes = Lists.newArrayList();

    {
        for (UnicornFileType type : UnicornFileType.values()) {
            fileTypes.add(type);
        }
    }

    /**
     * 开发人员
     */
    private String author;

    /**
     * 开启 ActiveRecord 模式
     */
    private boolean activeRecord = true;

    /**
     * 开启 BaseResultMap
     */
    private boolean baseResultMap = false;

    /**
     * 开启 baseColumnList
     */
    private boolean baseColumnList = false;
    /**
     * 各层文件名称方式，例如： %Controller 生成 UserController
     */
    private String mapperName = "%sMapper";
    private String xmlName = "%sMapper";
    private String serviceImplName = "%sService";
    private String controllerName = "%sController";
    private String pageDtoName = "%sPageRequestDto";

}
