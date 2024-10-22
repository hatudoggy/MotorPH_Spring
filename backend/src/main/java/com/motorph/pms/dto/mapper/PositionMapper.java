package com.motorph.pms.dto.mapper;

import com.motorph.pms.dto.PositionDTO;
import com.motorph.pms.model.Department;
import com.motorph.pms.model.Position;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PositionMapper {

    public PositionDTO toDTO(Position position) {
        if (position == null) {
            return null;
        }

        Department department = position.getDepartment();
        String departmentCode = (department != null) ? department.getDepartmentCode() : null;

        return PositionDTO.builder()
                .positionCode(position.getPositionCode())
                .departmentCode(departmentCode)
                .positionName(position.getPositionName())
                .isLeader(position.isLeader())
                .build();
    }

    public Position toEntity(PositionDTO positionDTO) {
        if (positionDTO == null) {
            return null;
        }

        return new Position(
                positionDTO.positionCode(),
                positionDTO.positionName(),
                positionDTO.isLeader(),
                new Department(positionDTO.departmentCode()));
    }

    public List<PositionDTO> toDTO(List<Position> positions) {
        if (positions == null) {
            return null;
        }

        return positions.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
