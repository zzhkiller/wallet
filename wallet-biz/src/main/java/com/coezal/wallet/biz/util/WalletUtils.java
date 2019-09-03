package com.coezal.wallet.biz.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.Contract;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * Version 1.0
 * Created by lll on 2019-09-03.
 * Description
 * copyright generalray4239@gmail.com
 */
public class WalletUtils {

  private static final Logger logger = LoggerFactory.getLogger("WalletTransaction");

  /**
   * 初始钱包数据
   *
   */
  private void initWallet() {
    for (int i = 0; i < 1000; i++) {

    }
  }

  /**
   *
   * @param money
   * @param tokenDecimal
   * @return
   */
  public static String getMoney(String money, String tokenDecimal) {
    BigDecimal bigDecimal = new BigDecimal(money);
    return bigDecimal.divide(BigDecimal.TEN.pow(Integer.parseInt(tokenDecimal))) + "";
  }
}
