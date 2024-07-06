package com.motorph.pms.repository;

import com.motorph.pms.model.LeaveRequest.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaveStatusRepository extends JpaRepository<LeaveStatus, Integer> {}
