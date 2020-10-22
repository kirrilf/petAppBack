package org.kirrilf.security.jwt;

import io.jsonwebtoken.*;
import org.kirrilf.model.RefreshToken;
import org.kirrilf.model.Role;
import org.kirrilf.model.Status;
import org.kirrilf.repository.RefreshTokenRepository;
import org.kirrilf.security.jwt.JwtAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtRefreshTokenProvider {

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.refresh.token.expired.sec}")
    private long validityInSeconds;


    private final UserDetailsService userDetailsService;

    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public JwtRefreshTokenProvider(@Qualifier("jwtUserDetailsService") UserDetailsService userDetailsService, RefreshTokenRepository refreshTokenRepository) {
        this.userDetailsService = userDetailsService;
        this.refreshTokenRepository = refreshTokenRepository;
    }


    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(String username, String fingerprint) {


        Claims claims = Jwts.claims().setSubject(username);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInSeconds);

        String token = Jwts.builder()//
                        .setClaims(claims)//
                        .setIssuedAt(now)//
                        .setExpiration(validity)//
                        .signWith(SignatureAlgorithm.HS256, secret)//
                        .compact();

        RefreshToken refreshToken = refreshTokenRepository.findByFingerprint(fingerprint);
        if(refreshToken != null){
            refreshToken.setToken(token);
            refreshToken.setUpdated(new Date());
            refreshTokenRepository.save(refreshToken);
        }else {
            RefreshToken newRefreshToken = new RefreshToken();
            newRefreshToken.setToken(token);
            newRefreshToken.setFingerprint(fingerprint);
            newRefreshToken.setStatus(Status.ACTIVE);
            newRefreshToken.setUpdated(new Date());
            newRefreshToken.setCreated(new Date());
            refreshTokenRepository.save(newRefreshToken);
        }

        return token;
    }



    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
            if (bearerToken != null && bearerToken.startsWith("Bearer_")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    public String resolveFingerprint(HttpServletRequest req) {
        String fingerprint = req.getHeader("Fingerprint");
        if (fingerprint != null) {
            return fingerprint;
        }
        return null;
    }

    public boolean validateToken(String token, String fingerprint) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
             RefreshToken tokenBD = refreshTokenRepository.findByFingerprint(fingerprint);
            if(tokenBD.getToken().equals(token) && tokenBD.getStatus() == Status.ACTIVE) {
                return !claims.getBody().getExpiration().before(new Date());
            }else {
                throw new JwtAuthenticationException("Bad fingerprint");
            }
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("JWT token is expired or invalid");
        }
    }

}