package com.motorph.pms.repository;

import com.motorph.pms.model.Benefits.BenefitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BenefitTypeRepository extends JpaRepository<BenefitType, Integer> {}
