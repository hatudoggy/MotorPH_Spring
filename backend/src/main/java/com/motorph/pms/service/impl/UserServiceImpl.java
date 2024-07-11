package com.motorph.pms.service.impl;

import com.motorph.pms.dto.UserAuth;
import com.motorph.pms.dto.UserDTO;
import com.motorph.pms.dto.mapper.UserMapper;
import com.motorph.pms.model.User;
import com.motorph.pms.repository.UserRepository;
import com.motorph.pms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CacheConfig(cacheNames = "users")
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @CachePut(key = "#result.username()")
    @Transactional
    @Override
    public UserDTO addNewUser(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.username())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmployee_EmployeeId(userDTO.employeeId())) {
            throw new IllegalArgumentException("Employee ID already exists");
        }

        User user = userMapper.toEntity(userDTO);

        User savedUser = userRepository.save(user);

        return userMapper.toDTO(savedUser);
    }

    @Cacheable(key = "#employeeId")
    @Override
    public Optional<UserDTO> getUserByEmployeeId(Long employeeId) {
        return userRepository.findByEmployee_EmployeeId(employeeId).map(userMapper::toDTO);
    }

    @Cacheable
    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @CachePut(key = "#userId")
    @Transactional
    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found"));

        userMapper.updateEntity(userDTO, user);

        User updatedUser = userRepository.save(user);

        return userMapper.toDTO(updatedUser);
    }

    @CacheEvict(key = "#userId")
    @Transactional
    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
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
