package com.motorph.ems.controller;


import com.motorph.pms.dto.*;
import com.motorph.pms.model.Attendance;
import com.motorph.pms.service.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@CrossOrigin()
@RequestMapping(path = "api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final PayrollService payrollService;
    private final AttendanceService attendanceService;
    private final BenefitsService benefitsService;
    private final LeaveBalanceService leaveBalanceService;

    @Autowired
    public EmployeeController(
            EmployeeService employeeService,
            PayrollService payrollService,
            AttendanceService attendanceService,
            BenefitsService benefitsService,
            LeaveBalanceService leaveBalanceService
    ){
        this.employeeService = employeeService;
        this.payrollService = payrollService;
        this.attendanceService = attendanceService;
        this.benefitsService = benefitsService;
        this.leaveBalanceService = leaveBalanceService;
    }

    @PostMapping("/register")
    public ResponseEntity<EmployeeDTO> registerEmployee(@RequestBody EmployeeDTO employee) {
        EmployeeDTO savedEmployee = employeeService.addNewEmployee(employee);
        return ResponseEntity.ok(savedEmployee);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> employees = employeeService.getEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable(value = "id") Long employeeID) {
        EmployeeDTO employee = employeeService.getEmployeeById(employeeID, true).orElseThrow(
                () -> new EntityNotFoundException("Employee not found")
        );

        EmployeeDTO result = EmployeeDTO.builder()
                .employeeId(employeeID)
                .firstName(employee.firstName())
                .lastName(employee.lastName())
                .dob(employee.dob())
                .address(employee.address())
                .hireDate(employee.hireDate())
                .basicSalary(employee.basicSalary())
                .semiMonthlyRate(employee.semiMonthlyRate())
                .hourlyRate(employee.hourlyRate())
                .position(employee.position())
                .department(employee.department())
                .status(employee.status())
                .supervisor(employee.supervisor())
                .contacts(employee.contacts())
                .governmentId(employee.governmentId())
                .benefits(employee.benefits())
                .leaveBalances(employee.leaveBalances())
                .build();

        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/update/{employeeID}")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @PathVariable(value = "employeeID") Long employeeID,
            @RequestBody EmployeeDTO employeeDetails
    ) {
        EmployeeDTO updatedEmployee = employeeService.updateEmployee(employeeID, employeeDetails);
        return ResponseEntity.ok(updatedEmployee);
    }


    @GetMapping("/{id}/attendances")
    public ResponseEntity<List<AttendanceDTO>> getEmployeeAttendances(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(attendanceService.getAllByEmployeeId(id));
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{id}/attendances/dateRange")
    private ResponseEntity<List<AttendanceDTO>> getAttendancesByDateRange(
            @PathVariable Long id,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate
    ) {
        try {
            LocalDate start = parseDate(startDate);
            LocalDate end = parseDate(endDate);

            List<AttendanceDTO> attendanceList = attendanceService.getAllAttendanceByEmployeeIdAndDateRange(id, start, end);

            return ResponseEntity.ok(attendanceList);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{id}/attendances/date")
    private ResponseEntity<AttendanceDTO> getAttendanceByDate(
            @PathVariable Long id,
            @RequestParam("date") String date
    ) {
        try {
            LocalDate localDate = parseDate(date);
            AttendanceDTO attendance = attendanceService.getAttendanceByEmployeeIdAndDate(id, localDate).orElse(null);

            if (attendance == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(attendance);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    private LocalDate parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }

    @PostMapping("/{employeeId}/attendances/timeIn")
    public ResponseEntity<AttendanceDTO> employeeTimeIn(@PathVariable Long employeeId) {
        Attendance attendanceToday = new Attendance(
                employeeId,
                LocalDate.now(),
                LocalTime.now()
        );

        return ResponseEntity.ok(attendanceService.addNewAttendance(attendanceToday));
    }

    @PutMapping("/{employeeId}/attendances/timeOut")
    public ResponseEntity<AttendanceDTO> employeeTimeOut(@PathVariable Long employeeId) {
        LocalTime timeOut = LocalTime.now();
        LocalDate date = LocalDate.now();

        if(timeOut.isAfter(LocalTime.of(0,0)) && timeOut.isBefore(LocalTime.of(8,0))){
            date = date.minusDays(1);
        }
        AttendanceDTO updatedAttendance = AttendanceDTO.builder()
                .date(date)
                .timeOut(timeOut)
                .build();

        return ResponseEntity.ok(attendanceService.updateAttendance(employeeId, updatedAttendance));
    }


//    @GetMapping("/{id}/leave-balances")
//    public List<LeaveBalance> getEmployeeLeaveBalance(@PathVariable long id) {
//        return leaveBalanceService.getLeaveBalancesByEmployeeId(id);
//    }
//
//    @GetMapping("/{id}/leave-requests")
//    public List<LeaveRequest> getEmployeeLeaveRequest(@PathVariable long id) {
//        return leaveRequestService.getAllLeaveRequestsByEmployeeId(id);
//    }


    @GetMapping("/{id}/payrolls")
    public ResponseEntity<List<PayrollDTO>> getEmployeePayroll(
            @PathVariable Long id,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate
    ){
        List<PayrollDTO> payrolls;
        if(startDate != null && endDate != null) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            payrolls = payrollService.getPayrollByEmployeeIdAndPeriodRange(id, start, end);
        } else {
            payrolls = payrollService.getPayrollsByEmployeeId(id);
        }

        return ResponseEntity.ok(payrolls);
    }
}

