package com.skytakeaway.server.handler;

import com.skytakeaway.common.constant.JwtClaimsConstant;
import com.skytakeaway.common.properties.JwtProperties;
import com.skytakeaway.common.utils.JwtUtils;
import com.skytakeaway.pojo.entity.User;
import com.skytakeaway.server.mapper.UserMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class OAuth2LoginSuccessHandler {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtProperties jwtProperties;
    @Value("${sky.user-frontend.redirect-url}")
    private String frontendRedirectUrl;


    @GetMapping("/oauth2/success")
    public void successHandler(Authentication authentication, HttpServletResponse response) throws IOException
    {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String avatar = oAuth2User.getAttribute("picture");
        // Get user from database or create new user
        User user = userMapper.getByEmail(email);
        if (user == null) {
            user = User.builder()
                    .userName(name)
                    .emailAddress(email)
                    .createTime(LocalDateTime.now())
                    .avatar(avatar)
                    .build();
            userMapper.insertUser(user);
        }

        // Generate JWT
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String jwt = JwtUtils.generateJwt(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims
        );

        // Redirect to the front-end with the token as a query parameter
        response.sendRedirect(frontendRedirectUrl + "/menu?token=" + jwt);
    }
}
