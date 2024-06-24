package com.motorph.ems.controller;

import com.motorph.ems.dto.DepartmentDTO;
import com.motorph.ems.dto.EmploymentStatusDTO;
import com.motorph.ems.dto.PositionDTO;
import com.motorph.ems.service.DepartmentService;
import com.motorph.ems.service.EmploymentStatusService;
import com.motorph.ems.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "api/company")
public class CompanyController {

    private final PositionService positionService;
    private final DepartmentService departmentService;
    private final EmploymentStatusService employmentStatusService;

    @Autowired
    public CompanyController(
            PositionService positionService,
            DepartmentService departmentService,
            EmploymentStatusService employmentStatusService) {
        this.positionService = positionService;
        this.departmentService = departmentService;
        this.employmentStatusService = employmentStatusService;
    }

    @GetMapping("/positions")
    public ResponseEntity<List<PositionDTO>> getPositionList(@RequestParam(value = "department", required = false) String department) {
        List<PositionDTO> positions;
        if(department != null){
            positions =  positionService.getPositionsByDepartment(department);
        } else {
            positions = positionService.getPositions();
        }

        return ResponseEntity.ok(positions);
    }

    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentDTO>> getDepartmentList() {
        return ResponseEntity.ok(departmentService.getDepartments());
    }

    @GetMapping("/statuses")
    public ResponseEntity<List<EmploymentStatusDTO>> getStatusList() {
        return ResponseEntity.ok(employmentStatusService.getEmploymentStatuses());
    }
}
