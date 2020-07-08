package org.unicorn.framework.util.common;

import java.lang.reflect.Field;

/**
 * 合并两个对象的属性（属性值互补）
 *
 * @author xiebin
 * @since 2017-08-25
 */
public class BeanCombineMapping {


    public static <T> T mergeBean(T targetBean, T sourceBean) {
        if (targetBean == null || sourceBean == null) {
            return null;
        }
        Class<?> sourceBeanClass = sourceBean.getClass();
        Class<?> targetBeanClass = targetBean.getClass();

        Field[] sourceFields = sourceBeanClass.getDeclaredFields();
        Field[] targetFields = targetBeanClass.getDeclaredFields();
        for (int i = 0; i < sourceFields.length; i++) {
            Field sourceField = sourceFields[i];
            Field targetField = targetFields[i];
            sourceField.setAccessible(true);
            targetField.setAccessible(true);
            try {
                if (!(sourceField.get(sourceBean) == null)
                        && !"serialVersionUID".equals(sourceField.getName().toString())) {
                    Object sourceValue = sourceField.get(sourceBean);
                    Object targetValue = sourceField.get(targetBean);
                    if (sourceValue != null) {
                        if (!isSimpleObject(sourceValue)) {
                            sourceValue = mergeBean(targetValue, sourceValue);

                        }
                        targetField.set(targetBean, sourceValue);
                    }

                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return targetBean;
    }

    /**
     * 判断是否简单对象
     *
     * @param o
     * @return
     */
    private static boolean isSimpleObject(Object o) {
        Class<?> type = o.getClass();
        if (type.isPrimitive()) { // 基本类型
            return true;
        }

        // 不可更改的变量类型 如 String，Long
        if (type.equals(String.class)) {
            return true;
        }
        if (type.equals(Long.class)) {
            return true;
        }
        if (type.equals(Boolean.class)) {
            return true;
        }
        if (type.equals(Short.class)) {
            return true;
        }
        if (type.equals(Integer.class)) {
            return true;
        }
        if (type.equals(Character.class)) {
            return true;
        }
        if (type.equals(Float.class)) {
            return true;
        }
        if (type.equals(Double.class)) {
            return true;
        }
        if (type.equals(Byte.class)) {
            return true;
        }
        return false;
    }

}
