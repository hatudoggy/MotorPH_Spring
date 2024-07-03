package com.motorph.pms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity @Table(name = "position")
public class Position {

    @Id
    private String positionCode;
    private String departmentCode;
    private String positionName;

}
