package com.motorph.ems.repository;

import com.motorph.ems.model.User.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByRoleName(String roleName);
}
