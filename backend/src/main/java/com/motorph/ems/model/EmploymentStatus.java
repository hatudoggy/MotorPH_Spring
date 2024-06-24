package com.motorph.ems.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity @Table(name = "employment_status")
public class EmploymentStatus {


    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int statusId;
    private String status;


    public EmploymentStatus() {
    }
}
