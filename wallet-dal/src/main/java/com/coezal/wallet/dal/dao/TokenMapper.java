package com.coezal.wallet.dal.dao;


import com.coezal.wallet.api.bean.Token;

import java.util.List;

public interface TokenMapper {
    /**
     * 保存数据
     */
    int insert(Token record);

    /**
     * 更新主键ID对应的非NULL字段数据
     */
    int update(Token record);

    /**
     * 通过主键删除数据
     */
    int deleteById(Long id);

    /**
     * 通过主键获取数据
     */
    Token selectById(Long id);

    /**
     * 通过条件查询数据信息
     */
    List<Token> select(Token record);

    /**
     * 通过条件查询唯一数据信息
     */
    Token selectOne(Token record);
}