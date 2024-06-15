package com.motorph.ems.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;

    private String lastName;

    private String email;

    private String gender;

    private String age;

    private String phoneNumber;

    private String address;

    private String employmentStatus;

    private String position;

//    private String supervisor;

    private String hireDate;

    private String tinNumber;

    private String sssNumber;

    private String pagibigNumber;

    private String philhealthNumber;

//    @Column(name = "salary_structure")
//    private String salaryStructure;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendances;
}