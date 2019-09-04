package com.coezal.wallet.api.rest;

import com.coezal.wallet.api.bean.FetchCashResponse;
import com.coezal.wallet.api.bean.PayCheckResponse;
import com.coezal.wallet.api.bean.WalletBean;
import com.coezal.wallet.api.vo.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.ModelAttribute;
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

@Api(value = "WalletBean API", description = "WalletBean API", protocols = "http")
public interface WalletApi {

  @ApiOperation(value = "获取钱包地址", notes = "获取钱包地址", protocols = "https")
  @RequestMapping(value = "/getaddr", method = RequestMethod.POST)
  BaseResponse<String> getAddr( @ApiParam(value = "请求", required = true) @RequestParam(value = "dataStr") String dataStr);

  @ApiOperation(value = "校验充值请求是否真实", notes = "校验充值请求是否真实", protocols = "https")
  @RequestMapping(value = "/paycheck", method = RequestMethod.POST)
  BaseResponse<PayCheckResponse> payCheck(@ApiParam(value = "请求", required = true) @RequestParam(value = "dataStr") String dataStr);


  @ApiOperation(value = "请求提现", notes = "fetchCash", protocols = "https")
  @RequestMapping(value = "/fetchCash", method = RequestMethod.POST)
  BaseResponse<FetchCashResponse> fetchCash(@ApiParam(value = "请求", required = true) @RequestParam(value = "dataStr") String dataStr);

  @ApiOperation(value = "刷新用户充值记录", notes = "paySearch", protocols = "https")
  @RequestMapping(value = "/paysearch", method = RequestMethod.POST)
  BaseResponse<BaseResponse> paySearch(@ApiParam(value = "请求", required = true) @RequestParam(value = "dataStr") String dataStr);


  @ApiOperation(value = "所有用户的钱包地址", notes = "所有用户的钱包地址", protocols = "https")
  @RequestMapping(value = "/getAllAddress", method = RequestMethod.GET)
  BaseResponse<List<WalletBean>> getAllUserAddresses();



}
