package com.coezal.wallet.biz.service;

import com.coezal.wallet.api.bean.FetchCashResponse;
import com.coezal.wallet.api.bean.PayCheckResponse;

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

  /**
   * 校验充值请求是否真实
   * @param dataStr
   * @return
   */
  PayCheckResponse payCheck(String dataStr);

  /**
   * 请求提现
   * @param dataStr
   * @return
   */
  FetchCashResponse fetchCash(String dataStr);
}
