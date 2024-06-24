package com.motorph.ems.controller;

import com.motorph.ems.dto.PayrollDTO;
import com.motorph.ems.service.PayrollService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public ResponseEntity<List<PayrollDTO>> getPayrolls(
            @RequestParam(value = "start_date", required = false) String start_date,
            @RequestParam(value = "end_date", required = false) String end_date
    ) {
        List<PayrollDTO> payrolls;

        if(start_date != null && !start_date.isEmpty() && end_date != null && !end_date.isEmpty()) {
            payrolls = payrollService.getPayrollsForPeriod(LocalDate.parse(start_date), LocalDate.parse(end_date));
        } else {
            payrolls = payrollService.getAllPayrolls();
        }

        return ResponseEntity.ok(payrolls);
    }

    @GetMapping("/years")
    public ResponseEntity<List<Integer>> getDistinctYears() {
        List<Integer> years = payrollService.getDistinctYears();

        return ResponseEntity.ok(years);
    }

    @GetMapping("/years/{year}")
    public ResponseEntity<List<LocalDate>> getDistinctMonthsByYear(@PathVariable(value = "year") int year) {
        List<LocalDate> months = payrollService.getDistinctMonthsByYear(year);

        return ResponseEntity.ok(months);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PayrollDTO> getPayrollById(@PathVariable(value = "id") Long id) {
        PayrollDTO payroll = payrollService.getPayrollById(id).orElseThrow(
                () -> new EntityNotFoundException("Payroll with id " + id + " not found")
        );

        return ResponseEntity.ok(payroll);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<PayrollDTO> updatePayroll(
            @RequestBody PayrollDTO payroll,
            @PathVariable Long id) {
        PayrollDTO updatedPayroll = payrollService.updatePayroll(id, payroll);

        return ResponseEntity.ok(updatedPayroll);
    }


    @PostMapping
    public ResponseEntity<PayrollDTO> addPayroll(@RequestBody PayrollDTO payroll) {
        PayrollDTO payrollDTO = payrollService.addNewPayroll(payroll);

        return ResponseEntity.ok(payrollDTO);
    }
}
