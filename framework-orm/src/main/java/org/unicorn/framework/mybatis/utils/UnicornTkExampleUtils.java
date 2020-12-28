package org.unicorn.framework.mybatis.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2020-07-16 12:24
 */
public class UnicornTkExampleUtils {

    /**
     * 设置查询条件
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Example createExample(Class<T> clazz) {
        return createExample(clazz, null);
    }


    /**
     * 返回example ，返回指定字段
     *
     * @param clazz
     * @param excludeProperties
     * @param <T>
     * @return
     */
    public static <T> Example createExampleExclude(Class<T> clazz, List<String> excludeProperties) {
        return createExample(clazz, getSelectProperties(clazz, excludeProperties));
    }


    /**
     * 返回example ，指定返回字段
     *
     * @param clazz
     * @param selectProperties
     * @param <T>
     * @return
     */
    public static <T> Example createExample(Class<T> clazz, String... selectProperties) {
        Example example = new Example(clazz);
        if (selectProperties != null && selectProperties.length > 0) {
            example.selectProperties(selectProperties);
        }
        return example;
    }

    /**
     * 设置查询条件
     * 只设置equal
     *
     * @param clazz
     * @param t
     * @param <T>
     * @return
     */
    public static <T> Example whereAllColumnsExample(Class<T> clazz, T t) {

        return whereAllColumnsExample(clazz, t, null);
    }

    /**
     * 返回指定字段列表
     *
     * @param clazz
     * @param t
     * @param selectProperties
     * @param <T>
     * @return
     */
    public static <T> Example whereAllColumnsExample(Class<T> clazz, T t, String... selectProperties) {
        Example example = createExample(clazz, selectProperties);
        Set<EntityColumn> columnSet = EntityHelper.getColumns(example.getEntityClass());
        BeanWrapper wrapper = new BeanWrapperImpl(t);
        Example.Criteria criteria = example.createCriteria();
        for (EntityColumn entityColumn : columnSet) {
            criteria.andEqualTo(entityColumn.getProperty(), wrapper.getPropertyValue(entityColumn.getProperty()));
        }
        return example;
    }

    /**
     * 返回 ,排除指定字段列表
     *
     * @param clazz
     * @param t
     * @param excludeProperties
     * @param <T>
     * @return
     */
    public static <T> Example whereAllColumnsExampleExclude(Class<T> clazz, T t, List<String> excludeProperties) {
        return whereAllColumnsExample(clazz, t, getSelectProperties(clazz, excludeProperties));
    }

    /**
     * 返回需要返回的字段数组
     *
     * @param clazz
     * @param excludeProperties
     * @param <T>
     * @return
     */
    private static <T> String[] getSelectProperties(Class<T> clazz, List<String> excludeProperties) {
        Set<EntityColumn> columnSet = EntityHelper.getColumns(clazz);
        List<String> properties = columnSet.stream()
                .map(entityColumn -> entityColumn.getProperty()).collect(Collectors.toList())
                .stream().filter(property -> !excludeProperties.contains(property)).collect(Collectors.toList());
        return properties.toArray(new String[]{});
    }
}
