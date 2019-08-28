package com.coezal.wallet.biz.wallet.network;

import com.coezal.wallet.api.bean.ETHNetworkInfo;
import com.coezal.wallet.api.bean.Token;
import io.reactivex.Single;

/**
 * Version 1.0
 * Created by lll on 2019-08-28.
 * Description
 * <pre>
 *
 * </pre>
 * copyright generalray4239@gmail.com
 */
public interface EthereumNetworkRepositoryType {
  ETHNetworkInfo getDefaultNetwork();

  void setDefaultNetworkInfo(ETHNetworkInfo networkInfo);

  ETHNetworkInfo[] getAvailableNetworkList();

  void addOnChangeDefaultNetwork(OnNetworkChangeListener onNetworkChanged);

  /**
   *
   * @return
   */
  Single<Token> getToken();

}
