package com.coezal.wallet.api.bean;

/**
 * Version 1.0
 * Created by lll on 2019-08-28.
 * Description
 * <pre>
 *   eth network INFO
 * </pre>
 * copyright generalray4239@gmail.com
 */
public class ETHNetworkInfo {
  public final String name;
  public final String symbol;
  public final String rpcServerUrl;
  public final String backendUrl;
  public final String etherscanUrl;
  public final int chainId;
  public final boolean isMainNetwork;

  public ETHNetworkInfo(
          String name,
          String symbol,
          String rpcServerUrl,
          String backendUrl,
          String etherscanUrl,
          int chainId,
          boolean isMainNetwork) {
    this.name = name;
    this.symbol = symbol;
    this.rpcServerUrl = rpcServerUrl;
    this.backendUrl = backendUrl;
    this.etherscanUrl = etherscanUrl;
    this.chainId = chainId;
    this.isMainNetwork = isMainNetwork;
  }
}
