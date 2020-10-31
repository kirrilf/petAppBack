package org.kirrilf.controller;

import org.apache.log4j.Logger;
import org.kirrilf.dto.RegistrationUserDto;
import org.kirrilf.dto.UserDto;
import org.kirrilf.model.User;
import org.kirrilf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/registration")
public class RegistrationController {

    private final UserService userService;

    private static final Logger logger = Logger.getLogger(RefreshController.class);

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Map<Object, Object>> registration(@RequestBody RegistrationUserDto userRegistrationDto) {
        User user = userRegistrationDto.toUser();

        Map<Object, Object> response = new HashMap<>();

        User registerUser = userService.register(user);

        logger.debug("Try register new user: " + userRegistrationDto.getUsername());

        if (registerUser == null) {
            response.put("message", "User with this username or email already exist");
            logger.info("User with username or email already exist, " + userRegistrationDto.getUsername());
            return new ResponseEntity<>(response, HttpStatus.I_AM_A_TEAPOT);
        } else {
            UserDto registerUserDto = UserDto.fromUser(registerUser);
            response.put("registerUser", registerUserDto);
            logger.info("User success register " + registerUserDto.getUsername());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }


    }

}
