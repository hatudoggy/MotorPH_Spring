package com.motorph.pms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_credentials")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String salt;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime lastModified;

    public User(
            Long employeeId,
            int roleId,
            String username,
            String password,
            LocalDateTime createdAt,
            LocalDateTime lastModified) {
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", supervisor=" + employee.getEmployeeId() +
                ", role=" + role +
                ", username='" + username + '\'' +
                ", salt='" + salt.hashCode() + '\'' +
                ", password='" + password.hashCode() + '\'' +
                ", createdAt=" + createdAt +
                ", lastModified=" + lastModified +
                '}';
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Entity
    @Table(name = "user_role")
    public static class Role {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int userRoleId;
        private String roleName;

        @Override
        public String toString() {
            return roleName;
        }
    }
}

