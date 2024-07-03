package com.motorph.ems.repository;

import com.motorph.ems.model.Benefits.BenefitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BenefitTypeRepository extends JpaRepository<BenefitType, Integer> {

    Optional<BenefitType> findBenefitTypeByBenefit(String benefit);
}
