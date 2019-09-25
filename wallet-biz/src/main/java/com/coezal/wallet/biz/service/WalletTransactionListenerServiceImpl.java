package com.coezal.wallet.biz.service;

import com.alibaba.fastjson.TypeReference;
import com.coezal.wallet.api.bean.TokenTransaction;
import com.coezal.wallet.api.vo.base.ETHScanBaseResponse;
import com.coezal.wallet.biz.util.ThirdApiInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * Version 1.0
 * Created by lll on 2019-09-02.
 * Description
 * <pre>
 *   钱包转账监控工具
 * </pre>
 * copyright generalray4239@gmail.com
 */
public class WalletTransactionListenerServiceImpl extends ThirdApiInvoker implements WalletTransactionListenerService {

  private static final Logger logger = LoggerFactory.getLogger("WalletTransactionListenerServiceImpl");

  @Value("${eth.scan.url}")
  private String domain;

  @Value("${eth.rpc.url}")
  private String rpcUrl;


  /**
   * 请求对应的api key
   */
  private static final String API_KEY = "NC3T8DA821WK436KE13TQNRQXWJDG5ZRHZ";


  /**
   *
   * 测试环境查询链接
   * https://api-ropsten.etherscan.io/api?module=account&action=tokentx&contractaddress=0x9f8f72aa9304c8b593d555f12ef6589cc3a579a2&address=0x4e83362442b8d1bec281594cea3050c8eb01311c&page=1&offset=100&sort=asc&apikey=YourApiKeyToken
   *
   * 线上环境：
   * https://api.etherscan.io/api?module=account&action=tokentx&contractaddress=0x9f8f72aa9304c8b593d555f12ef6589cc3a579a2&address=0x4e83362442b8d1bec281594cea3050c8eb01311c&page=1&offset=100&sort=asc&apikey=YourApiKeyToken
   *
   * @param address  钱包地址
   * @param tokenContractAddress 合约地址
   */
  public List<TokenTransaction> getTransactionByAddressAndTokenContractAddress(String server, String address, String tokenContractAddress) {
    try {
      StringBuilder sb = new StringBuilder();
      if (server.equalsIgnoreCase("official")) {//正式环境
        sb.append("https://api.etherscan.io/api");
      } else {
        sb.append("https://api-ropsten.etherscan.io/api");
      }
      sb.append("?module=account&action=tokentx&contractaddress=").append(tokenContractAddress);
      sb.append("&address=").append(address);
      sb.append("&page=1&offset=100&sort=asc&apikey=").append(API_KEY);

      ETHScanBaseResponse<List<TokenTransaction>> baseResponse = getETHScanBaseResponse(sb.toString(), null, new TypeReference<ETHScanBaseResponse<List<TokenTransaction>>>() {
      }, null);
      return baseResponse.getResult();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 获取余额
   *
   * 正式环境
   * https://api.etherscan.io/api?module=account&action=tokenbalance&contractaddress=0x57d90b64a1a57749b0f932f1a3395792e12e7055&address=0xe04f27eb70e025b78871a2ad7eabe85e61212761&tag=latest&apikey=YourApiKeyToken
   *
   *
   * @param fromAddress
   * @param tokenContractAddress
   */
  @Override
  public String getWalletBalanceOfByAddressAndTokenContractAddress(String server,String fromAddress, String tokenContractAddress) {
    //查询余额
    try {
      StringBuilder sb = new StringBuilder();
      if (server.equalsIgnoreCase("official")) {//正式环境
        sb.append("https://api.etherscan.io/api");
      } else {
        sb.append("https://api-ropsten.etherscan.io/api"); //测试环境没有获取余额的api暂时不管
      }
      sb.append("?module=account&action=tokenbalance&contractaddress=").append(tokenContractAddress);
      sb.append("&address=").append(fromAddress);
      sb.append("&tag=latest&apikey=").append(API_KEY);

      ETHScanBaseResponse<String> baseResponse = doHttpGet(sb.toString(), ETHScanBaseResponse.class, null, null);
      System.out.println("balance==="+baseResponse.getResult());
      return baseResponse.getResult();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }





  public static void main(String[] args) {
    WalletTransactionListenerServiceImpl im = new WalletTransactionListenerServiceImpl();
    //
        im.getTransactionByAddressAndTokenContractAddress("official","0x16be8F8fe00587AFa9e95744745C7124D6806535", "0x99a3721266997cd8CB48aFE11b3D43B4eb70d281");
//    String money = im.getWalletBalanceOfByAddressAndTokenContractAddress("official","0x16be8F8fe00587AFa9e95744745C7124D6806535", "0x99a3721266997cd8CB48aFE11b3D43B4eb70d281");
//    String dM = WalletUtils.getMoney(money, 18+"");
//    System.out.println("format balance==="+dM);
  }


}
