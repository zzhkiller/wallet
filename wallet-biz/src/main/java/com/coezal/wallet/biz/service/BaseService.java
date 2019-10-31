package com.coezal.wallet.biz.service;

import com.coezal.wallet.api.bean.Token;
import com.coezal.wallet.api.bean.TokenTransaction;
import com.coezal.wallet.api.bean.WalletBean;
import com.coezal.wallet.api.excetion.BizException;
import com.coezal.wallet.dal.dao.TokenMapper;
import com.coezal.wallet.dal.dao.TokenTransactionMapper;
import com.coezal.wallet.dal.dao.WalletBeanMapper;

import javax.annotation.Resource;

import java.util.List;

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

  @Resource
  TokenTransactionMapper tokenTransactionMapper;

  public WalletBean getUserWalletBean(String usrInfo) {
    WalletBean bean = new WalletBean();
    bean.setOwnerInfo(usrInfo);
    WalletBean resultBean = walletMapper.selectOne(bean);
    if (resultBean == null || resultBean.getAddress() == null) {
      throw new BizException(WALLET_NOT_EXIT);
    }
    return resultBean;
  }

  /**
   * 检查是否
   * @param tokenSymbol
   * @return
   */
  public Token getToken(String tokenSymbol) {
    Token token = new Token();
    token.setTokenSymbol(tokenSymbol);
    Token resultToken = tokenMapper.selectOne(token);
    if (resultToken == null) {
      throw new BizException(TOKEN_NOT_EXIT);
    }
    return resultToken;
  }

  /**
   * 检查钱包充值记录是否大于提现金额
   * @param address
   * @param money
   * @return
   */
  public boolean checkTokenTransaction(String address, double money) {
    TokenTransaction transaction = new TokenTransaction();
    transaction.setToAddress(address);
    List<TokenTransaction> transactionList = tokenTransactionMapper.select(transaction);
    if (null != transactionList && transactionList.size() > 0) {
      double total = 0;
      for (TokenTransaction trans : transactionList) {
        if (null != trans.getValue()) {
          try {
            total += Double.parseDouble(trans.getValue());
          } catch (Exception e) {
            e.printStackTrace();
            continue;
          }
        }
      }
      if (total == 0 || total < money) {
        return false;
      } else {
        return true;
      }
    }
    return false;
  }
}
