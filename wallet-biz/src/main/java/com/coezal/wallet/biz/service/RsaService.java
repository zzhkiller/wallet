package com.coezal.wallet.biz.service;

/**
 * Version 1.0
 * Created by lll on 2019-08-27.
 * Description
 * copyright generalray4239@gmail.com
 */
public interface RsaService {

  /**
   * 获取公钥
   * @return
   */
  String getPublicKey();

  /**
   * 获取私钥
   * @return
   */
  String getPrivateKey();
}
