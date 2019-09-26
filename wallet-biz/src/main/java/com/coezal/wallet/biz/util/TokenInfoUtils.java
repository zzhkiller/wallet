package com.coezal.wallet.biz.util;

import com.coezal.wallet.api.bean.Token;
import com.coezal.wallet.dal.dao.TokenMapper;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.coezal.wallet.biz.util.WalletConstant.USDT_CONTRACT_ADDRESS;
import static com.coezal.wallet.biz.util.WalletConstant.USDT_TOKEN_DECIMAL;

/**
 * Version 1.0
 * Created by lll on 2019-09-03.
 * Description
 * copyright generalray4239@gmail.com
 */
public class TokenInfoUtils {

  private static String emptyAddress = "0x0000000000000000000000000000000000000000";


  /**
   * 查询带单位的代币余额
   * @param web3j
   * @param fromAddress
   * @param contractAddress
   * @return BigInteger 返回对应的bigInteger 对象
   */
  public static BigInteger getTokenBalance(Web3j web3j, String fromAddress, String contractAddress) {

    String methodName = "balanceOf";
    List<Type> inputParameters = new ArrayList<>();
    List<TypeReference<?>> outputParameters = new ArrayList<>();
    Address address = new Address(fromAddress);
    inputParameters.add(address);

    TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
    };
    outputParameters.add(typeReference);
    Function function = new Function(methodName, inputParameters, outputParameters);
    String data = FunctionEncoder.encode(function);
    Transaction transaction = Transaction.createEthCallTransaction(fromAddress, contractAddress, data);

    EthCall ethCall;
    BigInteger balanceValue = BigInteger.ZERO;
    try {
      ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
      List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
      if (results != null && results.size() > 0) {
        balanceValue = (BigInteger) results.get(0).getValue();
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
    return balanceValue;
  }

  /**
   * 查询不带单位的代币余额
   * @param web3j
   * @param fromAddress
   * @param contractAddress
   * @param tokenDecimal 代币单位
   * @return 返回日常显示的余额
   */
  public static double getTokenBalance(Web3j web3j, String fromAddress, String contractAddress, int tokenDecimal) {
    BigInteger bal = getTokenBalance(web3j, fromAddress, contractAddress);
    BigDecimal balance = new BigDecimal(bal);
    balance = balance.divide(BigDecimal.TEN.pow(tokenDecimal));
    return balance.doubleValue();
  }

  /**
   * 查询代币名称
   *
   * @param web3j
   * @param contractAddress
   * @return
   */
  public static String getTokenName(Web3j web3j, String contractAddress) {
    String methodName = "name";
    String name = null;
    String fromAddr = emptyAddress;
    List<Type> inputParameters = new ArrayList<>();
    List<TypeReference<?>> outputParameters = new ArrayList<>();

    TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
    };
    outputParameters.add(typeReference);

    Function function = new Function(methodName, inputParameters, outputParameters);

    String data = FunctionEncoder.encode(function);
    Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

    EthCall ethCall;
    try {
      ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
      List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
      name = results.get(0).getValue().toString();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return name;
  }

  /**
   * 查询代币符号
   *
   * @param web3j
   * @param contractAddress
   * @return
   */
  public static String getTokenSymbol(Web3j web3j, String contractAddress) {
    String methodName = "symbol";
    String symbol = null;
    String fromAddr = emptyAddress;
    List<Type> inputParameters = new ArrayList<>();
    List<TypeReference<?>> outputParameters = new ArrayList<>();

    TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
    };
    outputParameters.add(typeReference);

    Function function = new Function(methodName, inputParameters, outputParameters);

    String data = FunctionEncoder.encode(function);
    Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

    EthCall ethCall;
    try {
      ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
      List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
      symbol = results.get(0).getValue().toString();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return symbol;
  }

  /**
   * 查询代币精度
   *
   * @param web3j
   * @param contractAddress
   * @return
   */
  public static int getTokenDecimals(Web3j web3j, String contractAddress) {
    String methodName = "decimals";
    String fromAddr = emptyAddress;
    int decimal = 0;
    List<Type> inputParameters = new ArrayList<>();
    List<TypeReference<?>> outputParameters = new ArrayList<>();

    TypeReference<Uint8> typeReference = new TypeReference<Uint8>() {
    };
    outputParameters.add(typeReference);

    Function function = new Function(methodName, inputParameters, outputParameters);

    String data = FunctionEncoder.encode(function);
    Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

    EthCall ethCall;
    try {
      ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
      List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
      decimal = Integer.parseInt(results.get(0).getValue().toString());
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return decimal;
  }

  /**
   * 查询代币发行总量
   *
   * @param web3j
   * @param contractAddress
   * @return
   */
  public static BigInteger getTokenTotalSupply(Web3j web3j, String contractAddress) {
    String methodName = "totalSupply";
    String fromAddr = emptyAddress;
    BigInteger totalSupply = BigInteger.ZERO;
    List<Type> inputParameters = new ArrayList<>();
    List<TypeReference<?>> outputParameters = new ArrayList<>();

    TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
    };
    outputParameters.add(typeReference);

    Function function = new Function(methodName, inputParameters, outputParameters);

    String data = FunctionEncoder.encode(function);
    Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

    EthCall ethCall;
    try {
      ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
      List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
      totalSupply = (BigInteger) results.get(0).getValue();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return totalSupply;
  }

}
