package com.motorph.pms.service;

import com.motorph.pms.dto.LeaveBalanceDTO;
import com.motorph.pms.dto.LeaveTypeDTO;
import com.motorph.pms.dto.mapper.LeaveBalanceMapper;
import com.motorph.pms.model.Employee;
import com.motorph.pms.model.LeaveBalance;
import com.motorph.pms.model.LeaveBalance.LeaveType;
import com.motorph.pms.repository.LeaveBalanceRepository;
import com.motorph.pms.repository.LeaveTypeRepository;
import com.motorph.pms.service.impl.LeaveBalanceServiceImpl;
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
    void LeaveBalanceService_getLeaveTypeById_ReturnsLeaveTypeDTO() {
        when(balanceMapper.toLeaveTypeDTO(any(LeaveType.class))).thenReturn(leaveTypeDTO1);

        when(leaveTypeRepository.findById(1)).thenReturn(Optional.of(leaveType1));

        Optional<LeaveTypeDTO> foundType = leaveBalanceService.getLeaveTypeById(1);

        assertThat(foundType).isPresent();
        assertThat(foundType.get().id()).isEqualTo(1);
    }

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
