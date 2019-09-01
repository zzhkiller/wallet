package com.coezal.wallet.biz.service;

import com.coezal.wallet.api.bean.RsaKey;
import com.coezal.wallet.api.bean.WalletAddressRequest;
import com.coezal.wallet.api.bean.WalletBean;
import com.coezal.wallet.biz.wallet.PasswordGenerator;
import com.coezal.wallet.biz.wallet.WalletGenerator;
import com.coezal.wallet.common.util.JsonUtil;
import com.coezal.wallet.common.util.RSACoder;
import com.coezal.wallet.common.util.StringFormat;
import com.coezal.wallet.dal.dao.RsaMapper;
import com.coezal.wallet.dal.dao.WalletMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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


  @Override
  public String getWalletAddress(String param) {
    try {
      String paramJson=RSACoder.decryptByPrivateKey1(param);
      WalletAddressRequest walletAddressRequest= JsonUtil.decode(paramJson,WalletAddressRequest.class);
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


  private void encryptAndInsertWallet(WalletBean walletBean){
    List<RsaKey> rsaKeyList = rsaMapper.select(null);

    walletBean.setSignKey(rsaKeyList.get(0).getPrivateKey());
//    RSACoder.encryptByPublicKey( ,)
//    walletBean.setPassword();

    walletMapper.insert(walletBean);//插入钱包数据
  }
}
