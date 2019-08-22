package com.coezal.wallet.dal.dao;


import java.util.List;

public interface  BaseMapper<T> {
    /**
     * 保存数据
     */
    int insert(T model);

    /**
     * 更新主键ID对应的非NULL字段数据
     */
    int update(T model);

    /**
     * 通过主键删除数据
     */
    int deleteById(Long id);

    /**
     * 通过主键获取数据
     */
    T selectById(Long id);

    /**
     * 通过条件查询数据信息
     */
    List<T> select(T model);

    /**
     * 通过条件查询唯一数据信息
     */
    T selectOne(T model);
}
