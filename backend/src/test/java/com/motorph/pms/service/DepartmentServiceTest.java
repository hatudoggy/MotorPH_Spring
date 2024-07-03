package com.motorph.pms.service;

import com.motorph.pms.dto.DepartmentDTO;
import com.motorph.pms.dto.mapper.DepartmentMapper;
import com.motorph.pms.model.Department;
import com.motorph.pms.repository.DepartmentRepository;
import com.motorph.pms.service.impl.DepartmentServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department department1;
    private DepartmentDTO departmentDTO1;

    @BeforeEach
    void setUp() {
        department1 = Department.builder()
                .departmentCode("D001")
                .departmentName("HR")
                .build();

        departmentDTO1 = DepartmentDTO.builder()
                .departmentCode("D001")
                .departmentName("HR")
                .build();
    }

    @Test
    @Transactional
    @DirtiesContext
    void DepartmentService_getDepartments_ReturnsListOfDepartmentDTO() {
        when(departmentRepository.findAll()).thenReturn(List.of(department1));

        when(departmentMapper.toDTO(any(List.class))).thenReturn(Collections.singletonList(departmentDTO1));

        List<DepartmentDTO> departments = departmentService.getDepartments();

        assertThat(departments).isNotEmpty();
        assertThat(departments.get(0).departmentCode()).isEqualTo("D001");
    }

    @Test
    @Transactional
    @DirtiesContext
    void DepartmentService_getDepartmentById_ReturnsDepartmentDTO() {
        when(departmentRepository.findById("D001")).thenReturn(Optional.of(department1));

        when(departmentMapper.toDTO(any(Department.class))).thenReturn(departmentDTO1);

        Optional<DepartmentDTO> foundDepartment = departmentService.getDepartmentByDepartmentCode("D001");

        assertThat(foundDepartment).isPresent();
        assertThat(foundDepartment.get().departmentCode()).isEqualTo("D001");
    }

//    @Test
//    @Transactional
//    @DirtiesContext
//    void DepartmentService_getDepartmentByName_ReturnsDepartmentDTO() {
//        when(departmentRepository.findByDepartmentName("HR")).thenReturn(Optional.of(department1));
//
//        when(departmentMapper.toDTO(any(Department.class))).thenReturn(departmentDTO1);
//
//        Optional<DepartmentDTO> foundDepartment = departmentService.getDepartmentByName("HR");
//
//        assertThat(foundDepartment).isPresent();
//        assertThat(foundDepartment.get().departmentName()).isEqualTo("HR");
//    }
}
