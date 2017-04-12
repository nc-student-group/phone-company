package com.phonecompany.config.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Slf4j
public class TokenAuthenticationService {

    static final long EXPIRATIONTIME = 864_000_000; // 10 days
    static final String SECRET = "ThisIsASecret";
    static final String TOKEN_PREFIX = "Bearer";
    static final int TOKEN_PREFIX_LENGTH = 7;
    static final String AUTHORIZATION = "Authorization";

    public static void addAuthentication(HttpServletResponse res, String username) {
        String JWT = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        res.addHeader(AUTHORIZATION, TOKEN_PREFIX + " " + JWT);
    }

    public static Authentication getAuthentication(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader == null) {
            return null;
        }
        // parse the authHeader.
        String jwtToken = authHeader.substring(TOKEN_PREFIX_LENGTH);

        String userName = getUserNameFromToken(jwtToken);

        return Optional.ofNullable(userName)
                .map(TokenAuthenticationService::getEmptyUserToken)
                .orElse(null);
    }

    private static String getUserNameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token.replace(TOKEN_PREFIX, "")) //TODO: rework to replace()
                .getBody()
                .getSubject();
    }

    private static UsernamePasswordAuthenticationToken getEmptyUserToken(String userName) {
        return new UsernamePasswordAuthenticationToken(userName, null, emptyList());
    }
}
