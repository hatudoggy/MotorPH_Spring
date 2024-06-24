package com.motorph.ems.controller;

import com.motorph.ems.model.Attendance;
import com.motorph.ems.model.Department;
import com.motorph.ems.model.EmploymentStatus;
import com.motorph.ems.model.Position;
import com.motorph.ems.service.AttendanceService;
import com.motorph.ems.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "api/company")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/positions")
    public List<Position> getPositionList(@RequestParam(value = "department", required = false) String department) {
        if(department != null){
            return companyService.getPositionsByDepartment(department);
        } else {
            return companyService.getPositions();
        }
    }

    @GetMapping("/departments")
    public List<Department> getDepartmentList() {
        return companyService.getDepartments();
    }

    @GetMapping("/statuses")
    public List<EmploymentStatus> getStatusList() {
        return companyService.getEmploymentStatuses();
    }
}
