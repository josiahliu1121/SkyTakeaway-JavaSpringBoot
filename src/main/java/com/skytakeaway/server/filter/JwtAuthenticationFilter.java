package com.skytakeaway.server.filter;
import com.skytakeaway.common.constant.JwtClaimsConstant;
import com.skytakeaway.common.context.BaseContext;
import com.skytakeaway.common.properties.JwtProperties;
import com.skytakeaway.common.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    JwtProperties jwtProperties;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestPath = request.getServletPath();

        // Exclude public or OAuth-specific routes
        if ("/oauth2/success".equals(requestPath) || requestPath.startsWith("/admin")) {
            log.info("Bypassing JWT validation for route: {}", requestPath);
            filterChain.doFilter(request, response);
            return;
        }


        // Get the token from the request header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.info("empty token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set HTTP 401 status
            response.getWriter().write("{\"status\":\"not_login\"}");
            return; // End request processing if the token is invalid
        }

        String token = authHeader.substring(7); // Remove "Bearer " prefix
        try {
            // Parse JWT
            Claims claims = JwtUtils.parseJwt(token, jwtProperties.getUserSecretKey());
            String userId = claims.get(JwtClaimsConstant.USER_ID).toString();

            // Store user ID in BaseContext
            BaseContext.setCurrentId(Long.valueOf(userId));

            log.info("User ID {} extracted from JWT and stored in BaseContext", userId);
        } catch (Exception e) {
            log.info("illegal token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set HTTP 401 status
            response.getWriter().write("{\"status\":\"not_login\"}");
            return; // End request processing if the token is invalid

        }

        filterChain.doFilter(request, response);
    }
}

