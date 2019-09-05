package com.coezal.wallet.biz.service;

import com.coezal.wallet.api.bean.FetchCashResponse;
import com.coezal.wallet.api.bean.GetAddressResponse;
import com.coezal.wallet.api.bean.PayCheckResponse;
import com.coezal.wallet.api.bean.WalletBean;
import com.coezal.wallet.api.vo.base.BaseResponse;

import java.util.List;

/**
 * Version 1.0
 * Created by lll on 2019-08-27.
 * Description
 * copyright generalray4239@gmail.com
 */
public interface WalletService {

  /**
   * 获取钱包地址
   * @param param  电话或者邮箱地址
   * @return
   */
  GetAddressResponse getWalletAddress(String param);

  /**
   * 校验充值请求是否真实
   * @param dataStr
   * @return
   */
  PayCheckResponse payCheck(String dataStr);

  /**
   * 请求提现
   * @param dataStr
   * @return
   */
  FetchCashResponse fetchCash(String dataStr);

  /**
   * 获取所有用户的钱包地址
   * @return
   */
  List<WalletBean> getAllUserAddresses();

  /**
   * 刷新充值（用户进入主界面，请求是否充值成功）
   * @param dataStr
   * @return
   */
  BaseResponse paySearch(String dataStr);
}
