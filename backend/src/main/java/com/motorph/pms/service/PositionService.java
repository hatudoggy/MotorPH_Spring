package com.motorph.pms.service;

import com.motorph.pms.dto.PositionDTO;

import java.util.List;
import java.util.Optional;

public interface PositionService {

    PositionDTO addPosition(PositionDTO positionDTO);

    List<PositionDTO> getPositionsByDepartment(String departmentCode);

    List<PositionDTO> getPositions();

    Optional<PositionDTO> getPositionByPositionCode(String positionCode);

//    Optional<PositionDTO> getPositionByName(String positionName);
}
