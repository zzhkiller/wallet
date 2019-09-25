package com.coezal.component;

import com.coezal.wallet.api.bean.FetchCash;
import com.coezal.wallet.api.bean.WalletBean;
import com.coezal.wallet.biz.service.WalletService;
import com.coezal.wallet.biz.service.WalletTransactionListenerServiceImpl;
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

import static com.coezal.wallet.biz.util.WalletConstant.USDT_CONTRACT_ADDRESS;
import static com.coezal.wallet.biz.wallet.WalletTransaction.ETH_DISPATCH_ADDRESS;

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

  @Value("${eth.rpc.url}")
  private String rpcUrl;

  /**
   * 每天12点跟有效用户充值eth
   *
   */
//  @Async
//  @Scheduled(cron = "0 0 0 * * ?")
//  public void sendEthToUserAddress() {
//    List<WalletBean> walletBeanList = walletService.getAllUserAddresses();
//    WalletTransactionListenerServiceImpl impl = new WalletTransactionListenerServiceImpl();
//    String pwd = WalletGenerator.getPwd();
//    for (WalletBean bean : walletBeanList) {
//       //获取用户钱包数量 usdt 数量
//      String balance = impl.getWalletBalanceOfByAddressAndTokenContractAddress("official", bean.getAddress(), "");
//      if (balance != null && !balance.equals("0")) {
//        String privateKey = AESUtils.decrypt(bean.getPrivateKey(), pwd);
//      }
//    }
//  }

  /**
   * 每天0点搜集U到指定账户
   */
  @Async
  @Scheduled(cron = "0 0 0 * * ?")
  public void dispatchEthToUserAddress() {
    List<WalletBean> walletBeanList = walletService.getAllUserAddresses();
    WalletTransaction transaction = new WalletTransaction(rpcUrl);
    if (walletBeanList != null && walletBeanList.size() > 0) {
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
   * 定时检查用户提现是否成功
   */
//  @Async
//  @Scheduled(cron = "0 0 0 * * ?")
//  public void checkFetchCashRequest() {
//    List<FetchCash> cashList = walletService.getAllFetchCashRequest();
//    if (cashList != null && cashList.size() > 0) {
//      WalletTransaction transaction = new WalletTransaction(rpcUrl);
//      for (FetchCash cash : cashList) {
//        String hash = cash.getTransactionHash();
//        if (hash != null) {
//          TransactionReceipt receipt = transaction.getTransactionReceipt(hash);
//          if (receipt != null) {
//
//          }
//        }
//      }
//    }
//  }
}
