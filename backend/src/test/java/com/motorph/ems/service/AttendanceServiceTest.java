package com.motorph.ems.service;

import com.motorph.ems.dto.AttendanceDTO;
import com.motorph.ems.dto.EmployeeDTO;
import com.motorph.ems.dto.mapper.AttendanceMapper;
import com.motorph.ems.model.Attendance;
import com.motorph.ems.model.Employee;
import com.motorph.ems.repository.AttendanceRepository;
import com.motorph.ems.service.impl.AttendanceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private AttendanceMapper attendanceMapper;

    @InjectMocks
    private AttendanceServiceImpl attendanceService;

    private Attendance attendance1;
    private Long attendanceId;
    private AttendanceDTO attendanceDTO1;
    private LocalDate date;
    private LocalTime timeIn;
    private LocalTime timeOut;

    @BeforeEach
    void setUp() {
        attendanceId = 1L;
        date = LocalDate.now();
        timeIn = LocalTime.of(9, 0);
        timeOut = LocalTime.of(17, 0);

        Employee employee1 = Employee.builder()
                .employeeId(2L)
                .firstName("John")
                .lastName("Doe")
                .dob(LocalDate.now())
                .address("123 Main St")
                .build();

        EmployeeDTO employeeFullDTO1 = EmployeeDTO.builder()
                .employeeId(2L)
                .firstName("John")
                .lastName("Doe")
                .dob(LocalDate.now())
                .address("123 Main St")
                .build();

        attendance1 = Attendance.builder()
                .attendanceId(attendanceId)
                .employee(employee1)
                .date(date)
                .timeIn(timeIn)
                .timeOut(timeOut)
                .build();

        attendanceDTO1 = AttendanceDTO.builder()
                .attendanceId(attendanceId)
                .employee(employeeFullDTO1)
                .date(date)
                .timeIn(timeIn)
                .timeOut(timeOut)
                .build();
    }

    @Test
    @Transactional
    @DirtiesContext
    void AttendanceService_addNewAttendance_ReturnsAttendance() {
        when(attendanceMapper.toEntity(any(AttendanceDTO.class))).thenReturn(attendance1);

        when(attendanceRepository.save(any(Attendance.class))).thenReturn(attendance1);

        when(attendanceMapper.toDTO(any(Attendance.class))).thenReturn(attendanceDTO1);

        AttendanceDTO savedAttendance = attendanceService.addNewAttendance(attendanceDTO1);

        assertThat(savedAttendance).isNotNull();
    }

    @Test
    @Transactional
    @DirtiesContext
    void AttendanceService_getAttendanceById_ReturnsAttendance() {
        when(attendanceRepository.findById(1L)).thenReturn(Optional.of(attendance1));

        when(attendanceMapper.toDTO(any(Attendance.class))).thenReturn(attendanceDTO1);

        Optional<AttendanceDTO> foundAttendance = attendanceService.getAttendanceById(1L);

        assertThat(foundAttendance).isPresent();
        assertThat(foundAttendance.get().attendanceId()).isEqualTo(1L);
    }

    @Test
    @Transactional
    @DirtiesContext
    void AttendanceService_getAttendanceByEmployeeIdAndDate_ReturnsAttendance() {
        when(attendanceRepository.findByEmployee_EmployeeIdAndDate(1L, date)).thenReturn(Optional.of(attendance1));

        when(attendanceMapper.toDTO(any(Attendance.class))).thenReturn(attendanceDTO1);

        Optional<AttendanceDTO> foundAttendance = attendanceService.getAttendanceByEmployeeIdAndDate(1L, date);

        assertThat(foundAttendance).isPresent();
        assertThat(foundAttendance.get().date()).isEqualTo(date);
    }

    @Test
    @Transactional
    @DirtiesContext
    void AttendanceService_getAllAttendances_ReturnsListOfAttendanceDTO() {
        when(attendanceRepository.findAll()).thenReturn(Collections.singletonList(attendance1));

        List<AttendanceDTO> attendances = attendanceService.getAllAttendances();

        assertThat(attendances).isNotEmpty();
    }

    @Test
    @Transactional
    @DirtiesContext
    void AttendanceService_getAllByEmployeeId_ReturnsListOfAttendance() {
        when(attendanceRepository.findAllByEmployee_EmployeeId_OrderByDateDesc(1L)).thenReturn(Collections.singletonList(attendance1));

        List<AttendanceDTO> attendances = attendanceService.getAllByEmployeeId(1L);

        assertThat(attendances).isNotEmpty();
    }

    @Test
    @Transactional
    @DirtiesContext
    void AttendanceService_getAllByEmployeeId_ReturnsListOfAttendanceDTO() {
        when(attendanceRepository.findAllByEmployee_EmployeeId_OrderByDateDesc(1L)).thenReturn(Collections.singletonList(attendance1));

        List<AttendanceDTO> attendances = attendanceService.getAllByEmployeeId(1L);

        assertThat(attendances).isNotEmpty();
    }

    @Test
    @Transactional
    @DirtiesContext
    void AttendanceService_getAllByDate_ReturnsListOfAttendance() {
        when(attendanceRepository.findAllByDate_OrderByDateDesc(date)).thenReturn(Collections.singletonList(attendance1));

        List<AttendanceDTO> attendances = attendanceService.getAllByDate(date);

        assertThat(attendances).isNotEmpty();
    }

    @Test
    @Transactional
    @DirtiesContext
    void AttendanceService_getAttendancesForDateRange_ReturnsListOfAttendanceDTO() {
        when(attendanceRepository.findAllByDateBetweenOrderByDateDesc(date.minusDays(1), date.plusDays(1)))
                .thenReturn(Collections.singletonList(attendance1));

        List<AttendanceDTO> attendances = attendanceService.getAttendancesForDateRange(date.minusDays(1), date.plusDays(1));

        assertThat(attendances).isNotEmpty();
    }

    @Test
    @Transactional
    @DirtiesContext
    void AttendanceService_getAttendancesAfterTimeIn_ReturnsListOfAttendanceDTO() {
        when(attendanceRepository.findAllByTimeInIsAfterAndDate(timeIn.minusHours(1), date))
                .thenReturn(Collections.singletonList(attendance1));

        List<AttendanceDTO> attendances = attendanceService.getAttendancesAfterTimeIn(timeIn.minusHours(1), date);

        assertThat(attendances).isNotEmpty();
    }

    @Test
    @Transactional
    @DirtiesContext
    void AttendanceService_getAttendancesAfterTimeOut_ReturnsListOfAttendanceDTO() {
        when(attendanceRepository.findAllByTimeOutIsAfterAndDate(timeOut.minusHours(1), date))
                .thenReturn(Collections.singletonList(attendance1));

        List<AttendanceDTO> attendances = attendanceService.getAttendancesAfterTimeOut(timeOut.minusHours(1), date);

        assertThat(attendances).isNotEmpty();
    }

    @Test
    @Transactional
    @DirtiesContext
    void AttendanceService_updateAttendance_UpdatesAndReturnsAttendance() {
        Attendance updated = attendance1;

        updated.setTimeOut(LocalTime.now().plusHours(10));

        AttendanceDTO attendanceDTO1 = AttendanceDTO.builder()
                .attendanceId(updated.getAttendanceId())
                .date(updated.getDate())
                .timeIn(updated.getTimeIn())
                .timeOut(updated.getTimeOut())
                .build();

        when(attendanceRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(attendance1));

        when(attendanceRepository.save(any(Attendance.class))).thenReturn(attendance1);

        when(attendanceMapper.toDTO(any(Attendance.class))).thenReturn(attendanceDTO1);


        AttendanceDTO updatedAttendance = attendanceService.updateAttendance(attendanceId, attendanceDTO1);

        assertThat(updatedAttendance).isNotNull();
        assertThat(updatedAttendance.timeOut()).isEqualTo(updated.getTimeOut());
    }

    @Test
    @Transactional
    @DirtiesContext
    void AttendanceService_deleteAttendance_DeletesAttendance() {
        when(attendanceRepository.existsById(1L)).thenReturn(true);

        attendanceService.deleteAttendanceById(1L);

        verify(attendanceRepository).deleteById(1L);
    }
}