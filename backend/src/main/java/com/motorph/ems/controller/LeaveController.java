package com.motorph.ems.controller;

import com.motorph.ems.service.LeaveBalanceService;
import com.motorph.ems.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

//TODO: Implement Leave Controller
@RestController
public class LeaveController {
    private final LeaveRequestService requestService;
    private final LeaveBalanceService balanceService;

    @Autowired
    public LeaveController(LeaveRequestService requestService, LeaveBalanceService balanceService) {
        this.requestService = requestService;
        this.balanceService = balanceService;
    }



}
