package com.coezal.wallet.dal.dao;


import com.coezal.wallet.api.bean.FetchCash;

import java.util.List;

public interface FetchCashMapper {
    /**
     * 保存数据
     */
    int insert(FetchCash record);

    /**
     * 更新主键ID对应的非NULL字段数据
     */
    int update(FetchCash record);

    /**
     * 通过主键删除数据
     */
    int deleteById(Long id);

    /**
     * 通过主键获取数据
     */
    FetchCash selectById(Long id);

    /**
     * 通过条件查询数据信息
     */
    List<FetchCash> select(FetchCash record);

    /**
     * 通过条件查询唯一数据信息
     */
    FetchCash selectOne(FetchCash record);
}