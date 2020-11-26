package org.kirrilf.controller;

import org.apache.log4j.Logger;
import org.kirrilf.dto.AuthenticationUserDto;
import org.kirrilf.model.User;
import org.kirrilf.security.jwt.AuthException;
import org.kirrilf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthenticationController {

    private static final Logger logger = Logger.getLogger(AuthenticationController.class);

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<Object, Object>> login(@RequestBody AuthenticationUserDto requestDto, HttpServletRequest request, HttpServletResponse response) {
        try {
            String username = requestDto.getUsername();
            logger.debug("Get requestDto for user"+ requestDto.getUsername());
            String m = requestDto.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
            User user = userService.findByUsername(username);

            if (user == null) {
                logger.error("Not found user with username: {}"+ username);
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }

            String accessToken = userService.getAccessToken(user);
            String refreshToken = userService.getRefreshToken(user, request);


            Map<Object, Object> res = new HashMap<>();
            res.put("id", user.getId());
            res.put("access_token", accessToken);
            res.put("refresh_token", refreshToken);
            logger.info("Authentication user: "+ username);

            return  new ResponseEntity<>(res, HttpStatus.OK);
        }catch (AuthException e){
            logger.error(e.getMessage(), e);
            throw new BadCredentialsException("Not fingerprint");
        }
        catch (AuthenticationException e) {
            logger.error(e.getMessage(), e);
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}