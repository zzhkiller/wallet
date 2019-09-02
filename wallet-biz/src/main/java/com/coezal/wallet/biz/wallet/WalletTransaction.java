package com.coezal.wallet.biz.wallet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
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
import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * Version 1.0
 * Created by lll on 2019-08-29.
 * Description
 * <pre>
 *   钱包转账
 *
 * </pre>
 * copyright generalray4239@gmail.com
 */
public class WalletTransaction {

  private static final Logger logger = LoggerFactory.getLogger("WalletTransaction");

  Web3j web3j;

  WalletTransaction(Web3j web3j) {
    this.web3j = web3j;
  }

  /**
   * erc 20 合约的token 交易
   * @param from 转出地址
   * @param to  转入地址
   * @param value  转账数量
   * @param privateKey  私钥
   * @param contractAddress  合约地址
   * @return
   */
  public EthSendTransaction transferERC20Token(String from, String to, BigInteger value, String privateKey, String contractAddress) throws Exception {
    //加载转账凭证
    Credentials credentials = Credentials.create(privateKey);
    // 获取nonce 交易笔数
    BigInteger nonce = getNonce(from);

    BigInteger gasPrice = getGasPrice();
    BigInteger gasLimit = Contract.GAS_LIMIT;

    Function function = new Function(
            "transfer",
            Arrays.asList(new Address(to), new Uint256(value)),
            Arrays.asList(new TypeReference<Type>() {
            }));

    String encodedFunction = FunctionEncoder.encode(function);

    RawTransaction rawTransaction = RawTransaction.createTransaction(nonce,
            gasPrice,
            gasLimit,
            contractAddress, encodedFunction);


    //签名Transaction，这里要对交易做签名
    byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
    String hexValue = Numeric.toHexString(signMessage);
    //发送交易
    EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
    return ethSendTransaction;
  }

  /**
   * 获取gas price
   *
   * @return
   * @throws IOException
   */
  private BigInteger getGasPrice() throws IOException {
    return web3j.ethGasPrice().send().getGasPrice();
  }

  /**
   * 获取转账nonce
   * @param from
   * @return
   * @throws ExecutionException
   * @throws InterruptedException
   */
  private BigInteger getNonce(String from) throws ExecutionException, InterruptedException {
    EthGetTransactionCount transactionCount = web3j.ethGetTransactionCount(from, DefaultBlockParameterName.LATEST).sendAsync().get();
    BigInteger nonce = transactionCount.getTransactionCount();
    logger.info("transaction nonce====:", nonce);
    return nonce;
  }




}
