package com.coezal.wallet;

import com.coezal.wallet.api.bean.FetchCashResponse;
import com.coezal.wallet.api.bean.PayCheckResponse;
import com.coezal.wallet.api.bean.WalletBean;
import com.coezal.wallet.api.rest.WalletApi;
import com.coezal.wallet.api.vo.base.BaseResponse;
import com.coezal.wallet.biz.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Version 1.0
 * Created by lll on 2019-08-27.
 * Description
 * copyright generalray4239@gmail.com
 */

@RestController
public class WalletController extends AbstractController implements WalletApi {

  @Resource
  WalletService walletService;

  private static final Logger logger  = LoggerFactory.getLogger("WalletController");

  @Override
  public BaseResponse<String> getWalletAddress(String str) {
    return buildJson(walletService.getWalletAddress(str));
  }

  @Override
  public BaseResponse<PayCheckResponse> payCheck(String dataStr) {
    return buildJson(walletService.payCheck(dataStr));
  }

  @Override
  public BaseResponse<FetchCashResponse> fetchCash(String dataStr) {
    return buildJson(walletService.fetchCash(dataStr));
  }

  @Override
  public BaseResponse<List<WalletBean>> getAllUserAddresses() {
    return buildJson(walletService.getAllUserAddresses());
  }

}
