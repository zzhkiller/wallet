package com.coezal.wallet.biz.wallet;

import com.coezal.wallet.api.bean.WalletBean;
import com.coezal.wallet.biz.util.BalanceUtils;
import com.coezal.wallet.common.util.AESUtils;
import com.coezal.wallet.dal.dao.WalletBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
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

  /**
   * eth分发地址key
   */
  private static String ETH_PRIVATE_KEY="5749mpFJNElIGu4+ZOwGL0XyEqa7tB9iH30xt6SP4TfC+wfDbNQarvddeIyBL2mJ4vc4Sv6OUnY1XelvGPWrwFPav9jBMHpPsCwLjl4FYVs=";

  /**
   * eth分发地址
   */
  private static String ETH_ADDRESS = "Pk6p3HGNje+d1VD7b8EOtkPu7a4VlxMjNdPI34Bdt66YFMxLKxT06hSEz5WgURTQ";

  /**
   * USDT 聚集地址
   */
  private static String COLLECT_USDT_ADDRESS = "";

  @Resource
  WalletBeanMapper walletBeanMapper;

  Web3j web3j;

  public WalletTransaction(String web3jUrl) {
    this.web3j = Web3j.build(new HttpService(web3jUrl));
  }


  /**
   * ETH转账
   * @throws IOException
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public String signETHTransaction(String fromAddress, String toAddress, String privateKey, String amount) throws Exception {

      //查询地址交易编号
    BigInteger nonce = web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).send().getTransactionCount();
    //支付的矿工费
    BigInteger gasPrice = getGasPrice();
    BigInteger gasLimit = getGasLimit();

    BigInteger amountWei = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();

    Credentials credentials = Credentials.create(privateKey);
    //签名交易
    RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, toAddress, amountWei, "");

    byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);

    //广播交易
    EthSendTransaction ethSendTransaction =  web3j.ethSendRawTransaction(Numeric.toHexString(signMessage)).sendAsync().get();//.getTransactionHash();

//    BigDecimal decimal = Convert.toWei(amount, Convert.Unit.ETHER);
//    TransactionReceipt transactionReceipt = Transfer.sendFunds(
//            web3j, credentials, toAddress,
//            decimal, Convert.Unit.ETHER).sendAsync().get();

    logger.info("eth transaction === " + toAddress + "======amount=====" + amount + "=======error===" + ethSendTransaction.getError() + "====hash=" + ethSendTransaction.getTransactionHash());
    return ethSendTransaction.getTransactionHash();
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
    BigInteger gasLimit = getGasLimit();

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
   * 获取eth 交易中，最近一笔交易的gas price
   *
   * @return
   * @throws IOException
   */
  private BigInteger getGasPrice() throws Exception {
    final int gasPriceMinGwei = BalanceUtils.getGasPriceMinGwei();
    return BalanceUtils.gweiToWei(BigDecimal.valueOf(20 + gasPriceMinGwei));
//    return web3j.ethGasPrice().sendAsync().get().getGasPrice();
  }

  private BigInteger getGasLimit() throws  Exception{
    return BigInteger.valueOf(BalanceUtils.GAS_LIMIT_MIN);
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


  /**
   * 获取转账的block hash。如果block hash !=0 表示转账被矿工打包了
   * @param txHash
   * @return
   */
  private String getBlockHash(String txHash) {
    try {
      EthTransaction transaction = web3j.ethGetTransactionByHash(txHash).sendAsync().get();
      Transaction result = transaction.getResult();
      String blockHash = result.getBlockHash();
      logger.info("transaction blockHash=="+blockHash);
      boolean isSuccess = Numeric.toBigInt(blockHash).compareTo(BigInteger.valueOf(0))!=0;
      if(isSuccess){
        return txHash;
      }
      return "";
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }


  /**
   * 校验当前转账被打包后的执行信息
   * @param transactionHash
   */
  private void getTransactionReceipt(String transactionHash){
    try {
      EthGetTransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt(transactionHash).sendAsync().get();
      TransactionReceipt receipt = transactionReceipt.getTransactionReceipt().get();
      String status = receipt.getStatus();
      BigInteger gasUsed = receipt.getGasUsed();
      BigInteger blockNumber = receipt.getBlockNumber();
      String blockHash = receipt.getBlockHash();
      logger.info("transaction status==" + status);
      logger.info("transaction gasUsed==" + gasUsed);
      logger.info("transaction blockNumber==" + blockNumber);
      logger.info("transaction blockHash==" + blockHash);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }



  /**
   * 测试eth 转账
   * @throws Exception
   */
  private void testTransEth() throws Exception {
    String pwd = WalletGenerator.getPwd();
    String formAddress =AESUtils.decrypt(ETH_ADDRESS, pwd);
    String toAddress = "0xf102121cbaaa2731F2c68C11157c8c56d970C3df";
//    String toAddress = "0xd9795Ac75229d4bE73cF817D0d65eC03E378849B";
//    String toAddress = "0x604f2DB361304e9C775d76182B00C3C2dC397Dcc";
//    String toAddress = "0xf102121cbaaa2731F2c68C11157c8c56d970C3df";
//    String toAddress = "0x16be8F8fe00587AFa9e95744745C7124D6806535";
//    String toAddress = "0x140c7553f5b8e3d1de34c1075f2a87b6aebff916";
    System.out.println("transaction eth formAddress ===" + formAddress);
    String privateKey = AESUtils.decrypt(ETH_PRIVATE_KEY, pwd);
    System.out.println("transaction eth privateKey ===" + privateKey);
    String hash = signETHTransaction(formAddress, toAddress, privateKey, "0.005");
    System.out.println("transaction eth hash ===" + hash);
  }

  /**
   * 测试转账 usdt
   * @throws Exception
   */
  private void testTransUsdt() throws Exception{



//    String hash = signETHTransaction(formAddress, toAddress, privateKey, "");
//    System.out.println("transaction hash ===" + hash);
  }



  /**
   *
   * @param args
   */
  public static void main(String[] args){
    WalletTransaction transaction = new WalletTransaction("https://mainnet.infura.io/v3/e7f92614009d4a28b55bb9576b59a828");
//    WalletTransaction transaction = new WalletTransaction("https://ropsten.infura.io/v3/e7f92614009d4a28b55bb9576b59a828");
    try {
      transaction.testTransEth();
      long value = transaction.getGasPrice().longValue();
      System.out.println("gas value===" + value);
      System.out.println("gas limit===" + Contract.GAS_LIMIT.longValue());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
