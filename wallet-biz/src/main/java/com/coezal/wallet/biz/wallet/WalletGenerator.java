package com.coezal.wallet.biz.wallet;

import com.coezal.wallet.api.bean.WalletBean;
import com.coezal.wallet.common.util.AESUtils;
import com.coezal.wallet.common.util.RSACoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.apache.commons.codec.binary.Base64;
import org.bitcoinj.crypto.*;
import org.bitcoinj.wallet.DeterministicSeed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.*;
import org.web3j.protocol.ObjectMapperFactory;
import sun.security.provider.SecureRandom;

import java.io.IOException;
import java.util.List;

import static com.coezal.wallet.biz.wallet.PasswordGenerator.GENERAL_KEY;
import static com.coezal.wallet.biz.wallet.PasswordGenerator.getPwd;
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


  /**
   * b44钱包地址通用钱包生成
   */
  private final static ImmutableList<ChildNumber> BIP44_ETH_ACCOUNT_ZERO_PATH =
          ImmutableList.of(new ChildNumber(44, true), new ChildNumber(60, true),
                  ChildNumber.ZERO_HARDENED, ChildNumber.ZERO);

  /**
   * 生成冷钱包
   *
   * @param password
   */
  public static WalletBean createEthColdWallet(String password) throws Exception {

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


  /**
   * 创建HD钱包
   * @throws MnemonicException.MnemonicLengthException
   */
  public static WalletBean createHDWallet()  throws MnemonicException.MnemonicLengthException {
    WalletBean bean = new WalletBean();
    SecureRandom secureRandom = new SecureRandom();
    byte[] entropy = new byte[DeterministicSeed.DEFAULT_SEED_ENTROPY_BITS / 8];
    secureRandom.engineNextBytes(entropy);

    //生成12位助记词
    List<String> mnemonicList = MnemonicCode.INSTANCE.toMnemonic(entropy);

    //使用助记词生成钱包种子
    byte[] seed = MnemonicCode.toSeed(mnemonicList, ""); //注意这里的密码为空
    DeterministicKey masterPrivateKey = HDKeyDerivation.createMasterPrivateKey(seed);
    DeterministicHierarchy deterministicHierarchy = new DeterministicHierarchy(masterPrivateKey);
    DeterministicKey deterministicKey = deterministicHierarchy.deriveChild(BIP44_ETH_ACCOUNT_ZERO_PATH, false, true, new ChildNumber(0));
    byte[] bytes = deterministicKey.getPrivKeyBytes();
    ECKeyPair keyPair = ECKeyPair.create(bytes);

    //通过公钥生成钱包地址
    String address = Keys.getAddress(keyPair.getPublicKey());
    String aesPwd = getPwd();
    String encryptMnemonic = AESUtils.encrypt(mnemonicList.toString(), aesPwd);
    String privateKey = "0x"+keyPair.getPrivateKey().toString(16);
    String encryptPrivateKey = AESUtils.encrypt(privateKey, aesPwd);

    bean.setAddress("0x"+address);
    bean.setPrivateKey(encryptPrivateKey);
    bean.setMnemonicList(encryptMnemonic);
    bean.setSignKey(GENERAL_KEY);
    return bean;
  }
}
