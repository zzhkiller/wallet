package com.coezal.wallet.common.util;

import com.coezal.wallet.api.bean.Wallet;

/**
 * Version 1.0
 * Created by lll on 2019-08-28.
 * Description
 * <pre>
 *   钱包工具类
 * </pre>
 * copyright generalray4239@gmail.com
 */
public class WalletUtils {

  /**
   *  创建钱包，并存储到数据库里面
   */
  public static Wallet createWallet(){
    return new Wallet();
  }

}
