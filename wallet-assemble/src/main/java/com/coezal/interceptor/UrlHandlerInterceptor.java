package com.coezal.interceptor;

import com.coezal.wallet.common.util.IpUtils;
import com.coezal.wallet.common.util.TimeUtils;
import org.bouncycastle.util.IPAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import sun.net.util.IPAddressUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Version 1.0
 * Created by lll on 2019-10-28.
 * Description
 * <pre>
 *
 * </pre>
 * copyright generalray4239@gmail.com
 */

@Component
public class UrlHandlerInterceptor implements HandlerInterceptor {

  private static final Logger logger = LoggerFactory.getLogger("UrlHandlerInterceptor");


  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
          throws Exception {

    String ip = IpUtils.getIpFromRequest(request);
    String requestUrl = request.getRequestURI();

    logger.info(TimeUtils.getLoggerFullTime() + "------receive the ip==" + ip +"---call the method==" + requestUrl);
    if (ip.equals("18.139.109.100")) {
      return true;
    }
    return false;
  }




}
