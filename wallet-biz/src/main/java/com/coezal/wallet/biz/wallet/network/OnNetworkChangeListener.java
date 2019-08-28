package com.coezal.wallet.biz.wallet.network;

import com.coezal.wallet.api.bean.ETHNetworkInfo;

/**
 * Version 1.0
 * Created by lll on 2019-08-28.
 * Description
 * copyright generalray4239@gmail.com
 */
public interface OnNetworkChangeListener {
  /**
   * 切换环境
   * @param networkInfo
   */
  void onNetworkChanged(ETHNetworkInfo networkInfo);
}
