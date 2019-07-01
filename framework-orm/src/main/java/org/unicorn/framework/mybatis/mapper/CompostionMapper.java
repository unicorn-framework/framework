package org.unicorn.framework.mybatis.mapper;

import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * 各种 sql 语句组合
 * @author zhanghaibo
 * @since 2019/7/1
 */
@tk.mybatis.mapper.annotation.RegisterMapper
public interface CompostionMapper<T> extends InsertOnExistenceMapper<T>, Mapper<T>, InsertListMapper<T>, IdListMapper<T,Long> {
}
