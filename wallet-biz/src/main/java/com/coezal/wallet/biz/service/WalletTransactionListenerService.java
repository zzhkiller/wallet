package com.coezal.wallet.biz.service;

/**
 * Version 1.0
 * Created by lll on 2019-09-02.
 * Description
 * copyright generalray4239@gmail.com
 */
public interface WalletTransactionListenerService {

  /**
   * 获取指定钱包指定合约的转账交易记录
   * @param address  钱包地址
   * @param tokenContractAddress 合约地址
   */
  void getTransactionByAddressAndTokenContractAddress(String address, String tokenContractAddress);

}
