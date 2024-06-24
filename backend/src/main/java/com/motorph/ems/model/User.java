package com.motorph.ems.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@Entity @Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private Long employeeId;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private String username;
    private String salt;
    private String password;
    private LocalDate createdAt;
    private LocalDate lastModified;

    public User() {}

    public User(
            Long employeeId,
            Role role,
            String username,
            String password) {
        this.employeeId = employeeId;
        this.role = role;
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", employeeId=" + employeeId +
                ", role=" + role +
                ", username='" + username + '\'' +
                ", salt='" + salt.hashCode() + '\'' +
                ", password='" + password.hashCode() + '\'' +
                ", createdAt=" + createdAt +
                ", lastModified=" + lastModified +
                '}';
    }

    @Getter @Setter
    @Entity @Table(name = "user_role")
    public static class Role {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int userRoleId;
        private String role;

        @Override
        public String toString() {
            return role;
        }
    }
}
