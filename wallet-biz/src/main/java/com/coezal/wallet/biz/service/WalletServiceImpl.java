package com.coezal.wallet.biz.service;

import com.coezal.wallet.api.bean.*;
import com.coezal.wallet.api.bean.request.CheckFetchCashRequest;
import com.coezal.wallet.api.bean.request.PaySearchRequest;
import com.coezal.wallet.api.excetion.BizException;
import com.coezal.wallet.biz.component.AsyncTask;
import com.coezal.wallet.biz.util.WalletUtils;
import com.coezal.wallet.biz.wallet.WalletGenerator;
import com.coezal.wallet.common.util.JsonUtil;
import com.coezal.wallet.common.util.Md5Util;
import com.coezal.wallet.common.util.RSACoder;
import com.coezal.wallet.common.util.StringFormat;
import com.coezal.wallet.dal.dao.*;
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
 * Created by lll on 2019-08-27.
 * Description
 * copyright generalray4239@gmail.com
 */

@Service
public class WalletServiceImpl implements WalletService {

  @Resource
  WalletBeanMapper walletMapper;

  @Resource
  TokenTransactionMapper tokenTransactionMapper;

  @Resource
  TokenMapper tokenMapper;

  @Resource
  FetchCashMapper fetchCashMapper;

  @Autowired
  AsyncTask asyncTask;



  private static String salt = "gQ#D63K*QW%U9l@X";

  private static final Logger logger = LoggerFactory.getLogger("WalletServiceImpl");

  @Override
  public GetAddressResponse getWalletAddress(String param) {

    String paramJson = null;
    try {
      paramJson = RSACoder.decryptAPIParams(param);
    } catch (Exception e) {
      throw new BizException(DECRYPT_ERROR);
    }
    WalletAddressRequest walletAddressRequest = JsonUtil.decode(paramJson, WalletAddressRequest.class);
    //校验参数
    checkWalletAddressRequestParams(walletAddressRequest);
    GetAddressResponse response = new GetAddressResponse();
    if (StringFormat.isMatchWalletOwnInfo(walletAddressRequest.getUsersign())) { //参数不是电话或者邮箱
      WalletBean queryWalletBean = new WalletBean();
      queryWalletBean.setOwnerInfo(walletAddressRequest.getUsersign() + "|" + walletAddressRequest.getCheckcode());
      WalletBean hadWalletBean = walletMapper.selectOne(queryWalletBean);
      if (hadWalletBean != null) {
//        //抛出异常，用户钱包已经存在了，一个用户只能有一个钱包
//        throw new BizException("用户已经存在了");
        response.setWallet(hadWalletBean.getAddress());
        return response;
      } else { //查询联系人地址为空的钱包
        queryWalletBean.setOwnerInfo("");
        List<WalletBean> walletBeanList = walletMapper.select(queryWalletBean);
        if (walletBeanList != null && walletBeanList.size() > 0) {
          WalletBean walletBean = walletBeanList.get(0);
          walletBean.setOwnerInfo(walletAddressRequest.getUsersign() + "|" + walletAddressRequest.getCheckcode());
          walletMapper.update(walletBean);//更新钱包数据
          response.setWallet(walletBean.getAddress());
          return response;
        } else { //钱包地址不够了，重新生成钱包
          try {
            WalletBean walletBean = WalletGenerator.createHDWallet();
            walletBean.setOwnerInfo(walletAddressRequest.getUsersign() + "|" + walletAddressRequest.getCheckcode());
            walletMapper.insert(walletBean);
            response.setWallet(walletBean.getAddress());
            return response;
          } catch (Exception e) {
            throw new BizException(CREATE_WALLET_ERROR);
          }
        }
      }
    } else {
      throw new BizException(PARAM_ERROR);
    }
  }

