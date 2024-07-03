package com.motorph.ems.repository;

import com.motorph.pms.model.GovernmentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GovernmentIdRepository extends
        JpaRepository<GovernmentId, Long> {
}
