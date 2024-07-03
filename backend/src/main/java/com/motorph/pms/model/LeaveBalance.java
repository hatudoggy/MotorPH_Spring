package com.motorph.pms.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name= "leave_balance")
public class LeaveBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveBalanceId;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "leave_type_id")
    @JsonManagedReference
    private LeaveType leaveType;

    private int balance;

    public LeaveBalance(
            Long leaveBalanceId,
            LeaveType leaveType,
            int balance) {
        this.leaveBalanceId = leaveBalanceId;
        this.leaveType = leaveType;
        this.balance = balance;
    }

    public LeaveBalance(
            Long employeeId,
            int leaveTypeId,
            int balance) {
        this.employee = new Employee(employeeId);
        this.leaveType = new LeaveType(leaveTypeId);
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "LeaveBalance{" +
                "leaveBalanceId=" + leaveBalanceId +
                ", supervisor=" + employee.getEmployeeId() +
                ", leaveType=" + leaveType +
                ", balance=" + balance +
                '}';
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Entity
    @Table(name= "leave_type")
    public static class LeaveType {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int leaveTypeId;
        private String type;

        public LeaveType(int leaveTypeId) {
            this.leaveTypeId = leaveTypeId;
        }

        public LeaveType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }
    }
}
