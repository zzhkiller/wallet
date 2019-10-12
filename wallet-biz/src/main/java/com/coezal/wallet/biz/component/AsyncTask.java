package com.coezal.wallet.biz.component;

import com.coezal.wallet.api.bean.*;
import com.coezal.wallet.api.bean.request.CheckFetchCashRequest;
import com.coezal.wallet.api.bean.request.FetchCashResultRequest;
import com.coezal.wallet.api.excetion.BizException;
import com.coezal.wallet.biz.service.FetchCashService;
import com.coezal.wallet.biz.service.NoticeService;
import com.coezal.wallet.biz.service.TokenService;
import com.coezal.wallet.biz.service.WalletTransactionListenerServiceImpl;
import com.coezal.wallet.biz.util.WalletUtils;
import com.coezal.wallet.biz.wallet.PasswordGenerator;
import com.coezal.wallet.biz.wallet.WalletTransaction;
import com.coezal.wallet.common.util.AESUtils;
import com.coezal.wallet.dal.dao.FetchCashMapper;
import com.coezal.wallet.dal.dao.TokenTransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static com.coezal.wallet.api.enums.ResultCode.FETCH_CASH_ERROR;
import static com.coezal.wallet.biz.util.WalletConstant.ETH_DISPATCH_ADDRESS;
import static com.coezal.wallet.biz.util.WalletConstant.USDT_DISPATCH_ADDRESS;
import static com.coezal.wallet.biz.wallet.PasswordGenerator.getPwd;

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
  TokenService tokenService;

  @Value("${eth.rpc.url}")
  private String rpcUrl;

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

  /**
   * 通知api充值成功
   * @param userSign
   * @param checkCode
   * @param userWalletAddress
   * @param transaction
   * @return
   */
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
//    if (beanList != null && beanList.size() > 0) {
//      WalletTransaction transaction = new WalletTransaction(rpcUrl);
//      String pwd = PasswordGenerator.getPwd();
//      String collectAddress = AESUtils.decrypt(ETH_DISPATCH_ADDRESS, pwd);
//      BigInteger nonce = null;
//      for (WalletBean bean : beanList) {
//        try {
//          //获取用户钱包数量 usdt 数量
//          double usdtBalance = transaction.getUsdtBalance(bean.getAddress());
//          if (usdtBalance > 500) {//usdt 数量大于500
//            double ethBalance = transaction.getEthBalance(bean.getAddress()); //获取eth剩余量
//            if (nonce == null) {
//              nonce = transaction.getNonce(collectAddress);
//            } else {
//              nonce = nonce.add(new BigInteger("1"));
//            }
//            if (ethBalance < 0.009) { //eth 数量太少
//              logger.info("schedule  dispatch eth address = " + bean.getAddress() + "=====usdt balance==" + usdtBalance + "===eth balance==" + ethBalance);
//              String hash = transaction.transEth(pwd, nonce, bean.getAddress(), "0.009");
//              if (hash == null || hash.equals("")) {//转账异常了，暂停，等待下一次
//                return;
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

  @Async
  public void collectUsdtToCollectAddress(List<WalletBean> beanList){
//    if (beanList != null && beanList.size() > 0) {
//      WalletTransaction transaction = new WalletTransaction(rpcUrl);
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
//              logger.info("collectUsdtToCollectAddress from_address" + bean.getAddress() + "    usdt balance==" + usdtBalance + "  hash===" + hash);
//              if (hash != null) {
//                while (true) {
//                  Optional<TransactionReceipt> receiptOptional = transaction.getTransactionReceipt(hash);
//                  if (receiptOptional.isPresent()) {
//                    TransactionReceipt receipt = receiptOptional.get();
//                    logger.info("collectUsdtToCollectAddress from_address" + bean.getAddress() + " hash===" + hash + " status==" + receipt.getStatus());
//                    logger.info("collectUsdtToCollectAddress from_address" + bean.getAddress() + " hash===" + hash + " status ok==" + receipt.isStatusOK());
//                    logger.info("collectUsdtToCollectAddress from_address" + bean.getAddress() + " hash===" + hash + " gasUsed==" + receipt.getGasUsed());
//                    logger.info("collectUsdtToCollectAddress from_address" + bean.getAddress() + " hash===" + hash + " blockNumber==" + receipt.getBlockNumber());
//                    logger.info("collectUsdtToCollectAddress from_address" + bean.getAddress() + " hash===" + hash + " blockHash==" + receipt.getBlockHash());
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


  @Async
  public void processUserFetchCash(List<FetchCash> cashList, FetchCashService fetchCashService) {
//    logger.info("processUserFetchCash size===" + cashList.size());
//    if (cashList != null && cashList.size() > 0) {
//      WalletTransaction transaction = new WalletTransaction(rpcUrl);
//      String pwd = PasswordGenerator.getPwd();
//      String dispatchAddress = AESUtils.decrypt(USDT_DISPATCH_ADDRESS, pwd);
//      BigInteger nonce = null;
//      for (FetchCash cash : cashList) {
//        logger.info("processUserFetchCash dispatchAddress===" + dispatchAddress + "====cash===" + cash.toString());
//        if (cash.getServer() == "test") { //直接通知用户体现成功
//          logger.info("processUserFetchCash test FetchCash===" + cash.getTokenName());
//          boolean success = noticeApiFetchCash(cash, "1");
//          byte sByte = success ? (byte) 1 : (byte) 0;
//          cash.setNoticeApiSuccess(sByte);
//          fetchCashService.updateFetchCash(cash);
//          continue;
//        }
//        Token token = tokenService.getTokenInfoByTokenName(cash.getTokenName());
//        if (token == null) {
//          logger.info("processUserFetchCash can not find token===" + cash.getTokenName());
//          continue;
//        }
//        try {
//          if (cash.getTransactionSuccess() == (byte) 1) {//提币成功了，没通知成功,重新通知
//            boolean success = noticeApiFetchCash(cash, "1");
//            byte sByte = success ? (byte) 1 : (byte) 0;
//            cash.setNoticeApiSuccess(sByte);
//            fetchCashService.updateFetchCash(cash);
//            logger.info("processUserFetchCash user+" + cash.getUserSign() + " ======address===" + cash.getWallet() + "== tran success notice failed");
//          } else {
//            if (nonce == null) {
//              nonce = transaction.getNonce(dispatchAddress);
//            } else {
//              nonce = nonce.add(new BigInteger("1"));
//            }
//            BigInteger amount = WalletUtils.getFetchMoney(cash.getMoney(), token.getTokenDecimals());
//            String hash = transaction.doFetchCashTransaction(pwd, dispatchAddress, nonce, cash.getWallet(), amount, token.getTokenContractAddress());
//            logger.info("processUserFetchCash user+" + cash.getUserSign() + " ======address===" + cash.getWallet() + "====nonce==="+nonce+"====money=="+cash.getMoney()+"==== hash==="+hash);
//
//            if (hash != null) {
//              while (true) {
//                Optional<TransactionReceipt> receiptOptional = transaction.getTransactionReceipt(hash);
//                logger.info("processUserFetchCash user+" + cash.getUserSign() + " ======address===" + cash.getWallet() + "=money=="+cash.getMoney()+"==== hash==="+hash+"=====receipt==="+receiptOptional.isPresent());
//
//                if (receiptOptional.isPresent()) {
//                  TransactionReceipt receipt = receiptOptional.get();
//                  logger.info("checkFetchCashRequest user+"+cash.getUserSign()+"==to_address" + cash.getWallet() + " hash===" + hash + " status==" + receipt.getStatus());
//                  logger.info("checkFetchCashRequest user+"+cash.getUserSign()+"==to_address" + cash.getWallet() + " hash===" + hash + " status ok==" + receipt.isStatusOK());
//                  logger.info("checkFetchCashRequest user+"+cash.getUserSign()+"==to_address" + cash.getWallet() + " hash===" + hash + " gasUsed==" + receipt.getGasUsed());
//                  logger.info("checkFetchCashRequest user+"+cash.getUserSign()+"==to_address" + cash.getWallet() + " hash===" + hash + " blockNumber==" + receipt.getBlockNumber());
//                  logger.info("checkFetchCashRequest user+"+cash.getUserSign()+"==to_address" + cash.getWallet() + " hash===" + hash + " blockHash==" + receipt.getBlockHash());
//
//                  if (receipt.isStatusOK()) {//提币成功
////                    cash.setTransactionHash(hash);
//                    cash.setTransactionSuccess((byte) 1);
//                    fetchCashService.updateFetchCash(cash);
//                    boolean success = noticeApiFetchCash(cash, "1");
//                    byte sByte = success ? (byte) 1 : (byte) 0;
//                    cash.setNoticeApiSuccess(sByte);
//                    fetchCashService.updateFetchCash(cash);
//                    logger.info("checkFetchCashRequest user+" + cash.getUserSign() + "==to_address" + cash.getWallet() + " notice api success=" + sByte);
//                  } else {
//                    noticeApiFetchCash(cash, "-1");
//                    logger.info("checkFetchCashRequest user+" + cash.getUserSign() + "==to_address" + cash.getWallet() + " notice api error=" );
//                  }
//                  break;
//                } else {
//                  logger.info("checkFetchCashRequest user+" + cash.getUserSign() + "==to_address" + cash.getWallet() + "no result  thread 10s");
//                  Thread.sleep(10000);
//                }
//              }
//            }
//          }
//        } catch (Exception e) {
//          e.printStackTrace();
//          logger.info("checkFetchCashRequest user+"+cash.getUserSign()+"  address" + cash.getWallet() + "");
//        }
//      }
//    }
  }

  /**
   * 发送提现请求
   * @param cash
   * @param status 提现状态，1，成功 -1 失败
   * @return
   */
//  private boolean noticeApiFetchCash(FetchCash cash, String status){
//    FetchCashResultRequest resultRequest = new FetchCashResultRequest();
//    resultRequest.setUsersign(cash.getUserSign());
//    resultRequest.setCheckcode(cash.getCheckCode());
//    resultRequest.setId(cash.getCode());
//    resultRequest.setTokenname(cash.getTokenName());
//    resultRequest.setWallet(cash.getWallet());
//    resultRequest.setMoney(cash.getMoney());
//    resultRequest.setStatus(status);
//    return noticeService.fetchCashResult(resultRequest);
//  }
}
