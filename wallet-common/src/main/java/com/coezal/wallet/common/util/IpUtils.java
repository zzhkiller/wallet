package com.coezal.wallet.common.util;

import org.apache.commons.lang.text.StrTokenizer;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * Version 1.0
 * Created by lll on 2019-10-31.
 * Description
 * <pre>
 *   获取请求的原始IP
 * </pre>
 * copyright generalray4239@gmail.com
 */
public class IpUtils {


  public static final String _255 = "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
  public static final Pattern pattern = Pattern.compile("^(?:" + _255 + "\\.){3}" + _255 + "$");

  public static String longToIpV4(long longIp) {
    int octet3 = (int) ((longIp >> 24) % 256);
    int octet2 = (int) ((longIp >> 16) % 256);
    int octet1 = (int) ((longIp >> 8) % 256);
    int octet0 = (int) ((longIp) % 256);
    return octet3 + "." + octet2 + "." + octet1 + "." + octet0;
  }

  public static long ipV4ToLong(String ip) {
    String[] octets = ip.split("\\.");
    return (Long.parseLong(octets[0]) << 24) + (Integer.parseInt(octets[1]) << 16)
            + (Integer.parseInt(octets[2]) << 8) + Integer.parseInt(octets[3]);
  }

  public static boolean isIPv4Private(String ip) {
    long longIp = ipV4ToLong(ip);
    return (longIp >= ipV4ToLong("10.0.0.0") && longIp <= ipV4ToLong("10.255.255.255"))
            || (longIp >= ipV4ToLong("172.16.0.0") && longIp <= ipV4ToLong("172.31.255.255"))
            || longIp >= ipV4ToLong("192.168.0.0") && longIp <= ipV4ToLong("192.168.255.255");
  }

  public static boolean isIPv4Valid(String ip) {
    return pattern.matcher(ip).matches();
  }

  /**
   * 获取请求的IP地址
   * @param request
   * @return
   */
  public static String getIpFromRequest(HttpServletRequest request) {
    String ip;
    boolean found = false;
    if ((ip = request.getHeader("x-forwarded-for")) != null) {
      StrTokenizer tokenizer = new StrTokenizer(ip, ",");
      while (tokenizer.hasNext()) {
        ip = tokenizer.nextToken().trim();
        if (isIPv4Valid(ip) && !isIPv4Private(ip)) {
          found = true;
          break;
        }
      }
    }
    if (!found) {
      if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("Proxy-Client-IP");
      }
      if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("WL-Proxy-Client-IP");
      }
      if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getRemoteAddr();
      }
    }
    return ip;
  }
}