  /**
   * 充值校验
   * @param dataStr
   * @return
   */
  @Override
  public PayCheckResponse payCheck(String dataStr) {
    String paramJson = null;
    try {
      paramJson = RSACoder.decryptAPIParams(dataStr);
    } catch (Exception e) {
      throw new BizException(DECRYPT_ERROR);
    }
    try {
      PayCheckRequest payCheckRequest = JsonUtil.decode(paramJson, PayCheckRequest.class);
      //校验参数
      checkPayCheckRequest(payCheckRequest);
      //第一步：校验用户是否存在钱包地址：
      WalletBean bean = getUserWalletBean(payCheckRequest.getUsersign() + "|" + payCheckRequest.getCheckcode());
      logger.info("paycheck===" + payCheckRequest.toString());

      TokenTransaction transaction = new TokenTransaction();
      transaction.setToAddress(bean.getAddress());
      transaction.setHash(payCheckRequest.getId());

      TokenTransaction result = tokenTransactionMapper.selectOne(transaction);
      if (null != result && result.getValue() != null) {
        PayCheckResponse response = new PayCheckResponse();
        response.setTime(result.getTimeStamp());
        response.setTokenname(result.getTokenSymbol());
        response.setWallet(bean.getAddress());
        logger.info("result TokenTransaction===="+result.toString());
        response.setMoney(WalletUtils.getMoney(transaction.getValue(), transaction.getTokenDecimal()));
        return response;
      } else {
        throw new BizException(PAY_CHECK_ERROR);
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new BizException(PAY_CHECK_ERROR);
    }
  }

  /**
   * 体现请求
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
    getUserWalletBean(fetchCashRequest.getUsersign()+"|"+ fetchCashRequest.getCheckcode());

    //4、校验相同提现请求是否已经存在
    FetchCash cash = new FetchCash();
    cash.setCode(fetchCashRequest.getId());
    cash.setMd5chk(fetchCashRequest.getMd5chk());
    cash.setUserSign(fetchCashRequest.getUsersign());
    cash.setCheckCode(fetchCashRequest.getCheckcode());
    cash.setMoney(fetchCashRequest.getMoney());
    cash.setTokenName(fetchCashRequest.getTokenname());
    cash.setServer(fetchCashRequest.getServer());
    cash.setTime(new Date(fetchCashRequest.getTime()));
    FetchCash result = fetchCashMapper.selectOne(cash);
    if (result != null) { //提现请求已经存在
      throw new BizException(FETCH_CASH_EXIT);
    }

    asyncTask.doFetchCashRequest(fetchCashRequest.getUsersign(),fetchCashRequest.getCheckcode(),fetchCashRequest.getId(),cash);
    return "";
  }

  /**
   * 返回所有关联过用户的钱包地址
   * @return
   */
  @Override
  public List<WalletBean> getAllUserAddresses() {
    return walletMapper.selectAllUsedAddress();
  }

  /**
   * 查询充值结果
   * @param dataStr
   * @return
   */
  @Override
  public void paySearch(String dataStr) {
    PaySearchRequest paySearchRequest;
    String paramJson = null;
    try {
      paramJson = RSACoder.decryptAPIParams(dataStr);
    } catch (Exception e) {
      e.printStackTrace();
      throw new BizException(DECRYPT_ERROR);
    }
    paySearchRequest = JsonUtil.decode(paramJson, PaySearchRequest.class);
    logger.info("paySearch====" + paySearchRequest.toString());
    checkPaySearchRequest(paySearchRequest);//校验参数

    //查询token 对应的contract address
    Token resultToken = getToken(paySearchRequest.getTokenname());
    logger.info("token info===="+resultToken.toString());
    if (resultToken == null || resultToken.getTokenContractAddress() == null) {
      throw new BizException(TOKEN_NOT_EXIT);// 报错，没有找到token数据
    }

    WalletBean resultBean = getUserWalletBean(paySearchRequest.getUsersign() + "|" + paySearchRequest.getCheckcode());
    logger.info("wallet bean==="+resultBean.toString());
    asyncTask.checkUserRecharge(resultBean.getAddress(), resultToken.getTokenContractAddress(), paySearchRequest.getServer(), paySearchRequest.getUsersign(), paySearchRequest.getCheckcode());
  }

  private WalletBean getUserWalletBean(String usrInfo) {
    WalletBean bean = new WalletBean();
    bean.setOwnerInfo(usrInfo);
    WalletBean resultBean = walletMapper.selectOne(bean);
    if (resultBean == null || resultBean.getAddress() == null) {
      throw new BizException(WALLET_NOT_EXIT);
    }
    return resultBean;
  }

  private Token getToken(String tokenSymbol){
    Token token = new Token();
    token.setTokenSymbol(tokenSymbol);
    Token resultToken = tokenMapper.selectOne(token);
    if(resultToken == null){
      throw new BizException(TOKEN_NOT_EXIT);
    }
    return resultToken;
  }


  private void checkWalletAddressRequestParams(WalletAddressRequest walletAddressRequest) {
    StringBuilder sb=new StringBuilder();
    sb.append(walletAddressRequest.getServer());
    sb.append(walletAddressRequest.getUsersign());
    sb.append(walletAddressRequest.getCheckcode());
    sb.append(walletAddressRequest.getTokenname());
    sb.append(salt);
    if(!Objects.equals(walletAddressRequest.getMd5chk(), Md5Util.MD5(sb.toString()))){
      throw new BizException(MD5_CHECK_ERROR);
    }
  }

  private void checkPayCheckRequest(PayCheckRequest payCheckRequest) {
    StringBuilder sb=new StringBuilder();
    sb.append(payCheckRequest.getServer());
    sb.append(payCheckRequest.getUsersign());
    sb.append(payCheckRequest.getCheckcode());
    sb.append(payCheckRequest.getId());
    sb.append(salt);
    if(!Objects.equals(payCheckRequest.getMd5chk(), Md5Util.MD5(sb.toString()))){
      throw new BizException(MD5_CHECK_ERROR);
    }
  }

  private void checkFetchCashRequest(FetchCashRequest fetchCashRequest) {
    StringBuilder sb=new StringBuilder();
    sb.append(fetchCashRequest.getUsersign());
    sb.append(fetchCashRequest.getCheckcode());
    sb.append(fetchCashRequest.getId());
    sb.append(salt);
    if(!Objects.equals(fetchCashRequest.getMd5chk(), Md5Util.MD5(sb.toString()))){
      throw new BizException(MD5_CHECK_ERROR);
    }
  }

  private void checkPaySearchRequest(PaySearchRequest paySearchRequest) {
    StringBuilder sb=new StringBuilder();
    sb.append(paySearchRequest.getServer());
    sb.append(paySearchRequest.getUsersign());
    sb.append(paySearchRequest.getCheckcode());
    sb.append(paySearchRequest.getTokenname());
    sb.append(salt);
    if(!Objects.equals(paySearchRequest.getMd5chk(), Md5Util.MD5(sb.toString()))){
      throw new BizException(MD5_CHECK_ERROR);
    }
  }

}
