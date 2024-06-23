package com.motorph.ems.service;

import com.motorph.ems.dto.PositionDTO;

import java.util.List;
import java.util.Optional;

public interface PositionService {

    PositionDTO addPosition(PositionDTO positionDTO);

    List<PositionDTO> getPositionsByDepartment(String departmentCode);

    List<PositionDTO> getPositions();

    Optional<PositionDTO> getPosition(String positionCode);

    Optional<PositionDTO> getPositionByName(String positionName);
}
