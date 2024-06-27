package com.motorph.ems.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents an employee in the system, providing access to various features such as attendance management,
 * leave management, and payroll management.
 * <p>
 * Available methods:
 * <ul>
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;
    private String lastName;
    private String firstName;
    private LocalDate dob;
    private String address;
    private LocalDate hireDate;
    private Double basicSalary;

    @Transient
    private Double semiMonthlyRate ;
    @Transient
    private Double hourlyRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor_id")
    private Employee supervisor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "position_code")
    private Position position;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_code")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private EmploymentStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "government_id")
    private GovernmentId governmentId;

    @OneToMany (mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Contact> contacts;

    @OneToMany (mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Benefits> benefits;

    @OneToMany (mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LeaveBalance> leaveBalances;

    public Employee(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Double getSemiMonthlyRate() {
        return Math.round((this.basicSalary / 2) * 100.0) / 100.0;
    }

    public Double getHourlyRate() {
        return Math.round((this.basicSalary / 20 / 8) * 100.0) / 100.0;
    }

    public Double getOvertimeRate(){
        return Math.round((this.basicSalary / 20 / 8 * 1.5) * 100.0) / 100.0;
    }

    public Employee (
        Long employeeId,
        String lastName,
        String firstName,
        LocalDate dob,
        String address,
        LocalDate hireDate,
        Double basicSalary,
        Long supervisorId,
        String positionCode,
        String departmentCode,
        int statusId,
        List<Contact> contacts,
        GovernmentId governmentId,
        List<Benefits> benefits,
        List<LeaveBalance> leaveBalances
    ){

        this.employeeId = employeeId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.dob = dob;
        this.address = address;
        this.hireDate = hireDate;
        this.basicSalary = basicSalary;
        this.supervisor = new Employee(supervisorId);
        this.position = new Position(positionCode);
        this.department = new Department(departmentCode);
        this.status = new EmploymentStatus(statusId);
        this.contacts = contacts;
        this.governmentId = governmentId;
        this.benefits = benefits;
        this.leaveBalances = leaveBalances;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employee=" + employeeId +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", dob=" + dob +
                ", address='" + address + '\'' +
                ", hireDate=" + hireDate +
                ", basicSalary=" + basicSalary +
                ", semiMonthlyRate=" + semiMonthlyRate +
                ", hourlyRate=" + hourlyRate +
                '}';
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Entity @Table(name = "employee_contact")
    public static class Contact { //May add another field for area/country code

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long contact_id;

        @ManyToOne
        @JoinColumn(name = "employee_id", nullable = false)
        private Employee employee;

        private String contactNo;

        public Contact(String contactNo) {
            this.contactNo = contactNo;
        }

        public Contact(
                Long employeeId,
                String number) {

            this.employee = new Employee(employeeId);
            this.contactNo = number;
        }

        @Override
        public String toString() {
            return "Contact{" +
                    "contact_id=" + contact_id +
                    ", employeeID=" + employee.getEmployeeId() +
                    ", contactNo='" + contactNo + '\'' +
                    '}';
        }
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Entity @Table(name = "employee_government_ids")
    public static class GovernmentId {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @OneToOne
        @JoinColumn(name = "id", nullable = false)
        private Employee employee;

        private String sssNo;
        private String philHealthNo;
        private String pagIbigNo;
        private String tinNo;

        public GovernmentId(
                Long employeeId,
                String sssNo,
                String philHealthNo,
                String pagIbigNo,
                String tinNo) {
            this.employee = new Employee(employeeId);
            this.sssNo = sssNo;
            this.philHealthNo = philHealthNo;
            this.pagIbigNo = pagIbigNo;
            this.tinNo = tinNo;
        }

        @Override
        public String toString() {
            return "GovernmentIds{" +
                    ", sssNo='" + sssNo + '\'' +
                    ", philHealthNo='" + philHealthNo + '\'' +
                    ", pagIbigNo='" + pagIbigNo + '\'' +
                    ", tinNo='" + tinNo + '\'' +
                    '}';
        }
    }
}

