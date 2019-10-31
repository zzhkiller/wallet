package com.coezal.wallet.biz.service;

import com.coezal.wallet.api.bean.FetchCash;
import com.coezal.wallet.api.bean.FetchCashRequest;
import com.coezal.wallet.api.bean.Token;
import com.coezal.wallet.api.bean.WalletBean;
import com.coezal.wallet.api.bean.request.CheckFetchCashRequest;
import com.coezal.wallet.api.excetion.BizException;
import com.coezal.wallet.biz.component.AsyncTask;
import com.coezal.wallet.common.util.JsonUtil;
import com.coezal.wallet.common.util.Md5Util;
import com.coezal.wallet.common.util.RSACoder;
import com.coezal.wallet.dal.dao.FetchCashMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.coezal.wallet.api.enums.ResultCode.*;

/**
 * Version 1.0
 * Created by lll on 2019-09-26.
 * Description
 * copyright generalray4239@gmail.com
 */
@Service
public class FetchCashServiceImpl extends BaseService implements FetchCashService {

  private static final Logger logger = LoggerFactory.getLogger("FetchCashServiceImpl");

  @Resource
  FetchCashMapper fetchCashMapper;

  @Autowired
  AsyncTask asyncTask;


  @Resource
  NoticeService noticeService;

  @Override
  public void updateFetchCash(FetchCash cash) {
    fetchCashMapper.update(cash);
  }

  /**
   * 获取所有没通知成功的体现请求
   * @return
   */
  @Override
  public List<FetchCash> getAllNoticeApiNotSuccessFetchCash() {
    FetchCash cash = new FetchCash();
    cash.setNoticeApiSuccess((byte) 0);
    List<FetchCash> cashList = fetchCashMapper.select(cash);
    return cashList;
  }

  /**
   * 提现请求
   *
   * @param dataStr
   * @return
   */
  @Override
  public String getRequest(String dataStr) {
    String paramJson = null;
    try {
      paramJson = RSACoder.decryptAPIParams(dataStr);
    } catch (Exception e) {
      e.printStackTrace();
    }
    FetchCashRequest fetchCashRequest = JsonUtil.decode(paramJson, FetchCashRequest.class);
    //1、校验参数
    checkFetchCashRequest(fetchCashRequest);

    //2、校验token是否存在
    getToken(fetchCashRequest.getTokenname());

    //3、校验用户钱包是否存在
    WalletBean walletBean = getUserWalletBean(fetchCashRequest.getUsersign() + "|" + fetchCashRequest.getCheckcode());

    //4、校验用户钱包是否有过充值记录
    boolean hasTrans = checkTokenTransaction(walletBean.getAddress(), Double.parseDouble(fetchCashRequest.getMoney()));
    if (!hasTrans) {
      throw new BizException(PAY_CHECK_ERROR);
    }

    //5、校验相同提现请求是否已经存在
    FetchCash cash = new FetchCash();
    cash.setCode(fetchCashRequest.getId());
    cash.setMd5chk(fetchCashRequest.getMd5chk());
    cash.setUserSign(fetchCashRequest.getUsersign());
    cash.setCheckCode(fetchCashRequest.getCheckcode());
    cash.setMoney(fetchCashRequest.getMoney());
    cash.setTokenName(fetchCashRequest.getTokenname());
    cash.setServer(fetchCashRequest.getServer());
    cash.setWallet(fetchCashRequest.getWallet());
    cash.setTime(fetchCashRequest.getTime());
    List<FetchCash> result = fetchCashMapper.select(cash);
    if (result != null && result.size() > 0) { //提现请求已经存在
      logger.info("insert fetch cash exit===" + cash.toString());
      throw new BizException(FETCH_CASH_EXIT);
    }

    //校验api 体现是合法提现
    CheckFetchCashRequest request = new CheckFetchCashRequest();
    request.setUsersign(fetchCashRequest.getUsersign());
    request.setCheckcode(fetchCashRequest.getCheckcode());
    request.setId(fetchCashRequest.getId());
    boolean checkFetch = noticeService.checkFetchCash(request);
    if (checkFetch) { //校验通过，
      fetchCashMapper.insert(cash);//存储到数据库
      logger.info("insert fetch cash success===" + cash.toString());
    } else {
      logger.info("insert fetch cash check error===" + cash.toString());
      throw new BizException(FETCH_CASH_ERROR);
    }
    return "success";
  }


  @Override
  public List<FetchCash> getAllFetchCashByUserInfo(int transStatus, int notifyApiStatus) {
    FetchCash cash = new FetchCash();
    cash.setTransactionSuccess((byte)transStatus);
    cash.setNoticeApiSuccess((byte)notifyApiStatus);
    List<FetchCash> cashList = fetchCashMapper.select(cash);
//    if(cashList != null && cashList.size() >0){
//      asyncTask.processUserFetchCash(cashList, this);
//    }
    return cashList;
  }


  private void checkFetchCashRequest(FetchCashRequest fetchCashRequest) {
    StringBuilder sb = new StringBuilder();
    sb.append(fetchCashRequest.getServer());
    sb.append(fetchCashRequest.getUsersign());
    sb.append(fetchCashRequest.getCheckcode());
    sb.append(fetchCashRequest.getId());
    sb.append(fetchCashRequest.getTokenname());
    sb.append(fetchCashRequest.getWallet());
    sb.append(fetchCashRequest.getMoney());
    sb.append(fetchCashRequest.getTime());
    sb.append(salt);
    if (!Objects.equals(fetchCashRequest.getMd5chk(), Md5Util.MD5(sb.toString()))) {
      throw new BizException(MD5_CHECK_ERROR);
    }
  }
}
