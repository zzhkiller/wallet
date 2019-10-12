package com.coezal.wallet.biz.service;

import com.coezal.wallet.api.bean.FetchCash;

import java.util.List;

/**
 * Version 1.0
 * Created by lll on 2019-09-26.
 * Description
 * copyright generalray4239@gmail.com
 */
public interface FetchCashService {

  /**
   * 更改
   * @param cash
   */
  void updateFetchCash(FetchCash cash);


  /**
   * 获取所有通知api 失败的提币请求
   * @return
   */
  List<FetchCash> getAllNoticeApiNotSuccessFetchCash();


  /**
   * 请求提现
   * @param dataStr
   * @return
   */
  String getRequest(String dataStr);


  /**
   * 获取指定用户的体现请求
   * @return
   */
  List<FetchCash> getAllFetchCashByUserInfo(int transStatus, int notifyApiStatus);

}
