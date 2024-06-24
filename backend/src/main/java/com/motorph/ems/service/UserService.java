package com.motorph.ems.service;

import com.motorph.ems.dto.UserAuth;
import com.motorph.ems.dto.UserDTO;
import com.motorph.ems.model.User.Role;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserService {

    UserDTO addNewUser(UserDTO user);

    Optional<UserDTO> getUserById(Long userId);

    List<UserDTO> getAllUsers();

    Optional<UserDTO> getUserByUsername(String username);

    Optional<UserDTO> getUserByEmployeeId(Long employeeId);

    List<UserDTO> getUsersByRoleName(String roleName);

    List<UserDTO> getUsersModifiedBetween(LocalDateTime start, LocalDateTime end);

    List<UserDTO> getUsersCreatedBetween(LocalDateTime start, LocalDateTime end);

    UserDTO updateUser(Long userId, UserDTO user);

    void deleteUser(Long userId);

    Optional<Role> getRoleById(int roleId);

    Optional<Role> getRoleByRoleName(String roleName);

    List<Role> getAllRoles();

    UserAuth authenticateUser(String username, String password);
}

