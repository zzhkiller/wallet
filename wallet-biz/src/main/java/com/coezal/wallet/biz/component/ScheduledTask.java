package com.coezal.wallet.biz.component;

import com.coezal.wallet.dal.dao.FetchCashMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Version 1.0
 * Created by lll on 2019-09-07.
 * Description
 *
 * <pre>
 *   定时任务
 *
 *   1、定时查询转账结果
 *   2、定时处理提现请求
 *
 * </pre>
 * copyright generalray4239@gmail.com
 */

@Component
public class ScheduledTask {

  @Resource
  FetchCashMapper fetchCashMapper;

  /**
   *
   * 当钱包地址用完后，动态生成钱包
   *
   */
  public void createWalletByFixRate(){

  }



}
