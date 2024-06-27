package com.motorph.ems.service.impl;

import com.motorph.ems.dto.PositionDTO;
import com.motorph.ems.dto.mapper.PositionMapper;
import com.motorph.ems.repository.PositionRepository;
import com.motorph.ems.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;

    @Autowired
    public PositionServiceImpl(PositionRepository positionRepository, PositionMapper positionMapper) {
        this.positionRepository = positionRepository;
        this.positionMapper = positionMapper;
    }

    @Override
    public PositionDTO addPosition(PositionDTO positionDTO) {
        if (positionRepository.existsById(positionDTO.positionCode())) {
            throw new IllegalStateException("Position with code " + positionDTO.positionCode() + " already exists");
        }
        if (positionRepository.findByPositionName(positionDTO.positionName()).isPresent()) {
            throw new IllegalStateException("Position with name " + positionDTO.positionName() + " already exists");
        }

        return positionMapper.toDTO(positionRepository.save(positionMapper.toEntity(positionDTO)));
    }

    @Override
    public List<PositionDTO> getPositionsByDepartment(String departmentCode) {
        return positionRepository.findAllByDepartment_DepartmentCode(departmentCode).stream()
                .map(positionMapper::toDTO).toList();
    }

    @Override
    public List<PositionDTO> getPositions() {
        return positionRepository.findAll().stream().map(positionMapper::toDTO).toList();
    }

    @Override
    public Optional<PositionDTO> getPositionByPositionCode(String positionCode) {
        return positionRepository.findById(positionCode).map(positionMapper::toDTO);
    }

}
