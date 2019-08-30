package com.coezal.wallet.biz.wallet;

import java.security.SecureRandom;

/**
 * Version 1.0
 * Created by lll on 2019-08-29.
 * Description
 * <pre>
 *   密码生成器
 * </pre>
 * copyright generalray4239@gmail.com
 */
public class PasswordGenerator {

  /**
   * 生成钱包密码
   * @return
   */
  public static String generatorPassword(){
    byte bytes[] = new byte[256];
    SecureRandom random = new SecureRandom();
    random.nextBytes(bytes);
    return new String(bytes);
  }
}
