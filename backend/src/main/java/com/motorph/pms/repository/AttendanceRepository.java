package com.motorph.pms.repository;

import com.motorph.pms.model.Attendance;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findAllByEmployee_EmployeeId_OrderByDateDesc(Long employeeId);

    List<Attendance> findAllByDate_OrderByDateDesc(LocalDate date);

    Optional<Attendance> findByEmployee_EmployeeIdAndDate(Long employeeId, LocalDate date);

    boolean existsByEmployee_EmployeeIdAndDate(Long employeeId, LocalDate date);

    List<Attendance> findAllByEmployee_EmployeeId_AndDateBetween(Long employeeId, LocalDate start, LocalDate end);
}
