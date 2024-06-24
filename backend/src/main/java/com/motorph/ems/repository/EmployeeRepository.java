package com.motorph.ems.repository;

import com.motorph.ems.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByFirstNameAndLastName(String firstName, String lastName);

    List<Employee> findAllByStatus_StatusName(String statusName);

    List<Employee> findAllByPosition_PositionName(String positionName);

    List<Employee> findAllByDepartment_DepartmentName(String departmentName);

    List<Employee> findAllBySupervisor_FirstName_AndSupervisor_LastName(String supervisor_firstName, String supervisor_lastName);

    List<Employee> findAllBySupervisor_EmployeeId(Long supervisorId);

    List<Employee> findAllByHireDateBetween(LocalDate startDate, LocalDate endDate);

    List<Employee> findAllByUser_Role_RoleName(String roleName);

    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    List<Employee> findAllExceptByStatus_StatusId(int status_statusId);
}
