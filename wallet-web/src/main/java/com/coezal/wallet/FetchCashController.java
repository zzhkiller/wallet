package com.coezal.wallet;

import com.coezal.wallet.api.bean.FetchCash;
import com.coezal.wallet.api.rest.FetchCashApi;
import com.coezal.wallet.api.vo.base.BaseResponse;
import com.coezal.wallet.biz.service.FetchCashService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Version 1.0
 * Created by lll on 2019-10-11.
 * Description
 * copyright generalray4239@gmail.com
 */

@RestController
public class FetchCashController extends AbstractController implements FetchCashApi {

  @Resource
  FetchCashService fetchCashService;


  @Override
  public BaseResponse<List<FetchCash>> getAllFetchCashRequest(Integer transactionSuccess, Integer notifyApiSuccess) {
    return buildJson(fetchCashService.getAllFetchCashByUserInfo(transactionSuccess, notifyApiSuccess));
  }
}
