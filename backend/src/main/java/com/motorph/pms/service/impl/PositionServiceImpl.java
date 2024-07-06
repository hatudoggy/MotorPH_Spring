package com.motorph.pms.service.impl;

import com.motorph.pms.dto.PositionDTO;
import com.motorph.pms.dto.mapper.PositionMapper;
import com.motorph.pms.repository.PositionRepository;
import com.motorph.pms.service.extended.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheConfig(cacheNames = "positions")
@Service
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;

    @Autowired
    public PositionServiceImpl(PositionRepository positionRepository, PositionMapper positionMapper) {
        this.positionRepository = positionRepository;
        this.positionMapper = positionMapper;
    }

    @Cacheable()
    @Override
    public List<PositionDTO> getPositions() {
        return positionRepository.findAll().stream().map(positionMapper::toDTO).toList();
    }
}
