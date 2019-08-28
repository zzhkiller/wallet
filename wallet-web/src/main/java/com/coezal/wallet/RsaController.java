package com.coezal.wallet;

import com.coezal.wallet.api.rest.RsaApi;
import com.coezal.wallet.api.vo.base.BaseResponse;
import com.coezal.wallet.biz.service.RsaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Version 1.0
 * Created by lll on 2019-08-27.
 * Description
 * copyright generalray4239@gmail.com
 */

@RestController
public class RsaController extends AbstractController implements RsaApi {

  @Resource
  RsaService rsaService;

  private static final Logger logger = LoggerFactory.getLogger("WalletController");

  @Override
  public BaseResponse<String> getPublicKey(HttpServletRequest request) {
    return buildJson(rsaService.getPublicKey());
  }
}
