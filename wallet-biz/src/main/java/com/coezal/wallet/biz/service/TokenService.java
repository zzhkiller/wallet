package com.coezal.wallet.biz.service;

import com.coezal.wallet.api.bean.Token;

/**
 * Version 1.0
 * Created by lll on 2019-09-25.
 * Description
 * copyright generalray4239@gmail.com
 */
public interface TokenService {


  /**
   *
   * @param tokenName
   * @return
   */
  Token getTokenInfoByTokenName(String tokenName);
}
