package com.motorph.ems.controller;

import com.motorph.ems.model.Attendance;
import com.motorph.ems.model.LeaveBalance;
import com.motorph.ems.model.LeaveRequest;
import com.motorph.ems.service.LeaveBalanceService;
import com.motorph.ems.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<LeaveRequest> getLeaveRequests() {
        return requestService.getAllLeaveRequests();
    }

    @GetMapping("/requests/{id}")
    public LeaveRequest getLeaveRequestById(@PathVariable(value = "id") Long id) {
        return requestService.getLeaveRequestById(id);
    }

    @GetMapping("/balances/{id}")
    public LeaveBalance getLeaveBalanceById(@PathVariable(value = "id") Long id) {
        return balanceService.getLeaveBalanceById(id);
    }

    @PatchMapping("/requests/{id}")
    public void updateLeaveRequest(
            @RequestBody LeaveRequest leaveRequest
    ) {
        requestService.updateLeaveRequest(leaveRequest);
    }

    @PatchMapping("/balances/{id}")
    public void updateLeaveBalance(
            @RequestBody LeaveBalance leaveBalance
    ) {
        balanceService.updateLeaveBalance(leaveBalance);
    }

    @PostMapping("/requests")
    public void addLeaveRequest(@RequestBody LeaveRequest leaveRequest) {
        requestService.addNewLeaveRequest(leaveRequest);
    }

}
