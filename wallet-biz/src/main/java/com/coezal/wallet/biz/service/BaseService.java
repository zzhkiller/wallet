package com.coezal.wallet.biz.service;

import com.coezal.wallet.api.bean.Token;
import com.coezal.wallet.api.bean.WalletBean;
import com.coezal.wallet.api.excetion.BizException;
import com.coezal.wallet.dal.dao.TokenMapper;
import com.coezal.wallet.dal.dao.WalletBeanMapper;

import javax.annotation.Resource;

import static com.coezal.wallet.api.enums.ResultCode.TOKEN_NOT_EXIT;
import static com.coezal.wallet.api.enums.ResultCode.WALLET_NOT_EXIT;

/**
 * Version 1.0
 * Created by lll on 2019-09-26.
 * Description
 * copyright generalray4239@gmail.com
 */
public class BaseService {

  public static final String salt = "gQ#D63K*QW%U9l@X";


  @Resource
  WalletBeanMapper walletMapper;

  @Resource
  TokenMapper tokenMapper;

  public WalletBean getUserWalletBean(String usrInfo) {
    WalletBean bean = new WalletBean();
    bean.setOwnerInfo(usrInfo);
    WalletBean resultBean = walletMapper.selectOne(bean);
    if (resultBean == null || resultBean.getAddress() == null) {
      throw new BizException(WALLET_NOT_EXIT);
    }
    return resultBean;
  }

  public Token getToken(String tokenSymbol){
    Token token = new Token();
    token.setTokenSymbol(tokenSymbol);
    Token resultToken = tokenMapper.selectOne(token);
    if(resultToken == null){
      throw new BizException(TOKEN_NOT_EXIT);
    }
    return resultToken;
  }
}
