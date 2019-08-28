package com.coezal.wallet.common.util;

/**
 * Version 1.0
 * Created by lll on 2019-08-28.
 * Description
 * copyright generalray4239@gmail.com
 */
public class StringFormat {

  /**
   * 判断是否满足钱包用户信息：手机号或者邮箱
   * @param param
   * @return
   */
  public static boolean isMatchWalletOwnInfo(String param) {
    return MobileUtil.validate(param) || EmailUtils.validate(param);
  }
}
