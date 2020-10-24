package org.kirrilf.controller;

import org.kirrilf.model.User;
import org.kirrilf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/auth")
public class RefreshController {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    @Autowired
    public RefreshController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @GetMapping("/refresh")
    public ResponseEntity<Map<Object, Object>> refresh(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());

        String accessToken = userService.getAccessToken(user);
        String refreshToken = userService.getRefreshToken(user, request);

        Map<Object, Object> res = new HashMap<>();
        res.put("access_token", accessToken);
        res.put("refresh_token", refreshToken);

        /*Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setMaxAge(60 * 24 * 60 * 60); // expires in 7 days
        cookie.setSecure(true);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);*/

        return  new ResponseEntity<>(res, HttpStatus.OK);
    }
}
