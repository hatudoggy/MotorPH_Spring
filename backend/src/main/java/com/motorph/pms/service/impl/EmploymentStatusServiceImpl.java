package com.motorph.pms.service.impl;

import com.motorph.pms.dto.EmploymentStatusDTO;
import com.motorph.pms.dto.mapper.EmploymentStatusMapper;
import com.motorph.pms.model.EmploymentStatus;
import com.motorph.pms.repository.EmploymentStatusRepository;
import com.motorph.pms.service.EmploymentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmploymentStatusServiceImpl implements EmploymentStatusService {

    private final EmploymentStatusRepository employmentStatusRepository;
    private final EmploymentStatusMapper employmentStatusMapper;

    @Autowired
    public EmploymentStatusServiceImpl(
            EmploymentStatusRepository employmentStatusRepository,
            EmploymentStatusMapper employmentStatusMapper) {
        this.employmentStatusRepository = employmentStatusRepository;
        this.employmentStatusMapper = employmentStatusMapper;
    }

    @Override
    public EmploymentStatusDTO addNewStatus(EmploymentStatusDTO statusDTO) {
        if (employmentStatusRepository.findByStatusName(statusDTO.statusName()).isPresent()) {
            throw new IllegalStateException("EmploymentStatus with name " + statusDTO.statusName() + " already exists");
        }

        EmploymentStatus status = employmentStatusMapper.toEntity(statusDTO);
        return employmentStatusMapper.toDTO(employmentStatusRepository.save(status));
    }

    @Override
    public List<EmploymentStatusDTO> getEmploymentStatuses() {
        return employmentStatusRepository.findAll().stream()
                .map(employmentStatusMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EmploymentStatusDTO> getEmploymentStatusById(int statusId) {
        return employmentStatusRepository.findById(statusId)
                .map(employmentStatusMapper::toDTO);
    }

//    @Override
//    public Optional<EmploymentStatusDTO> getEmploymentStatusByStatusName(String statusName) {
//        return employmentStatusRepository.findByStatusName(statusName)
//                .map(employmentStatusMapper::toDTO);
//    }
}
