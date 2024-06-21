package com.motorph.ems.repository;

import com.motorph.ems.model.Attendance;
import com.motorph.ems.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByEmployeeEmployeeIdOrderByDateDesc(Long employeeId);
    Optional<Attendance> findByEmployeeEmployeeIdAndDate(Long employeeId, LocalDate date);
    List<Attendance> findByDate(LocalDate date);
    @Query("SELECT a FROM Attendance a WHERE (a.employee.firstName LIKE %:name% OR a.employee.lastName LIKE %:name%)  AND a.date = %:date%")
    List<Attendance> findByDateAndNameContaining(@Param("date") LocalDate date, @Param("name") String name);
}
