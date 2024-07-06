package com.motorph.pms.repository;

import com.motorph.pms.model.PhilhealthMatrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PhilhealthMatrixRepository  extends JpaRepository<PhilhealthMatrix, Long> {}