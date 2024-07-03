package com.motorph.ems.controller;

import com.motorph.ems.dto.UserAuth;
import com.motorph.ems.dto.UserDTO;
import com.motorph.ems.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(path = "api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/authenticateUser")
    public UserAuth authenticateUser(@RequestBody UserDTO user) {
        return userService.authenticateUser(user.username(), user.password());
    }
}
