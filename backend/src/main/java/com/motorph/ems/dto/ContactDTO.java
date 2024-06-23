package com.motorph.ems.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ContactDTO(
        //May add some email here in the future
        Long employeeId,
        List<String> contactNumbers
) {}
