package com.motorph.ems.repository;

import com.motorph.ems.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findAllByEmployee_EmployeeId_OrderByDateDesc(Long employeeId);

    List<Attendance> findAllByDate_OrderByDateDesc(LocalDate date);

    List<Attendance> findAllByDateBetweenOrderByDateDesc(LocalDate start, LocalDate end);

    List<Attendance> findAllByTimeInIsAfterAndDate(LocalTime timeIn, LocalDate date);

    List<Attendance> findAllByTimeOutIsAfterAndDate(LocalTime timeOut, LocalDate date);

    Optional<Attendance> findByEmployee_EmployeeIdAndDate(Long employeeId, LocalDate date);

    boolean existsByEmployee_EmployeeIdAndDate(Long employeeId, LocalDate date);
}
