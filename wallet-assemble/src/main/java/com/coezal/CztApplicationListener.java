package com.coezal;

import com.coezal.component.InitComponent;
import com.coezal.wallet.api.bean.RsaKey;
import com.coezal.wallet.api.bean.Token;
import com.coezal.wallet.api.bean.TokenTransaction;
import com.coezal.wallet.api.bean.WalletBean;
import com.coezal.wallet.biz.wallet.PasswordGenerator;
import com.coezal.wallet.biz.wallet.WalletGenerator;
import com.coezal.wallet.common.util.AESUtils;
import com.coezal.wallet.common.util.RSACoder;
import com.coezal.wallet.dal.dao.RsaKeyMapper;
import com.coezal.wallet.dal.dao.TokenMapper;
import com.coezal.wallet.dal.dao.TokenTransactionMapper;
import com.coezal.wallet.dal.dao.WalletBeanMapper;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Version 1.0
 * Created by lll on 2019-09-03.
 * Description
 * copyright generalray4239@gmail.com
 */

@Component
@EnableAsync
public class CztApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

  private static final Logger logger = LoggerFactory.getLogger("CztApplicationListener");

  @Resource
  TokenMapper tokenMapper;

  @Resource
  RsaKeyMapper rsaKeyMapper;

  @Autowired
  InitComponent initComponent;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    initRsaKey();
    initToken();
    initComponent.initWallet();
  }

  private void initRsaKey() {
    try {
      List<RsaKey> rsaKeys = rsaKeyMapper.select(null);
      if (rsaKeys == null || rsaKeys.size() == 0) {
        RsaKey key = new RsaKey();
        Map<String, Object> keyMap = RSACoder.initKey();
        String publicKeyStr = Base64.encodeBase64String(RSACoder.getPublicKey(keyMap));
        String privateKeyStr = Base64.encodeBase64String(RSACoder.getPrivateKey(keyMap));
        key.setPublicKey(publicKeyStr);
        key.setPrivateKey(privateKeyStr);
        logger.info("CztApplicationListener ras public key", publicKeyStr);
        logger.info("CztApplicationListener ras private key", privateKeyStr);
        rsaKeyMapper.insert(key);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void initToken() {
    Token token = new Token();
    List<Token> tokenList = tokenMapper.select(token);
    if (tokenList == null || tokenList.size() == 0) {
      logger.info("init token no token insert token===");
      Token usdtToken = new Token("0xdac17f958d2ee523a2206206994597c13d831ec7", 6, "Tether USD", "USDT", "");
      tokenMapper.insert(usdtToken);
      //存储CZT token
      Token cztToken = new Token("0x99a3721266997cd8cb48afe11b3d43b4eb70d281", 18, "coezal", "czt", "");
      tokenMapper.insert(cztToken);
    } else {
      logger.info("init token has token" + tokenList.size());
    }
  }
}
