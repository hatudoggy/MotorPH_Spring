package com.motorph.pms.repository;

import com.motorph.pms.model.User;
import com.motorph.pms.model.Employee;
import com.motorph.pms.model.User.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRoleRepository roleRepository;

    private Employee employee1, employee2;
    private Role role1, role2;
    private User user1, user2;

    @BeforeEach
    void setUp() {
        employee1 = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .dob(LocalDateTime.now().toLocalDate())
                .address("123 Main St")
                .build();

        employee2 = Employee.builder()
                .firstName("Anna")
                .lastName("Doe")
                .dob(LocalDateTime.now().toLocalDate())
                .address("123 Main St")
                .build();

        role1 = Role.builder().roleName("ROLE_USER").build();
        role2 = Role.builder().roleName("ROLE_ADMIN").build();

        roleRepository.save(role1);
        roleRepository.save(role2);

        user1 = User.builder()
                .employee(employee1)
                .role(role1)
                .username("johndoe")
                .password("password123")
                .salt("salt123")
                .createdAt(LocalDateTime.now())
                .lastModified(LocalDateTime.now())
                .build();

        user2 = User.builder()
                .employee(employee2)
                .role(role2)
                .username("annadoe")
                .password("password123")
                .salt("salt123")
                .createdAt(LocalDateTime.now())
                .lastModified(LocalDateTime.now())
                .build();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
    @Transactional
    @DirtiesContext
    void UserRepository_saveUser_returnsSavedUser() {
        User savedUser1 = userRepository.save(user1);
        User savedUser2 = userRepository.save(user2);

        assertNotNull(savedUser1);
        assertThat(savedUser1.getUsername()).isEqualTo("johndoe");
        assertNotNull(savedUser2);
        assertThat(savedUser2.getUsername()).isEqualTo("annadoe");
    }

    @Test
    @Transactional
    @DirtiesContext
    void UserRepository_findByUsername_Found() {
        Optional<User> foundUser = userRepository.findByUsername("johndoe");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("johndoe");
    }

    @Test
    @Transactional
    @DirtiesContext
    void UserRepository_findByEmployee_EmployeeId_Found() {
        Optional<User> foundUser = userRepository.findByEmployee_EmployeeId(employee1.getEmployeeId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmployee().getEmployeeId()).isEqualTo(employee1.getEmployeeId());
    }

    @Test
    @Transactional
    @DirtiesContext
    void UserRepository_findAllByRole_RoleName_Found() {
        List<User> usersWithRoleUser = userRepository.findAllByRole_RoleName("ROLE_USER");
        assertThat(usersWithRoleUser).hasSize(1);
        assertThat(usersWithRoleUser.get(0).getRole().getRoleName()).isEqualTo("ROLE_USER");

        List<User> usersWithRoleAdmin = userRepository.findAllByRole_RoleName("ROLE_ADMIN");
        assertThat(usersWithRoleAdmin).hasSize(1);
        assertThat(usersWithRoleAdmin.get(0).getRole().getRoleName()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    @Transactional
    @DirtiesContext
    void UserRepository_findAllByLastModifiedBetween_Found() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        List<User> users = userRepository.findAllByLastModifiedBetween(start, end);
        assertThat(users).hasSize(2);
    }

    @Test
    @Transactional
    @DirtiesContext
    void UserRepository_findAllByCreatedAtBetween_Found() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        List<User> users = userRepository.findAllByCreatedAtBetween(start, end);
        assertThat(users).hasSize(2);
    }

    @Test
    @Transactional
    @DirtiesContext
    void UserRepository_deleteUser() {
        userRepository.delete(user1);
        Optional<User> deletedUser = userRepository.findByUsername("johndoe");
        assertThat(deletedUser).isNotPresent();
    }
}
