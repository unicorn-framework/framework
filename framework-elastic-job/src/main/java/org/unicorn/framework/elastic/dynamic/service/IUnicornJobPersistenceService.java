package org.unicorn.framework.elastic.dynamic.service;

import org.assertj.core.util.Lists;
import org.unicorn.framework.elastic.dynamic.bean.Job;

import java.util.List;

/**
 * 一次性job持久化操作接口
 * 业务端可根据自己的情况选择持久化实现
 *
 * @Author: xiebin
 * @Description:
 * @Date:Create：in 2021-03-30 14:03
 */
public interface IUnicornJobPersistenceService {
    /**
     * 保存job
     *
     * @param job
     */
   default void saveJob(Job job){

    }

    /**
     * 默认返回空列表
     *
     * @return
     */
    default List<String> jobInfoJsonList() {
        return Lists.newArrayList();
    }

    /**
     * 删除job
     *
     * @param job
     */
    default void removeJob(Job job){

    }

}
