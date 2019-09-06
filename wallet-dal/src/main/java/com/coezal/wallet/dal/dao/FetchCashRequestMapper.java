package com.coezal.wallet.dal.dao;

import com.coezal.wallet.api.bean.FetchCashRequest;

import java.util.List;

/**
 * Version 1.0
 * Created by lll on 2019-09-06.
 * Description
 * copyright generalray4239@gmail.com
 */
public interface FetchCashRequestMapper {
  /**
   * 保存数据
   */
  int insert(FetchCashRequest request);

  /**
   * 更新主键ID对应的非NULL字段数据
   */
  int update(FetchCashRequest request);

  /**
   * 通过主键删除数据
   */
  int deleteById();

  /**
   * 通过主键获取数据
   */
  FetchCashRequest selectById();

  /**
   * 通过条件查询数据信息
   */
  List<FetchCashRequest> select(FetchCashRequest token);

  /**
   * 通过条件查询唯一数据信息
   */
  FetchCashRequest selectOne(FetchCashRequest token);
}
