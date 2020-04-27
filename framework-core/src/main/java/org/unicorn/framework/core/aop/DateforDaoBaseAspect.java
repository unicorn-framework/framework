
package org.unicorn.framework.core.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.unicorn.framework.base.base.AbstractService;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author xiebin
 */
@Slf4j
public class DateforDaoBaseAspect extends AbstractService {

    public void initTime(String propertyName, Object arg) {
        try {
            //获取参数类型class
            Class paramterClass = BeanUtils.findPropertyType(propertyName, arg.getClass());
            String dateSimpleType = paramterClass.getSimpleName();
            //获取方法
            Method method = BeanUtils.findMethod(arg.getClass(), getMethodName(propertyName), paramterClass);
            if (method == null) {
                return;
            }
            if ("LocalDateTime".equalsIgnoreCase(dateSimpleType)) {
                method.invoke(arg, LocalDateTime.now());
            }
            if ("Date".equalsIgnoreCase(dateSimpleType)) {
                method.invoke(arg, new Date());
            }
        } catch (Exception e) {
            log.warn("设置数据修改时间异常", e);
        }
    }

    /**
     * 根据属性名称获取方法名
     * @param propertyName
     * @return
     */
    private  String getMethodName(String propertyName){
        return "set"+propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    }

}
