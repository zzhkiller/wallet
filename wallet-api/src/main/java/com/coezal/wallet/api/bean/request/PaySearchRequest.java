package com.coezal.wallet.api.bean.request;

/**
 * Version 1.0
 * Created by lll on 2019-09-03.
 * Description
 * copyright generalray4239@gmail.com
 */
public class PaySearchRequest {
  /**
   * test 测试环境
   * official 正式环境
   */
  private String server;

  /**用户标记*/
  private String usersign;

  /**用户校验码*/
  private String checkcode;

  /**币名称*/
  private String tokenname;

  /**md5校验*/
  private String md5chk;

  public String getServer() {
    return server;
  }

  public void setServer(String server) {
    this.server = server;
  }

  public String getUsersign() {
    return usersign;
  }

  public void setUsersign(String usersign) {
    this.usersign = usersign;
  }

  public String getCheckcode() {
    return checkcode;
  }

  public void setCheckcode(String checkcode) {
    this.checkcode = checkcode;
  }

  public String getTokenname() {
    return tokenname;
  }

  public void setTokenname(String tokenname) {
    this.tokenname = tokenname;
  }

  public String getMd5chk() {
    return md5chk;
  }

  public void setMd5chk(String md5chk) {
    this.md5chk = md5chk;
  }

  @Override
  public String toString() {
    return "PaySearchRequest{" +
            "server='" + server + '\'' +
            ", usersign='" + usersign + '\'' +
            ", checkcode='" + checkcode + '\'' +
            ", tokenname='" + tokenname + '\'' +
            ", md5chk='" + md5chk + '\'' +
            '}';
  }
}
