package com.coezal.wallet.api.rest;

import com.coezal.wallet.api.vo.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Version 1.0
 * Created by lll on 2019-08-27.
 * Description
 * copyright generalray4239@gmail.com
 */

@Api(value = "des API", description = "des API", protocols = "http")
public interface RsaApi {


  @ApiOperation(value = "获取DES public key", notes = "获取DES public key", protocols = "https", httpMethod = "GET")
  @RequestMapping(value = "/getPublicKey", method = RequestMethod.POST)
  BaseResponse<String> getPublicKey(HttpServletRequest request);

}
