package org.kirrilf.controller;

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
    private UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity registration(@RequestBody RegistrationUserDto userRegistrationDto){
        User user = userRegistrationDto.toUser();

        Map<Object, Object> response = new HashMap<>();

        User registerUser = userService.register(user);

        if(registerUser == null){

            return new ResponseEntity<>("User with this username or email already exist", HttpStatus.I_AM_A_TEAPOT);
        }
        else {
            UserDto registerUserDto = UserDto.fromUser(registerUser);

            response.put("register User", registerUserDto);

            return ResponseEntity.ok(response);
        }


    }

}
