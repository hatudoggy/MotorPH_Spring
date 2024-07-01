package com.motorph.ems.dto.mapper;

import com.motorph.ems.dto.PositionDTO;
import com.motorph.ems.model.Department;
import com.motorph.ems.model.Position;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PositionMapper {

    public PositionDTO toDTO(Position position) {
        if (position == null) {
            return null;
        }

        return PositionDTO.builder().
                positionCode(position.getPositionCode()).
                departmentCode(position.getDepartment().getDepartmentCode()).
                positionName(position.getPositionName()).
                isLeader(position.isLeader()).
                build();
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
