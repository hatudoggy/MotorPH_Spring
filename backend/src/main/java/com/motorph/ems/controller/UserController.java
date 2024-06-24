package com.motorph.ems.controller;

import com.motorph.ems.dto.UserAuth;
import com.motorph.ems.model.Attendance;
import com.motorph.ems.model.Employee;
import com.motorph.ems.model.User;
import com.motorph.ems.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

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
    public UserAuth employeeTimeIn(@RequestBody User user) {
        return userService.authenticateUser(user.getUsername(), user.getPassword());
    }
}
