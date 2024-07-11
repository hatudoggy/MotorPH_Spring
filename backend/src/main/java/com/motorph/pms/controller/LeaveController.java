package com.motorph.pms.controller;

import com.motorph.pms.dto.LeaveBalanceDTO;
import com.motorph.pms.dto.LeaveRequestDTO;
import com.motorph.pms.service.LeaveBalanceService;
import com.motorph.pms.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @GetMapping("/requests/range")
    public ResponseEntity<List<LeaveRequestDTO>> getLeaveRequestsByDateRange(
            @RequestParam(value = "requestDateAfter", required = false) String requestDateAfter,
            @RequestParam(value = "requestDateBefore", required = false) String requestDateBefore
    ) {
        List<LeaveRequestDTO> requests;

        if (requestDateBefore == null || requestDateAfter == null) {
            // Set start and end date to the first and last date of the this month
            LocalDate nowDate = LocalDate.now();
            LocalDate afterDate = LocalDate.of(nowDate.getYear(), nowDate.getMonth(), 1);
            LocalDate beforeDate = afterDate.plusMonths(1).minusDays(1);
            requests = requestService.getLeaveRequestsByRequestDateRange(afterDate, beforeDate);
        } else {
            requests = requestService.getLeaveRequestsByRequestDateRange(LocalDate.parse(requestDateAfter), LocalDate.parse(requestDateBefore));
        }

        return ResponseEntity.ok(requests);
    }

    @PatchMapping("/requests/{id}")
    public ResponseEntity<LeaveRequestDTO> updateLeaveRequest(
            @RequestBody LeaveRequestDTO leaveRequest,
            @PathVariable Long id) {
        LeaveRequestDTO request = requestService.updateLeaveRequest(leaveRequest);
        return ResponseEntity.ok(request);
    }

    @PostMapping("/requests/apply")
    public ResponseEntity<LeaveRequestDTO> applyLeave(@RequestBody LeaveRequestDTO leaveRequest) {

        if(leaveRequest.startDate().isAfter(leaveRequest.endDate())) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(requestService.addNewLeaveRequest(leaveRequest));
    }

    @PutMapping("/requests/update")
    public ResponseEntity<LeaveRequestDTO> updateLeaveRequestStatus(
            @RequestBody LeaveRequestDTO leaveRequest) {
        LeaveRequestDTO request = requestService.updateLeaveRequest(leaveRequest);
        return ResponseEntity.ok(request);
    }

    @PatchMapping("/balances/{id}")
    public ResponseEntity<LeaveBalanceDTO> updateLeaveBalance(
            @RequestBody LeaveBalanceDTO leaveBalance,
            @PathVariable Long id) {
        LeaveBalanceDTO balance = balanceService.updateLeaveBalance(id, leaveBalance);
        return ResponseEntity.ok(balance);
    }

//    @PostMapping("/requests")
//    public ResponseEntity<LeaveRequestDTO> addLeaveRequest(@RequestBody LeaveRequestDTO leaveRequest) {
//        LeaveRequestDTO request = requestService.addNewLeaveRequest(leaveRequest);
//        return ResponseEntity.ok(request);
//    }

}
