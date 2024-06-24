package com.motorph.ems.controller;

import com.motorph.ems.dto.LeaveBalanceDTO;
import com.motorph.ems.dto.LeaveRequestDTO;
import com.motorph.ems.service.LeaveBalanceService;
import com.motorph.ems.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "api/leaves")
public class LeaveController {
    private final LeaveRequestService requestService;
    private final LeaveBalanceService balanceService;

    @Autowired
    public LeaveController(LeaveRequestService requestService, LeaveBalanceService balanceService) {
        this.requestService = requestService;
        this.balanceService = balanceService;
    }

    @GetMapping("/requests")
    public ResponseEntity<List<LeaveRequestDTO>> getLeaveRequests() {
        List<LeaveRequestDTO> requests = requestService.getAllLeaveRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/requests/{id}")
    public ResponseEntity<LeaveRequestDTO> getLeaveRequestById(@PathVariable(value = "id") Long id) {
        LeaveRequestDTO request = requestService.getLeaveRequestById(id).orElseThrow(
                () -> new IllegalArgumentException("Leave request not found")
        );
        return ResponseEntity.ok(request);
    }

    @GetMapping("/balances/{id}")
    public ResponseEntity<LeaveBalanceDTO> getLeaveBalanceById(@PathVariable(value = "id") Long id) {
        LeaveBalanceDTO balance = balanceService.getLeaveBalanceById(id).orElseThrow(
                () -> new IllegalArgumentException("Leave balance not found")
        );
        return ResponseEntity.ok(balance);
    }

    @PatchMapping("/requests/{id}")
    public ResponseEntity<LeaveRequestDTO> updateLeaveRequest(
            @RequestBody LeaveRequestDTO leaveRequest,
            @PathVariable Long id) {
        LeaveRequestDTO request = requestService.updateLeaveRequest(id, leaveRequest);
        return ResponseEntity.ok(request);
    }

    @PatchMapping("/balances/{id}")
    public ResponseEntity<LeaveBalanceDTO> updateLeaveBalance(
            @RequestBody LeaveBalanceDTO leaveBalance,
            @PathVariable Long id) {
        LeaveBalanceDTO balance = balanceService.updateLeaveBalance(id, leaveBalance);
        return ResponseEntity.ok(balance);
    }

    @PostMapping("/requests")
    public ResponseEntity<LeaveRequestDTO> addLeaveRequest(@RequestBody LeaveRequestDTO leaveRequest) {
        LeaveRequestDTO request = requestService.addNewLeaveRequest(leaveRequest);
        return ResponseEntity.ok(request);
    }
}
