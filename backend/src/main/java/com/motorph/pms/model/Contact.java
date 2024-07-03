package com.motorph.ems.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity @Table(name = "employee_contact")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contact_id;
    private String contactNo;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonBackReference
    private Employee employee;

    public Contact() {}

    public Contact(String contactNo) {
        this.contactNo = contactNo;
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

