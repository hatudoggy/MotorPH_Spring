package com.motorph.ems.controller;


import com.motorph.ems.dto.*;
import com.motorph.ems.service.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@CrossOrigin
@RestController
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

        List<BenefitDTO> benefit = benefitsService.getBenefitsByEmployeeId(employeeID);
        List<LeaveBalanceDTO> leaveBalance = leaveBalanceService.getLeaveBalancesByEmployeeId(employeeID);

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
                .benefits(benefit)
                .leaveBalances(leaveBalance)
                .build();

        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/update/{employeeID}")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @PathVariable(value = "employeeID") Long employeeID,
            @RequestBody EmployeeDTO employeeDetails
    ) {
        EmployeeDTO updatedEmployee = employeeService.updateEmployee(employeeID,employeeDetails);
        return ResponseEntity.ok(updatedEmployee);
    }



    @GetMapping("/{id}/attendances")
    public ResponseEntity<List<AttendanceDTO>> getEmployeeAttendance(
            @PathVariable Long id,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate
    ) {
        List<AttendanceDTO> attendanceList;
        try {
            if (startDate != null && endDate != null) {
                LocalDate start = LocalDate.parse(startDate);
                LocalDate end = LocalDate.parse(endDate);
                attendanceList = attendanceService.getAllAttendanceByEmployeeIdAndDateRange(id, start, end);
                return ResponseEntity.ok(attendanceList);
            } else if (startDate != null) {
                LocalDate start = LocalDate.parse(startDate);
                AttendanceDTO attendance = attendanceService.getAttendanceByEmployeeIdAndDate(id, start).orElse(null);
                assert attendance != null;
                attendanceList = List.of(attendance);
                return ResponseEntity.ok(attendanceList);
            }
            return ResponseEntity.ok(attendanceService.getAllByEmployeeId(id));
        } catch (DateTimeParseException e) {
            // Handle the case where the date is not properly formatted
            throw new IllegalArgumentException("Invalid date format: " + startDate + " " + endDate);
        } catch (Exception e) {
            // Log the exception and return an appropriate response
            e.printStackTrace();
            throw new IllegalArgumentException("Invalid date format: " + startDate + " " + endDate);
        }
    }

    @GetMapping("/{id}/attendances/summary")
    public ResponseEntity<AttendanceSummaryDTO> getEmployeeAttendanceSummary(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(attendanceService.getAttendanceSummaryByEmployeeId(id));
    }

    @PostMapping("/{id}/attendances/timeIn")
    public ResponseEntity<AttendanceDTO> employeeTimeIn(@PathVariable Long id) {
        AttendanceDTO attendanceToday = AttendanceDTO.builder()
                .employee(employeeService.getEmployeeById(id, false).orElseThrow(
                        () -> new EntityNotFoundException("Employee not found")
                ))
                .date(LocalDate.now())
                .timeIn(LocalTime.now())
                .build();

        return ResponseEntity.ok(attendanceService.addNewAttendance(attendanceToday));
    }

    @PostMapping("/{id}/attendances/timeOut")
    public ResponseEntity<AttendanceDTO> employeeTimeOut(@PathVariable Long id) {
        AttendanceDTO attendanceToday = attendanceService.getAttendanceByEmployeeIdAndDate(id, LocalDate.now()).orElseThrow(
                () -> new EntityNotFoundException("You haven't clocked in yet")
        );

        if (attendanceToday.timeOut() != null) {
            throw new IllegalArgumentException("You have already clocked out");
        }

        AttendanceDTO updatedAttendance = AttendanceDTO.builder()
                .attendanceId(attendanceToday.attendanceId())
                .employee(attendanceToday.employee())
                .timeIn(attendanceToday.timeIn())
                .timeOut(LocalTime.now())
                .build();

        return ResponseEntity.ok(attendanceService.updateAttendance(attendanceToday.attendanceId(), updatedAttendance));
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
