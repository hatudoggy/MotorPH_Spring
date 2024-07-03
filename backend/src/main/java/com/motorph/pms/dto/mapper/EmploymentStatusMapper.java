package com.motorph.ems.dto.mapper;

import com.motorph.ems.dto.EmploymentStatusDTO;
import com.motorph.ems.model.EmploymentStatus;
import org.springframework.stereotype.Component;

@Component
public class EmploymentStatusMapper {

    public EmploymentStatusDTO toDTO(EmploymentStatus entity) {
        if (entity == null) {
            return null;
        }

        return EmploymentStatusDTO.builder()
                .statusId(entity.getStatusId())
                .statusName(entity.getStatusName())
                .build();
    }

    public EmploymentStatus toEntity(EmploymentStatusDTO dto) {
        return EmploymentStatus.builder()
                .statusId(dto.statusId())
                .statusName(dto.statusName())
                .build();
    }
}
