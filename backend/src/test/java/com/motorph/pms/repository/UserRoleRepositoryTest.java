package com.motorph.ems.repository;

import com.motorph.ems.model.User.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRoleRepositoryTest {

    @Autowired
    private UserRoleRepository userRoleRepository;

    private Role role1, role2;

    @BeforeEach
    void setUp() {
        role1 = Role.builder().roleName("ROLE_USER").build();
        role2 = Role.builder().roleName("ROLE_ADMIN").build();

        userRoleRepository.save(role1);
        userRoleRepository.save(role2);
    }

    @Test
    @Transactional
    @DirtiesContext
    void UserRoleRepository_saveRole_returnsSavedRole() {
        Role savedRole1 = userRoleRepository.save(role1);
        Role savedRole2 = userRoleRepository.save(role2);

        assertNotNull(savedRole1);
        assertThat(savedRole1.getRoleName()).isEqualTo("ROLE_USER");
        assertNotNull(savedRole2);
        assertThat(savedRole2.getRoleName()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    @Transactional
    @DirtiesContext
    void UserRoleRepository_findByRoleName_Found() {
        Optional<Role> foundRoleUser = userRoleRepository.findByRoleName("ROLE_USER");
        assertThat(foundRoleUser).isPresent();
        assertThat(foundRoleUser.get().getRoleName()).isEqualTo("ROLE_USER");

        Optional<Role> foundRoleAdmin = userRoleRepository.findByRoleName("ROLE_ADMIN");
        assertThat(foundRoleAdmin).isPresent();
        assertThat(foundRoleAdmin.get().getRoleName()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    @Transactional
    @DirtiesContext
    void UserRoleRepository_findByRoleName_NotFound() {
        Optional<Role> foundRole = userRoleRepository.findByRoleName("ROLE_NON_EXISTENT");
        assertThat(foundRole).isNotPresent();
    }

    @Test
    @Transactional
    @DirtiesContext
    void UserRoleRepository_deleteRole() {
        userRoleRepository.delete(role1);
        Optional<Role> deletedRole = userRoleRepository.findByRoleName("ROLE_USER");
        assertThat(deletedRole).isNotPresent();
    }
}
