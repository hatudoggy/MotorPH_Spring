package com.motorph.pms.repository;

<<<<<<< HEAD:backend/src/main/java/com/motorph/ems/repository/AttendanceRepository.java
import com.motorph.ems.model.Attendance;
import com.motorph.ems.model.Employee;
=======
import com.motorph.pms.model.Attendance;
import org.springframework.data.jpa.repository.EntityGraph;
>>>>>>> UI-integration:backend/src/main/java/com/motorph/pms/repository/AttendanceRepository.java
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

    @Query("SELECT COUNT(a) FROM Attendance a " +
            "WHERE a.employee.employeeId = %:employeeId% " +
            "AND a.date BETWEEN %:startDate AND %:endDate% " +
            "AND a.timeIn IS NOT NULL " +
            "AND a.timeOut IS NOT NULL")
    Long countPresentAttendancesByEmployeeId(
            @Param("employeeId") Long employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(a.overtime) FROM Attendance a " +
            "WHERE a.employee.employeeId = %:employeeId% " +
            "AND a.date BETWEEN %:startDate AND %:endDate% " +
            "AND a.timeIn IS NOT NULL " +
            "AND a.timeOut IS NOT NULL")
    Long sumOvertimeHoursByEmployeeId(
            @Param("employeeId") Long employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
