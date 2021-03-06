package com.coezal.wallet.biz.service;

import com.coezal.wallet.api.bean.request.CheckFetchCashRequest;
import com.coezal.wallet.api.bean.RechargeRequest;
import com.coezal.wallet.api.bean.request.FetchCashResultRequest;

public interface NoticeService {
    /**
     * 充值成功通知
     * @param rechargeRequest
     * @return
     */
    Boolean rechargeNotice(RechargeRequest rechargeRequest);

    /**
     * 校验提现请求是否真实
     * @return
     */
    Boolean checkFetchCash(CheckFetchCashRequest checkWithdrawalRequest);

    /**
     * 提现成功通知
     * @param withdrawalResultRequest
     * @return
     */
    Boolean fetchCashResult(FetchCashResultRequest withdrawalResultRequest);

}
