package com.motorph.pms.dto;

import lombok.Builder;

@Builder
public record RoleDTO (
        int id,
        String roleName
){
}
