package com.motorph.ems.repository;

import com.motorph.ems.model.GovernmentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GovernmentIdRepository extends
        JpaRepository<GovernmentId, Long> {
}
