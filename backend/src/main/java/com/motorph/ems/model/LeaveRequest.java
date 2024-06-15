package com.motorph.ems.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@Entity @Table(name = "leave_request")
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveRequestId;

    private LocalDate requestDate;
    private Long employeeId;
    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "leave_status_id")
    @JsonManagedReference
    private LeaveStatus status;

    private String reason;

    public LeaveRequest() {}

    public LeaveRequest(
            LocalDate requestDate,
            Long employeeId,
            LocalDate startDate,
            LocalDate endDate,
            LeaveStatus status,
            String reason) {
        this.requestDate = requestDate;
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "LeaveRequest{" +
                "leaveRequestId=" + leaveRequestId +
                ", requestDate=" + requestDate +
                ", employeeId=" + employeeId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }

    @Getter @Setter
    @Entity @Table(name = "leave_status")
    public static class LeaveStatus {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int leaveStatusId;

        private String status;

        public LeaveStatus() {}

        public LeaveStatus(String status) {
            this.status = status;
        }

        @Getter
        public enum Status {
            APPROVED("Approved"),
            PENDING("Pending"),
            REJECTED("Rejected");

            private final String status;

            Status(String status) {
                this.status = status;
            }

            @Override
            public String toString() {
                return status;
            }
        }
    }
}
