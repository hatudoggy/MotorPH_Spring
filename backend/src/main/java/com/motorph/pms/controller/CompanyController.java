package com.motorph.pms.controller;

import com.motorph.pms.dto.*;
import com.motorph.pms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "api/company")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(
            CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/supervisors/all")
    public ResponseEntity<List<SupervisorDTO>> getSupervisorList() {
        return ResponseEntity.ok(companyService.getSupervisors());
    }

    @GetMapping("/positions/all")
    public ResponseEntity<List<PositionDTO>> getPositionList() {
        List<PositionDTO> positions;
            positions = companyService.getPositions();
        return ResponseEntity.ok(positions);
    }

    @GetMapping("/departments/all")
    public ResponseEntity<List<DepartmentDTO>> getDepartmentList() {
        return ResponseEntity.ok(companyService.getDepartments());
    }

    @GetMapping("/statuses/all")
    public ResponseEntity<List<EmploymentStatusDTO>> getStatusList() {
        return ResponseEntity.ok(companyService.getEmploymentStatuses());
    }

    @GetMapping("/leave/types/all")
    public ResponseEntity<List<LeaveTypeDTO>> getLeaveTypeList() {
        return ResponseEntity.ok(companyService.getLeaveTypes());
    }

    @GetMapping("/leave/status/all")
    public ResponseEntity<List<LeaveStatusDTO>> getLeaveStatusList() {
        return ResponseEntity.ok(companyService.getLeaveStatuses());
    }

    @GetMapping("/benefit/types/all")
    public ResponseEntity<List<BenefitTypeDTO>> getBenefitTypesList(){
        return ResponseEntity.ok(companyService.getBenefitTypes());
    }
}
