package com.motorph.pms.service;

import com.motorph.pms.dto.RoleDTO;

import java.util.List;

public interface UserRolesService {
    List<RoleDTO> getRoles();

    RoleDTO getRole(int id);
}
