package com.coezal.wallet.dal.dao;

import com.coezal.wallet.api.bean.Token;
import com.coezal.wallet.api.bean.TokenTransaction;

import java.util.List;

/**
 * Version 1.0
 * Created by lll on 2019-09-03.
 * Description
 * copyright generalray4239@gmail.com
 */
public interface TokenMapper {

  /**
   * 保存数据
   */
  int insert(Token token);

  /**
   * 更新主键ID对应的非NULL字段数据
   */
  int update(Token token);

  /**
   * 通过主键删除数据
   */
  int deleteById();

  /**
   * 通过主键获取数据
   */
  Token selectById();

  /**
   * 通过条件查询数据信息
   */
  List<Token> select(Token token);

  /**
   * 通过条件查询唯一数据信息
   */
  Token selectOne(Token token);
}
