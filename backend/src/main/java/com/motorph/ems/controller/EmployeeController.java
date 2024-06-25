package com.motorph.ems.controller;


import com.motorph.ems.dto.BenefitDTO;
import com.motorph.ems.dto.EmployeeDTO;
import com.motorph.ems.service.BenefitsService;
import com.motorph.ems.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final BenefitsService benefitsService;

    @Autowired
    public EmployeeController(
            EmployeeService employeeService,
            BenefitsService benefitsService
    ){
        this.employeeService = employeeService;
        this.benefitsService = benefitsService;
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
        EmployeeDTO employee = employeeService.getEmployeeById(employeeID).orElse(null);
        return ResponseEntity.ok().body(employee);
    }

    @PutMapping("/update/{employeeID}")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @PathVariable(value = "employeeID") Long employeeID,
            @RequestBody EmployeeDTO employeeDetails
    ) {
        EmployeeDTO updatedEmployee = employeeService.updateEmployee(employeeID,employeeDetails);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable(value = "id") Long employeeID) {
        if (employeeService.getEmployeeById(employeeID).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        employeeService.deleteEmployee(employeeID);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all/department/{departmentName}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByDepartment(@PathVariable(value = "departmentName") String departmentName) {
        List<EmployeeDTO> employeeList = employeeService.getEmployeesByDepartment(departmentName);
        return ResponseEntity.ok(employeeList);
    }

    @GetMapping("/all/position/{positionName}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByPosition(@PathVariable(value = "positionName") String positionName) {
        List<EmployeeDTO> employeeList = employeeService.getEmployeesByPosition(positionName);
        return ResponseEntity.ok(employeeList);
    }

    @GetMapping("/all/status/{statusName}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByStatus(@PathVariable(value = "statusName") String statusName) {
        List<EmployeeDTO> employeeList = employeeService.getEmployeesByStatus(statusName);
        return ResponseEntity.ok(employeeList);
    }

    @GetMapping("/benefits/{id}")
    public ResponseEntity<List<BenefitDTO>> getBenefitsByEmployeeId(@PathVariable(value = "id") Long employeeID) {
        List<BenefitDTO> benefits = benefitsService.getBenefitsByEmployeeId(employeeID);
        return ResponseEntity.ok(benefits);
    }


}
