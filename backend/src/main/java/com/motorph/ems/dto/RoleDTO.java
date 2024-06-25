package com.motorph.ems.dto;

import lombok.Builder;

@Builder
public record RoleDTO (
        int id,
        String roleName
){
}
