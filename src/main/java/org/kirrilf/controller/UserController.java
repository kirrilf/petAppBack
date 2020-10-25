package org.kirrilf.controller;

import org.apache.log4j.Logger;
import org.kirrilf.dto.UserDto;
import org.kirrilf.model.User;
import org.kirrilf.security.jwt.JwtAccessTokenProvider;
import org.kirrilf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    private final UserService userService;

    private static final Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService, JwtAccessTokenProvider jwtAccessTokenProvider) {
        this.userService = userService;
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "id") Long id, HttpServletRequest request) {
        User user = userService.findById(id);

        if (user == null) {
            logger.debug("User with id " + id + " not found ");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        logger.debug("Get user by id " + id);
        UserDto result = UserDto.fromUser(user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}