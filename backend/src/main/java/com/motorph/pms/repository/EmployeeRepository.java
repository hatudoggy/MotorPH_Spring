package com.motorph.pms.repository;

import com.motorph.pms.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT e FROM Employee e WHERE e.firstName LIKE %:name% OR e.lastName LIKE %:name%")
    List<Employee> findByNameContaining(@Param("name") String name);
}
