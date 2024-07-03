package com.motorph.pms.service;

import com.motorph.pms.dto.RoleDTO;
import com.motorph.pms.dto.UserAuth;
import com.motorph.pms.dto.UserDTO;

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

    Optional<RoleDTO> getRoleById(int roleId);

    List<RoleDTO> getAllRoles();

    UserAuth authenticateUser(String username, String password);
}

