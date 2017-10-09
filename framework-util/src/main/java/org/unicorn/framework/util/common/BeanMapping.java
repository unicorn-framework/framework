package org.unicorn.framework.util.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 不同类型的bean对象属性映射转化
 * 基于Dozer封装, 对使用者透明
 * @author xiebin
 * @since 2017-08-25
 */
public class BeanMapping {

    private final static Logger logger = LoggerFactory.getLogger(BeanMapping.class);

    private static Mapper __instance__;

    private static Lock lock = new ReentrantLock();

    private BeanMapping() {

    }

    private static Mapper getInstance() {
        // TODO 支持XML配置或者传入映射关系

        if (__instance__ == null) {
            lock.lock();
            if (__instance__ == null) {
                __instance__ = new DozerBeanMapper();
            }
            lock.unlock();
        }
        return __instance__;
    }


    /**
     * 源对象 {@code source} 转换成目标bean对象 {@code D}; 将名称一致的属性进行转换; 转换完成后进行回调后续处理
     * @param source 源Bean
     * @param dstClass 目标Bean的class
     * @param biConsumer 目标Bean转换完成后的回调操作
     * @param <S> 原Bean的泛型定义
     * @param <D> 目标Bean的泛型定义
     * @return 目标Bean对象
     * @throws PendingException 
     * @throws BeanMappingException
     */
    public static <S, D> D map(S source, Class<D> dstClass)  {
        if (source == null) {
            return null;
        }
        try {
            D dstObject = getInstance().map(source, dstClass);
            return dstObject;
        } catch (MappingException e) {
            logger.error("对象映射出错, 原对象类型: {}, 目标对象类型: {}", source.getClass(), dstClass);
             e.printStackTrace(); 
             return null;
        }
    }

    /**
     * 源对象集合 {@code source} 转换成目标bean对象后添加到传入的集合中 {@code D}; 将名称一致的属性进行转换
     * @param source 源对象集合
     * @param destination 目标对象集合
     * @param dstClass 目标Bean的class
     * @param <S> 原Bean的泛型定义
     * @param <D> 目标Bean的泛型定义
     * @throws Exception 
     * @throws BeanMappingException
     */
    public static <S, D> void map(Collection<S> source, Collection<D> destination, Class<D> dstClass) throws Exception {
    	 for (S s : source) {
             destination.add(map(s, dstClass));
         }
    }


    /**
     * 源对象集合 {@code source} 转换成目标bean对象 {@code D}; 将名称一致的属性进行转换
     * @param source 源对象集合
     * @param dstClass 目标Bean的class
     * @param <S> 原Bean的泛型定义
     * @param <D> 目标Bean的泛型定义
     * @return 映射完的List集合, 默认返回ArrayList
     * @throws Exception 
     * @throws BeanMappingException
     */
    public static <S, D> List<D> mapList(Collection<S> source, Class<D> dstClass) throws Exception {
    	 List<D> result = new ArrayList<>();
         map(source, result, dstClass);
         return result;
    }
    
}
