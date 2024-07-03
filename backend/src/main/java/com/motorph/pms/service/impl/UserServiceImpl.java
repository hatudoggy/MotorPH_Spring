package com.motorph.pms.service.impl;

<<<<<<< HEAD:backend/src/main/java/com/motorph/ems/service/impl/UserServiceImpl.java
import com.motorph.ems.dto.UserAuth;
import com.motorph.ems.model.User;
import com.motorph.ems.repository.UserRepository;
import com.motorph.ems.service.UserService;
=======
import com.motorph.pms.dto.RoleDTO;
import com.motorph.pms.dto.UserAuth;
import com.motorph.pms.dto.UserDTO;
import com.motorph.pms.dto.mapper.UserMapper;
import com.motorph.pms.model.User;
import com.motorph.pms.repository.UserRepository;
import com.motorph.pms.repository.UserRoleRepository;
import com.motorph.pms.service.UserService;
>>>>>>> UI-integration:backend/src/main/java/com/motorph/pms/service/impl/UserServiceImpl.java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//TODO: Add implementation for UserService
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User addNewUser(User user) {
        return null;
    }

    @Override
    public User getUserById(Long userId) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return List.of();
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public void deleteUser(Long userId) {

    }

    @Override
    public UserAuth authenticateUser(String username, String password) {
        User user = userRepository.findUserByUsernameAndPassword(username, password);
        UserAuth userAuth = new UserAuth();
        userAuth.setEmployeeId(user.getEmployeeId());
        userAuth.setRoleId((long) user.getRole().getUserRoleId());
        return userAuth;
    }
}
