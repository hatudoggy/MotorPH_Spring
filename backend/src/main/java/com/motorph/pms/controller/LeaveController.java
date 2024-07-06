package com.motorph.pms.controller;

import com.motorph.pms.dto.LeaveBalanceDTO;
import com.motorph.pms.dto.LeaveRequestDTO;
import com.motorph.pms.service.LeaveBalanceService;
import com.motorph.pms.service.LeaveRequestService;
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
