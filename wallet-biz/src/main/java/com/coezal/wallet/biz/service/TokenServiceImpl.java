package com.coezal.wallet.biz.service;

import com.coezal.wallet.api.bean.Token;
import com.coezal.wallet.dal.dao.TokenMapper;
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

  @Resource
  TokenMapper tokenMapper;

  @Override
  public Token getTokenInfoByTokenName(String tokenName) {
    Token token = new Token();
    token.setTokenName(tokenName);
    List<Token> tokenList = tokenMapper.select(token);
    if (tokenList != null && tokenList.size() == 1) {
      return tokenList.get(0);
    }
    return null;
  }
}
