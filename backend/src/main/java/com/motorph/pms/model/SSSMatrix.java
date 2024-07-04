package com.motorph.pms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity @Table(name = "sss_matrix")
public class SSSMatrix {

    @Id
    private int id;
    private Double minRange;
    private Double maxRange;
    private Double contribution;

    @Override
    public String toString() {
        return "SSSMatrix [matrixId=" + id + ", minRange=" + minRange + ", maxRange=" + maxRange
                + ", contribution=" + contribution + "]";
    }
}
