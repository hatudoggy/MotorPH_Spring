package com.motorph.ems.service;

import com.motorph.ems.dto.EmploymentStatusDTO;
import com.motorph.ems.dto.mapper.EmploymentStatusMapper;
import com.motorph.ems.model.EmploymentStatus;
import com.motorph.ems.repository.EmploymentStatusRepository;
import com.motorph.ems.service.impl.EmploymentStatusServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmploymentStatusServiceTest {

    @Mock
    private EmploymentStatusRepository employmentStatusRepository;

    @Mock
    private EmploymentStatusMapper statusMapper;
    @InjectMocks
    private EmploymentStatusServiceImpl employmentStatusService;

    private EmploymentStatus employmentStatus1;
    private EmploymentStatusDTO employmentStatusDTO1;

    @BeforeEach
    void setUp() {
        employmentStatus1 = EmploymentStatus.builder()
                .statusId(1)
                .statusName("Active")
                .build();

        employmentStatusDTO1 = new EmploymentStatusDTO(1, "Active");
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmploymentStatusService_addNewStatus_ReturnsEmploymentStatusDTO() {
        when(employmentStatusRepository.findByStatusName(any(String.class))).thenReturn(Optional.empty());

        when(statusMapper.toEntity(any(EmploymentStatusDTO.class))).thenReturn(employmentStatus1);

        when(employmentStatusRepository.save(any(EmploymentStatus.class))).thenReturn(employmentStatus1);

        when(statusMapper.toDTO(any(EmploymentStatus.class))).thenReturn(employmentStatusDTO1);

        EmploymentStatusDTO savedStatus = employmentStatusService.addNewStatus(employmentStatusDTO1);

        assertThat(savedStatus).isNotNull();
        assertThat(savedStatus.statusId()).isEqualTo(1);
        assertThat(savedStatus.statusName()).isEqualTo("Active");
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmploymentStatusService_getEmploymentStatuses_ReturnsListOfEmploymentStatusDTO() {
        when(employmentStatusRepository.findAll()).thenReturn(List.of(employmentStatus1));

        when(statusMapper.toDTO(any(EmploymentStatus.class))).thenReturn(employmentStatusDTO1);

        List<EmploymentStatusDTO> statuses = employmentStatusService.getEmploymentStatuses();

        assertThat(statuses).isNotEmpty();
        assertThat(statuses.get(0).statusId()).isEqualTo(1);
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmploymentStatusService_getEmploymentStatusById_ReturnsEmploymentStatusDTO() {
        when(employmentStatusRepository.findById(1)).thenReturn(Optional.of(employmentStatus1));

        when(statusMapper.toDTO(any(EmploymentStatus.class))).thenReturn(employmentStatusDTO1);

        Optional<EmploymentStatusDTO> foundStatus = employmentStatusService.getEmploymentStatusById(1);

        assertThat(foundStatus).isPresent();
        assertThat(foundStatus.get().statusId()).isEqualTo(1);
    }

    @Test
    @Transactional
    @DirtiesContext
    void EmploymentStatusService_getEmploymentStatusByStatusName_ReturnsEmploymentStatusDTO() {
        when(employmentStatusRepository.findByStatusName("Active")).thenReturn(Optional.of(employmentStatus1));

        when(statusMapper.toDTO(any(EmploymentStatus.class))).thenReturn(employmentStatusDTO1);

        Optional<EmploymentStatusDTO> foundStatus = employmentStatusService.getEmploymentStatusByStatusName("Active");

        assertThat(foundStatus).isPresent();
        assertThat(foundStatus.get().statusName()).isEqualTo("Active");
    }
}
