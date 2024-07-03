package com.motorph.ems.repository;

import com.motorph.ems.model.Attendance;
import com.motorph.ems.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class AttendanceRepositoryTest {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee1, employee2;
    private Attendance attendance1, attendance2, attendance3;

    @BeforeEach
    void setUp() {
        employee1 = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .dob(LocalDate.now())
                .address("123 Main St")
                .build();

        employee2 = Employee.builder()
                .firstName("Anna")
                .lastName("Doe")
                .dob(LocalDate.now())
                .address("123 Main St")
                .build();

        attendance1 = Attendance.builder()
                .employee(employee1)
                .date(LocalDate.now())
                .timeIn(LocalTime.of(8, 0))
                .timeOut(LocalTime.of(17, 0))
                .build();

        attendance2 = Attendance.builder()
                .employee(employee1)
                .date(LocalDate.now().plusDays(1))
                .timeIn(LocalTime.of(9, 0))
                .timeOut(LocalTime.of(18, 0))
                .build();

        attendance3 = Attendance.builder()
                .employee(employee2)
                .date(LocalDate.now().plusDays(2))
                .timeIn(LocalTime.of(10, 0))
                .timeOut(LocalTime.of(19, 0))
                .build();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
    }

    @Test
    @Transactional
    @DirtiesContext
    void AttendanceRepository_saveAttendance_returnsSavedAttendance() {

        attendanceRepository.save(attendance1);
        attendanceRepository.save(attendance2);

        assertNotNull(attendance1);
        assertThat(attendance1.getEmployee()).isEqualTo(employee1);
        assertNotNull(attendance2);
        assertThat(attendance2.getEmployee()).isEqualTo(employee1);
    }

    @Test
    @Transactional
    @DirtiesContext
    void AttendanceRepository_findAttendancesByEmployeeId_Desc_Found() {

        attendanceRepository.save(attendance1);
        attendanceRepository.save(attendance2);

        List<Attendance> attendances = attendanceRepository.
                findAllByEmployee_EmployeeId_OrderByDateDesc(employee1.getEmployeeId());

        assertThat(attendances).hasSizeGreaterThan(1);
        assertThat(attendances.getFirst()).isEqualTo(attendance2);
        assertThat(attendances.get(1)).isEqualTo(attendance1);
    }

    @Test
    @Transactional
    @DirtiesContext
    void AttendanceRepository_findByEmployeeIdAndDate_Found() {

        attendanceRepository.save(attendance1);
        attendanceRepository.save(attendance2);

        Optional<Attendance> attendance1 = attendanceRepository.
                findByEmployee_EmployeeIdAndDate(
                        employee1.getEmployeeId(), LocalDate.now());

        Optional<Attendance> attendance2 = attendanceRepository.
                findByEmployee_EmployeeIdAndDate(
                        employee1.getEmployeeId(), LocalDate.now().plusDays(1));

        assertThat(attendance1).isPresent();
        assertThat(attendance2).isPresent();
    }

    @Test
    @Transactional
    @DirtiesContext
    void AttendanceRepository_findAllByDate_OrderByDateDesc_Found() {
        attendanceRepository.save(attendance1);
        attendanceRepository.save(attendance2);
        attendanceRepository.save(attendance3);

        List<Attendance> attendances = attendanceRepository.
                findAllByDate_OrderByDateDesc(LocalDate.now());

        assertThat(attendances).hasSize(1);
        assertThat(attendances.getFirst()).isEqualTo(attendance1);
    }

    @Test
    @Transactional
    @DirtiesContext
    void AttendanceRepository_findAllByDateBetweenOrderByDateDesc_Found() {

        attendanceRepository.save(attendance1);
        attendanceRepository.save(attendance2);
        attendanceRepository.save(attendance3);

        List<Attendance> attendances = attendanceRepository.
                findAllByDateBetweenOrderByDateDesc(LocalDate.now(), LocalDate.now().plusDays(2));

        assertThat(attendances).hasSize(3);
        assertThat(attendances.get(0)).isEqualTo(attendance3);
        assertThat(attendances.get(1)).isEqualTo(attendance2);
        assertThat(attendances.get(2)).isEqualTo(attendance1);
    }

    @Test
    @Transactional
    @DirtiesContext
    void AttendanceRepository_findAllByTimeInIsAfterAndDate_Found() {

        attendanceRepository.save(attendance1);
        attendanceRepository.save(attendance2);

        List<Attendance> attendances = attendanceRepository.
                findAllByTimeInIsAfterAndDate(LocalTime.of(7, 0), LocalDate.now());

        assertThat(attendances).hasSize(1);
        assertThat(attendances.iterator().next()).isEqualTo(attendance1);
    }

    @Test
    @Transactional
    @DirtiesContext
    void AttendanceRepository_findAllByTimeOutIsAfterAndDate_Found() {

        attendanceRepository.save(attendance1);
        attendanceRepository.save(attendance2);

        List<Attendance> attendances = attendanceRepository.
                findAllByTimeOutIsAfterAndDate(LocalTime.of(16, 0), LocalDate.now());

        assertThat(attendances).hasSize(1);
        assertThat(attendances.iterator().next()).isEqualTo(attendance1);
    }

    @Test
    @Transactional
    @DirtiesContext
    void AttendanceRepository_getAllAttendances_Found() {

        attendanceRepository.save(attendance1);
        attendanceRepository.save(attendance2);

        List<Attendance> attendances = attendanceRepository.findAll();
        assertThat(attendances).hasSizeGreaterThan(1);
    }

    @Test
    @Transactional
    @DirtiesContext
    void AttendanceRepository_getAllAttendances_ReturnsEmptyList() {
        List<Attendance> attendances = attendanceRepository.findAll();
        assertThat(attendances).isEmpty();
    }

    @Test
    @Transactional
    @DirtiesContext
    void AttendanceRepository_updateAttendance_ReturnsUpdatedAttendance() {

        attendanceRepository.save(attendance1);

        LocalTime timeOut = LocalTime.now();
        LocalTime previousTimeOut = attendance1.getTimeOut();
        attendance1.setTimeOut(timeOut);

        Attendance updated_attendance1 = attendanceRepository.save(attendance1);

        assertThat(updated_attendance1.getTimeOut()).isEqualTo(timeOut);
        assertThat(updated_attendance1.getTimeOut()).isNotEqualTo(previousTimeOut);
    }

    @Test
    @Transactional
    @DirtiesContext
    void AttendanceRepository_deleteAttendance() {
        attendanceRepository.save(attendance1);
        attendanceRepository.delete(attendance1);
        List<Attendance> attendances = attendanceRepository.findAll();
        assertThat(attendances).isEmpty();
    }
}
