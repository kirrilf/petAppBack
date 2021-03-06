package org.kirrilf.controller;

import org.apache.log4j.Logger;
import org.kirrilf.model.User;
import org.kirrilf.security.jwt.AuthException;
import org.kirrilf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/auth")
public class RefreshController {

    private final UserService userService;

    private static final Logger logger = Logger.getLogger(RefreshController.class);

    @Autowired
    public RefreshController(AuthenticationManager authenticationManager, UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/refresh")
    public ResponseEntity<Map<Object, Object>> refresh(HttpServletRequest request, HttpServletResponse response){
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findByUsername(auth.getName());

            String accessToken = userService.getAccessToken(user);
            String refreshToken = userService.getRefreshToken(user, request);

            Map<Object, Object> res = new HashMap<>();
            res.put("access_token", accessToken);
            res.put("refresh_token", refreshToken);

            logger.info("Get new refresh token for user: " + user.getUsername());

            return new ResponseEntity<>(res, HttpStatus.OK);

        }catch (AuthException e){
            logger.error(e.getMessage(), e);
            throw new BadCredentialsException(e.getMessage());
        }


    }
}
