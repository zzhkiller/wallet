package com.coezal.wallet.api.rest;

import com.coezal.wallet.api.bean.FetchCash;
import com.coezal.wallet.api.bean.GetAddressResponse;
import com.coezal.wallet.api.bean.PayCheckResponse;
import com.coezal.wallet.api.bean.WalletBean;
import com.coezal.wallet.api.vo.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Version 1.0
 * Created by lll on 2019-08-27.
 * Description
 * copyright generalray4239@gmail.com
 */

@Api(value = "FetchCash API", description = "FetchCash API", protocols = "http")
public interface FetchCashApi {

  @ApiOperation(value = "获取所有用户体现请求", notes = "获取所有用户体现请求", protocols = "https")
  @RequestMapping(value = "/getAllFetchCashRequest", method = RequestMethod.GET)
  BaseResponse<List<FetchCash>> getAllFetchCashRequest(@RequestParam(value = "trans") Integer transactionSuccess, @RequestParam(value = "notifyapi") Integer notifyApiSuccess);


}
