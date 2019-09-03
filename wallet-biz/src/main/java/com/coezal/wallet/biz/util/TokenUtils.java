package com.coezal.wallet.biz.util;

import com.coezal.wallet.api.bean.Token;
import com.coezal.wallet.dal.dao.TokenMapper;

import javax.annotation.Resource;

/**
 * Version 1.0
 * Created by lll on 2019-09-03.
 * Description
 * copyright generalray4239@gmail.com
 */
public class TokenUtils {


  @Resource
  TokenMapper tokenMaper;

  /**
   * 添加token
   */
  public void initToken() {
    //存储 usdt token
    Token usdtToken = new Token("0xdac17f958d2ee523a2206206994597c13d831ec7", 6, "Tether USD", "USDT","");
    tokenMaper.insert(usdtToken);
    //存储CZT token
    Token cztToken = new Token("0x99a3721266997cd8cb48afe11b3d43b4eb70d281", 6, "coezal", "czt","");
    tokenMaper.insert(cztToken);
  }
}
