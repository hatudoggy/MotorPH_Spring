package com.motorph.ems.controller;


import com.motorph.ems.model.*;
import com.motorph.ems.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

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

    @PostMapping("/register")
    public void registerEmployee(@RequestBody Employee employee) {
        employeeService.addNewEmployee(employee);
    }

    @GetMapping()
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
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

    @GetMapping("/{id}/governmentId")
    public List<GovernmentId> getGovernmentIdByEmployeeId(@PathVariable(value = "id") Long employeeID) {
        return governmentIdService.getAllGovernmentIdByEmployeeId(employeeID);
    }
    @GetMapping("/{id}/benefits")
    public List<Benefits> getBenefitsByEmployeeId(@PathVariable(value = "id") Long employeeID) {
        return benefitsService.getAllBenefitsByEmployeeId(employeeID);
    }

    @GetMapping("/{id}/attendance")
    public List<Attendance> getEmployeeAttendance(@PathVariable long id) {
        return attendanceService.getAllAttendancesByEmployeeId(id);
    }

    @GetMapping("/{id}/leave-balance")
    public List<LeaveBalance> getEmployeeLeaveBalance(@PathVariable long id) {
        return leaveBalanceService.getLeaveBalancesByEmployeeId(id);
    }

    @GetMapping("/{id}/leave-request")
    public List<LeaveRequest> getEmployeeLeaveRequest(@PathVariable long id) {
        return leaveRequestService.getAllLeaveRequestsByEmployeeId(id);
    }

    @GetMapping("/{id}/payroll")
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
