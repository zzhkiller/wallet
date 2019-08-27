package com.coezal.wallet.api.rest;

import com.coezal.wallet.api.bean.request.WalletRequest;
import com.coezal.wallet.api.vo.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Version 1.0
 * Created by lll on 2019-08-27.
 * Description
 * copyright generalray4239@gmail.com
 */

@Api(value = "Wallet API", description = "Wallet API", protocols = "http")
public interface WalletApi {

  @ApiOperation(value="ces", notes="ces", protocols="http")
  @RequestMapping(value = "/getWalletAddress", method = RequestMethod.POST)
  BaseResponse<String> getWalletAddress(WalletRequest request);

  @RequestMapping(value = "/getPublicKey", method = RequestMethod.POST)
  BaseResponse<String> getPublicKey();
}
