package com.motorph.pms.controller;

<<<<<<< HEAD:backend/src/main/java/com/motorph/ems/controller/PayrollController.java
import com.motorph.ems.dto.MonthlyPayrollReportDTO;
import com.motorph.ems.dto.PayrollDTO;
import com.motorph.ems.model.LeaveBalance;
import com.motorph.ems.model.LeaveRequest;
import com.motorph.ems.model.Payroll;
import com.motorph.ems.service.LeaveBalanceService;
import com.motorph.ems.service.LeaveRequestService;
import com.motorph.ems.service.PayrollService;
=======
import com.motorph.pms.dto.PayrollDTO;
import com.motorph.pms.service.PayrollService;
import jakarta.persistence.EntityNotFoundException;
>>>>>>> UI-integration:backend/src/main/java/com/motorph/pms/controller/PayrollController.java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
<<<<<<< HEAD:backend/src/main/java/com/motorph/ems/controller/PayrollController.java
import java.util.Map;
=======
>>>>>>> UI-integration:backend/src/main/java/com/motorph/pms/controller/PayrollController.java

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
    public List<PayrollDTO> getPayrolls(
            @RequestParam(value = "date", required = false) String date
    ) {
        if(date != null && !date.isEmpty()){
            return payrollService.getAllPayrollsDTOByDate(LocalDate.parse(date));
        } else {
            return payrollService.getAllPayrollsDTO();
        }
    }

    @GetMapping("/years")
    public List<Integer> getDistinctYears() {
        return payrollService.getDistinctYear();
    }

    @GetMapping("/years/{year}")
    public List<LocalDate> getDistinctMonthsByYear(@PathVariable(value = "year") int year) {
        return payrollService.getDistinctMonthsByYear(year);
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

    @PostMapping("/batchGenerate")
    public void addPayroll(@RequestBody Map<String, Object> requestBody) {
        String startDate = (String) requestBody.get("startDate");
        String endDate = (String) requestBody.get("endDate");
        payrollService.batchGeneratePayroll(LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    @GetMapping("/reports/monthlyTotal")
    public List<MonthlyPayrollReportDTO> getMonthlyTotalReport(
            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "endDate") String endDate
    ) {
        return payrollService.getMonthlyReport(LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

}
