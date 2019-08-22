package com.coezal.wallet.web.interceptor;

import com.coezal.wallet.api.enums.ResultCode;
import com.coezal.wallet.api.excetion.BizException;
import com.coezal.wallet.common.util.ThreadLocalUtil;
import com.coezal.wallet.redis.RedisCache;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yj
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    RedisCache redisCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {


        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
}
