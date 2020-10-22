package org.kirrilf.security.jwt.refresh;

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
public class JwtRefreshTokenFilter extends GenericFilterBean {

    private final JwtRefreshTokenProvider jwtRefreshTokenProvider;


    public JwtRefreshTokenFilter(JwtRefreshTokenProvider jwtRefreshTokenProvider) {
        this.jwtRefreshTokenProvider = jwtRefreshTokenProvider;
    }


    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {

        String token = jwtRefreshTokenProvider.resolveToken((HttpServletRequest) req);
        if (token != null && jwtRefreshTokenProvider.validateToken(token)) {
            Authentication auth = jwtRefreshTokenProvider.getAuthentication(token);

            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(req, res);
    }

}
