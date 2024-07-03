package com.motorph.pms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "position")
public class Position {
    @Id
    private String positionCode;
    private String positionName;
    private boolean isLeader;

    @ManyToOne
    @JoinColumn(name = "department_code")
    private Department department;

    public Position(String positionCode) {
        this.positionCode = positionCode;
    }

    @Override
    public String toString() {
        return "Position{" +
                "positionName='" + positionCode + '\'' +
                ", positionName='" + positionName + '\'' +
                '}';
    }
}
