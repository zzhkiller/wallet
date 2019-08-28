package com.coezal.wallet.biz.wallet;

import com.coezal.wallet.api.bean.WalletBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.*;
import org.web3j.protocol.ObjectMapperFactory;

import java.io.IOException;

import static jdk.nashorn.internal.objects.Global.print;

/**
 * Version 1.0
 * Created by lll on 2019-08-29.
 * Description
 * <pre>
 *   钱包生成器
 * </pre>
 * copyright generalray4239@gmail.com
 */
public class WalletGenerator {


  private static final Logger logger  = LoggerFactory.getLogger("WalletGenerator");

  /**
   * 生成钱包bean
   *
   * @param password
   */
  public static WalletBean createEthWallet(String password) throws Exception {

    WalletBean bean = new WalletBean();
    bean.setPassword(password);
    ECKeyPair ecKeyPair = Keys.createEcKeyPair();
    WalletFile walletFile = Wallet.createStandard(password, ecKeyPair);
    bean.setAddress("0x" + walletFile.getAddress());

    ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
    String keystore = objectMapper.writeValueAsString(walletFile);
    System.out.println("keystore json file " + keystore);
    bean.setKeyStoreJson(keystore);

    WalletFile walletFile2 = objectMapper.readValue(keystore, WalletFile.class);
    ECKeyPair ecKeyPair2 = Wallet.decrypt(password, walletFile2);
    String privateKey = ecKeyPair2.getPrivateKey().toString(16);
    bean.setPrivateKey(privateKey);
    String publicKey = ecKeyPair2.getPublicKey().toString(16);
    bean.setPrivateKey(publicKey);

    return bean;
  }


}
