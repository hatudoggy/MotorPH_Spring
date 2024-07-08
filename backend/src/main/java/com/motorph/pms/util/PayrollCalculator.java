package com.motorph.pms.util;

import com.motorph.pms.model.*;
import com.motorph.pms.service.MatrixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PayrollCalculator {

    private final MatrixService matrixService;

    @Autowired
    public PayrollCalculator(MatrixService matrixService) {
        this.matrixService = matrixService;
    }
    
    public double calculateSSS(double grossIncome){
        SSSMatrix sssMatrix = matrixService.getSSSMatrix(grossIncome);

        return Math.round(sssMatrix.getContribution() * 100) / 100.0;
    }

    public double calculatePagIbig(double grossIncome){
        PagibigMatrix pagibigMatrix = matrixService.getPagibigMatrix(grossIncome);

        double pagIbig = Math.round(grossIncome * pagibigMatrix.getEmployeeRate() * 100) / 100.0;

        return Math.min(pagIbig, pagibigMatrix.getMaxContribution());
    }

    public double calculatePhilHealth(double grossIncome){
        PhilhealthMatrix philhealthMatrix = matrixService.getPhilhealthMatrix();

        double philHealth = Math.min(
                grossIncome * philhealthMatrix.getPremiumRate(), philhealthMatrix.getPremiumCap());

        return (double) Math.round(philHealth * philhealthMatrix.getEmployeeShare() * 100) / 100;
    }

    public double calculateTax(double grossIncome, List<Double> partialDeductions){
        double taxableIncome = grossIncome - (partialDeductions.stream().reduce(0.0, Double::sum));

        WithholdingTaxMatrix withholdingTaxMatrix = matrixService.getWithholdingTaxMatrix(taxableIncome);

        double baseTax = withholdingTaxMatrix.getBaseTax();
        double taxRate = withholdingTaxMatrix.getTaxRate();
        double excess = withholdingTaxMatrix.getExcess();

        if (taxableIncome <= withholdingTaxMatrix.getMaxRange()){
            return Math.round((baseTax + ((taxableIncome - excess) * taxRate)) * 100) / 100.0;
        }

        return 0;
    }

    public double calculateHoursWorked(List<Attendance> attendances) {
        return Math.round(attendances.stream().mapToDouble(Attendance::getTotalHours).sum() * 100) / 100.0;
    }

    public double calculateOvertimeHours(List<Attendance> attendances) {
        return Math.round(attendances.stream().mapToDouble(Attendance::getOvertimeHours).sum() * 100) / 100.0;
    }

    public int countPresent(List<Attendance> attendances) {
        return attendances.size();
    }

    public double calculateGrossIncome(double hoursWorked, double hourlyRate, double overtimeHours, double overtimeRate) {
        double basicPay = (hoursWorked - overtimeHours) * hourlyRate;
        double overtimePay = overtimeHours * overtimeRate;
        return Math.round((basicPay + overtimePay) * 100) / 100.0;
    }

    public double calculateTotalDeductions(List<Double> deductions) {
        return Math.round(deductions.stream().reduce(0.0, Double::sum) * 100) / 100.0;
    }

    public double calculateTotalBenefits(List<Double> benefits) {
        return Math.round(benefits.stream().reduce(0.0, Double::sum) * 100) / 100.0;
    }

    public double calculateGross(double hoursWorked, double hourlyRate, double overtimeHours, double overtimePay) {
        double gross = hoursWorked * hourlyRate;
        if (overtimeHours > 0){
            gross += overtimePay;
        }
        return Math.round(gross * 100) / 100.0; // Round the gross;
    }

    public double calculateNetPay(double grossIncome, double totalDeductions, double totalBenefits) {
        return (double) Math.round((grossIncome - totalDeductions + totalBenefits) * 100) / 100;
    }
}
