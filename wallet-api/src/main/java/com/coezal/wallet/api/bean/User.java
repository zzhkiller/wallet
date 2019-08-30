package com.coezal.wallet.api.bean;

/**
 * Version 1.0
 * Created by lll on 2019-08-30.
 * Description
 * <pre>
 *   用户信息
 * </pre>
 * copyright generalray4239@gmail.com
 */
public class User {

  private Long id;
  /**
   * 用户电话或者邮箱
   */
  private String usersign;

  /**
   * api 服务生成的用户校验码
   */
  private String checkcode;

}
