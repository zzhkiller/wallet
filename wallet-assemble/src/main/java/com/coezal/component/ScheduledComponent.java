package com.coezal.component;

import com.coezal.wallet.api.bean.WalletBean;
import com.coezal.wallet.biz.service.WalletService;
import com.coezal.wallet.biz.service.WalletTransactionListenerServiceImpl;
import com.coezal.wallet.biz.wallet.WalletGenerator;
import com.coezal.wallet.biz.wallet.WalletTransaction;
import com.coezal.wallet.common.util.AESUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

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
  @Async
  @Scheduled(cron = "0 0 0 * * ?")
  public void sendEthToUserAddress() {
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
  }

  /**
   * 每天2点搜集U到指定账户
   */
  @Async
  @Scheduled(cron = "0 0 0 * * ?")
  public void collectUsdtTokenToAddress() {
    try {
      List<WalletBean> walletBeanList = walletService.getAllUserAddresses();
      WalletTransactionListenerServiceImpl impl = new WalletTransactionListenerServiceImpl();
      WalletTransaction transaction = new WalletTransaction(rpcUrl);
      for (WalletBean bean : walletBeanList) {
        //获取用户钱包数量 usdt 数量
        double usdtBalance = Double.parseDouble(impl.getWalletBalanceOfByAddressAndTokenContractAddress("official", bean.getAddress(), ""));
        if (usdtBalance > 500) {//usdt 数量大于500
          double ethBalance = transaction.getEthBalance(bean.getAddress()); //获取eth剩余量
          if (ethBalance < 0.005) { //eth 数量太少
            transaction.transEth(bean.getAddress(), "0.005");
            logger.info("schedule  dispatch eth address = " + bean.getAddress() + "=====usdt balance==" + usdtBalance + "===eth balance==" + ethBalance);
          } else { //聚集usdt
            String hash = transaction.collectUsdt(bean.getAddress(), bean.getPrivateKey(), usdtBalance + "");
            logger.info("schedule  collect usdt address = " + bean.getAddress() + "=====usdt balance==" + usdtBalance + "===eth balance==" + ethBalance + " success== " + (hash == null));
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 定时检查用户提现是否成功
   */
  @Async
  @Scheduled(cron = "0 0 0 * * ?")
  public void checkFetchCashRequest() {

  }
}
