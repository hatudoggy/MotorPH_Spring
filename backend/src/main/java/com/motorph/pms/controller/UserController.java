package com.motorph.pms.controller;

import com.motorph.pms.dto.UserAuth;
import com.motorph.pms.dto.UserDTO;
import com.motorph.pms.service.UserService;
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
