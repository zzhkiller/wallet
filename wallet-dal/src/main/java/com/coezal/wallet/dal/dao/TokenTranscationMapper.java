package com.coezal.wallet.dal.dao;


import com.coezal.wallet.api.bean.TokenTranscation;

import java.util.List;

public interface TokenTranscationMapper {
    /**
     * 保存数据
     */
    int insert(TokenTranscation record);

    /**
     * 更新主键ID对应的非NULL字段数据
     */
    int update(TokenTranscation record);

    /**
     * 通过主键删除数据
     */
    int deleteById(Long id);

    /**
     * 通过主键获取数据
     */
    TokenTranscation selectById(Long id);

    /**
     * 通过条件查询数据信息
     */
    List<TokenTranscation> select(TokenTranscation record);

    /**
     * 通过条件查询唯一数据信息
     */
    TokenTranscation selectOne(TokenTranscation record);
}