package com.motorph.pms.dto.mapper;

import com.motorph.pms.dto.EmploymentStatusDTO;
import com.motorph.pms.model.EmploymentStatus;
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
