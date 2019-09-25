package com.coezal.component;

import com.coezal.wallet.api.bean.FetchCash;
import com.coezal.wallet.api.bean.Token;
import com.coezal.wallet.api.bean.WalletBean;
import com.coezal.wallet.biz.service.TokenService;
import com.coezal.wallet.biz.service.WalletService;
import com.coezal.wallet.biz.wallet.PasswordGenerator;
import com.coezal.wallet.biz.wallet.WalletTransaction;
import com.coezal.wallet.common.util.AESUtils;
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

import static com.coezal.wallet.biz.util.WalletConstant.ETH_DISPATCH_ADDRESS;
import static com.coezal.wallet.biz.util.WalletConstant.USDT_DISPATCH_ADDRESS;

/**
 * Version 1.0
 * Created by lll on 2019-09-22.
 * Description 定时任务
 * <pre>
 *   定时任务
 * </pre>
 * copyright generalray4239@gmail.com
 */
@Component
public class ScheduledComponent {

  private static final Logger logger = LoggerFactory.getLogger("ScheduledComponent");

  @Resource
  WalletService walletService;

  @Resource
  TokenService tokenService;

  @Value("${eth.rpc.url}")
  private String rpcUrl;



  /**
   * 每天0点做分eth分发
   */
  @Async
  @Scheduled(cron = "0 0 0 * * ?")
  public void dispatchEthToUserAddress() {
    List<WalletBean> walletBeanList = walletService.getAllUserAddresses();
    if (walletBeanList != null && walletBeanList.size() > 0) {
      WalletTransaction transaction = new WalletTransaction(rpcUrl);
      String pwd = PasswordGenerator.getPwd();
      String collectAddress = AESUtils.decrypt(ETH_DISPATCH_ADDRESS, pwd);
      BigInteger nonce = null;
      for (WalletBean bean : walletBeanList) {
        try {
          //获取用户钱包数量 usdt 数量
          double usdtBalance = transaction.getUsdtBalance(bean.getAddress());
          if (usdtBalance > 500) {//usdt 数量大于500
            double ethBalance = transaction.getEthBalance(bean.getAddress()); //获取eth剩余量
            if (nonce == null) {
              nonce = transaction.getNonce(collectAddress);
            } else {
              nonce = nonce.add(new BigInteger("1"));
            }
            if (ethBalance < 0.009) { //eth 数量太少
              logger.info("schedule  dispatch eth address = " + bean.getAddress() + "=====usdt balance==" + usdtBalance + "===eth balance==" + ethBalance);
              String hash = transaction.transEth(pwd, nonce, bean.getAddress(), "0.009");
              if (hash == null || hash.equals("")) {//转账异常了，暂停，等待下一次
                return;
              }
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
          logger.info("schedule  collectUsdtTokenToAddress error = " + bean.getAddress(), e);
        }
      }
    }
  }

  /**
   * 每天5点做USDT收集
   */
  @Async
  @Scheduled(cron = "0 0 5 * * ?")
  public void collectUsdtToCollectAddress(){
    List<WalletBean> walletBeanList = walletService.getAllUserAddresses();
    if (walletBeanList != null && walletBeanList.size() > 0) {
      WalletTransaction transaction = new WalletTransaction(rpcUrl);
      String pwd = PasswordGenerator.getPwd();
      for (WalletBean bean : walletBeanList) {
        try {
          //获取用户钱包数量 usdt 数量
          double usdtBalance = transaction.getUsdtBalance(bean.getAddress());
          if (usdtBalance > 500) {//usdt 数量大于500
            double ethBalance = transaction.getEthBalance(bean.getAddress()); //获取eth剩余量
            if (ethBalance >= 0.009) { //eth 数量太少
              BigInteger nonce = transaction.getNonce(bean.getAddress());
              String hash = transaction.collectUsdt(pwd, nonce, bean.getAddress(), bean.getPrivateKey(), usdtBalance + "");
              logger.info("collectUsdtToCollectAddress from_address" + bean.getAddress() + "    usdt balance==" + usdtBalance + "  hash===" + hash);
              if (hash != null) {
                while (true) {
                  Optional<TransactionReceipt> receiptOptional = transaction.getTransactionReceipt(hash);
                  if (receiptOptional.isPresent()) {
                    TransactionReceipt receipt = receiptOptional.get();
                    logger.info("collectUsdtToCollectAddress from_address" + bean.getAddress() + " hash===" + hash + " status==" + receipt.getStatus());
                    logger.info("collectUsdtToCollectAddress from_address" + bean.getAddress() + " hash===" + hash + " status ok==" + receipt.isStatusOK());
                    logger.info("collectUsdtToCollectAddress from_address" + bean.getAddress() + " hash===" + hash + " gasUsed==" + receipt.getGasUsed());
                    logger.info("collectUsdtToCollectAddress from_address" + bean.getAddress() + " hash===" + hash + " blockNumber==" + receipt.getBlockNumber());
                    logger.info("collectUsdtToCollectAddress from_address" + bean.getAddress() + " hash===" + hash + " blockHash==" + receipt.getBlockHash());
                    break;
                  } else {
                    Thread.sleep(10000);
                  }
                }
              }
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
          logger.info("schedule  collectUsdtTokenToAddress error = " + bean.getAddress(), e);
        }
      }
    }
  }


  /**
   * 定时检查用户提现是否成功
   */
  @Async
  @Scheduled(cron = "0 0 0 * * ?")
  public void checkFetchCashRequest() {
    List<FetchCash> cashList = walletService.getAllFetchCashRequest();
    if (cashList != null && cashList.size() > 0) {
      WalletTransaction transaction = new WalletTransaction(rpcUrl);
      String pwd = PasswordGenerator.getPwd();
      String dispatchAddress = AESUtils.decrypt(USDT_DISPATCH_ADDRESS, pwd);
      BigInteger nonce = null;
      for (FetchCash cash : cashList) {
        Token token = tokenService.getTokenInfoByTokenName(cash.getTokenName());
        if (token == null) {
          logger.info("can not find token===" + cash.getTokenName());
          continue;
        }
      }
    }
  }
}
