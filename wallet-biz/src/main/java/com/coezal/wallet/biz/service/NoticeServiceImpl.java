package com.coezal.wallet.biz.service;

import com.coezal.wallet.api.bean.ApiReq;
import com.coezal.wallet.api.bean.request.CheckFetchCashRequest;
import com.coezal.wallet.api.bean.request.FetchCashResultRequest;
import com.coezal.wallet.api.bean.RechargeRequest;
import com.coezal.wallet.api.vo.base.BaseResponse;
import com.coezal.wallet.biz.util.ThirdApiInvoker;
import com.coezal.wallet.common.util.JsonUtil;
import com.coezal.wallet.common.util.Md5Util;
import com.coezal.wallet.common.util.RSACoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl extends ThirdApiInvoker implements NoticeService {
    private static String salt = "gQ#D63K*QW%U9l@X";
    @Value("${api.domain}")
    private String domain;

    @Override
    public Boolean rechargeNotice(RechargeRequest rechargeRequest) {
        StringBuilder sb = new StringBuilder();
        sb.append(rechargeRequest.getUsersign());
        sb.append(rechargeRequest.getCheckcode());
        sb.append(rechargeRequest.getId());
        sb.append(rechargeRequest.getTokenname());
        sb.append(rechargeRequest.getWallet());
        sb.append(rechargeRequest.getMoney());
        sb.append(rechargeRequest.getTime());
        sb.append(salt);
        System.out.println("rechargeNotice===str--------"+sb.toString());
        rechargeRequest.setMd5chk(Md5Util.MD5(sb.toString()));
        String jsonObj = JsonUtil.encode(rechargeRequest);
        ApiReq apiReq = new ApiReq();
        try {
            apiReq.setDatastr(RSACoder.encryptAPIParams(jsonObj));
            System.out.println("rechargeNotice==="+jsonObj);
            System.out.println("rechargeNotice==="+apiReq.getDatastr());
            BaseResponse baseResponse = doHttpPost(domain + "/api/pay/paynotice", BaseResponse.class, null, apiReq.getDatastr());
            if (baseResponse.getCode() == 0) {
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 提现校验
     * @param checkFetchCashRequest
     * @return
     */
    @Override
    public Boolean checkFetchCash(CheckFetchCashRequest checkFetchCashRequest) {
        StringBuilder sb = new StringBuilder();
        sb.append(checkFetchCashRequest.getUsersign());
        sb.append(checkFetchCashRequest.getCheckcode());
        sb.append(checkFetchCashRequest.getId());
        sb.append(salt);
        checkFetchCashRequest.setMd5chk(Md5Util.MD5(sb.toString()));
        String jsonObj = JsonUtil.encode(checkFetchCashRequest);
        ApiReq apiReq = new ApiReq();
        try {
            apiReq.setDatastr(RSACoder.encryptAPIParams(jsonObj));
            System.out.println("rechargeNotice==="+jsonObj);
            System.out.println("rechargeNotice==="+apiReq.getDatastr());
            BaseResponse baseResponse = doHttpPost(domain + "/api/pay/getcheck", BaseResponse.class, null, apiReq.getDatastr());
            if (baseResponse.getCode() == 0) {
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 体现成功的通知
     * @param fetchCashResultRequest
     * @return
     */
    @Override
    public Boolean fetchCashResult(FetchCashResultRequest fetchCashResultRequest) {
        StringBuilder sb = new StringBuilder();
        sb.append(fetchCashResultRequest.getUsersign());
        sb.append(fetchCashResultRequest.getCheckcode());
        sb.append(fetchCashResultRequest.getId());
        sb.append(fetchCashResultRequest.getTokenname());
        sb.append(fetchCashResultRequest.getWallet());
        sb.append(fetchCashResultRequest.getMoney());
        sb.append(fetchCashResultRequest.getStatus());
        sb.append(salt);
        fetchCashResultRequest.setMd5chk(Md5Util.MD5(sb.toString()));
        String jsonObj = JsonUtil.encode(fetchCashResultRequest);
        ApiReq apiReq = new ApiReq();
        try {
            apiReq.setDatastr(RSACoder.encryptAPIParams(jsonObj));
            System.out.println("rechargeNotice==="+jsonObj);
            System.out.println("rechargeNotice==="+apiReq.getDatastr());
            BaseResponse baseResponse = doHttpPost(domain + "/api/pay/getnotice", BaseResponse.class, null, apiReq.getDatastr());
            if (baseResponse.getCode() == 0) {
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
