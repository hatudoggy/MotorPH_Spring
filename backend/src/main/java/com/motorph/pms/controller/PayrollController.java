package com.motorph.pms.controller;

import com.motorph.pms.dto.PayrollDTO;
import com.motorph.pms.service.PayrollService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
            payrolls = payrollService.getPayrollsForPeriod(false, LocalDate.parse(start_date), LocalDate.parse(end_date));
        } else if (end_date != null) {
            payrolls = payrollService.getPayrollsByDate(false, false,LocalDate.parse(end_date));
        } else {
            payrolls = payrollService.getAllPayrolls(false);
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


    @PostMapping("generate")
    public ResponseEntity<Integer> generatePayroll(
            @RequestParam (value = "startDate", required = false) String start_date,
            @RequestParam (value = "endDate", required = false) String end_date,
            @RequestParam (value = "employeeId", required = false) Long employee_id){

        LocalDate nowDate = LocalDate.now();
        LocalDate start;
        LocalDate end;

        if (start_date != null && end_date != null) {
            try {
                start = LocalDate.parse(start_date);
                end = LocalDate.parse(end_date);
            } catch (DateTimeParseException e) {
                return ResponseEntity.badRequest().build();
            }
        } else {
            // Set start and end date to the first and last date of the previous month
            start = nowDate.minusMonths(1).withDayOfMonth(1);
            end = start.plusMonths(1).minusDays(1);
        }

        if (employee_id == null) {
            int generatedPayroll = payrollService.batchGeneratePayroll(start, end);

            return ResponseEntity.ok(generatedPayroll);
        }

        PayrollDTO payroll = payrollService.generatePayroll(start_date, end_date, employee_id);

        return ResponseEntity.ok(payroll.payrollId().intValue());
    }
}
