package com.motorph.ems.service.impl;

import com.motorph.pms.dto.RoleDTO;
import com.motorph.pms.dto.UserAuth;
import com.motorph.pms.dto.UserDTO;
import com.motorph.pms.dto.mapper.UserMapper;
import com.motorph.pms.model.User;
import com.motorph.pms.model.User.Role;
import com.motorph.pms.repository.UserRepository;
import com.motorph.pms.repository.UserRoleRepository;
import com.motorph.pms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO addNewUser(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.username())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmployee_EmployeeId(userDTO.employeeId())) {
            throw new IllegalArgumentException("Employee ID already exists");
        }

        User user = userMapper.toEntity(userDTO);
        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    public Optional<UserDTO> getUserById(Long userId) {
        return userRepository.findById(userId).map(userMapper::toDTO);
    }

    @Override
    public Optional<UserDTO> getUserByUsername(String username) {
        return userRepository.findByUsername(username).map(userMapper::toDTO);
    }

    @Override
    public Optional<UserDTO> getUserByEmployeeId(Long employeeId) {
        return userRepository.findByEmployee_EmployeeId(employeeId).map(userMapper::toDTO);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getUsersByRoleName(String roleName) {
        return userRepository.findAllByRole_RoleName(roleName).stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getUsersModifiedBetween(LocalDateTime start, LocalDateTime end) {
        return userRepository.findAllByLastModifiedBetween(start, end).stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getUsersCreatedBetween(LocalDateTime start, LocalDateTime end) {
        return userRepository.findAllByCreatedAtBetween(start, end).stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found"));

        userMapper.updateEntity(userDTO,user);

        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override()
    public Optional<RoleDTO> getRoleById(int roleId) {
        return userRoleRepository.findById(roleId).map(userMapper::toDTO);
    }

//    @Override
//    public Optional<RoleDTO> getRoleByRoleName(String roleName) {
//        return userRoleRepository.findByRoleName(roleName).map(userMapper::toDTO);
//    }

    @Override
    public List<RoleDTO> getAllRoles() {
        return userRoleRepository.findAll().stream().map(userMapper::toDTO).toList();
    }

    @Override
    public UserAuth authenticateUser(String username, String password) {
        User user = userRepository.findUserByUsernameAndPassword(username, password);

        return new UserAuth(
                user.getEmployee().getEmployeeId(),
                (long) user.getRole().getUserRoleId()
        );
    }
}
