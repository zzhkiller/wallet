package com.coezal.wallet.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Version 1.0
 * Created by lll on 2019-08-27.
 * Description
 * <pre>
 *   定义拦截器，做请求头和参数校验
 *
 * </pre>
 * copyright generalray4239@gmail.com
 */
public class WalletInterceptor implements HandlerInterceptor {

  /**
   * source header
   */
  public static final String SOURCE_MD5 = "075C85373DC7020C6BA7ABEA6614163F";


  private static final Logger logger = LoggerFactory.getLogger("WalletInterceptor");

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

//    String source = request.getHeader("source");
//    if (source != null && source.equalsIgnoreCase(SOURCE_MD5)) {
//      return true;
//    } else {
//
//    }
    return false;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

  }
}


