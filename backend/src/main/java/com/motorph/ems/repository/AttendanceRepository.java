package com.motorph.ems.repository;

import com.motorph.ems.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("SELECT a FROM Attendance a WHERE (a.employee.firstName LIKE concat('%', :name, '%') OR a.employee.lastName LIKE concat('%', :name, '%'))  AND a.date = :date")
    List<Attendance> findByDateAndNameContaining(@Param("date") LocalDate date, @Param("name") String name);

    @Query("SELECT COUNT(a) FROM Attendance a " +
            "WHERE a.employee.employeeId = %:employeeId% " +
            "AND a.date BETWEEN %:startDate AND %:endDate% " +
            "AND a.timeIn IS NOT NULL " +
            "AND a.timeOut IS NOT NULL")

    List<Attendance> findAllByEmployee_EmployeeId_AndDateBetween(Long employeeId, LocalDate start, LocalDate end);
}
