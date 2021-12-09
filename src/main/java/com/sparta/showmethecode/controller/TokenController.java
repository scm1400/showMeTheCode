package com.sparta.showmethecode.controller;

import com.sparta.showmethecode.security.JwtUtils;
import com.sparta.showmethecode.service.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class TokenController {
    private final JwtUtils jwtUtils;

    @GetMapping("/token/expired")
    public String auth() {
        throw new RuntimeException();
    }

    @GetMapping("/token/refresh")
    public String refreshAuth(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("Refresh");

        if (token != null && jwtUtils.verifyToken(token)) {
            String email = jwtUtils.getUid(token);
            Token newToken = jwtUtils.createToken(email);

            response.addHeader("Authorization", newToken.getToken());
            response.addHeader("Refresh", newToken.getRefreshToken());
            response.setContentType("application/json;charset=UTF-8");

            return "HAPPY NEW TOKEN";
        }

        throw new RuntimeException();
    }

}
