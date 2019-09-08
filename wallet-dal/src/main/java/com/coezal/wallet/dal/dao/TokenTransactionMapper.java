package com.coezal.wallet.dal.dao;

import com.coezal.wallet.api.bean.TokenTransaction;

import java.util.List;

/**
 * Version 1.0
 * Created by lll on 2019-09-03.
 * Description
 * copyright generalray4239@gmail.com
 */
public interface TokenTransactionMapper {

  /**
   * 保存数据
   */
  int insert(TokenTransaction transaction);

  /**
   * 更新主键ID对应的非NULL字段数据
   */
  int update(TokenTransaction transaction);

  /**
   * 通过主键删除数据
   */
  int deleteById(Long id);

  /**
   * 通过主键获取数据
   */
  TokenTransaction selectById(Long id);

  /**
   * 通过条件查询数据信息
   */
  List<TokenTransaction> select(TokenTransaction transaction);

  /**
   * 通过条件查询唯一数据信息
   */
  TokenTransaction selectOne(TokenTransaction transaction);
}
