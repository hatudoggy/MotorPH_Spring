package com.motorph.ems.controller;

import com.motorph.ems.dto.*;
import com.motorph.ems.service.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "api/company")
public class CompanyController {

    private final EmployeeService employeeService;
    private final PositionService positionService;
    private final DepartmentService departmentService;
    private final EmploymentStatusService employmentStatusService;
    private final LeaveBalanceService leaveTypeService;
    private final LeaveRequestService leaveStatusService;
    private final BenefitsService benefitsService;

    @Autowired
    public CompanyController(
            EmployeeService employeeService,
            PositionService positionService,
            DepartmentService departmentService,
            EmploymentStatusService employmentStatusService,
            LeaveBalanceService leaveTypeService,
            LeaveRequestService leaveStatusService,
            BenefitsService benefitsService) {
        this.employeeService = employeeService;
        this.positionService = positionService;
        this.departmentService = departmentService;
        this.employmentStatusService = employmentStatusService;
        this.leaveTypeService = leaveTypeService;
        this.leaveStatusService = leaveStatusService;
        this.benefitsService = benefitsService;
    }

    @GetMapping("/supervisors/all")
    public ResponseEntity<List<SupervisorDTO>> getSupervisorList() {
        return ResponseEntity.ok(employeeService.getSupervisors());
    }

    @GetMapping("/positions/all")
    public ResponseEntity<List<PositionDTO>> getPositionList(@RequestParam(value = "department", required = false) String department) {
        List<PositionDTO> positions;
        if(department != null){
            positions =  positionService.getPositionsByDepartment(department);
        } else {
            positions = positionService.getPositions();
        }

        return ResponseEntity.ok(positions);
    }

    @GetMapping("/positions/{id}")
    public ResponseEntity<PositionDTO> getPositionByPositionCoded(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(positionService.getPositionByPositionCode(id).orElseThrow(
                () -> new EntityNotFoundException("Position " + id + " not found")
        ));
    }

    @GetMapping("/departments/all")
    public ResponseEntity<List<DepartmentDTO>> getDepartmentList() {
        return ResponseEntity.ok(departmentService.getDepartments());
    }

    @GetMapping("/departments/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentByDepartmentCoded(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(departmentService.getDepartmentByDepartmentCode(id).orElseThrow(
                () -> new EntityNotFoundException("Department " + id + " not found")
        ));
    }

    @GetMapping("/statuses/all")
    public ResponseEntity<List<EmploymentStatusDTO>> getStatusList() {
        return ResponseEntity.ok(employmentStatusService.getEmploymentStatuses());
    }

    @GetMapping("/statuses/{id}")
    public ResponseEntity<EmploymentStatusDTO> getStatusByStatusId(@PathVariable(value = "id") int id) {
        return ResponseEntity.ok(employmentStatusService.getEmploymentStatusById(id).orElseThrow(
                () -> new EntityNotFoundException("Employment status not found")
        ));
    }

    @GetMapping("/leave/types/all")
    public ResponseEntity<List<LeaveTypeDTO>> getLeaveTypeList() {
        return ResponseEntity.ok(leaveTypeService.getAllLeaveTypes());
    }

    @GetMapping("/leave/types/{id}")
    public ResponseEntity<LeaveTypeDTO> getLeaveTypeById(@PathVariable(value = "id") int id) {
        return ResponseEntity.ok(leaveTypeService.getLeaveTypeById(id).orElseThrow(
                () -> new EntityNotFoundException("Leave type not found")
        ));
    }

    @GetMapping("/leave/status/all")
    public ResponseEntity<List<LeaveStatusDTO>> getLeaveStatusList() {
        return ResponseEntity.ok(leaveStatusService.getAllLeaveStatus());
    }

    @GetMapping("/leave/status/{id}")
    public ResponseEntity<LeaveStatusDTO> getLeaveStatusById(@PathVariable(value = "id") int id) {
        return ResponseEntity.ok(leaveStatusService.getLeaveStatusById(id).orElseThrow(
                () -> new EntityNotFoundException("Leave status not found")
        ));
    }

    @GetMapping("/benefit/types/all")
    public ResponseEntity<List<BenefitTypeDTO>> getBenefitTypesList(){
        return ResponseEntity.ok(benefitsService.getAllBenefitTypes());
    }
}
