package com.motorph.pms.service;

<<<<<<< HEAD:backend/src/main/java/com/motorph/ems/service/AttendanceService.java
import com.motorph.ems.dto.AttendanceSummary;
import com.motorph.ems.model.Attendance;
=======
import com.motorph.pms.dto.AttendanceDTO;
import com.motorph.pms.dto.AttendanceSummaryDTO;
import com.motorph.pms.model.Attendance;
>>>>>>> UI-integration:backend/src/main/java/com/motorph/pms/service/AttendanceService.java

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    public Attendance addNewAttendance(Attendance attendance);

    public List<Attendance> getAllAttendances();

    public List<Attendance> getAllAttendancesByDate(LocalDate localDate);

    public List<Attendance> getAllAttendancesByDateAndNameContains(LocalDate localDate, String name);

    public List<Attendance> getAllAttendancesByEmployeeId(Long employeeId);

    public Attendance getAttendanceById(Long attendanceId);

    public Attendance getAttendanceByEmployeeIdAndDate(Long employeeId, LocalDate date);

    public AttendanceSummary getAttendanceSummaryByEmployeeId(Long employeeId);

    public Attendance updateAttendance(Attendance attendance);
    public void deleteAttendance(Long attendanceId);

}
