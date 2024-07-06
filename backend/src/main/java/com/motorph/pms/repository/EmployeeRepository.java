package com.motorph.pms.repository;

import com.motorph.pms.model.Employee;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
//TODO: try paging
    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    @Query("SELECT e FROM Employee e WHERE e.status.statusId NOT IN :statusIds")
    List<Employee> findAllExceptByStatus_StatusIds(List<Integer> statusIds);

    List<Employee> findAllByPosition_isLeader(boolean isLeader);
}
