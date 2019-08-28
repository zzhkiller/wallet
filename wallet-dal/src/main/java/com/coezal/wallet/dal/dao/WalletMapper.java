package com.coezal.wallet.dal.dao;

import com.coezal.wallet.api.bean.WalletBean;

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
   * @param walletBean
   * @return
   */
  int insert(WalletBean walletBean);

  /**
   *
   * @param walletBean
   * @return
   */
  int update(WalletBean walletBean);

  /**
   * 通过主键获取id
   * @param id
   * @return
   */
  WalletBean selectById(Long id);


  /**
   * 查询满足条件的 所有wallet
   * @param walletBean
   * @return
   */
  List<WalletBean> selectAll(WalletBean walletBean);

  /**
   * 查询满足条件的唯一wallet
   * @param walletBean
   * @return
   */
  WalletBean selectOne(WalletBean walletBean);
}
