package com.motorph.pms.repository;

import com.motorph.pms.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
//TODO: try paging
    Optional<Employee> findByFirstNameAndLastName(String firstName, String lastName);

    List<Employee> findAllByStatus_StatusName(String statusName);

    List<Employee> findAllByPosition_PositionName(String positionName);

    List<Employee> findAllByDepartment_DepartmentName(String departmentName);

    List<Employee> findAllBySupervisor_FirstName_AndSupervisor_LastName(String supervisor_firstName, String supervisor_lastName);

    List<Employee> findAllBySupervisor_EmployeeId(Long supervisorId);

    List<Employee> findAllByHireDateBetween(LocalDate startDate, LocalDate endDate);

//    List<Employee> findAllByUser_Role_RoleName(String roleName);

    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    @Query("SELECT e FROM Employee e WHERE e.status.statusId NOT IN :statusIds")
    List<Employee> findAllExceptByStatus_StatusIds(List<Integer> statusIds);

    List<Employee> findAllByPosition_isLeader(boolean isLeader);
}
