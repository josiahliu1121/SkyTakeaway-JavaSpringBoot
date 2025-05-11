package com.skytakeaway.server.interceptor;

import com.skytakeaway.common.constant.JwtClaimsConstant;
import com.skytakeaway.common.context.BaseContext;
import com.skytakeaway.common.properties.JwtProperties;
import com.skytakeaway.common.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

//No usage
@Component
@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {
    @Autowired
    JwtProperties jwtProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // Check if the handler is not an instance of HandlerMethod
        if(!(handler instanceof HandlerMethod)){
            // If the handler is not a controller method (e.g., a static resource request), allow it to proceed
            return true;
        }

        String token = request.getHeader("authentication");

        if(!StringUtils.hasLength(token)){
            log.info("token is empty");
            response.getWriter().write("{\"status\":\"not_login\"}");
            return false;
        }

        try {
            Claims claims = JwtUtils.parseJwt(token, jwtProperties.getUserSecretKey());
            //get the user id of current user from jwt token
            String currentUser = claims.get(JwtClaimsConstant.USER_ID).toString();
            BaseContext.setCurrentId(Long.valueOf(currentUser));
        }catch (Exception e){
            e.printStackTrace();
            log.info("illegal token");
            response.getWriter().write("{\"status\":\"not_login\"}");
            return false;
        }

        log.info("token passed");
       return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
