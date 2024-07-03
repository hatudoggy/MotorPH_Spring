package com.motorph.pms.dto.mapper;

import com.motorph.pms.dto.RoleDTO;
import com.motorph.pms.dto.UserDTO;
import com.motorph.pms.model.User;
import com.motorph.pms.model.User.Role;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserMapper {

    public UserDTO toDTO(User entity) {
        if (entity == null) {
            return null;
        }

        String fullName = entity.getEmployee().getLastName() + ", " + entity.getEmployee().getFirstName();
        return UserDTO.builder()
                .userId(entity.getUserId())
                .employeeId(Objects.requireNonNullElse(entity.getEmployee(), null).getEmployeeId())
                .employeeName(fullName)
                .positionCode(Objects.requireNonNullElse(entity.getEmployee(), null).getPosition().getPositionCode())
                .departmentCode(Objects.requireNonNullElse(entity.getEmployee(), null).getDepartment().getDepartmentCode())
                .roleId(entity.getRole().getUserRoleId())
                .username(entity.getUsername())
                .createdAt(entity.getCreatedAt())
                .lastModified(entity.getLastModified())
                .build();
    }

    public User toEntity(UserDTO dto) {
        if (dto == null || dto.employeeId() == null) {
            throw new IllegalArgumentException("Employee ID cannot be null when creating user");
        }

        return new User(
                dto.employeeId(),
                dto.roleId(),
                dto.username(),
                dto.password(),
                dto.createdAt(),
                dto.lastModified()
        );
    }

    public void updateEntity(UserDTO dto, User entity) {
        if (dto.username() != null) {
            entity.setUsername(dto.username());
        }
        if (dto.password() != null) {
            entity.setPassword(dto.password());
        }
    }

    public RoleDTO toDTO(Role entity) {
        if (entity == null) {
            return null;
        }
        return RoleDTO.builder()
                .id(entity.getUserRoleId())
                .roleName(entity.getRoleName())
                .build();
    }
}
