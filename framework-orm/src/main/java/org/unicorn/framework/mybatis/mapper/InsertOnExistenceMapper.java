package org.unicorn.framework.mybatis.mapper;

import org.apache.ibatis.annotations.InsertProvider;

/**
 * 存在就更新Mapper
 * @author zhanghaibo
 * @since 2019/7/1
 */
@tk.mybatis.mapper.annotation.RegisterMapper
public interface InsertOnExistenceMapper<T> {

    /**
     * 插入一条记录存在就更新
     * @param record
     * @return
     */
    @InsertProvider(type = InsertOnExistenceProvider.class, method = "dynamicSQL")
    int insertOnExistence(T record);
}
