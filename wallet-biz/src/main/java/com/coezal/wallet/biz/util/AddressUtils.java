package com.coezal.wallet.biz.util;


import org.apache.http.util.TextUtils;

import java.util.regex.Pattern;

/**
 * Version 1.0
 * Created by lll on 2019-08-26.
 * Description 地址工具类
 * <pre>
 *
 *
 * </pre>
 * copyright generalray4239@gmail.com
 */
public class AddressUtils {

  private static final Pattern ignoreCaseAddrPattern = Pattern.compile("(?i)^(0x)?[0-9a-f]{40}$");
  private static final Pattern lowerCaseAddrPattern = Pattern.compile("^(0x)?[0-9a-f]{40}$");
  private static final Pattern upperCaseAddrPattern = Pattern.compile("^(0x)?[0-9A-F]{40}$");

  /**
   * 是否是地址
   * @param address
   * @return
   */
  public static boolean isAddress(String address) {
    return !(TextUtils.isEmpty(address) || !ignoreCaseAddrPattern.matcher(address).find())
            && (lowerCaseAddrPattern.matcher(address).find() || upperCaseAddrPattern.matcher(address).find());
  }
}
