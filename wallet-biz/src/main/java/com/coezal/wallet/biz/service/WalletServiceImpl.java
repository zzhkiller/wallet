package com.coezal.wallet.biz.service;

import com.coezal.wallet.api.bean.*;
import com.coezal.wallet.api.excetion.BizException;
import com.coezal.wallet.biz.wallet.PasswordGenerator;
import com.coezal.wallet.biz.wallet.WalletGenerator;
import com.coezal.wallet.common.util.JsonUtil;
import com.coezal.wallet.common.util.Md5Util;
import com.coezal.wallet.common.util.RSACoder;
import com.coezal.wallet.common.util.StringFormat;
import com.coezal.wallet.dal.dao.RsaMapper;
import com.coezal.wallet.dal.dao.WalletMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
  WalletMapper walletMapper;

  @Resource
  RsaMapper rsaMapper;
  private static String salt = "gQ#D63K*QW%U9l@X";

  @Override
  public String getWalletAddress(String param) {
    try {
      String paramJson=RSACoder.decryptByPrivateKey1(param);
      WalletAddressRequest walletAddressRequest= JsonUtil.decode(paramJson,WalletAddressRequest.class);
      //校验参数
      checkWalletAddressRequestParams(walletAddressRequest);

      if (!StringFormat.isMatchWalletOwnInfo(walletAddressRequest.getUsersign())) { //参数不是电话或者邮箱
        WalletBean queryWalletBean = new WalletBean();
        queryWalletBean.setOwnerInfo(param);
        WalletBean hadWalletBean = walletMapper.selectOne(queryWalletBean);
        if (hadWalletBean != null) {
          //抛出异常，用户钱包已经存在了，一个用户只能有一个钱包
          return null;
        } else { //查询联系人地址为空的钱包
          queryWalletBean.setOwnerInfo(null);
          List<WalletBean> walletBeanList = walletMapper.selectAll(queryWalletBean);
          if (walletBeanList != null && walletBeanList.size() > 0) {
            WalletBean walletBean = walletBeanList.get(0);
            walletBean.setOwnerInfo(param);
            walletMapper.update(walletBean);//更新钱包数据
            return walletBeanList.get(0).getAddress();
          } else { //钱包地址不够了，重新生成钱包
            WalletBean walletBean = WalletGenerator.createEthWallet(PasswordGenerator.generatorPassword());
            walletBean.setOwnerInfo(param);
            encryptAndInsertWallet(walletBean);
            return walletBean.getAddress();
          }
        }
      } else {
        return null;
      }
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public PayCheckResponse payCheck(String dataStr){
    try {
      String paramJson=RSACoder.decryptByPrivateKey1(dataStr);
      PayCheckRequest payCheckRequest= JsonUtil.decode(paramJson, PayCheckRequest.class);
      //校验参数
      checkPayCheckRequest(payCheckRequest);
      //todo 业务逻辑


    } catch (Exception e) {
      e.printStackTrace();
    }

    return new PayCheckResponse();
  }

  @Override
  public FetchCashResponse fetchCash(String dataStr) {
    try {
      String paramJson=RSACoder.decryptByPrivateKey1(dataStr);
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
    //TODO 获取所有用户的钱包地址
    return null;
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


  private void encryptAndInsertWallet(WalletBean walletBean){
    List<RsaKey> rsaKeyList = rsaMapper.select(null);

    walletBean.setSignKey(rsaKeyList.get(0).getPrivateKey());
//    RSACoder.encryptByPublicKey( ,)
//    walletBean.setPassword();

    walletMapper.insert(walletBean);//插入钱包数据
  }
}
