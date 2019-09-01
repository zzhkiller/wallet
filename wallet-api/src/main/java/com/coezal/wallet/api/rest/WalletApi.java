package com.coezal.wallet.api.rest;

import com.coezal.wallet.api.vo.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Version 1.0
 * Created by lll on 2019-08-27.
 * Description
 * copyright generalray4239@gmail.com
 */

@Api(value = "WalletBean API", description = "WalletBean API", protocols = "http")
public interface WalletApi {

  @ApiOperation(value = "获取钱包地址", notes = "获取钱包地址", protocols = "https")
  @RequestMapping(value = "/getWalletAddress", method = RequestMethod.POST)
  BaseResponse<String> getWalletAddress(@ApiParam(value = "请求", required = true) @RequestParam(value = "dataStr") String dataStr);


}
