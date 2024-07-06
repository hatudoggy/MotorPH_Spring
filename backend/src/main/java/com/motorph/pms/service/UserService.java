package com.motorph.pms.service;

import com.motorph.pms.dto.RoleDTO;
import com.motorph.pms.dto.UserAuth;
import com.motorph.pms.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserService {

    UserDTO addNewUser(UserDTO user);

    List<UserDTO> getAllUsers();

    Optional<UserDTO> getUserByEmployeeId(Long employeeId);

    UserDTO updateUser(Long userId, UserDTO user);

    void deleteUser(Long userId);

    UserAuth authenticateUser(String username, String password);
}

