package com.motorph.ems.controller;


import com.motorph.ems.model.Benefits;
import com.motorph.ems.model.Employee;
import com.motorph.ems.model.Employment;
import com.motorph.ems.model.GovernmentId;
import com.motorph.ems.service.BenefitsService;
import com.motorph.ems.service.EmployeeService;
import com.motorph.ems.service.EmploymentService;
import com.motorph.ems.service.GovernmentIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final BenefitsService benefitsService;
    private final EmploymentService employmentService;
    private final GovernmentIdService governmentIdService;

    @Autowired
    public EmployeeController(
            EmployeeService employeeService,
            @Qualifier("employeeServiceImpl")
            BenefitsService benefitsService,
            @Qualifier("employeeServiceImpl")
            EmploymentService employmentService,
            @Qualifier("employeeServiceImpl")
            GovernmentIdService governmentIdService
    ){
        this.employeeService = employeeService;
        this.benefitsService = benefitsService;
        this.employmentService = employmentService;
        this.governmentIdService = governmentIdService;
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
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeID) {
        Employee employee = employeeService.getEmployeeById(employeeID);
        return ResponseEntity.ok().body(employee);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable(value = "id") Long employeeID,
            @RequestBody Employee employeeDetails
    ) {
        Employee updatedEmployee = employeeService.updateEmployee(employeeDetails);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable(value = "id") Long employeeID) {
        if (employeeService.getEmployeeById(employeeID) == null) {
            return ResponseEntity.notFound().build();
        }
        employeeService.deleteEmployee(employeeID);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employment/{id}")
    public Employment getEmploymentByEmployeeId(@PathVariable(value = "id") Long employeeID) {
        return employmentService.getEmploymentByEmployeeId(employeeID);
    }

    @GetMapping("/governmentId/{id}")
    public List<GovernmentId> getGovernmentIdByEmployeeId(@PathVariable(value = "id") Long employeeID) {
        return governmentIdService.getAllGovernmentIdByEmployeeId(employeeID);
    }
    @GetMapping("/benefits/{id}")
    public List<Benefits> getBenefitsByEmployeeId(@PathVariable(value = "id") Long employeeID) {
        return benefitsService.getAllBenefitsByEmployeeId(employeeID);
    }
}
