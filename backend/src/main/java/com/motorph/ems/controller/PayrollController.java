package com.motorph.ems.controller;

import com.motorph.ems.dto.PayrollDTO;
import com.motorph.ems.model.LeaveBalance;
import com.motorph.ems.model.LeaveRequest;
import com.motorph.ems.model.Payroll;
import com.motorph.ems.service.LeaveBalanceService;
import com.motorph.ems.service.LeaveRequestService;
import com.motorph.ems.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "api/payrolls")
public class PayrollController {
    private final PayrollService payrollService;

    @Autowired
    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    @GetMapping
    public List<PayrollDTO> getPayrolls() {
        return payrollService.getAllPayrollsDTO();
    }

    @GetMapping("/{id}")
    public Payroll getPayrollById(@PathVariable(value = "id") Long id) {
        return payrollService.getPayrollById(id);
    }


    @PatchMapping("/{id}")
    public void updatePayroll(
            @RequestBody Payroll payroll
    ) {
        payrollService.updatePayroll(payroll);
    }


    @PostMapping
    public void addPayroll(@RequestBody Payroll payroll) {
        payrollService.addNewPayroll(payroll);
    }

}
