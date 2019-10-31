package com.coezal.config;

import com.coezal.interceptor.UrlHandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Version 1.0
 * Created by lll on 2019-10-31.
 * Description
 * <pre>
 *  拦截器配置
 * </pre>
 * copyright generalray4239@gmail.com
 */

@Configuration
public class WalletConfiguration extends WebMvcConfigurationSupport {

  private static final Logger logger = LoggerFactory.getLogger("WalletConfiguration");

  @Autowired
  UrlHandlerInterceptor urlHandlerInterceptor;



  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    logger.info("=====addInterceptors===");
    registry.addInterceptor(urlHandlerInterceptor).addPathPatterns("/**");
  }
}
