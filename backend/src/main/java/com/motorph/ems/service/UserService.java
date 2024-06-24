package com.motorph.ems.service;

import com.motorph.ems.dto.UserAuth;
import com.motorph.ems.model.User;

import java.util.List;

public interface UserService {
    User addNewUser (User user);

    User getUserById (Long userId);

    List<User> getAllUsers();

    User updateUser (User user);

    void deleteUser (Long userId);

    UserAuth authenticateUser (String username, String password);
}
