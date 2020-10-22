package org.kirrilf.config;


import org.kirrilf.security.jwt.refresh.JwtRefreshTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter{

    private final JwtRefreshTokenFilter jwtRefreshTokenFilter;

    @Autowired
    public SecurityTokenConfig(JwtRefreshTokenFilter jwtRefreshTokenFilter) {
        this.jwtRefreshTokenFilter = jwtRefreshTokenFilter;
    }

    CorsFilter corsFilter() {
        CorsFilter filter = new CorsFilter();
        return filter;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .addFilterBefore(corsFilter(), SessionManagementFilter.class)
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .antMatcher("/refresh")
                .addFilterBefore(jwtRefreshTokenFilter, UsernamePasswordAuthenticationFilter.class);

    }




}
