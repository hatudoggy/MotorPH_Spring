package com.motorph.pms.repository;

import com.motorph.pms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmployee_EmployeeId(Long employeeId);

    List<User> findAllByRole_RoleName(String roleName);

    List<User> findAllByLastModifiedBetween(LocalDateTime start, LocalDateTime end);

    List<User> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    boolean existsByUsername(String username);

    boolean existsByEmployee_EmployeeId(Long aLong);

    User findUserByUsernameAndPassword(String username, String password);
}
