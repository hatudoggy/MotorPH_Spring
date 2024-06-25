package com.motorph.ems.service;

import com.motorph.ems.dto.LeaveBalanceDTO;
import com.motorph.ems.dto.LeaveTypeDTO;
import com.motorph.ems.dto.mapper.LeaveBalanceMapper;
import com.motorph.ems.model.Employee;
import com.motorph.ems.model.LeaveBalance;
import com.motorph.ems.model.LeaveBalance.LeaveType;
import com.motorph.ems.repository.LeaveBalanceRepository;
import com.motorph.ems.repository.LeaveTypeRepository;
import com.motorph.ems.service.impl.LeaveBalanceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeaveBalanceServiceTest {

    @Mock
    private LeaveBalanceRepository leaveBalanceRepository;

    @Mock
    private LeaveBalanceMapper balanceMapper;

    @Mock
    private LeaveTypeRepository leaveTypeRepository;

    @InjectMocks
    private LeaveBalanceServiceImpl leaveBalanceService;

    private LeaveBalance leaveBalance1;
    private LeaveBalanceDTO leaveBalanceDTO1;
    private LeaveType leaveType1;
    private LeaveTypeDTO leaveTypeDTO1;

    @BeforeEach
    void setUp() {
        leaveType1 = LeaveType.builder()
                .leaveTypeId(1)
                .type("Sick Leave")
                .build();

        leaveTypeDTO1 = new LeaveTypeDTO(1, "Sick Leave");

        leaveBalance1 = LeaveBalance.builder()
                .leaveBalanceId(1L)
                .employee( Employee.builder().employeeId(1L).build() )
                .leaveType(leaveType1)
                .balance(10)
                .build();

        leaveBalanceDTO1 = new LeaveBalanceDTO(1L, 1L, leaveTypeDTO1.id(), 10);
    }

    @Test
    @Transactional
    @DirtiesContext
    void LeaveBalanceService_addNewLeaveBalance_ReturnsLeaveBalanceDTO() {
        when(leaveBalanceRepository.existsByEmployee_EmployeeIdAndLeaveType_LeaveTypeId(any(Long.class), any(int.class))).thenReturn(false);

        when(balanceMapper.toEntity(any(LeaveBalanceDTO.class))).thenReturn(leaveBalance1);
        when(leaveBalanceRepository.save(any(LeaveBalance.class))).thenReturn(leaveBalance1);
        when(balanceMapper.toDTO(any(LeaveBalance.class))).thenReturn(leaveBalanceDTO1);

        LeaveBalanceDTO savedBalance = leaveBalanceService.addNewLeaveBalance(leaveBalanceDTO1);

        assertThat(savedBalance).isNotNull();
        assertThat(savedBalance.id()).isEqualTo(1L);
        assertThat(savedBalance.leaveTypeId()).isEqualTo(1);
    }

    @Test
    @Transactional
    @DirtiesContext
    void LeaveBalanceService_addMultipleLeaveBalances_ReturnsListOfLeaveBalanceDTO() {
        when(balanceMapper.toEntity(any(LeaveBalanceDTO.class))).thenReturn(leaveBalance1);

        when(leaveBalanceRepository.saveAll(any(List.class))).thenReturn(List.of(leaveBalance1));

        when(balanceMapper.toDTO(any(LeaveBalance.class))).thenReturn(leaveBalanceDTO1);

        List<LeaveBalanceDTO> savedBalances = leaveBalanceService.addMultipleLeaveBalances(List.of(leaveBalanceDTO1));

        assertThat(savedBalances).isNotEmpty();
        assertThat(savedBalances.get(0).id()).isEqualTo(1L);
    }

    @Test
    @Transactional
    @DirtiesContext
    void LeaveBalanceService_getLeaveBalanceById_ReturnsLeaveBalanceDTO() {
        when(balanceMapper.toDTO(any(LeaveBalance.class))).thenReturn(leaveBalanceDTO1);
        when(leaveBalanceRepository.findById(1L)).thenReturn(Optional.of(leaveBalance1));

        Optional<LeaveBalanceDTO> foundBalance = leaveBalanceService.getLeaveBalanceById(1L);

        assertThat(foundBalance).isPresent();
        assertThat(foundBalance.get().id()).isEqualTo(1L);
    }

    @Test
    @Transactional
    @DirtiesContext
    void LeaveBalanceService_getLeaveBalanceByEmployeeIdAndLeaveType_ReturnsLeaveBalanceDTO() {
        when(balanceMapper.toDTO(any(LeaveBalance.class))).thenReturn(leaveBalanceDTO1);
        when(leaveBalanceRepository.getLeaveBalanceByEmployee_EmployeeIdAndLeaveType_LeaveTypeId(1L, 1)).thenReturn(Optional.of(leaveBalance1));

        Optional<LeaveBalanceDTO> foundBalance = leaveBalanceService.getLeaveBalanceByEmployeeIdAndLeaveType(1L, 1);

        assertThat(foundBalance).isPresent();
        assertThat(foundBalance.get().leaveTypeId()).isEqualTo(1);
    }

    @Test
    @Transactional
    @DirtiesContext
    void LeaveBalanceService_getAllLeaveBalances_ReturnsListOfLeaveBalanceDTO() {
        when(balanceMapper.toDTO(any(LeaveBalance.class))).thenReturn(leaveBalanceDTO1);

        when(leaveBalanceRepository.findAll()).thenReturn(List.of(leaveBalance1));

        List<LeaveBalanceDTO> balances = leaveBalanceService.getAllLeaveBalances();

        assertThat(balances).isNotEmpty();
        assertThat(balances.get(0).id()).isEqualTo(1L);
    }

    @Test
    @Transactional
    @DirtiesContext
    void LeaveBalanceService_getLeaveBalancesByEmployeeId_ReturnsListOfLeaveBalanceDTO() {
        when(balanceMapper.toDTO(any(LeaveBalance.class))).thenReturn(leaveBalanceDTO1);

        when(leaveBalanceRepository.findAllByEmployee_EmployeeId(any(Long.class))).thenReturn(List.of(leaveBalance1));

        List<LeaveBalanceDTO> balances = leaveBalanceService.getLeaveBalancesByEmployeeId(1L);

        assertThat(balances).isNotEmpty();
        assertThat(balances.get(0).id()).isEqualTo(1L);
    }

    @Test
    @Transactional
    @DirtiesContext
    void LeaveBalanceService_updateLeaveBalance_ReturnsUpdatedLeaveBalanceDTO() {
        when(leaveBalanceRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(leaveBalance1));

        when(leaveBalanceRepository.save(any(LeaveBalance.class))).thenReturn(leaveBalance1);
        when(balanceMapper.toDTO(any(LeaveBalance.class))).thenReturn(leaveBalanceDTO1);


        LeaveBalanceDTO updatedBalance = leaveBalanceService.updateLeaveBalance(1L, leaveBalanceDTO1);

        assertThat(updatedBalance).isNotNull();
        assertThat(updatedBalance.id()).isEqualTo(1L);
        assertThat(updatedBalance.balance()).isEqualTo(10);
    }

    @Test
    @Transactional
    @DirtiesContext
    void LeaveBalanceService_deleteLeaveBalanceById_RemovesLeaveBalance() {
        leaveBalanceService.deleteLeaveBalanceById(1L);

        Mockito.verify(leaveBalanceRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    @Transactional
    @DirtiesContext
    void LeaveBalanceService_deleteMultipleLeaveBalancesByEmployeeId_RemovesAllLeaveBalances() {
        when(leaveBalanceRepository.findAllByEmployee_EmployeeId(any(Long.class))).thenReturn(List.of(leaveBalance1));

        leaveBalanceService.deleteMultipleLeaveBalancesByEmployeeId(1L);

        Mockito.verify(leaveBalanceRepository, Mockito.times(1)).deleteAllById(any(List.class));
    }

    @Test
    @Transactional
    @DirtiesContext
    void LeaveBalanceService_addNewLeaveType_ReturnsLeaveTypeDTO() {
        when(leaveTypeRepository.existsByType(any(String.class))).thenReturn(false);

        when(balanceMapper.toLeaveTypeEntity(any(LeaveTypeDTO.class))).thenReturn(leaveType1);

        when(leaveTypeRepository.save(any(LeaveType.class))).thenReturn(leaveType1);

        when(balanceMapper.toLeaveTypeDTO(any(LeaveType.class))).thenReturn(leaveTypeDTO1);

        LeaveTypeDTO savedType = leaveBalanceService.addNewLeaveType(leaveTypeDTO1);

        assertThat(savedType).isNotNull();
        assertThat(savedType.typeName()).isEqualTo("Sick Leave");
    }

    @Test
    @Transactional
    @DirtiesContext
    void LeaveBalanceService_getLeaveTypeById_ReturnsLeaveTypeDTO() {
        when(balanceMapper.toLeaveTypeDTO(any(LeaveType.class))).thenReturn(leaveTypeDTO1);

        when(leaveTypeRepository.findById(1)).thenReturn(Optional.of(leaveType1));

        Optional<LeaveTypeDTO> foundType = leaveBalanceService.getLeaveTypeById(1);

        assertThat(foundType).isPresent();
        assertThat(foundType.get().id()).isEqualTo(1);
    }

//    @Test
//    @Transactional
//    @DirtiesContext
//    void LeaveBalanceService_getLeaveTypeByTypeName_ReturnsLeaveTypeDTO() {
//        when(balanceMapper.toLeaveTypeDTO(any(LeaveType.class))).thenReturn(leaveTypeDTO1);
//
//        when(leaveTypeRepository.findByType("Sick Leave")).thenReturn(Optional.of(leaveType1));
//
//        Optional<LeaveTypeDTO> foundType = leaveBalanceService.getLeaveTypeByTypeName("Sick Leave");
//
//        assertThat(foundType).isPresent();
//        assertThat(foundType.get().typeName()).isEqualTo("Sick Leave");
//    }

    @Test
    @Transactional
    @DirtiesContext
    void LeaveBalanceService_getAllLeaveTypes_ReturnsListOfLeaveTypeDTO() {
        when(balanceMapper.toLeaveTypeDTO(any(LeaveType.class))).thenReturn(leaveTypeDTO1);

        when(leaveTypeRepository.findAll()).thenReturn(List.of(leaveType1));

        List<LeaveTypeDTO> leaveTypes = leaveBalanceService.getAllLeaveTypes();

        assertThat(leaveTypes).isNotEmpty();
        assertThat(leaveTypes.get(0).typeName()).isEqualTo("Sick Leave");
    }
}
