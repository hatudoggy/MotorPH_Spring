package com.motorph.pms.service;

<<<<<<< HEAD:backend/src/main/java/com/motorph/ems/service/UserService.java
import com.motorph.ems.dto.UserAuth;
import com.motorph.ems.model.User;
=======
import com.motorph.pms.dto.RoleDTO;
import com.motorph.pms.dto.UserAuth;
import com.motorph.pms.dto.UserDTO;
>>>>>>> UI-integration:backend/src/main/java/com/motorph/pms/service/UserService.java

import java.util.List;

public interface UserService {
    User addNewUser (User user);

    User getUserById (Long userId);

    List<User> getAllUsers();

    User updateUser (User user);

    void deleteUser (Long userId);

    UserAuth authenticateUser (String username, String password);
}
