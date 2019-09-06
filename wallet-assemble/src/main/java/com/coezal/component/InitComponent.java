package com.coezal.component;

import com.coezal.wallet.api.bean.WalletBean;
import com.coezal.wallet.biz.wallet.WalletGenerator;
import com.coezal.wallet.dal.dao.WalletBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Version 1.0
 * Created by lll on 2019-09-07.
 * Description
 * copyright generalray4239@gmail.com
 */

@Component
public class InitComponent {

  private static final Logger logger = LoggerFactory.getLogger("InitComponent");


  @Resource
  WalletBeanMapper walletBeanMapper;


  /**
   * 初始化钱包
   */
  @Async
  public void initWallet() {
    WalletBean walletBean = new WalletBean();
    walletBean.setOwnerInfo("");
    List<WalletBean> walletBeans = walletBeanMapper.select(walletBean);
    if (walletBeans == null || walletBeans.size() == 0) {
      logger.info("init wallet");
      try {
        for (int i = 0; i < 1000; i++) {
          WalletBean bean = WalletGenerator.createHDWallet();
          bean.setOwnerInfo("");
          walletBeanMapper.insert(bean);
          logger.info("initWallet" + bean.toString());
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }


}
