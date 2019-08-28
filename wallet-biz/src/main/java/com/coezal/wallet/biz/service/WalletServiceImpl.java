package com.coezal.wallet.biz.service;

import com.coezal.wallet.api.bean.Wallet;
import com.coezal.wallet.common.util.RSACoder;
import com.coezal.wallet.common.util.StringFormat;
import com.coezal.wallet.common.util.WalletUtils;
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


  @Override
  public String getWalletAddress(String param) {
    try {
      //@todo  模拟rsa解密数据
      byte[] bytes = param.getBytes();
      String ownerInfo = new String(RSACoder.decryptByPrivateKey(param.getBytes(), bytes));
      if (!StringFormat.isMatchWalletOwnInfo(ownerInfo)) { //参数不是电话或者邮箱
        Wallet queryWallet = new Wallet();
        queryWallet.setOwnerInfo(param);
        Wallet hadWallet = walletMapper.selectOne(queryWallet);
        if (hadWallet != null) {
          //抛出异常，用户钱包已经存在了，一个用户只能有一个钱包
          return null;
        } else { //查询联系人地址为空的钱包
          queryWallet.setOwnerInfo(null);
          List<Wallet> walletList = walletMapper.selectAll(queryWallet);
          if (walletList != null && walletList.size() > 0) {
            Wallet wallet = walletList.get(0);
            wallet.setOwnerInfo(param);
            walletMapper.update(wallet);//更新钱包数据
            return walletList.get(0).getAddress();
          } else { //钱包地址不够了，重新生成钱包
            Wallet wallet = WalletUtils.createWallet();
            wallet.setOwnerInfo(param);
            walletMapper.insert(wallet);//插入钱包数据
            return wallet.getAddress();
          }
        }
      } else {
        return null;
      }
    } catch (Exception e) {
      return null;
    }
//    return "0x90008C50463fd01106bE72Fcff15e44e86c8088E";
  }
}
