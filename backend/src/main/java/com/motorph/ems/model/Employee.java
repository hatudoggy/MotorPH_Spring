package com.motorph.ems.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

@Getter @Setter
@Entity @Table(name = "employee")
@SuppressWarnings("unused")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;
    private String lastName;
    private String firstName;
    private LocalDate dob;
    private String address;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Contact> contacts;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Benefits> benefits;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Employment employment;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private GovernmentId governmentId;

    public Employee() {}

    public Employee(
            String lastName,
            String firstName,
            LocalDate dob,
            String address
    ) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.dob = dob;
        this.address = address;
    }

    public Employee(
            String lastName,
            String firstName,
            LocalDate dob,
            String address,
            List<Contact> contacts,
            List<Benefits> benefits,
            Employment employment,
            GovernmentId governmentId
    ) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.dob = dob;
        this.address = address;
        this.contacts = contacts;
        this.benefits = benefits;
        this.employment = employment;
        this.governmentId = governmentId;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", dob=" + dob +
                ", address='" + address + '\'' +
                '}';
    }

}