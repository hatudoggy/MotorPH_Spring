package com.motorph.pms.controller;


<<<<<<< HEAD:backend/src/main/java/com/motorph/ems/controller/EmployeeController.java
import com.motorph.ems.dto.AttendanceSummary;
import com.motorph.ems.model.*;
import com.motorph.ems.service.*;
=======
import com.motorph.pms.dto.*;
import com.motorph.pms.model.Attendance;
import com.motorph.pms.service.*;
import jakarta.persistence.EntityNotFoundException;
>>>>>>> UI-integration:backend/src/main/java/com/motorph/pms/controller/EmployeeController.java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.time.temporal.ChronoUnit.HOURS;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final BenefitsService benefitsService;
    private final EmploymentService employmentService;
    private final GovernmentIdService governmentIdService;
    private final AttendanceService attendanceService;
    private final LeaveBalanceService leaveBalanceService;
    private final LeaveRequestService leaveRequestService;
    private final PayrollService payrollService;

    @Autowired
    public EmployeeController(
            EmployeeService employeeService,
            @Qualifier("employeeServiceImpl")
            BenefitsService benefitsService,
            @Qualifier("employeeServiceImpl")
            EmploymentService employmentService,
            @Qualifier("employeeServiceImpl")
            GovernmentIdService governmentIdService,
            AttendanceService attendanceService,
            LeaveBalanceService leaveBalanceService,
            LeaveRequestService leaveRequestService,
            PayrollService payrollService
    ){
        this.employeeService = employeeService;
        this.benefitsService = benefitsService;
        this.employmentService = employmentService;
        this.governmentIdService = governmentIdService;
        this.attendanceService = attendanceService;
        this.leaveBalanceService = leaveBalanceService;
        this.leaveRequestService = leaveRequestService;
        this.payrollService = payrollService;
    }

    @PostMapping
    public void registerEmployee(@RequestBody Employee employee) {
        employeeService.addNewEmployee(employee);
    }

    @GetMapping()
    public List<Employee> getAllEmployees(
        @RequestParam(value = "name", required = false) String name
    ) {
        if(name != null) {
            if(!name.isEmpty()) {
                return employeeService.getAllEmployeesNameContains(name);
            } else {
                return employeeService.getAllEmployees();
            }
        } else {
            return employeeService.getAllEmployees();
        }
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable(value = "id") Long employeeID) {
        return employeeService.getEmployeeById(employeeID);
    }

    @PatchMapping("/{id}")
    public Employee updateEmployee(
            @PathVariable(value = "id") Long employeeID,
            @RequestBody Employee employeeDetails
    ) {
        return employeeService.updateEmployee(employeeDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable(value = "id") Long employeeID) {
        if (employeeService.getEmployeeById(employeeID) == null) {
            return ResponseEntity.notFound().build();
        }
        employeeService.deleteEmployee(employeeID);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/employment")
    public Employment getEmploymentByEmployeeId(@PathVariable(value = "id") Long employeeID) {
        return employmentService.getEmploymentByEmployeeId(employeeID);
    }

    @GetMapping("/{id}/governmentIds")
    public List<GovernmentId> getGovernmentIdByEmployeeId(@PathVariable(value = "id") Long employeeID) {
        return governmentIdService.getAllGovernmentIdByEmployeeId(employeeID);
    }
    @GetMapping("/{id}/benefits")
    public List<Benefits> getBenefitsByEmployeeId(@PathVariable(value = "id") Long employeeID) {
        return benefitsService.getAllBenefitsByEmployeeId(employeeID);
    }

    @GetMapping("/{id}/attendances")
    public List<Attendance> getEmployeeAttendance(
            @PathVariable long id,
            @RequestParam(value = "date", required = false) String date
    ) {
        if(date != null) {
            List<Attendance> attendanceList = new ArrayList<>();
            attendanceList.add(attendanceService.getAttendanceByEmployeeIdAndDate(id, LocalDate.parse(date)));
            return attendanceList;
        } else {
            return attendanceService.getAllAttendancesByEmployeeId(id);
        }
    }

    @GetMapping("/{id}/attendances/summary")
    public AttendanceSummary getEmployeeAttendanceSummary(
            @PathVariable long id
    ) {
        return attendanceService.getAttendanceSummaryByEmployeeId(id);
    }

    @PostMapping("/{id}/attendances/timeIn")
    public Attendance employeeTimeIn(@PathVariable long id) {
        Employee employee = employeeService.getEmployeeById(id);
        Attendance attendanceToday = new Attendance();
        attendanceToday.setEmployee(employee);
        attendanceToday.setDate(LocalDate.now());
        attendanceToday.setTimeIn(LocalTime.now());
        return attendanceService.addNewAttendance(attendanceToday);
    }

    @PostMapping("/{id}/attendances/timeOut")
    public Attendance employeeTimeOut(@PathVariable long id) {
        Attendance attendanceToday = attendanceService.getAttendanceByEmployeeIdAndDate(id, LocalDate.now());
        LocalTime timeOut = LocalTime.now();

        attendanceToday.setTimeOut(timeOut);
        attendanceToday.setHoursWorked(
                (int) HOURS.between(attendanceToday.getTimeIn(),timeOut)
        );
        attendanceToday.setOvertime(
                Math.max(0, (int) HOURS.between(LocalTime.of(17, 0),timeOut))
        );
        return attendanceService.updateAttendance(attendanceToday);
    }


    @GetMapping("/{id}/leave-balances")
    public List<LeaveBalance> getEmployeeLeaveBalance(@PathVariable long id) {
        return leaveBalanceService.getLeaveBalancesByEmployeeId(id);
    }

    @GetMapping("/{id}/leave-requests")
    public List<LeaveRequest> getEmployeeLeaveRequest(@PathVariable long id) {
        return leaveRequestService.getAllLeaveRequestsByEmployeeId(id);
    }

    @GetMapping("/{id}/payrolls")
    public List<Payroll> getEmployeePayroll(
            @PathVariable long id,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate
    ){
        if(startDate != null && endDate != null) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            return payrollService.getPayrollByEmployeeIdAndPeriodDates(id, start, end);
        } else {
            return payrollService.getPayrollsByEmployeeId(id);
        }
    }


}
