package com.coezal.wallet.biz.service;

import com.coezal.wallet.api.bean.*;
import com.coezal.wallet.api.bean.request.PaySearchRequest;
import com.coezal.wallet.api.excetion.BizException;
import com.coezal.wallet.api.vo.base.BaseResponse;
import com.coezal.wallet.biz.util.WalletUtils;
import com.coezal.wallet.biz.wallet.PasswordGenerator;
import com.coezal.wallet.biz.wallet.WalletGenerator;
import com.coezal.wallet.common.util.JsonUtil;
import com.coezal.wallet.common.util.Md5Util;
import com.coezal.wallet.common.util.RSACoder;
import com.coezal.wallet.common.util.StringFormat;
import com.coezal.wallet.dal.dao.RsaKeyMapper;
import com.coezal.wallet.dal.dao.TokenMapper;
import com.coezal.wallet.dal.dao.TokenTransactionMapper;
import com.coezal.wallet.dal.dao.WalletBeanMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

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
  RsaKeyMapper rsaMapper;

  @Resource
  TokenTransactionMapper tokenTransactionMapper;

  @Resource
  TokenMapper tokenMapper;

  @Resource
  NoticeService noticeService;


  private static String salt = "gQ#D63K*QW%U9l@X";

  @Override
  public String getWalletAddress(String param) {

    String paramJson = null;
    try {
      paramJson = RSACoder.decryptAPIParams(param);
    } catch (Exception e) {
      throw new BizException("解密参数异常");
    }
    WalletAddressRequest walletAddressRequest = JsonUtil.decode(paramJson, WalletAddressRequest.class);
    //校验参数
    checkWalletAddressRequestParams(walletAddressRequest);

    if (StringFormat.isMatchWalletOwnInfo(walletAddressRequest.getUsersign())) { //参数不是电话或者邮箱
      WalletBean queryWalletBean = new WalletBean();
      queryWalletBean.setOwnerInfo(walletAddressRequest.getUsersign() + "|" + walletAddressRequest.getCheckcode());
      WalletBean hadWalletBean = walletMapper.selectOne(queryWalletBean);
      if (hadWalletBean != null) {
//        //抛出异常，用户钱包已经存在了，一个用户只能有一个钱包
//        throw new BizException("用户已经存在了");
        return hadWalletBean.getAddress();
      } else { //查询联系人地址为空的钱包
        queryWalletBean.setOwnerInfo(null);
        List<WalletBean> walletBeanList = walletMapper.select(queryWalletBean);
        if (walletBeanList != null && walletBeanList.size() > 0) {
          WalletBean walletBean = walletBeanList.get(0);
          walletBean.setOwnerInfo(walletAddressRequest.getUsersign() + "|" + walletAddressRequest.getCheckcode());
          walletMapper.update(walletBean);//更新钱包数据
          return walletBean.getAddress();
        } else { //钱包地址不够了，重新生成钱包
          try {
            WalletBean walletBean = WalletGenerator.createHDWallet();
            walletBean.setOwnerInfo(walletAddressRequest.getUsersign() + "|" + walletAddressRequest.getCheckcode());
            walletMapper.insert(walletBean);
            return walletBean.getAddress();
          } catch (Exception e) {
            throw new BizException("生成钱包异常"+ e.getMessage());
          }
        }
      }
    } else {
      throw new BizException("用户必须是手机号或者邮箱");
    }
  }

  /**
   * 充值校验
   * @param dataStr
   * @return
   */
  @Override
  public PayCheckResponse payCheck(String dataStr) {
    try {
      String paramJson = RSACoder.decryptAPIParams(dataStr);
      PayCheckRequest payCheckRequest = JsonUtil.decode(paramJson, PayCheckRequest.class);
      //校验参数
      checkPayCheckRequest(payCheckRequest);
      //第一步：校验用户是否存在钱包地址：
      WalletBean bean = new WalletBean();
      bean.setOwnerInfo(payCheckRequest.getUsersign() + "|" + payCheckRequest.getCheckcode());
      WalletBean resultBean = walletMapper.selectOne(bean);
      if (resultBean == null || resultBean.getAddress() == null) {
        throw new BizException(payCheckRequest.getUsersign() + "没有钱包地址");
      }

      TokenTransaction transaction = new TokenTransaction();
      transaction.setTo(bean.getAddress());
      transaction.setHash(payCheckRequest.getId());

      TokenTransaction result = tokenTransactionMapper.selectOne(transaction);
      if (result != null || result.getValue() != null) {
        PayCheckResponse response = new PayCheckResponse();
        response.setTime(result.getTimeStamp());
        response.setTokenname(result.getTokenSymbol());
        response.setWallet(bean.getAddress());
        return response;
      } else {
        throw new BizException(payCheckRequest.getId() + "该充值不存在");
      }
    } catch (Exception e) {
      throw new BizException(e.getMessage());
    }
  }

  @Override
  public FetchCashResponse fetchCash(String dataStr) {
    try {
      String paramJson=RSACoder.decryptAPIParams(dataStr);
      FetchCashRequest fetchCashRequest= JsonUtil.decode(paramJson, FetchCashRequest.class);
      //校验参数
      checkFetchCashRequest(fetchCashRequest);
      //todo 业务逻辑
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new FetchCashResponse();
  }

  @Override
  public List<WalletBean> getAllUserAddresses() {

    return null;
  }

  /**
   * 查询充值结果
   * @param dataStr
   * @return
   */
  @Override
  public BaseResponse paySearch(String dataStr) {
    try {
      String paramJson = RSACoder.decryptAPIParams(dataStr);
      PaySearchRequest paySearchRequest= JsonUtil.decode(paramJson, PaySearchRequest.class);
      checkPaySearchRequest(paySearchRequest);//校验参数
      //查询token 对应的contract address
      Token token = new Token();
      token.setTokenSymbol(paySearchRequest.getTokenname());
      Token resultToken  = tokenMapper.selectOne(token);
      if(resultToken == null || token.getTokenContractAddress() == null){
        // 报错，没有找到token数据
        throw new BizException("没有找到对应的token");
      }
      WalletBean bean = new WalletBean();
      bean.setOwnerInfo(paySearchRequest.getUsersign()+"|"+paySearchRequest.getCheckcode());
      WalletBean resultBean = walletMapper.selectOne(bean);

      if(resultBean == null || resultBean.getAddress() == null){
        throw new BizException(paySearchRequest.getUsersign()+"没有钱包地址");
      }

      new Thread(){
        @Override
        public void run() {
          try {
            TokenTransaction queryT = new TokenTransaction();
            queryT.setTo(resultBean.getAddress());
            TokenTransaction lastToken = tokenTransactionMapper.selectOne(queryT);
            WalletTransactionListenerServiceImpl impl = new WalletTransactionListenerServiceImpl();
            String balance = impl.getWalletBalanceOfByAddressAndTokenContractAddress(paySearchRequest.getServer(), resultBean.getAddress(), resultToken.getTokenContractAddress());
            if(balance != null && !balance.equals(0)){ //用户余额不为0
              List<TokenTransaction> transactionList = impl.getTransactionByAddressAndTokenContractAddress(paySearchRequest.getServer(), resultBean.getAddress(), resultToken.getTokenContractAddress());
              if (transactionList == null || transactionList.size() > 0) { //
                for(TokenTransaction transaction: transactionList){
                  if (transaction.getTo().equals(resultBean.getAddress())) { //如果是转入
                    if (lastToken != null && Long.parseLong(transaction.getTimeStamp()) > Long.parseLong(lastToken.getTimeStamp())) {
                      //存储到数据库，通知api有充值
                      tokenTransactionMapper.update(transaction);
                      RechargeRequest rechargeRequest = new RechargeRequest();
                      rechargeRequest.setUsersign(paySearchRequest.getUsersign());
                      rechargeRequest.setCheckcode(paySearchRequest.getCheckcode());
                      rechargeRequest.setId(transaction.getHash());
                      rechargeRequest.setTokenname(transaction.getTokenSymbol());
                      rechargeRequest.setTime(transaction.getTimeStamp());
                      rechargeRequest.setMoney(WalletUtils.getMoney(transaction.getValue(), transaction.getTokenDecimal()));
                      noticeService.rechargeNotice(rechargeRequest);
                    } else if (lastToken == null) {
                      tokenTransactionMapper.insert(transaction);//插入数据
                      //通知API有充值
                      RechargeRequest rechargeRequest = new RechargeRequest();
                      rechargeRequest.setUsersign(paySearchRequest.getUsersign());
                      rechargeRequest.setCheckcode(paySearchRequest.getCheckcode());
                      rechargeRequest.setId(transaction.getHash());
                      rechargeRequest.setTokenname(transaction.getTokenSymbol());
                      rechargeRequest.setTime(transaction.getTimeStamp());
                      rechargeRequest.setMoney(WalletUtils.getMoney(transaction.getValue(), transaction.getTokenDecimal()));
                      noticeService.rechargeNotice(rechargeRequest);
                    }
                  }
                }
              }
            }
          } catch (Exception e) {
            throw new BizException(e.getMessage());
          }
        }
      }.start();
      return new BaseResponse();
    } catch (Exception e) {
      throw new BizException("解密参数异常");
    }
  }

  private void checkWalletAddressRequestParams(WalletAddressRequest walletAddressRequest) {
    StringBuilder sb=new StringBuilder();
    sb.append(walletAddressRequest.getServer());
    sb.append(walletAddressRequest.getUsersign());
    sb.append(walletAddressRequest.getCheckcode());
    sb.append(walletAddressRequest.getTokenname());
    sb.append(salt);
    if(!Objects.equals(walletAddressRequest.getMd5chk(), Md5Util.MD5(sb.toString()))){
      throw new BizException("加密错误");
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
      throw new BizException("加密错误");
    }
  }

  private void checkFetchCashRequest(FetchCashRequest fetchCashRequest) {
    StringBuilder sb=new StringBuilder();
    sb.append(fetchCashRequest.getUsersign());
    sb.append(fetchCashRequest.getCheckcode());
    sb.append(fetchCashRequest.getId());
    sb.append(salt);
    if(!Objects.equals(fetchCashRequest.getMd5chk(), Md5Util.MD5(sb.toString()))){
      throw new BizException("加密错误");
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
      throw new BizException("加密错误");
    }
  }

}
