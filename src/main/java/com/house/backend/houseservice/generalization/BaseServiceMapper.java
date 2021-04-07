package com.house.backend.houseservice.generalization;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通用操作mapper
 */
public interface BaseServiceMapper<T> {
    /**
     * 根据条件多选
     *
     * @param condition
     * @return
     */
    List<T> selectByCondition(@Param("condition") T condition);

    /**
     * 根据条件查询并排序
     *
     * @param condition
     * @param orderByClause
     * @return
     */
    List<T> selectByConditionOrder(@Param("condition") T condition, @Param("orderByClause") String orderByClause);

    /**
     * 根据条件单选
     *
     * @param condition
     * @return
     */
    T selectByConditionSingle(@Param("condition") T condition);


    /**
     * 根据条件删除
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(String id);

    /**
     * 根据条件查询
     *
     * @param id
     * @return
     */
    T selectByPrimaryKey(String id);

    /**
     * 插入（全字段）
     *
     * @param record
     * @return
     */
    int insert(T record);

    /**
     * 插入（不为空字段）
     *
     * @param record
     * @return
     */
    int insertSelective(T record);

    /**
     * 根据条件更新（不为空字段）
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(T record);

    /**
     * 根据条件更新（全字段）
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(T record);

    /**
     * 根据条件选择
     *
     * @param example
     * @return
     */
    List<T> selectByExample(Object example);


}
