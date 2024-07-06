package com.motorph.pms.repository;

import com.motorph.pms.model.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmployee_EmployeeId(Long employeeId);

    boolean existsByUsername(String username);

    boolean existsByEmployee_EmployeeId(Long aLong);

    User findUserByUsernameAndPassword(String username, String password);
}
