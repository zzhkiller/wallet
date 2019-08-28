package com.coezal.wallet.biz.wallet.network;

import com.coezal.wallet.api.bean.ETHNetworkInfo;
import com.coezal.wallet.api.bean.Token;
import com.coezal.wallet.common.util.TextUtils;
import io.reactivex.Single;

import java.util.HashSet;
import java.util.Set;

import static com.coezal.wallet.context.ETHContext.*;

/**
 * Version 1.0
 * Created by lll on 2019-08-28.
 * Description
 * <pre>
 *   eth  network 网络信息
 * </pre>
 * copyright generalray4239@gmail.com
 */
public class EthereumNetworkRepository implements EthereumNetworkRepositoryType {
  private final ETHNetworkInfo[] NETWORKS = new ETHNetworkInfo[]{

          //eth 正式环境
          new ETHNetworkInfo(ETHEREUM_NETWORK_NAME, ETH_SYMBOL,
                  "https://mainnet.infura.io/v3/e7f92614009d4a28b55bb9576b59a828",
                  "https://api.trustwalletapp.com/",
                  "https://etherscan.io/", 1, true),

//          new ETHNetworkInfo(CLASSIC_NETWORK_NAME, ETC_SYMBOL,
//                  "https://mewapi.epool.io/",
//                  "https://classic.trustwalletapp.com",
//                  "https://gastracker.io", 61, true),
//          new ETHNetworkInfo(POA_NETWORK_NAME, POA_SYMBOL,
//                  "https://core.poa.network",
//                  "https://poa.trustwalletapp.com", "poa", 99, false),
//          new ETHNetworkInfo(KOVAN_NETWORK_NAME, ETH_SYMBOL,
//                  "https://kovan.infura.io/v3/e7f92614009d4a28b55bb9576b59a828",
//                  "https://kovan.trustwalletapp.com/",
//                  "https://kovan.etherscan.io", 42, false),
          //eth ropsten 测试环境
          new ETHNetworkInfo(ROPSTEN_NETWORK_NAME, ETH_SYMBOL,
                  "https://ropsten.infura.io/v3/e7f92614009d4a28b55bb9576b59a828",
                  "https://ropsten.trustwalletapp.com/",
                  "https://ropsten.etherscan.io", 3, false),
  };

//  private final TickerService tickerService;

  private ETHNetworkInfo defaultNetwork = NETWORKS[1]; //ropsten 测试环境

  private final Set<OnNetworkChangeListener> onNetworkChangedListeners = new HashSet<>();

  public EthereumNetworkRepository() {

  }

  private ETHNetworkInfo getByName(String name) {
    if (!TextUtils.isEmpty(name)) {
      for (ETHNetworkInfo NETWORK : NETWORKS) {
        if (name.equals(NETWORK.name)) {
          return NETWORK;
        }
      }
    }
    return null;
  }

  @Override
  public ETHNetworkInfo getDefaultNetwork() {
    return defaultNetwork;
  }

  @Override
  public void setDefaultNetworkInfo(ETHNetworkInfo networkInfo) {
    defaultNetwork = networkInfo;
    for (OnNetworkChangeListener listener : onNetworkChangedListeners) {
      listener.onNetworkChanged(networkInfo);
    }
  }

  @Override
  public ETHNetworkInfo[] getAvailableNetworkList() {
    return NETWORKS;
  }

  @Override
  public void addOnChangeDefaultNetwork(OnNetworkChangeListener onNetworkChanged) {
    onNetworkChangedListeners.add(onNetworkChanged);
  }

  @Override
  public Single<Token> getToken() {
    return null;
  }
}
