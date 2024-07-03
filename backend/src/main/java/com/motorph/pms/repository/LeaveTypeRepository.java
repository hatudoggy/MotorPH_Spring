package com.motorph.pms.repository;

import com.motorph.pms.model.LeaveBalance.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaveTypeRepository extends JpaRepository<LeaveType, Integer> {

    Optional<LeaveType> findByType(String type);

    boolean existsByType(String type);
}
