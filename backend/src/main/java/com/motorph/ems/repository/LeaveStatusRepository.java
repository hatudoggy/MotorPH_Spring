package com.motorph.ems.repository;

import com.motorph.ems.model.LeaveRequest.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaveStatusRepository extends JpaRepository<LeaveStatus, Integer> {

    Optional<LeaveStatus> findByStatusName(String statusName);
}
