package com.coezal.wallet.biz.component;

import com.coezal.wallet.api.bean.*;
import com.coezal.wallet.api.bean.request.CheckFetchCashRequest;
import com.coezal.wallet.api.excetion.BizException;
import com.coezal.wallet.biz.service.NoticeService;
import com.coezal.wallet.biz.service.WalletTransactionListenerServiceImpl;
import com.coezal.wallet.biz.util.WalletUtils;
import com.coezal.wallet.biz.wallet.PasswordGenerator;
import com.coezal.wallet.biz.wallet.WalletGenerator;
import com.coezal.wallet.biz.wallet.WalletTransaction;
import com.coezal.wallet.common.util.AESUtils;
import com.coezal.wallet.dal.dao.FetchCashMapper;
import com.coezal.wallet.dal.dao.TokenTransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static com.coezal.wallet.api.enums.ResultCode.FETCH_CASH_ERROR;
import static com.coezal.wallet.biz.util.WalletConstant.USDT_CONTRACT_ADDRESS;
import static com.coezal.wallet.biz.wallet.PasswordGenerator.getPwd;
import static com.coezal.wallet.biz.wallet.WalletTransaction.ETH_DISPATCH_ADDRESS;

/**
 * Version 1.0
 * Created by lll on 2019-09-07.
 * Description
 * copyright generalray4239@gmail.com
 */

@Component
public class AsyncTask {

  private static final Logger logger  = LoggerFactory.getLogger("AsyncTask");

  @Resource
  NoticeService noticeService;

  @Resource
  TokenTransactionMapper tokenTransactionMapper;

  @Resource
  FetchCashMapper mapper;

  @Value("${eth.rpc.url}")
  String web3jUrl;

  /**
   * 异步检查用户充值记录
   * @param userWalletAddress
   * @param tokenContractAddress
   * @param server  检查环境： test | official
   * @param userSign
   * @param checkCode
   */
  @Async
  public void checkUserRecharge(String userWalletAddress, String tokenContractAddress, String server, String userSign, String checkCode) {

    //2、查询服务器，获取充值数据
    WalletTransactionListenerServiceImpl impl = new WalletTransactionListenerServiceImpl();
    List<TokenTransaction> transactionList = impl.getTransactionByAddressAndTokenContractAddress(server, userWalletAddress, tokenContractAddress);
    if (transactionList != null && transactionList.size() > 0) { //
      for (TokenTransaction transaction : transactionList) {
        if (transaction.getToAddress().equals(userWalletAddress)) { //如果是转入
          try {
            transaction.setNotifySuccessFlag((byte) 0);
            tokenTransactionMapper.insert(transaction);
            boolean success = sendRechargeNotice(userSign, checkCode, userWalletAddress, transaction);
            if (success) {
              transaction.setNotifySuccessFlag((byte) 1);
              tokenTransactionMapper.update(transaction);//插入数据
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  private boolean sendRechargeNotice(String userSign, String checkCode, String userWalletAddress, TokenTransaction transaction) {
    RechargeRequest rechargeRequest = new RechargeRequest();
    rechargeRequest.setUsersign(userSign);
    rechargeRequest.setCheckcode(checkCode);
    rechargeRequest.setId(transaction.getHash());
    rechargeRequest.setTokenname(transaction.getTokenSymbol());
    rechargeRequest.setWallet(userWalletAddress);
    rechargeRequest.setTime(transaction.getTimeStamp());
    rechargeRequest.setMoney(WalletUtils.getMoney(transaction.getValue(), transaction.getTokenDecimal()));
    return noticeService.rechargeNotice(rechargeRequest);
  }


  /**
   * 异步提现
   * @param userSign
   * @param checkCode
   * @param id
   * @param cash
   */
  @Async
  public void doFetchCashRequest(String userSign, String checkCode, long id, FetchCash cash, Token token) {
    CheckFetchCashRequest request = new CheckFetchCashRequest();
    request.setUsersign(userSign);
    request.setCheckcode(checkCode);
    request.setId(id);
    boolean checkFetch = noticeService.checkFetchCash(request);
    if (checkFetch) { //校验通过，
      mapper.insert(cash);//转账
    } else {
      throw new BizException(FETCH_CASH_ERROR);
    }
  }

  /**
   * 获取有用户关联的钱包地址余额
   * @param beanList
   */
  @Async
  public void getAllBalanceNotNullAddress(List<WalletBean> beanList, String contractAddress){
    WalletTransactionListenerServiceImpl impl = new WalletTransactionListenerServiceImpl();
    String pwd = getPwd();
    for (WalletBean bean : beanList) {
      String balance = impl.getWalletBalanceOfByAddressAndTokenContractAddress("official", bean.getAddress(), contractAddress);
      if (balance != null && !balance.equals("0")) {
        String privateKey = AESUtils.decrypt(bean.getPrivateKey(), pwd);
        logger.info("the address== " + bean.getAddress() + " privatekey=====" + privateKey + " balance==" + balance);
      }
    }
  }

  /**
   * 分发eth 或者收集usdt
   * @param beanList
   */
  @Async
  public void collectUsdtTokenToAddress(List<WalletBean> beanList) {
//    WalletTransaction transaction = new WalletTransaction(web3jUrl);
//    if (beanList != null && beanList.size() > 0) {
//      String pwd = PasswordGenerator.getPwd();
//      for (WalletBean bean : beanList) {
//        try {
//          //获取用户钱包数量 usdt 数量
//          double usdtBalance = transaction.getUsdtBalance(bean.getAddress());
//          if (usdtBalance > 500) {//usdt 数量大于500
//            double ethBalance = transaction.getEthBalance(bean.getAddress()); //获取eth剩余量
//            if (ethBalance >= 0.009) { //eth 数量太少
//              BigInteger nonce = transaction.getNonce(bean.getAddress());
//              String hash = transaction.collectUsdt(pwd, nonce, bean.getAddress(), bean.getPrivateKey(), usdtBalance + "");
//              logger.info("schedule  collect usdt address = " + bean.getAddress() + "==private key==="+bean.getPrivateKey()+"-----usdt balance==" + usdtBalance + "===eth balance==" + ethBalance + "=====hash ====");
//              if (hash != null) {
//                while (true) {
//                  Optional<TransactionReceipt> receiptOptional = transaction.getTransactionReceipt(hash);
//                  if (receiptOptional.isPresent()) {
//                    TransactionReceipt receipt = receiptOptional.get();
//
//                    logger.info("getTransactionReceipt " + hash + " status==" + receipt.getStatus());
//                    logger.info("getTransactionReceipt " + hash + " status ok==" + receipt.isStatusOK());
//                    logger.info("getTransactionReceipt " + hash + " gasUsed==" + receipt.getGasUsed());
//                    logger.info("getTransactionReceipt " + hash + " blockNumber==" + receipt.getBlockNumber());
//                    logger.info("getTransactionReceipt " + hash + " blockHash==" + receipt.getBlockHash());
//                    break;
//                  } else {
//                    Thread.sleep(10000);
//                  }
//                }
//              }
//            }
//          }
//        } catch (Exception e) {
//          e.printStackTrace();
//          logger.info("schedule  collectUsdtTokenToAddress error = " + bean.getAddress(), e);
//        }
//      }
//    }
  }

}
