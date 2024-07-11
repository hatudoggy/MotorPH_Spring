package com.motorph.pms.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.motorph.pms.model.LeaveBalance.LeaveType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity @Table(name = "leave_request")
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveRequestId;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonManagedReference
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "leave_type_id")
    @JsonManagedReference
    private LeaveType leaveType;

    private LocalDate requestDate;
    private LocalDate startDate;
    private LocalDate endDate;

    @Transient
    private int daysRequested;

    @ManyToOne
    @JoinColumn(name = "leave_status_id")
    @JsonManagedReference
    private LeaveStatus status;

    private String reason;

    public LeaveRequest(
            Long employeeId,
            int leaveTypeId,
            LocalDate requestDate,
            LocalDate startDate,
            LocalDate endDate,
            int statusId,
            String reason){

        this.employee = new Employee(employeeId);
        this.leaveType = new LeaveType(leaveTypeId);
        this.requestDate = requestDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = new LeaveStatus(statusId);
        this.reason = reason;
    }

    public int getDaysRequested() {
        return (int) (endDate.toEpochDay() - startDate.toEpochDay());
    }

    @Override
    public String toString() {
        return "LeaveRequest{" +
                "leaveRequestId=" + leaveRequestId +
                ", requestDate=" + requestDate +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", statusName='" + status + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity @Table(name = "leave_status")
    public static class LeaveStatus {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int leaveStatusId;

        private String statusName;

        public LeaveStatus(String statusName) {
            this.statusName = statusName;
        }

        public LeaveStatus(int statusId) {
            this.leaveStatusId = statusId;
        }
    }
}
