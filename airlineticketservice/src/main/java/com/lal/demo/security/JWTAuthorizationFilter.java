package com.lal.demo.security;
import com.auth0.jwt.JWT;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final String HEADER_STRING = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";
    public static final String SECRET = "mySecretKey";

    @Autowired
    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        String token = req.getHeader(HEADER_STRING);

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req, token);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, String token) {

        if (token != null) {
            // parsiranje tokena
            DecodedJWT jwt = JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build()
                    .verify(token.replace(TOKEN_PREFIX, ""));

            // subject je email od korisnika i spakovan je u JWT
            String rola = jwt.getClaims().get("roles").asString();

            // Provera da li se nalazi user u bazi


            List<SimpleGrantedAuthority> role = new ArrayList<>();


            System.out.println("role je: "+ rola);
            role.add(new SimpleGrantedAuthority(rola));
            return new UsernamePasswordAuthenticationToken(null, null, role);




        }
        return null;
    }
}
