package com.motorph.ems.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@Entity @Table(name = "employee_job_details")
public class Employment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;
    @OneToOne
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnore
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "supervisor_id")
    @JsonIgnore
    private Employee supervisor;

    @ManyToOne
    @JoinColumn(name = "department_code", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Department department;

    @ManyToOne
    @JoinColumn(name = "position_code", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Position position;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private EmploymentStatus status;

    private LocalDate hireDate;
    private Double basicSalary;

    @Transient
    private Double semiMonthlyRate ;
    @Transient
    private Double hourlyRate;

    public Employment() {}

    public Employment(
            Department department,
            Position position,
            Employee supervisor,
            EmploymentStatus status,
            Double basicSalary
    ) {
        this.department = department;
        this.position = position;
        this.supervisor = supervisor;
        this.status = status;
        this.basicSalary = basicSalary;
    }

    public Double getSemiMonthlyRate() {
        return Math.round((this.basicSalary / 2) * 100.0) / 100.0;
    }

    public Double getHourlyRate() {
        return Math.round((this.basicSalary / 21 / 8) * 100.0) / 100.0;
    }

    @JsonProperty("supervisor")
    public Supervisor getSupervisor() {
        if (supervisor != null) {
            return new Supervisor(supervisor.getEmployeeId(), supervisor.getFirstName(), supervisor.getLastName());
        }
        return null;
    }

    @Override
    public String toString() {
        return "Employment{" +
                "employeeId=" + employeeId +
                ", department='" + department + '\'' +
                ", position='" + position + '\'' +
                ", supervisor='" + supervisor.getLastName() + ", " + supervisor.getFirstName() + '\'' +
                ", status='" + status + '\'' +
                ", basicSalary=" + basicSalary +
                ", semiMonthlyRate=" + semiMonthlyRate +
                ", hourlyRate=" + hourlyRate +
                '}';
    }


    public static class Supervisor {
        private Long id;
        private String firstName;
        private String lastName;

        public Supervisor(Long id, String firstName, String lastName) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
        }

        @JsonProperty("id")
        public Long getId() {
            return id;
        }

        @JsonProperty("firstName")
        public String getFirstName() {
            return firstName;
        }

        @JsonProperty("lastName")
        public String getLastName() {
            return lastName;
        }
    }


    @Getter @Setter
    @Entity @Table(name = "employment_status")
    public static class EmploymentStatus {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int statusId;
        private String status;

        public EmploymentStatus() {
        }

        public EmploymentStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "EmploymentStatus{" +
                    "statusId=" + statusId +
                    ", status='" + status + '\'' +
                    '}';
        }
    }

    @Getter @Setter
    @Entity @Table(name = "department")
    public static class Department {
        @Id
        private String departmentCode;
        private String departmentName;

        @Override
        public String toString() {
            return "Department{" +
                    "departmentCode='" + departmentCode + '\'' +
                    ", departmentName='" + departmentName + '\'' +
                    '}';
        }
    }

    @Getter @Setter
    @Entity @Table(name = "position")
    public static class Position {
        @Id
        private String positionCode;
        private String positionName;

        @Override
        public String toString() {
            return "Position{" +
                    "positionCode='" + positionCode + '\'' +
                    ", positionName='" + positionName + '\'' +
                    '}';
        }
    }
}
