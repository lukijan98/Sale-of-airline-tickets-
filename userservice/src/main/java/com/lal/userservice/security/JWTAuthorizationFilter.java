package com.lal.userservice.security;
import static com.lal.userservice.security.SecurityConstants.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lal.userservice.model.Admin;
import com.lal.userservice.repository.AdminRepository;
import com.lal.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
public class JWTAuthorizationFilter extends BasicAuthenticationFilter{

    private UserRepository userRepo;
    private AdminRepository adminRepository;

    @Autowired
    public JWTAuthorizationFilter(AuthenticationManager authManager, UserRepository userRepo, AdminRepository adminRepository) {
        super(authManager);
        this.userRepo = userRepo;
        this.adminRepository = adminRepository;
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
            String email = jwt.getSubject();

            // Provera da li se nalazi user u bazi

            if (email != null) {
                List<SimpleGrantedAuthority> role = new ArrayList<>();
                if (userRepo.existsByEmail(email))
                {
                    role.add(new SimpleGrantedAuthority("ROLE_USER"));
                    return new UsernamePasswordAuthenticationToken(email, null, role);
                }
                if(adminRepository.existsByUsername(email))
                {
                    System.out.println("usao ovde");
                    role.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                    return new UsernamePasswordAuthenticationToken(email, null, role);
                }

            }
            return null;
        }
        return null;
    }

}
