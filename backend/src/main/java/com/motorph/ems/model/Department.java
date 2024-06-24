package com.motorph.ems.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity @Table(name = "department")
public class Department {

    @Id
    private String departmentCode;
    private String departmentName;
}
