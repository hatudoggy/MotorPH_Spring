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
@Table(name = "benefits")
public class Benefits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long benefitId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    private Double amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "benefit_type_id", nullable = false)
    private BenefitType benefitType;

    public Benefits(int benefitTypeId, Double amount) {
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

        public BenefitType(int benefitTypeId) {
            this.benefitTypeId = benefitTypeId;
        }

        @Override
        public String toString() {
            return "BenefitType{" +
                    "benefitTypeId=" + benefitTypeId +
                    ", benefit='" + benefit + '\'' +
                    '}';
        }
    }
}
