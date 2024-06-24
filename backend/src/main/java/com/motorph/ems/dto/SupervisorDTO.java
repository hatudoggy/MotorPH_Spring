package com.motorph.ems.dto;

import lombok.Builder;

@Builder
public record SupervisorDTO (
    Long supervisorId,
    String lastName,
    String firstName,
    String address,
    String positionCode,
    ContactDTO contacts
){}