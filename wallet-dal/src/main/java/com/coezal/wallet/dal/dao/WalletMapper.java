package com.coezal.wallet.dal.dao;

import com.coezal.wallet.api.bean.Wallet;

import java.util.List;

/**
 * Version 1.0
 * Created by lll on 2019-08-28.
 * Description
 * copyright generalray4239@gmail.com
 */
public interface WalletMapper {

  /**
   *
   * @param wallet
   * @return
   */
  int insert(Wallet wallet);

  /**
   *
   * @param wallet
   * @return
   */
  int update(Wallet wallet);

  /**
   * 通过主键获取id
   * @param id
   * @return
   */
  Wallet selectById(Long id);


  /**
   * 查询满足条件的 所有wallet
   * @param wallet
   * @return
   */
  List<Wallet> selectAll(Wallet wallet);

  /**
   * 查询满足条件的唯一wallet
   * @param wallet
   * @return
   */
  Wallet selectOne(Wallet wallet);
}
