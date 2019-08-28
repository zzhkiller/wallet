package com.coezal.wallet.common.util;

/**
 * Version 1.0
 * Created by lll on 2019-08-28.
 * Description
 * copyright generalray4239@gmail.com
 */
public class EmailUtils {

  private static final String EMAIL_REGEX = "^([a-z0-9A-Z]+[-|\\\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\\\.)+[a-zA-Z]{2,}$";

  /**
   * 校验邮箱格式是否正确
   * @param emailStr
   * @return
   */
  public static boolean validate(String emailStr) {
    Boolean flag = null;
    if(emailStr==null || emailStr.length()==0){
      flag= false;
    }else {
      flag= emailStr.matches(EMAIL_REGEX);
    }
    return flag;
  }
}
