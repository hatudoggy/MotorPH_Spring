package com.motorph.pms.model;

<<<<<<< HEAD:backend/src/main/java/com/motorph/ems/model/Benefits.java
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
=======
>>>>>>> UI-integration:backend/src/main/java/com/motorph/pms/model/Benefits.java
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
@Entity @Table(name = "benefits")
public class Benefits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long benefitId;
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonBackReference
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "benefit_type_id", nullable = false)
    private BenefitType benefitType;

    public Benefits() {}

    public Benefits(
            Double amount,
            BenefitType benefitType) {
        this.amount = amount;
        this.benefitType = benefitType;
    }

    @Override
    public String toString() {
        return "Benefits{" +
                "benefitId=" + benefitId +
                ", employeeID=" + employee.getEmployeeId() +
                ", amount=" + amount +
                ", benefitType=" + benefitType +
                '}';
    }

    @Getter @Setter
    @Entity @Table(name = "benefit_type")
    public static class BenefitType {

        @Id
        private int benefitTypeId;
        private String benefit;

        public BenefitType() {
        }

        public BenefitType(String benefit) {
            this.benefit = benefit;
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

