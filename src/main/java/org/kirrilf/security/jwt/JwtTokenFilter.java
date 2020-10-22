package org.kirrilf.security.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Component
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtAccessTokenProvider jwtAccessTokenProvider;
    private final JwtRefreshTokenProvider jwtRefreshTokenProvider;

    public JwtTokenFilter(JwtAccessTokenProvider jwtAccessTokenProvider, JwtRefreshTokenProvider jwtRefreshTokenProvider) {
        this.jwtAccessTokenProvider = jwtAccessTokenProvider;
        this.jwtRefreshTokenProvider = jwtRefreshTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        String ref = request.getRequestURI();
        if ("/api/auth/refresh".equals(ref)) {
            String token = jwtRefreshTokenProvider.resolveToken((HttpServletRequest) req);
            String fingerprint = jwtRefreshTokenProvider.resolveFingerprint((HttpServletRequest) req);
            if (token != null && jwtRefreshTokenProvider.validateToken(token, fingerprint)) {
                Authentication auth = jwtRefreshTokenProvider.getAuthentication(token);

                if (auth != null) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } else {
            String token = jwtAccessTokenProvider.resolveToken((HttpServletRequest) req);
            if (token != null && jwtAccessTokenProvider.validateToken(token)) {
                Authentication auth = jwtAccessTokenProvider.getAuthentication(token);

                if (auth != null) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }
        filterChain.doFilter(req, res);
    }


}