package com.coezal.wallet.biz.service;

import com.coezal.wallet.api.bean.Token;
import com.coezal.wallet.api.bean.TokenTranscation;
import com.coezal.wallet.api.vo.base.BaseResponse;
import com.coezal.wallet.api.vo.base.ETHScanBaseResponse;
import com.coezal.wallet.biz.util.ThirdApiInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.web3j.protocol.core.DefaultBlockParameterName;

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
public class WalletTransactionListenerServiceImpl extends ThirdApiInvoker  implements WalletTransactionListenerService{

  private static final Logger logger = LoggerFactory.getLogger("WalletTransactionListenerServiceImpl");

  @Value("${eth.scan.url}")
  private String domain;

  /**
   * 请求对应的api key
   */
  private static final String API_KEY = "NC3T8DA821WK436KE13TQNRQXWJDG5ZRHZ";

  @Override
  public void getTransactionByAddressAndTokenContractAddress(String address, String tokenContractAddress) {
    try {
      StringBuilder sb = new StringBuilder("https://api.etherscan.io/api");
      sb.append("?module=account&action=txlist&address=").append(address);
      sb.append("&startblock=0&endblock=").append(DefaultBlockParameterName.LATEST);
      sb.append("&page=1&offset=3&sort=asc&apikey=").append(API_KEY);
      ETHScanBaseResponse<List<TokenTranscation>> baseResponse = doHttpGet(sb.toString(), ETHScanBaseResponse.class, null, null);
      List<TokenTranscation>  transcationist = baseResponse.getResult();

      for(TokenTranscation transcation: transcationist){
        System.out.println(transcation.toString());
      }


    } catch (Exception e) {
      e.printStackTrace();
//      return Boolean.FALSE;
    }
  }


  public static void main(String[] args){

    WalletTransactionListenerServiceImpl im = new WalletTransactionListenerServiceImpl();

    im.getTransactionByAddressAndTokenContractAddress("0x16be8F8fe00587AFa9e95744745C7124D6806535","");
  }


}
