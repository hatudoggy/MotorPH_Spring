package com.motorph.ems.repository;

import com.motorph.ems.model.Benefits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BenefitsRepository extends JpaRepository<Benefits, Long> {
}
