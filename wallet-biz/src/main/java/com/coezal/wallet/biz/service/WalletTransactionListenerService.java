package com.coezal.wallet.biz.service;

import com.coezal.wallet.api.bean.TokenTransaction;

import java.util.List;

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
  List<TokenTransaction> getTransactionByAddressAndTokenContractAddress(String server, String address, String tokenContractAddress);


  /**
   * 获取钱包某个token的余额
   * @param server
   * @param address
   * @param tokenContractAddress
   * @return 钱包余额
   */
  String getWalletBalanceOfByAddressAndTokenContractAddress(String server, String address, String tokenContractAddress);

}
