package com.coezal.wallet.biz.service;

import com.coezal.wallet.api.bean.Token;
import com.coezal.wallet.dal.dao.TokenMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Version 1.0
 * Created by lll on 2019-09-25.
 * Description
 * copyright generalray4239@gmail.com
 */

@Service
public class TokenServiceImpl  implements TokenService{

  private final static Logger logger = LoggerFactory.getLogger("TokenServiceImpl");

  @Resource
  TokenMapper tokenMapper;

  @Override
  public Token getTokenInfoByTokenName(String tokenName) {
    Token token = new Token();
    token.setTokenSymbol(tokenName);
    List<Token> tokenList = tokenMapper.select(token);
    logger.info("getTokenInfoByTokenName===" + (tokenList == null ? "not find token" : tokenList.toString()));
    if (tokenList != null && tokenList.size() >0) {
      return tokenList.get(0);
    }
    return null;
  }
}
