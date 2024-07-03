package com.motorph.pms.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity @Table(name= "leave_balance")
public class LeaveBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveBalanceId;

    private Long employeeId;

    @ManyToOne
    @JoinColumn(name = "leave_type_id")
    @JsonManagedReference
    private LeaveType leaveType;

    private Integer balance;

    public LeaveBalance() {}

    public LeaveBalance(
            Long employeeId,
            LeaveType leaveType,
            Integer balance) {
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "LeaveBalance{" +
                "leaveBalanceId=" + leaveBalanceId +
                ", employeeId=" + employeeId +
                ", leaveType=" + leaveType +
                ", balance=" + balance +
                '}';
    }

    @Getter @Setter
    @Entity @Table(name= "leave_type")
    public static class LeaveType {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int leaveTypeId;
        private String type;

        @Override
        public String toString() {
            return type;
        }
    }
}
