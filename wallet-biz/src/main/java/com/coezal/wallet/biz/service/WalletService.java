package com.coezal.wallet.biz.service;

/**
 * Version 1.0
 * Created by lll on 2019-08-27.
 * Description
 * copyright generalray4239@gmail.com
 */
public interface WalletService {

  /**
   * 获取钱包地址
   * @param param  电话或者邮箱地址
   * @return
   */
  String getWalletAddress(String param);


}
