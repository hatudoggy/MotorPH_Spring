package com.motorph.ems.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity @Table(name = "employee_government_ids")
public class GovernmentId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;
    private String sssNo;
    private String philHealthNo;
    private String pagIbigNo;
    private String tinNo;


    @OneToOne
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonBackReference
    private Employee employee;

    public GovernmentId() {}

    public GovernmentId(
            String sssNo,
            String philHealthNo,
            String pagIbigNo,
            String tinNo
    ) {
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
