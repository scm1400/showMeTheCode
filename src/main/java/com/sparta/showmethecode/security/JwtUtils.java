package com.sparta.showmethecode.security;

import com.sparta.showmethecode.service.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtUtils {

    private final UserDetailsServiceImpl userDetailsService;
//    private final SignatureAlgorithm SIGNATURE = SignatureAlgorithm.HS256;
    private String JWT_SECRET = "secret";

    @PostConstruct
    protected void init() {
        JWT_SECRET = Base64.getEncoder().encodeToString(JWT_SECRET.getBytes());
    }

    public Token createToken(String uid) {

        long tokenPeriod = 1000L * 60L * 10L;
        long refreshPeriod = 1000L * 60L * 60L * 24L * 30L * 3L;
        Date now = new Date();

        Claims claims = Jwts.claims().setSubject(uid);

        System.out.println("ooooooooooooooooooooooooo");

        return new Token(
                Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + tokenPeriod))
                        .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                        .compact(),
                Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + refreshPeriod))
                        .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                        .compact());
    }

    public boolean verifyToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .parseClaimsJws(token);
            return claims.getBody()
                    .getExpiration()
                    .after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String getUid(String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody().getSubject();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = getAllClaims(token);

        return Long.valueOf(String.valueOf(claims.get("userId")));
    }

    public boolean isValidToken(String token){
        Claims claims = getAllClaims(token);

        Date expiration = claims.getExpiration();
        String username = String.valueOf(claims.get("username"));

        return expiration.after(new Date()) && userDetailsService.loadUserByUsername(username) != null;
    }

    public TokenDto getUserInfo(String token) {
        Claims claims = getAllClaims(token);
        return new TokenDto(claims.get("username").toString());
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
    }



}
