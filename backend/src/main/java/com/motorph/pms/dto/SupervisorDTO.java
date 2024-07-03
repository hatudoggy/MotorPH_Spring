package com.motorph.ems.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record SupervisorDTO (
    Long supervisorId,
    String lastName,
    String firstName,
    String address,
    PositionDTO position,
    List<ContactDTO> contacts
){}
