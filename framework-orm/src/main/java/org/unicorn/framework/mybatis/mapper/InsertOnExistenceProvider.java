package org.unicorn.framework.mybatis.mapper;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.provider.base.BaseInsertProvider;

/**
 * @author zhanghaibo
 * @since 2019/7/1
 */
public class InsertOnExistenceProvider extends BaseInsertProvider {

    public InsertOnExistenceProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     *  insert into table (xx,xx,xx)  values(xx,xx,xx) on  DUPLICATE key update id=1111,aa='22'
     *
     * 插入一条记录，存在就更新
     * @param ms
     * @return
     */
    public String insertOnExistence(MappedStatement ms) {
        StringBuilder sql = new StringBuilder(super.insertSelective(ms));
        sql.append("ON DUPLICATE KEY UPDATE");
        String updateSetColumns = SqlHelper.updateSetColumns(getEntityClass(ms), null, true, true);
        sql.append(" <trim prefix=\"\" suffix=\"\" suffixOverrides=\",\">");
        sql.append(updateSetColumns.substring(5,updateSetColumns.length()-6));
        sql.append("</trim>");
        return sql.toString();
    }
}
