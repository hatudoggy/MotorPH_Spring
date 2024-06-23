package com.motorph.ems.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "benefits")
public class Benefits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long benefitId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonManagedReference
    private Employee employee;

    private Double amount;

    @ManyToOne
    @JoinColumn(name = "benefit_type_id", nullable = false)
    @JsonManagedReference
    private BenefitType benefitType;

    public Benefits(Long employeeId, int benefitTypeId, Double amount) {
        this.employee = new Employee(employeeId);
        this.amount = amount;
        this.benefitType = new BenefitType(benefitTypeId);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Entity
    @Table(name = "benefit_type")
    public static class BenefitType {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int benefitTypeId;
        private String benefit;

        public BenefitType(String benefit) {
            this.benefit = benefit;
        }

        public BenefitType(int benefitTypeId) {}

        @Override
        public String toString() {
            return "BenefitType{" +
                    "benefitTypeId=" + benefitTypeId +
                    ", benefit='" + benefit + '\'' +
                    '}';
        }
    }
}
