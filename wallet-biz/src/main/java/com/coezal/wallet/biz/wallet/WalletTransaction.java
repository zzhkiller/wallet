package com.coezal.wallet.biz.wallet;

import com.coezal.wallet.biz.util.BalanceUtils;
import com.coezal.wallet.biz.util.TokenInfoUtils;
import com.coezal.wallet.biz.util.WalletUtils;
import com.coezal.wallet.common.util.AESUtils;
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
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static com.coezal.wallet.biz.util.WalletConstant.*;

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

  public WalletTransaction(String web3jUrl) {
    this.web3j = Web3j.build(new HttpService(web3jUrl));
  }


  /**
   * ETH分发转账
   * @param nonce
   * @param fromAddress
   * @param toAddress
   * @param privateKey
   * @param amount
   * @return
   * @throws Exception
   */
  public String signETHTransaction(BigInteger nonce, String fromAddress, String toAddress, String privateKey, String amount) throws Exception {

    //支付的矿工费
    BigInteger gasPrice = getGasPrice();
    BigInteger gasLimit = getGasLimit();

    BigInteger amountWei = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();

    Credentials credentials = Credentials.create(privateKey);
    //签名交易
    RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, toAddress, amountWei, "");

    byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, credentials);

    //广播交易
    EthSendTransaction transactionReceipt =  web3j.ethSendRawTransaction(Numeric.toHexString(signMessage)).sendAsync().get();//.getTransactionHash();
    if (transactionReceipt.getError() == null) {
      String hash = transactionReceipt.getTransactionHash();
      logger.info("eth transaction address=== " + toAddress + "======amount=====" + amount + "=======success hash=== " + hash);
      return hash;
    } else {
      String msg = transactionReceipt.getError().getMessage();
      logger.info("eth transaction address=== " + toAddress + "======amount=====" + amount + "=======error=== " + msg);
      return null;
    }
  }

  /**
   * erc 20 合约的token 交易
   * @param from 转出地址
   * @param to  转入地址
   * @param value  转账数量
   * @param privateKey  私钥
   * @param contractAddress  合约地址
   * @return 转账hash
   */
  public String transferERC20Token(BigInteger nonce, String from, String privateKey, String to, BigInteger value, String contractAddress) throws Exception {
    //加载转账凭证
    Credentials credentials = Credentials.create(privateKey);

    BigInteger gasPrice = getGasPrice();
    BigInteger gasLimit = BigInteger.valueOf(60000L);

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
    if (ethSendTransaction.getError() != null) { //转账报错
      logger.info("transferERC20Token from==" + from + " to==" + to + " has error =" + ethSendTransaction.getError().getMessage());
      return null;
    } else {
      return ethSendTransaction.getTransactionHash();
    }
  }


  /**
   * 获取eth 交易中，最近一笔交易的gas price
   *
   * @return
   * @throws IOException
   */
  private BigInteger getGasPrice() throws Exception {
//    final int gasPriceMinGwei = BalanceUtils.getGasPriceMinGwei();
//    return BalanceUtils.gweiToWei(BigDecimal.valueOf(20 + gasPriceMinGwei));
    return web3j.ethGasPrice().sendAsync().get().getGasPrice();
  }

  /**
   * 获取eth 交易中的 gas limit 默认为21000L
   * @return
   * @throws Exception
   */
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
  public BigInteger getNonce(String from) throws ExecutionException, InterruptedException {
    EthGetTransactionCount transactionCount = web3j.ethGetTransactionCount(from, DefaultBlockParameterName.LATEST).sendAsync().get();
    BigInteger nonce = transactionCount.getTransactionCount();
    logger.info("transaction nonce====" + nonce.longValue());
    return nonce;
  }

  /**
   * 获取eth余额
   * @param address
   * @return
   * @throws Exception
   */
  public double getEthBalance(String address) throws Exception{
    BigInteger balance = web3j.ethGetBalance(address,DefaultBlockParameterName.LATEST).send().getBalance();
    double num = Convert.fromWei(balance.toString(), Convert.Unit.ETHER).doubleValue();
    logger.info(" get ETH num ====="+num);
    return num;
  }

  /**
   * 获取usdt余额
   * @param address
   * @return
   * @throws Exception
   */
  public double getUsdtBalance(String address) throws Exception{
    BigDecimal balance = new BigDecimal(TokenInfoUtils.getTokenBalance(web3j, address, USDT_CONTRACT_ADDRESS));
    balance = balance.divide(BigDecimal.TEN.pow(USDT_TOKEN_DECIMAL));
    return balance.doubleValue();
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
        return blockHash;
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
  public Optional<TransactionReceipt> getTransactionReceipt(String transactionHash){
    try {
      EthGetTransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt(transactionHash).sendAsync().get();
      Optional<TransactionReceipt> receipt = transactionReceipt.getTransactionReceipt();
      return receipt;
    } catch (Exception e) {
      e.printStackTrace();
      logger.info("getTransactionReceipt==" + transactionHash + "error");
      return null;
    }
  }



  /**
   * eth 分发
   * @throws Exception
   */
  public String transEth(String pwd, BigInteger nonce, String toAddress, String amount) throws Exception {
    String privateKey = AESUtils.decrypt(ETH_DISPATCH_PRIVATE_KEY, pwd);
    String hash = signETHTransaction(nonce, "", toAddress, privateKey, amount);
    logger.info("transaction eth address===" + toAddress + " amount===" + amount + "hash ===" + hash);
    return hash;
  }

  /**
   * 聚集usdt
   *
   * @throws Exception
   */
  public String collectUsdt(String pwd, BigInteger nonce,String fromAddress, String privateKey, String amount) throws Exception{
    String collectAddress = AESUtils.decrypt(USDT_COLLECT_ADDRESS, pwd);
    String decryPrivateKey = AESUtils.decrypt(privateKey, pwd);
    String transactionHash = transferERC20Token(nonce,fromAddress, decryPrivateKey, collectAddress, WalletUtils.getFetchMoney(amount, USDT_TOKEN_DECIMAL), USDT_CONTRACT_ADDRESS);
    logger.info("collect usdt from address===" + fromAddress + " toAddress==="+collectAddress+" amount===" + amount + "hash ===" + transactionHash);
    return transactionHash;
  }

  /**
   * 用户提现转账
   * @param to
   * @param value
   * @param contractAddress
   * @return
   */
  public String doFetchCashTransaction(String pwd, BigInteger nonce,String to, BigInteger value, String contractAddress) throws Exception {
    String fromAddress = AESUtils.decrypt(USDT_DISPATCH_ADDRESS, pwd);
    String privateKey = AESUtils.decrypt(USDT_DISPATCH_PRIVATE_KEY, pwd);
    return transferERC20Token(nonce, fromAddress, privateKey, to, value, contractAddress);
  }

  /**
   *
   * @param args
   */
  public static void main(String[] args){
//    WalletTransaction transaction = new WalletTransaction("https://mainnet.infura.io/v3/e7f92614009d4a28b55bb9576b59a828");
//    String address = "0x37f2dfd7e57a1e90ceca529ee9ed50fddebe1b63";
//    String privateKey = "I9OqGysZiKWCEGlN6ULM4N1WkIYh2x2cxkZjz6Empk2uwauBY3JWNOhCtahyFKSTEfv0luIXSw77amyq4qTI++IWkJ6X22bNvjG8n1micak=";
//    String pwd = PasswordGenerator.getPwd();
//    try {
//      double usdtBalance = transaction.getUsdtBalance(address);
//      if (usdtBalance > 500) {
//        BigInteger nonce = transaction.getNonce(address);
//        String hash = transaction.collectUsdt(pwd, nonce, address, privateKey, usdtBalance + "");
//        System.out.println("test trans  address=" + address + "--balance===" + usdtBalance + "--hash=====" + hash);
//        if (hash != null) {
//          while (true) {
//            Optional<TransactionReceipt> receiptOptional = transaction.getTransactionReceipt(hash);
//            if (receiptOptional.isPresent()) {
//              TransactionReceipt receipt = receiptOptional.get();
//
//              logger.info("getTransactionReceipt " + hash + " status==" + receipt.getStatus());
//              logger.info("getTransactionReceipt " + hash + " status ok==" + receipt.isStatusOK());
//              logger.info("getTransactionReceipt " + hash + " gasUsed==" + receipt.getGasUsed());
//              logger.info("getTransactionReceipt " + hash + " blockNumber==" + receipt.getBlockNumber());
//              logger.info("getTransactionReceipt " + hash + " blockHash==" + receipt.getBlockHash());
//              break;
//            } else {
//              Thread.sleep(10000);
//            }
//          }
//        }
//      }
//
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
  }


}
